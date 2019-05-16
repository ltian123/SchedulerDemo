/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ltian.washUrl.adirectmanifest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Comparator;

/**
 * A stored entry represents a file in the zip. The entry may or may not be written to the zip
 * file.
 *
 * <p>Stored entries provide the operations that are related to the files themselves, not to the
 * zip. It is through the {@code StoredEntry} class that entries can be deleted ({@link #delete()},
 * open ({@link #open()}) or realigned ({@link #realign()}).
 *
 * <p>Entries are not created directly. They are created using
 * {@link ZFile#add(String, InputStream, boolean)} and obtained from the zip file
 * using {@link ZFile#get(String)} or {@link ZFile#entries()}.
 *
 * <p>Most of the data in the an entry is in the Central Directory Header. This includes the name,
 * compression method, file compressed and uncompressed sizes, CRC32 checksum, etc. The CDH can
 * be obtained using the {@link #getCentralDirectoryHeader()} method.
 */
public class StoredEntry {
    /**
     * Signature of the data descriptor.
     */
    private static final int DATA_DESC_SIGNATURE = 0x08074b50;

    /**
     * Local header field: signature.
     */
    private static final ZipField.F4 F_LOCAL_SIGNATURE = new ZipField.F4(0, 0x04034b50,
            "Signature");

    /**
     * Local header field: version to extract, should match the CDH's.
     */
    static final ZipField.F2 F_VERSION_EXTRACT = new ZipField.F2(
            F_LOCAL_SIGNATURE.endOffset(), "Version to extract");

    /**
     * Local header field: GP bit flag, should match the CDH's.
     */
    private static final ZipField.F2 F_GP_BIT = new ZipField.F2(F_VERSION_EXTRACT.endOffset(),
            "GP bit flag");

    /**
     * Local header field: compression method, should match the CDH's.
     */
    private static final ZipField.F2 F_METHOD = new ZipField.F2(F_GP_BIT.endOffset(),
            "Compression method");

    /**
     * Local header field: last modification time, should match the CDH's.
     */
    private static final ZipField.F2 F_LAST_MOD_TIME = new ZipField.F2(F_METHOD.endOffset(),
            "Last modification time");

    /**
     * Local header field: last modification time, should match the CDH's.
     */
    private static final ZipField.F2 F_LAST_MOD_DATE = new ZipField.F2(F_LAST_MOD_TIME.endOffset(),
            "Last modification date");

    /**
     * Local header field: CRC32 checksum, should match the CDH's. 0 if there is no data.
     */
    private static final ZipField.F4 F_CRC32 = new ZipField.F4(F_LAST_MOD_DATE.endOffset(),
            "CRC32");

    /**
     * Local header field: compressed size, size the data takes in the zip file.
     */
    private static final ZipField.F4 F_COMPRESSED_SIZE = new ZipField.F4(F_CRC32.endOffset(),
            "Compressed size");

    /**
     * Local header field: uncompressed size, size the data takes after extraction.
     */
    private static final ZipField.F4 F_UNCOMPRESSED_SIZE = new ZipField.F4(
            F_COMPRESSED_SIZE.endOffset(), "Uncompressed size");

    /**
     * Local header field: length of the file name.
     */
    private static final ZipField.F2 F_FILE_NAME_LENGTH = new ZipField.F2(
            F_UNCOMPRESSED_SIZE.endOffset(), "@File name length");

    /**
     * Local header filed: length of the extra field.
     */
    private static final ZipField.F2 F_EXTRA_LENGTH = new ZipField.F2(
            F_FILE_NAME_LENGTH.endOffset(), "Extra length");

    /**
     * Local header size (fixed part, not counting file name or extra field).
     */
    static final int FIXED_LOCAL_FILE_HEADER_SIZE = F_EXTRA_LENGTH.endOffset();

    /**
     * Type of entry.
     */
    
    private StoredEntryType type;

    /**
     * The central directory header with information about the file.
     */
    
    private CentralDirectoryHeader cdh;

    /**
     * The file this entry is associated with
     */
    
    private ZFile file;

    /**
     * Extra field specified in the local directory.
     */
    
    private ExtraField localExtra;

    /**
     * Type of data descriptor associated with the entry.
     */
    
    private DataDescriptorType dataDescriptorType;

    /**
     * Creates a new stored entry.
     *
     * @param header the header with the entry information; if the header does not contain an
     * offset it means that this entry is not yet written in the zip file
     * @param file the zip file containing the entry
     * @param source the entry's data source; it can be {@code null} only if the source can be
     * read from the zip file, that is, if {@code header.getOffset()} is non-negative
     * @throws IOException failed to create the entry
     */
    StoredEntry(
             CentralDirectoryHeader header,
             ZFile file)
            throws IOException {
        cdh = header;
        this.file = file;

        if (header.getOffset() >= 0) {
            /*
             * This will be overwritten during readLocalHeader. However, IJ complains if we don't
             * assign a value to localExtra because of the  annotation.
             */
            localExtra = new ExtraField();

            readLocalHeader();
        } else {
            /*
             * There is no local extra data for new files.
             */
            localExtra = new ExtraField();
        }

        /*
         * It seems that zip utilities store directories as names ending with "/".
         * This seems to be respected by all zip utilities although I could not find there anywhere
         * in the specification.
         */
        if (cdh.getName().endsWith(Character.toString(ZFile.SEPARATOR))) {
            type = StoredEntryType.DIRECTORY;
        } else {
            type = StoredEntryType.FILE;
        }

        /*
         * By default we assume there is no data descriptor unless the CRC is marked as deferred
         * in the header's GP Bit.
         */
        dataDescriptorType = DataDescriptorType.NO_DATA_DESCRIPTOR;
        if (header.getGpBit().isDeferredCrc()) {
            try {
                readDataDescriptorRecord();
            } catch (IOException e) {
                throw new IOException("Failed to read data descriptor record.", e);
            }
        }
    }

    /**
     * Obtains the size of the local header of this entry.
     *
     * @return the local header size in bytes
     */
    public int getLocalHeaderSize() {
        return FIXED_LOCAL_FILE_HEADER_SIZE + cdh.getEncodedFileName().length + localExtra.size();
    }

    /**
     * Obtains the size of the whole entry on disk, including local header and data descriptor.
     * This method will wait until compression information is complete, if needed.
     *
     * @return the number of bytes
     * @throws IOException failed to get compression information
     */
    long getInFileSize() throws IOException {
        return cdh.getCompressedSize() + getLocalHeaderSize()
                + dataDescriptorType.size;
    }

    /**
     * Obtains the type of entry.
     *
     * @return the type of entry
     */
    
    public StoredEntryType getType() {
        return type;
    }

    /**
     * Obtains the CDH associated with this entry.
     *
     * @return the CDH
     */
    
    public CentralDirectoryHeader getCentralDirectoryHeader() {
        return cdh;
    }

    /**
     * Reads the file's local header and verifies that it matches the Central Directory
     * Header provided in the constructor. This method should only be called if the entry already
     * exists on disk; new entries do not have local headers.
     * <p>
     * This method will define the {@link #localExtra} field that is only defined in the
     * local descriptor.
     *
     * @throws IOException failed to read the local header
     */
    private void readLocalHeader() throws IOException {
        System.out.println("readLocalHeader at " + Long.toHexString(cdh.getOffset()));
        byte[] localHeader = new byte[FIXED_LOCAL_FILE_HEADER_SIZE];
        file.directFullyRead(cdh.getOffset(), localHeader);

        ByteBuffer bytes = ByteBuffer.wrap(localHeader);
        F_LOCAL_SIGNATURE.verify(bytes);
        F_VERSION_EXTRACT.verify(bytes, cdh.getVersionExtract());
        F_GP_BIT.verify(bytes, cdh.getGpBit().getValue());
        F_METHOD.verify(bytes, cdh.getMethod().methodCode);

        if (false/*file.areTimestampsIgnored()*/) {
            F_LAST_MOD_TIME.skip(bytes);
            F_LAST_MOD_DATE.skip(bytes);
        } else {
            F_LAST_MOD_TIME.verify(bytes, cdh.getLastModTime());
            F_LAST_MOD_DATE.verify(bytes, cdh.getLastModDate());
        }

        /*
         * If CRC-32, compressed size and uncompressed size are deferred, their values in Local
         * File Header must be ignored and their actual values must be read from the Data
         * Descriptor following the contents of this entry. See readDataDescriptorRecord().
         */
        if (cdh.getGpBit().isDeferredCrc()) {
            F_CRC32.skip(bytes);
            F_COMPRESSED_SIZE.skip(bytes);
            F_UNCOMPRESSED_SIZE.skip(bytes);
        } else {
            F_CRC32.verify(bytes, cdh.getCrc32());
            F_COMPRESSED_SIZE.verify(bytes, cdh.getCompressedSize());
            F_UNCOMPRESSED_SIZE.verify(bytes, cdh.getUncompressedSize());
        }

        F_FILE_NAME_LENGTH.verify(bytes, cdh.getEncodedFileName().length);
        long extraLength = F_EXTRA_LENGTH.read(bytes);
        long fileNameStart = cdh.getOffset() + F_EXTRA_LENGTH.endOffset();
        System.out.println("readLocalHeader: fileNameLength=" + cdh.getEncodedFileName().length);
        System.out.println("readLocalHeader: extraLength=" + extraLength);
        byte[] fileNameAndExtra = new byte[cdh.getEncodedFileName().length + (int) extraLength];
        file.directFullyRead(fileNameStart, fileNameAndExtra);

        byte[] fileNameData = new byte[cdh.getEncodedFileName().length];
        System.arraycopy(fileNameAndExtra, 0, fileNameData, 0, fileNameData.length); 
        String fileName = EncodeUtils.decode(fileNameData, cdh.getGpBit());
        if (!fileName.equals(cdh.getName())) {
        }
        System.out.println("readLocalHeader: fileName=" + fileName);

        byte[] localExtraRaw = new byte[(int) extraLength];
        System.arraycopy(fileNameAndExtra, fileNameData.length, localExtraRaw, 0, localExtraRaw.length); 
        localExtra = new ExtraField(localExtraRaw);
    }

    /**
     * Reads the data descriptor record. This method can only be invoked once it is established
     * that a data descriptor does exist. It will read the data descriptor and check that the data
     * described there matches the data provided in the Central Directory.
     * <p>
     * This method will set the {@link #dataDescriptorType} field to the appropriate type of
     * data descriptor record.
     *
     * @throws IOException failed to read the data descriptor record
     */
    private void readDataDescriptorRecord() throws IOException {
        long ddStart = cdh.getOffset() + FIXED_LOCAL_FILE_HEADER_SIZE
                + cdh.getName().length() + localExtra.size() + cdh.getCompressedSize();
        System.out.println("readDataDescriptorRecord at " + Long.toHexString(ddStart));
        byte[] ddData = new byte[DataDescriptorType.DATA_DESCRIPTOR_WITH_SIGNATURE.size];
        file.directFullyRead(ddStart, ddData);

        ByteBuffer ddBytes = ByteBuffer.wrap(ddData);

        ZipField.F4 signatureField = new ZipField.F4(0, "Data descriptor signature");
        int cpos = ddBytes.position();
        long sig = signatureField.read(ddBytes);
        if (sig == DATA_DESC_SIGNATURE) {
            dataDescriptorType = DataDescriptorType.DATA_DESCRIPTOR_WITH_SIGNATURE;
        } else {
            dataDescriptorType = DataDescriptorType.DATA_DESCRIPTOR_WITHOUT_SIGNATURE;
            ddBytes.position(cpos);
        }

        ZipField.F4 crc32Field = new ZipField.F4(0, "CRC32");
        ZipField.F4 compressedField = new ZipField.F4(crc32Field.endOffset(), "Compressed size");
        ZipField.F4 uncompressedField = new ZipField.F4(compressedField.endOffset(),
                "Uncompressed size");

        crc32Field.verify(ddBytes, cdh.getCrc32());
        compressedField.verify(ddBytes, cdh.getCompressedSize());
        uncompressedField.verify(ddBytes, cdh.getUncompressedSize());
    }

    /**
     * Obtains the type of data descriptor used in the entry.
     *
     * @return the type of data descriptor
     */
    
    public DataDescriptorType getDataDescriptorType() {
        return dataDescriptorType;
    }

    /**
     * Obtains the contents of the local extra field.
     *
     * @return the contents of the local extra field
     */
    
    public ExtraField getLocalExtra() {
        return localExtra;
    }
}
