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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * The ZipField class represents a field in a record in a zip file. Zip files are made with records
 * that have fields. This class makes it easy to read, write and verify field values.
 * <p>
 * There are two main types of fields: 2-byte fields and 4-byte fields. We represent each one as
 * a subclass of {@code ZipField}, {@code F2} for the 2-byte field and {@code F4} for the 4-byte
 * field. Because Java's {@code int} data type is guaranteed to be 4-byte, all methods use Java's
 * native {@link int} as data type.
 * <p>
 * For each field we can either read, write or verify. Verification is used for fields whose value
 * we know. Some fields, <em>e.g.</em> signature fields, have fixed value. Other fields have
 * variable values, but in some situations we know which value they have. For example, the last
 * modification time of a file's local header will have to match the value of the file's
 * modification time as stored in the central directory.
 * <p>
 * Because records are compact, <em>i.e.</em> fields are stored sequentially with no empty spaces,
 * fields are generally created in the sequence they exist and the end offset of a field is used
 * as the offset of the next one. The end of a field can be obtained by invoking
 * {@link #endOffset()}. This allows creating fields in sequence without doing offset computation:
 * <pre>
 * ZipField.F2 firstField = new ZipField.F2(0, "First field");
 * ZipField.F4 secondField = new ZipField(firstField.endOffset(), "Second field");
 * </pre>
 */
abstract class ZipField {

    /**
     * Field name. Used for providing (more) useful error messages.
     */
    
    private final String name;

    /**
     * Offset of the file in the record.
     */
    protected final int offset;

    /**
     * Size of the field. Only 2 or 4 allowed.
     */
    private final int size;

    /**
     * If a fixed value exists for the field, then this attribute will contain that value.
     */
    
    private final Long expected;

    /**
     * Creates a new field that does not contain a fixed value.
     *
     * @param offset the field's offset in the record
     * @param size the field size
     * @param name the field's name
     */
    ZipField(int offset, int size,  String name) {

        this.name = name;
        this.offset = offset;
        this.size = size;
        expected = null;
    }

    /**
     * Creates a new field that contains a fixed value.
     *
     * @param offset the field's offset in the record
     * @param size the field size
     * @param expected the expected field value
     * @param name the field's name
     */
    ZipField(int offset, int size, long expected,  String name) {

        this.name = name;
        this.offset = offset;
        this.size = size;
        this.expected = expected;
    }

    /**
     * Advances the position in the provided byte buffer by the size of this field.
     *
     * @param bytes the byte buffer; at the end of the method its position will be greater by
     * the size of this field
     * @throws IOException failed to advance the buffer
     */
    void skip( ByteBuffer bytes) throws IOException {
        if (bytes.remaining() < size) {
            throw new IOException("Cannot skip field " + name + " because only "
                    + bytes.remaining() + " remain in the buffer.");
        }

        bytes.position(bytes.position() + size);
    }

    /**
     * Reads a field value.
     *
     * @param bytes the byte buffer with the record data; after this method finishes, the buffer
     * will be positioned at the first byte after the field
     * @return the value of the field
     * @throws IOException failed to read the field
     */
    long read( ByteBuffer bytes) throws IOException {
        if (bytes.remaining() < size) {
            throw new IOException("Cannot skip field " + name + " because only "
                    + bytes.remaining() + " remain in the buffer.");
        }

        bytes.order(ByteOrder.LITTLE_ENDIAN);

        long r;
        if (size == 2) {
            r = LittleEndianUtils.readUnsigned2Le(bytes);
        } else {
            r =  LittleEndianUtils.readUnsigned4Le(bytes);
        }

        return r;
    }


    /**
     * Verifies that the field at the current buffer position has the expected value. The field
     * must have been created with the constructor that defines the expected value.
     *
     * @param bytes the byte buffer with the record data; after this method finishes, the buffer
     * will be positioned at the first byte after the field
     */
    void verify( ByteBuffer bytes) throws IOException {
        verify(bytes, expected);
    }

    /**
     * Verifies that the field has an expected value.
     *
     * @param bytes the byte buffer with the record data; after this method finishes, the buffer
     * will be positioned at the first byte after the field
     * @param expected the value we expect the field to have
     */
    void verify(
             ByteBuffer bytes,
            long expected) throws IOException {
        long r = read(bytes);
        if (r != expected) {
            String error =
                    String.format(
                            "Incorrect value for field '%s': value is %s but %s expected.",
                            name,
                            r,
                            expected);

                throw new IOException(error);
        }
    }

    /**
     * Writes the value of the field.
     *
     * @param output where to write the field; the field will be written at the current position
     * of the buffer
     * @param value the value to write
     * @throws IOException failed to write the value in the stream
     */
    void write( ByteBuffer output, long value) throws IOException {


        if (size == 2) {
            LittleEndianUtils.writeUnsigned2Le(output, (int) value);
        } else {
            LittleEndianUtils.writeUnsigned4Le(output, value);
        }
    }

    /**
     * Writes the value of the field. The field must have an expected value set in the constructor.
     *
     * @param output where to write the field; the field will be written at the current position
     * of the buffer
     * @throws IOException failed to write the value in the stream
     */
    void write( ByteBuffer output) throws IOException {
        write(output, expected);
    }

    /**
     * Obtains the offset at which the field starts.
     *
     * @return the start offset
     */
    int offset() {
        return offset;
    }

    /**
     * Obtains the offset at which the field ends. This is the exact offset at which the next
     * field starts.
     *
     * @return the end offset
     */
    int endOffset() {
        return offset + size;
    }

    /**
     * Concrete implementation of {@link ZipField} that represents a 2-byte field.
     */
    static class F2 extends ZipField {

        /**
         * Creates a new field.
         *
         * @param offset the field's offset in the record
         * @param name the field's name
         */
        F2(int offset,  String name) {
            super(offset, 2, name);
        }

        /**
         * Creates a new field that contains a fixed value.
         *
         * @param offset the field's offset in the record
         * @param expected the expected field value
         * @param name the field's name
         */
        F2(int offset, long expected,  String name) {
            super(offset, 2, expected, name);
        }
    }

    /**
     * Concrete implementation of {@link ZipField} that represents a 4-byte field.
     */
    static class F4 extends ZipField {
        /**
         * Creates a new field.
         *
         * @param offset the field's offset in the record
         * @param name the field's name
         */
        F4(int offset,  String name) {
            super(offset, 4, name);
        }

        /**
         * Creates a new field that contains a fixed value.
         *
         * @param offset the field's offset in the record
         * @param expected the expected field value
         * @param name the field's name
         */
        F4(int offset, long expected,  String name) {
            super(offset, 4, expected, name);
        }
    }
}
