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
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * The Central Directory Header contains information about files stored in the zip. Instances of
 * this class contain information for files that already are in the zip and, for which the data was
 * read from the Central Directory. But some instances of this class are used for new files.
 * Because instances of this class can refer to files not yet on the zip, some of the fields may
 * not be filled in, or may be filled in with default values.
 * <p>
 * Because compression decision is done lazily, some data is stored with futures.
 */
public class CentralDirectoryHeader implements Cloneable {
    /**
    * Version of zip file that only supports stored files.
    */
    public static final long VERSION_WITH_STORE_FILES_ONLY = 10L;
    
    /**
    * Version of zip file that only supports directories and deflated files.
    */
    public static final long VERSION_WITH_DIRECTORIES_AND_DEFLATE = 20L;
    
    /**
    * The compression method.
    */
    
    private final CompressionMethod mMethod;
    
    /**
    * Size of the file compressed. 0 if the file has no data.
    */
    private final long compressedSize;
    
    /**
    * Version needed to extract the zip.
    */
    private final long versionExtract;
    
    
    /**
     * Default "version made by" field: upper byte needs to be 0 to set to MS-DOS compatibility.
     * Lower byte can be anything, really. We use 18 because aapt uses 17 :)
     */
    private static final int DEFAULT_VERSION_MADE_BY = 0x0018;

    /**
     * Name of the file.
     */
    
    private String name;

    /**
     * CRC32 of the data. 0 if not yet computed.
     */
    private long crc32;

    /**
     * Size of the file uncompressed. 0 if the file has no data.
     */
    private long uncompressedSize;

    /**
     * Code of the program that made the zip. We actually don't care about this.
     */
    private long madeBy;

    /**
     * General-purpose bit flag.
     */
    
    private GPFlags gpBit;

    /**
     * Last modification time in MS-DOS format (see {@link MsDosDateTimeUtils#packTime(long)}).
     */
    private long lastModTime;

    /**
     * Last modification time in MS-DOS format (see {@link MsDosDateTimeUtils#packDate(long)}).
     */
    private long lastModDate;

    /**
     * Extra data field contents. This field follows a specific structure according to the
     * specification.
     */
    
    private ExtraField extraField;

    /**
     * File comment.
     */
    
    private byte[] comment;

    /**
     * File internal attributes.
     */
    private long internalAttributes;

    /**
     * File external attributes.
     */
    private long externalAttributes;

    /**
     * Offset in the file where the data is located. This will be -1 if the header corresponds to
     * a new file that is not yet written in the zip and, therefore, has no written data.
     */
    private long offset;

    /**
     * Encoded file name.
     */
    private byte[] encodedFileName;

    /**
     * Creates data for a file.
     *
     * @param name the file name
     * @param encodedFileName the encoded file name, this array will be owned by the header
     * @param uncompressedSize the uncompressed file size
     * @param compressInfo computation that defines the compression information
     * @param flags flags used in the entry
     * @param zFile the file this header belongs to
     */
    CentralDirectoryHeader(
             String name,
             byte[] encodedFileName,
             CompressionMethod method,
             long compressedSize,
            long uncompressedSize,
            long versionToExtract,
             GPFlags flags) {
        this.name = name;
        mMethod = method;
        this.compressedSize = compressedSize;
        this.uncompressedSize = uncompressedSize;
        versionExtract = versionToExtract;
//        if (getName().endsWith("/") || method == CompressionMethod.DEFLATE) {
//            /*
//            * Directories and compressed files only in version 2.0.
//            */
//            versionExtract = VERSION_WITH_DIRECTORIES_AND_DEFLATE;
//        } else {
//            versionExtract = VERSION_WITH_STORE_FILES_ONLY;
//        }
        crc32 = 0;

        /*
         * Set sensible defaults for the rest.
         */
        madeBy = DEFAULT_VERSION_MADE_BY;

        gpBit = flags;
        lastModTime = 0;
        lastModDate = 0;
        extraField = new ExtraField();
        comment = new byte[0];
        internalAttributes = 0;
        externalAttributes = 0;
        offset = -1;
        this.encodedFileName = encodedFileName;
    }

    /**
    * Obtains the compression data size.
    *
    * @return the compressed data size
    */
    public long getCompressedSize() {
        return compressedSize;
    }
    
    /**
    * Obtains the compression method.
    *
    * @return the compression method
    */
    public CompressionMethod getMethod() {
        return mMethod;
    }
    
    /**
    * Obtains the minimum version for extract.
    *
    * @return the minimum version
    */
    public long getVersionExtract() {
        return versionExtract;
    }

    /**
     * Obtains the name of the file.
     *
     * @return the name
     */
    
    public String getName() {
        return name;
    }

    /**
     * Obtains the size of the uncompressed file.
     *
     * @return the size of the file
     */
    public long getUncompressedSize() {
        return uncompressedSize;
    }

    /**
     * Obtains the CRC32 of the data.
     *
     * @return the CRC32, 0 if not yet computed
     */
    public long getCrc32() {
        return crc32;
    }

    /**
     * Sets the CRC32 of the data.
     *
     * @param crc32 the CRC 32
     */
    void setCrc32(long crc32) {
        this.crc32 = crc32;
    }

    /**
     * Obtains the code of the program that made the zip.
     *
     * @return the code
     */
    public long getMadeBy() {
        return madeBy;
    }

    /**
     * Sets the code of the progtram that made the zip.
     *
     * @param madeBy the code
     */
    void setMadeBy(long madeBy) {
        this.madeBy = madeBy;
    }

    /**
     * Obtains the general-purpose bit flag.
     *
     * @return the bit flag
     */
    
    public GPFlags getGpBit() {
        return gpBit;
    }

    /**
     * Obtains the last modification time of the entry.
     *
     * @return the last modification time in MS-DOS format (see
     * {@link MsDosDateTimeUtils#packTime(long)})
     */
    public long getLastModTime() {
        return lastModTime;
    }

    /**
     * Sets the last modification time of the entry.
     *
     * @param lastModTime the last modification time in MS-DOS format (see
     * {@link MsDosDateTimeUtils#packTime(long)})
     */
    void setLastModTime(long lastModTime) {
        this.lastModTime = lastModTime;
    }

    /**
     * Obtains the last modification date of the entry.
     *
     * @return the last modification date in MS-DOS format (see
     * {@link MsDosDateTimeUtils#packDate(long)})
     */
    public long getLastModDate() {
        return lastModDate;
    }

    /**
     * Sets the last modification date of the entry.
     *
     * @param lastModDate the last modification date in MS-DOS format (see
     * {@link MsDosDateTimeUtils#packDate(long)})
     */
    void setLastModDate(long lastModDate) {
        this.lastModDate = lastModDate;
    }

    /**
     * Obtains the data in the extra field.
     *
     * @return the data (returns an empty array if there is none)
     */
    
    public ExtraField getExtraField() {
        return extraField;
    }

    /**
     * Sets the data in the extra field.
     *
     * @param extraField the data to set
     */
    public void setExtraField( ExtraField extraField) {
        setExtraFieldNoNotify(extraField);
    }

    /**
     * Sets the data in the extra field, but does not notify {@link ZFile}. This method is invoked
     * when the {@link ZFile} knows the extra field is being set.
     *
     * @param extraField the data to set
     */
    void setExtraFieldNoNotify( ExtraField extraField) {
        this.extraField = extraField;
    }

    /**
     * Obtains the entry's comment.
     *
     * @return the comment (returns an empty array if there is no comment)
     */
    
    public byte[] getComment() {
        return comment;
    }

    /**
     * Sets the entry's comment.
     *
     * @param comment the comment
     */
    void setComment( byte[] comment) {
        this.comment = comment;
    }

    /**
     * Obtains the entry's internal attributes.
     *
     * @return the entry's internal attributes
     */
    public long getInternalAttributes() {
        return internalAttributes;
    }

    /**
     * Sets the entry's internal attributes.
     *
     * @param internalAttributes the entry's internal attributes
     */
    void setInternalAttributes(long internalAttributes) {
        this.internalAttributes = internalAttributes;
    }

    /**
     * Obtains the entry's external attributes.
     *
     * @return the entry's external attributes
     */
    public long getExternalAttributes() {
        return externalAttributes;
    }

    /**
     * Sets the entry's external attributes.
     *
     * @param externalAttributes the entry's external attributes
     */
    void setExternalAttributes(long externalAttributes) {
        this.externalAttributes = externalAttributes;
    }

    /**
     * Obtains the offset in the zip file where this entry's data is.
     *
     * @return the offset or {@code -1} if the file has no data in the zip and, therefore, data
     * is stored in memory
     */
    public long getOffset() {
        return offset;
    }

    /**
     * Sets the offset in the zip file where this entry's data is.
     *
     * @param offset the offset or {@code -1} if the file is new and has no data in the zip yet
     */
    void setOffset(long offset) {
        this.offset = offset;
    }

    /**
     * Obtains the encoded file name.
     *
     * @return the encoded file name
     */
    public byte[] getEncodedFileName() {
        return encodedFileName;
    }

    /**
     * Resets the deferred CRC flag in the GP flags.
     */
    void resetDeferredCrc() {
        /*
         * We actually create a new set of flags. Since the only information we care about is the
         * UTF-8 encoding, we'll just create a brand new object.
         */
        gpBit = GPFlags.make(gpBit.isUtf8FileName());
    }
}
