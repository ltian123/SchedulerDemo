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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

/**
 * Representation of the central directory of a zip archive.
 */
class CentralDirectory {

    /**
     * Field in the central directory with the central directory signature.
     */
    private static final ZipField.F4 F_SIGNATURE = new ZipField.F4(0, 0x02014b50, "Signature");

    /**
     * Field in the central directory with the "made by" code.
     */
    private static final ZipField.F2 F_MADE_BY = new ZipField.F2(F_SIGNATURE.endOffset(),
            "Made by");

    /**
     * Field in the central directory with the minimum version required to extract the entry.
     */
    static final ZipField.F2 F_VERSION_EXTRACT = new ZipField.F2(F_MADE_BY.endOffset(),
            "Version to extract");

    /**
     * Field in the central directory with the GP bit flag.
     */
    private static final ZipField.F2 F_GP_BIT = new ZipField.F2(F_VERSION_EXTRACT.endOffset(),
            "GP bit");

    /**
     * Field in the central directory with the code of the compression method. See
     * {@link CompressionMethod#fromCode(long)}.
     */
    private static final ZipField.F2 F_METHOD = new ZipField.F2(F_GP_BIT.endOffset(), "Method");

    /**
     * Field in the central directory with the last modification time in MS-DOS format (see
     * {@link MsDosDateTimeUtils#packTime(long)}).
     */
    private static final ZipField.F2 F_LAST_MOD_TIME = new ZipField.F2(F_METHOD.endOffset(),
            "Last modification time");

    /**
     * Field in the central directory with the last modification date in MS-DOS format. See
     * {@link MsDosDateTimeUtils#packDate(long)}.
     */
    private static final ZipField.F2 F_LAST_MOD_DATE = new ZipField.F2(F_LAST_MOD_TIME.endOffset(),
            "Last modification date");

    /**
     * Field in the central directory with the CRC32 checksum of the entry. This will be zero for
     * directories and files with no content.
     */
    private static final ZipField.F4 F_CRC32 = new ZipField.F4(F_LAST_MOD_DATE.endOffset(),
            "CRC32");

    /**
     * Field in the central directory with the entry's compressed size, <em>i.e.</em>, the file on
     * the archive. This will be the same as the uncompressed size if the method is
     * {@link CompressionMethod#STORE}.
     */
    private static final ZipField.F4 F_COMPRESSED_SIZE = new ZipField.F4(F_CRC32.endOffset(),
            "Compressed size");

    /**
     * Field in the central directory with the entry's uncompressed size, <em>i.e.</em>, the size
     * the file will have when extracted from the zip. This will be zero for directories and empty
     * files and will be the same as the compressed size if the method is
     * {@link CompressionMethod#STORE}.
     */
    private static final ZipField.F4 F_UNCOMPRESSED_SIZE = new ZipField.F4(
            F_COMPRESSED_SIZE.endOffset(), "Uncompressed size");

    /**
     * Field in the central directory with the length of the file name. The file name is stored
     * after the offset field ({@link #F_OFFSET}). The number of characters in the file name are
     * stored in this field.
     */
    private static final ZipField.F2 F_FILE_NAME_LENGTH = new ZipField.F2(
            F_UNCOMPRESSED_SIZE.endOffset(), "File name length");

    /**
     * Field in the central directory with the length of the extra field. The extra field is
     * stored after the file name ({@link #F_FILE_NAME_LENGTH}). The contents of this field are
     * partially defined in the zip specification but we do not parse it.
     */
    private static final ZipField.F2 F_EXTRA_FIELD_LENGTH = new ZipField.F2(
            F_FILE_NAME_LENGTH.endOffset(), "Extra field length");

    /**
     * Field in the central directory with the length of the comment. The comment is stored after
     * the extra field ({@link #F_EXTRA_FIELD_LENGTH}). We do not parse the comment.
     */
    private static final ZipField.F2 F_COMMENT_LENGTH = new ZipField.F2(
            F_EXTRA_FIELD_LENGTH.endOffset(), "Comment length");

    /**
     * Number of the disk where the central directory starts. Because we do not support multi-file
     * archives, this field has to have value {@code 0}.
     */
    private static final ZipField.F2 F_DISK_NUMBER_START = new ZipField.F2(
            F_COMMENT_LENGTH.endOffset(), 0, "Disk start");

    /**
     * Internal attributes. This field can only contain one bit set, the {@link #ASCII_BIT}.
     */
    private static final ZipField.F2 F_INTERNAL_ATTRIBUTES = new ZipField.F2(
            F_DISK_NUMBER_START.endOffset(), "Int attributes");

    /**
     * External attributes. This field is ignored.
     */
    private static final ZipField.F4 F_EXTERNAL_ATTRIBUTES = new ZipField.F4(
            F_INTERNAL_ATTRIBUTES.endOffset(), "Ext attributes");

    /**
     * Offset into the archive where the entry starts. This is the offset to the local header
     * (see {@link StoredEntry} for information on the local header), not to the file data itself.
     * The file data, if there is any, will be stored after the local header.
     */
    private static final ZipField.F4 F_OFFSET = new ZipField.F4(F_EXTERNAL_ATTRIBUTES.endOffset(),
            "Offset");

    /**
     * Maximum supported version to extract.
     */
    private static final int MAX_VERSION_TO_EXTRACT = 20;

    /**
     * Bit that can be set on the internal attributes stating that the file is an ASCII file. We
     * don't do anything with this information, but we check that nothing unexpected appears in the
     * internal attributes.
     */
    private static final int ASCII_BIT = 1;

    /**
     * Contains all entries in the directory mapped from their names.
     */
    private final Map<String, CentralDirectoryHeader> entries;

     /**
     * Creates a new, empty, central directory, for a given zip file.
     *
     * @param file the file
     */
    CentralDirectory() {
        entries = new HashMap();
    }

    /**
     * Reads the central directory data from a zip file, parses it, and creates the in-memory
     * structure representing the directory.
     *
     * @param bytes the data of the central directory; the directory is read from the buffer's
     * current position; when this method terminates, the buffer's position is the first byte
     * after the directory
     * @param count the number of entries expected in the central directory (usually read from the
     * {@link Eocd}).
     * @param file the zip file this central directory belongs to
     * @return the central directory
     * @throws IOException failed to read data from the zip, or the central directory is corrupted
     * or has unsupported features
     */
    static CentralDirectory makeFromData( ByteBuffer bytes, int count)
            throws IOException {

        CentralDirectory directory = new CentralDirectory();

        for (int i = 0; i < count; i++) {
            try {
                directory.readEntry(bytes);
            } catch (IOException e) {
                throw new IOException(
                        "Failed to read directory entry index "
                                + i
                                + " (total "
                                + "directory bytes read: "
                                + bytes.position()
                                + ").",
                        e);
            }
        }

        return directory;
    }

    /**
     * Reads the next entry from the central directory and adds it to {@link #entries}.
     *
     * @param bytes the central directory's data, positioned starting at the beginning of the next
     * entry to read; when finished, the buffer's position will be at the first byte after the
     * entry
     * @throws IOException failed to read the directory entry, either because of an I/O error,
     * because it is corrupt or contains unsupported features
     */
    private void readEntry( ByteBuffer bytes) throws IOException {
        F_SIGNATURE.verify(bytes);
        long madeBy = F_MADE_BY.read(bytes);

        long versionNeededToExtract = F_VERSION_EXTRACT.read(bytes);

        long gpBit = F_GP_BIT.read(bytes);
        GPFlags flags = GPFlags.from(gpBit);

        long methodCode = F_METHOD.read(bytes);
        CompressionMethod method = CompressionMethod.fromCode(methodCode);

        long lastModTime;
        long lastModDate;
        if (false/*file.areTimestampsIgnored()*/) {
            lastModTime = 0;
            lastModDate = 0;
            F_LAST_MOD_TIME.skip(bytes);
            F_LAST_MOD_DATE.skip(bytes);
        } else {
            lastModTime = F_LAST_MOD_TIME.read(bytes);
            lastModDate = F_LAST_MOD_DATE.read(bytes);
        }

        long crc32 = F_CRC32.read(bytes);
        long compressedSize = F_COMPRESSED_SIZE.read(bytes);
        long uncompressedSize = F_UNCOMPRESSED_SIZE.read(bytes);
        int fileNameLength = (int) F_FILE_NAME_LENGTH.read(bytes);
        int extraFieldLength = (int) F_EXTRA_FIELD_LENGTH.read(bytes);
        int fileCommentLength = (int) F_COMMENT_LENGTH.read(bytes);

        F_DISK_NUMBER_START.verify(bytes);
        long internalAttributes = F_INTERNAL_ATTRIBUTES.read(bytes);

        long externalAttributes = F_EXTERNAL_ATTRIBUTES.read(bytes);
        long entryOffset = F_OFFSET.read(bytes);

        long remainingSize = fileNameLength + extraFieldLength + fileCommentLength;

        if (bytes.remaining() < fileNameLength + extraFieldLength + fileCommentLength) {
            throw new IOException(
                    "Directory entry should have "
                            + remainingSize
                            + " bytes remaining (name = "
                            + fileNameLength
                            + ", extra = "
                            + extraFieldLength
                            + ", comment = "
                            + fileCommentLength
                            + "), but it has "
                            + bytes.remaining()
                            + ".");
        }

        byte[] encodedFileName = new byte[fileNameLength];
        bytes.get(encodedFileName);
        String fileName = EncodeUtils.decode(encodedFileName, flags);

        byte[] extraField = new byte[extraFieldLength];
        bytes.get(extraField);

        byte[] fileCommentField = new byte[fileCommentLength];
        bytes.get(fileCommentField);

        CentralDirectoryHeader centralDirectoryHeader =
                new CentralDirectoryHeader(
                        fileName, encodedFileName, method, compressedSize, uncompressedSize, versionNeededToExtract, flags);
        centralDirectoryHeader.setMadeBy(madeBy);
        centralDirectoryHeader.setLastModTime(lastModTime);
        centralDirectoryHeader.setLastModDate(lastModDate);
        centralDirectoryHeader.setCrc32(crc32);
        centralDirectoryHeader.setInternalAttributes(internalAttributes);
        centralDirectoryHeader.setExternalAttributes(externalAttributes);
        centralDirectoryHeader.setOffset(entryOffset);
        centralDirectoryHeader.setExtraFieldNoNotify(new ExtraField(extraField));
        centralDirectoryHeader.setComment(fileCommentField);

        if (entries.containsKey(fileName)) {
        }
        
        entries.put(fileName, centralDirectoryHeader);
    }

    /**
     * Obtains all the entries in the central directory.
     *
     * @return all entries on a non-modifiable map
     */
    
    Map<String, CentralDirectoryHeader> getEntries() {
        return entries;
    }
}
