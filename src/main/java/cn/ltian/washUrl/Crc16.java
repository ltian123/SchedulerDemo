package cn.ltian.washUrl;

/**
 * 未用
 */
public class Crc16 {

    private static int getCrc(byte[] data) {
        int high;
        int flag;
        // 16位寄存器，所有数位均为1
        int wcrc = 0xffff;
        for (int i = 0; i < data.length; i++) {
            // 16 位寄存器的高位字节
            high = wcrc >> 8;
            // 取被校验串的一个字节与 16 位寄存器的高位字节进行“异或”运算
            wcrc = high ^ data[i];

            for (int j = 0; j < 8; j++) {
                flag = wcrc & 0x0001;
                // 把这个 16 寄存器向右移一位
                wcrc = wcrc >> 1;
                // 若向右(标记位)移出的数位是 1,则生成多项式 1010 0000 0000 0001 和这个寄存器进行“异或”运算
                if (flag == 1)
                    wcrc ^= 0xa001;
            }
        }

        return wcrc;
    }

    public static final int crc64Long(String in) {
        if (in == null || in.length() == 0) {
            return 0;
        }
        return getCrc(getBytes(in));
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


    public static void main(String[] args) {

    }

}
