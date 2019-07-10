/*
 * Copyright (C) 2016 The Android Open Source Project
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

import cn.ltian.washUrl.ArcSyncHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZFile {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger("ZFile");
    /**
     * The file separator in paths in the zip file. This is fixed by the zip specification
     * (section 4.4.17).
     */
    public static final char SEPARATOR = '/';

    /**
     * Minimum size the EOCD can have.
     */
    private static final int MIN_EOCD_SIZE = 22;

    /**
     * Number of bytes of the Zip64 EOCD locator record.
     */
    private static final int ZIP64_EOCD_LOCATOR_SIZE = 20;

    /**
     * Maximum size for the EOCD.
     */
    private static final int MAX_EOCD_COMMENT_SIZE = 65535;

    /**
     * How many bytes to look back from the end of the file to look for the EOCD signature.
     */
    private static final int LAST_BYTES_TO_READ = MIN_EOCD_SIZE + ZIP64_EOCD_LOCATOR_SIZE + MAX_EOCD_COMMENT_SIZE;

    /**
     * Signature of the Zip64 EOCD locator record.
     */
    private static final int ZIP64_EOCD_LOCATOR_SIGNATURE = 0x07064b50;

    /**
     * Signature of the EOCD record.
     */
    private static final byte[] EOCD_SIGNATURE = new byte[] { 0x06, 0x05, 0x4b, 0x50 };

    /**
     * Size of buffer for I/O operations.
     */
    private static final int IO_BUFFER_SIZE = 1024 * 1024;

    /**
     * When extensions request re-runs, we do maximum number of cycles until we decide to stop and
     * flag a infinite recursion problem.
     */
    private static final int MAXIMUM_EXTENSION_CYCLE_COUNT = 10;

    /**
     * Minimum size for the extra field when we have to add one. We rely on the alignment segment
     * to do that so the minimum size for the extra field is the minimum size of an alignment
     * segment.
     */
    private static final int MINIMUM_EXTRA_FIELD_SIZE = ExtraField.AlignmentSegment.MINIMUM_SIZE;

    /**
     * Maximum size of the extra field.
     *
     * <p>Theoretically, this is (1 << 16) - 1 = 65535 and not (1 < 15) -1 = 32767. However, due to
     * http://b.android.com/221703, we need to keep this limited.
     */
    private static final int MAX_LOCAL_EXTRA_FIELD_CONTENTS_SIZE = (1 << 15) - 1;

    /**
     * The random access file used to access the zip file. This will be {@code null} if and only
     * if {@link #state} is {@link ZipFileState#CLOSED}.
     */

    private RandomAccessFile raf;
    private FileInputStream inp;
    private  URLConnection uc;
    /**
     * Creates a new zip file. If the zip file does not exist, then no file is created at this
     * point and {@code ZFile} will contain an empty structure. However, an (empty) zip file will
     * be created if either {@link #update()} or {@link #close()} are used. If a zip file exists,
     * it will be parsed and read.
     *
     * @param file the zip file
     * @throws IOException some file exists but could not be read
     */
    public ZFile() {
    }

    private  String downurl;

    public static char getSEPARATOR() {
        return SEPARATOR;
    }

    public String getDownurl() {
        return downurl;
    }

    public void setDownurl(String downurl) {
        this.downurl = downurl;
    }

    public ZFile(String downurl) {
        this.downurl = downurl;
    }


    /**
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public  byte[] readData(String filename) throws IOException {
        URL root = new URL(downurl);
        uc = root.openConnection();
//        log.info("readData: filelength=" + uc.getContentLength());
        Eocd eocd = readEocd();
        CentralDirectory directory = readCentralDirectory(eocd);
        Map<String, CentralDirectoryHeader> entries = directory.getEntries();
        CentralDirectoryHeader header = entries.get(filename);
//        log.info("readData: found entry for " + filename + ", local header offset=" + Long.toHexString(header.getOffset()) +
//                ", compressedSize=" + Long.toHexString(header.getCompressedSize()) +
//                ", uncompressedSize=" + Long.toHexString(header.getUncompressedSize()));
        StoredEntry entry = new StoredEntry(header, this);
        long start = entry.getCentralDirectoryHeader().getOffset();
        long end = start + entry.getInFileSize();
//        log.info("readData: file offset=" + Long.toHexString(start) + ", end=" + Long.toHexString(end));
        byte[] buf = new byte[(int) (end - start)];
        directFullyRead(start, buf);
        //处理压缩文件为二进制文件
        return dealZip(buf);
    }

    /**
     * 处理压缩文件为二进制
     * @param buf
     */
    private static byte[] dealZip(byte[] buf) {
        InputStream fis = null;
        ZipInputStream zis = null;
        ByteArrayOutputStream swapStream = null;
        try {
            fis =  new ByteArrayInputStream( buf);
            zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                String name = ze.getName();
                if (ze.isDirectory()) {
                    log.info("directory: " + name);
                } else {
                    log.info("file: " + name);
//                    byte[] buff = new byte[buf.length];
//                    zis.read(buff,0,buf.length);
//                    return  buff;

                    swapStream = new ByteArrayOutputStream();
                    byte[] buff = new byte[100]; //buff用于存放循环读取的临时数据
                    int rc = 0;
                    while ((rc = zis.read(buff, 0, 100)) > 0) {
                        swapStream.write(buff, 0, rc);
                    }
                    byte[] in_b = swapStream.toByteArray(); //in_b为转换之后的结果
                    return in_b;
//                    FileOutputStream fos = new FileOutputStream(new File(dealDir+salt+"-2"));
//                    log.info("二进制文件："+dealDir+name+salt+"-2");
//                    int r = -1;
//                    while ((r = zis.read()) != -1) {
//                        fos.write(r);
//                    }
//                    fos.close();
                }
            }
        } catch (Exception e) {
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.error("!!!dealZip(),error={}",e.getMessage());
                    e.printStackTrace();
                }
            }
            if (zis != null) {
                try {
                    zis.close();
                } catch (IOException e) {
                    log.error("!!!dealZip(),error={}",e.getMessage());
                    e.printStackTrace();
                }
            }
            if (swapStream != null) {
                try {
                    swapStream.close();
                } catch (IOException e) {
                    log.error("!!!dealZip(),error={}",e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Finds the EOCD marker and reads it. It will populate the {@link #eocdEntry} variable.
     *
     * @throws IOException failed to read the EOCD
     */
    private Eocd readEocd() throws IOException {
        log.info("readEocd");
        /*
         * Read the last part of the zip into memory. If we don't find the EOCD signature by then,
         * the file is corrupt.
         */
        int lastToRead = LAST_BYTES_TO_READ;
        //替换
        if (lastToRead > uc.getContentLength()) {
            lastToRead = (int) uc.getContentLength();
        }

        byte[] last = new byte[lastToRead];
        //替换
//        log.info("uc.getContentLength() - lastToRead:"+(uc.getContentLength() - lastToRead)+"uc.getContentLength():"+uc.getContentLength()+"lastToRead"+lastToRead);
        directFullyRead(uc.getContentLength() - lastToRead, last);


        /*
         * Start endIdx at the first possible location where the signature can be located and then
         * move backwards. Because the EOCD must have at least MIN_EOCD size, the first byte of the
         * signature (and first byte of the EOCD) must be located at last.length - MIN_EOCD_SIZE.
         *
         * Because the EOCD signature may exist in the file comment, when we find a signature we
         * will try to read the Eocd. If we fail, we continue searching for the signature. However,
         * we will keep the last exception in case we don't find any signature.
         */
        Eocd eocd = null;
        int foundEocdSignature = -1;
        IOException errorFindingSignature = null;
        int eocdStart = -1;

        for (int endIdx = last.length - MIN_EOCD_SIZE; endIdx >= 0 && foundEocdSignature == -1;
             endIdx--) {
            /*
             * Remember: little endian...
             */
            if (last[endIdx] == EOCD_SIGNATURE[3]
                    && last[endIdx + 1] == EOCD_SIGNATURE[2]
                    && last[endIdx + 2] == EOCD_SIGNATURE[1]
                    && last[endIdx + 3] == EOCD_SIGNATURE[0]) {

                /*
                 * We found a signature. Try to read the EOCD record.
                 */

                foundEocdSignature = endIdx;
                ByteBuffer eocdBytes =
                        ByteBuffer.wrap(last, foundEocdSignature, last.length - foundEocdSignature);

                try {
                    eocd = new Eocd(eocdBytes);
                    //替换
                    eocdStart = (int)  (uc.getContentLength() - lastToRead + foundEocdSignature);


                    /*
                     * Make sure the EOCD takes the whole file up to the end. Log an error if it
                     * doesn't.
                     */
                    //替换
                    if (eocdStart + eocd.getEocdSize() != uc.getContentLength()) {
                        log.info("EOCD starts at "
                                + eocdStart
                                + " and has "
                                + eocd.getEocdSize()
                                + " bytes, but file ends at "
                                + uc.getContentLength()
                                + ".");
                    }
                    log.info("readEocd: found EOCD at " + Long.toHexString(eocdStart) + " : directoryOffset=" + Long.toHexString(eocd.getDirectoryOffset()) +
                            " : directorySize=" + Long.toHexString(eocd.getDirectorySize()));
                } catch (IOException e) {
                    if (errorFindingSignature != null) {
                        e.addSuppressed(errorFindingSignature);
                    }

                    errorFindingSignature = e;
                    foundEocdSignature = -1;
                    eocd = null;
                }
            }
        }

        if (foundEocdSignature == -1) {
            throw new IOException("EOCD signature not found in the last "
                    + lastToRead + " bytes of the file.", errorFindingSignature);
        }

        /*
         * Look for the Zip64 central directory locator. If we find it, then this file is a Zip64
         * file and we do not support it.
         */
        int zip64LocatorStart = eocdStart - ZIP64_EOCD_LOCATOR_SIZE;
        if (zip64LocatorStart >= 0) {
//            log.info("readEocd: search Zip64 EOCD at " + Long.toHexString(zip64LocatorStart));
            if (LittleEndianUtils.readUnsigned4Le(ByteBuffer.wrap(last, foundEocdSignature - ZIP64_EOCD_LOCATOR_SIZE, 4)) ==
                    ZIP64_EOCD_LOCATOR_SIGNATURE) {
                throw new IOException(
                        "Zip64 EOCD locator found but Zip64 format is not supported.");
            } else {
                log.info("readEocd: no Zip64 EOCD");
            }
        }

        return eocd;
    }

    /**
     * Reads the zip's central directory and populates the {@link #directoryEntry} variable. This
     * method can only be called after the EOCD has been read. If the central directory is empty
     * (if there are no files on the zip archive), then {@link #directoryEntry} will be set to
     * {@code null}.
     *
     * @throws IOException failed to read the central directory
     */
    private CentralDirectory readCentralDirectory(Eocd eocd) throws IOException {
//        log.info("readCentralDirectory: offset=" + Long.toHexString(eocd.getDirectoryOffset()) + ", size=" + Long.toHexString(eocd.getDirectorySize()));
        long dirSize = eocd.getDirectorySize();
        if (dirSize > Integer.MAX_VALUE) {
            throw new IOException("Cannot read central directory with size " + dirSize + ".");
        }

        long centralDirectoryEnd = eocd.getDirectoryOffset() + dirSize;

        byte[] directoryData = new byte[(int)  dirSize];
        directFullyRead(eocd.getDirectoryOffset(), directoryData);

        CentralDirectory directory =
                CentralDirectory.makeFromData(
                        ByteBuffer.wrap(directoryData),
                        eocd.getTotalRecords());
        return directory;
    }

    /**
     * Reads exactly {@code data.length} bytes of data, failing if it was not possible to read all
     * the requested data.
     *
     * @param offset the offset at which to start reading
     * @param data the array that receives the data read
     * @throws IOException failed to read some data or there is not enough data to read
     */
    public void directFullyRead(long offset,  byte[] data) throws IOException {
//        log.info("offset:"+offset+"end:"+(data.length+offset));
        new ArcSyncHttpClient().downloadRead(downurl, data,offset,data.length+offset);

    }

    /**
     * Reads exactly {@code dest.remaining()} bytes of data, failing if it was not possible to read
     * all the requested data.
     *
     * @param offset the offset at which to start reading
     * @param dest the output buffer to fill with data
     * @throws IOException failed to read some data or there is not enough data to read
     */
    public void directFullyRead(long offset,  ByteBuffer dest) throws IOException {
        if (!dest.hasRemaining()) {
            return;
        }
        FileChannel fileChannel = inp.getChannel();
        while (dest.hasRemaining()) {
            fileChannel.position(offset);
            int chunkSize = fileChannel.read(dest);
//            log.info(">>>>>>>>readfile: offset=" + Long.toHexString(offset) + ", chunkSize=" + Long.toHexString(chunkSize));
            if (chunkSize == -1) {
                throw new EOFException(
                        "Failed to read " + dest.remaining() + " more bytes: premature EOF");
            }
            offset += chunkSize;
        }
    }
}
