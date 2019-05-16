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

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Utilities to read and write 16 and 32 bit integers with support for little-endian
 * encoding, as used in zip files. Zip files actually use unsigned data types. We use Java's native
 * (signed) data types but will use long (64 bit) to ensure we can fit the whole range.
 */
public class LittleEndianUtils {
    /**
     * Utility class, no constructor.
     */
    private LittleEndianUtils() {
    }

    /**
     * Reads 4 bytes in little-endian format and converts them into a 32-bit value.
     *
     * @param bytes from where should the bytes be read; the first 4 bytes of the source will be
     * read
     * @return the 32-bit value
     * @throws IOException failed to read the value
     */
    public static long readUnsigned4Le( ByteBuffer bytes) throws IOException {

        if (bytes.remaining() < 4) {
            throw new EOFException("Not enough data: 4 bytes expected, " + bytes.remaining()
                    + " available.");
        }

        byte b0 = bytes.get();
        byte b1 = bytes.get();
        byte b2 = bytes.get();
        byte b3 = bytes.get();
        long r = (b0 & 0xff) | ((b1 & 0xff) << 8) | ((b2 & 0xff) << 16) | ((b3 & 0xffL) << 24);
        return r;
    }

    /**
     * Reads 2 bytes in little-endian format and converts them into a 16-bit value.
     *
     * @param bytes from where should the bytes be read; the first 2 bytes of the source will be
     * read
     * @return the 16-bit value
     * @throws IOException failed to read the value
     */
    public static int readUnsigned2Le( ByteBuffer bytes) throws IOException {

        if (bytes.remaining() < 2) {
            throw new EOFException(
                    "Not enough data: 2 bytes expected, "
                            + bytes.remaining()
                            + " available.");
        }

        byte b0 = bytes.get();
        byte b1 = bytes.get();
        int r = (b0 & 0xff) | ((b1 & 0xff) << 8);

        return r;
    }

    /**
     * Writes 4 bytes in little-endian format, converting them from a 32-bit value.
     *
     * @param output the output stream where the bytes will be written
     * @param value the 32-bit value to convert
     * @throws IOException failed to write the value data
     */
    public static void writeUnsigned4Le( ByteBuffer output, long value)
            throws IOException {

        output.put((byte) (value & 0xff));
        output.put((byte) ((value >> 8) & 0xff));
        output.put((byte) ((value >> 16) & 0xff));
        output.put((byte) ((value >> 24) & 0xff));
    }

    /**
     * Writes 2 bytes in little-endian format, converting them from a 16-bit value.
     *
     * @param output the output stream where the bytes will be written
     * @param value the 16-bit value to convert
     * @throws IOException failed to write the value data
     */
    public static void writeUnsigned2Le( ByteBuffer output, int value)
            throws IOException {

        output.put((byte) (value & 0xff));
        output.put((byte) ((value >> 8) & 0xff));
    }
}
