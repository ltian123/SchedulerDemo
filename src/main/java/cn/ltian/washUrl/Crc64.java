package cn.ltian.washUrl;

/**
 * crc64算法
 * ltian
 */
public class Crc64 {

    private static final long INITIALCRC = 0xFFFFFFFFFFFFFFFFL;
    private static long[] sCrcTable = new long[256];
    private static final long POLY64REV = 0x95AC9329AC4BC9B5L;

    static {
        // http://bioinf.cs.ucl.ac.uk/downloads/crc64/crc64.c
        long part;
        for (int i = 0; i < 256; i++) {
            part = i;
            for (int j = 0; j < 8; j++) {
                long x = ((int) part & 1) != 0 ? POLY64REV : 0;
                part = (part >> 1) ^ x;
            }
            sCrcTable[i] = part;
        }
    }

    public static final long crc64Long(String in) {
        if (in == null || in.length() == 0) {
            return 0;
        }
        return crc64Long(getBytes(in));
    }

    // 将String转换成字节数组
    public static byte[] getBytes(String in) {
        byte[] result = new byte[in.length() * 2];// 一个字符占两个字节
        int output = 0;
        for (char ch : in.toCharArray()) {
            result[output++] = (byte) (ch & 0xFF);// 取低8位
            result[output++] = (byte) (ch >> 8);// 取高8位
        }
        return result;
    }

    // crc64算法
    public static final long crc64Long(byte[] buffer) {
        long crc = INITIALCRC;
        for (int k = 0, n = buffer.length; k < n; ++k) {
            crc = sCrcTable[(((int) crc) ^ buffer[k]) & 0xff] ^ (crc >> 8);
        }
        return crc;
    }

    public static final String crc64Hex(String in) {
        long l = crc64Long(in);
        return Long.toHexString(l);
    }

}
