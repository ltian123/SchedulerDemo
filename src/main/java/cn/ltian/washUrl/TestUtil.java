package cn.ltian.washUrl;

import java.util.Calendar;

public class TestUtil {

    public static void main(String[] args) {
//        String str1 = "";
//        String url_key = "/fs07/2016/04/06/5/106_1913f7505c25d7fb953f385981f4a93b";
//        str1 = new TestUtil().CreateKey(url_key);
//        System.out.println("key=="+str1);
//
//
//        String url_key2 = "p.gdown.baidu.com/42981f5f78ff650b5bbf86b040ff18aeb1476897d074009fe743157826a558c55096b952498a7a09932dc1d1cfb7cf8d86a80a1b4fb33a64a211f81f53be856182144e5f75ee3956f37ea3b79115d4c0d9acbf1572c1facae9d23a3c521fb3ee4dd4e14dc8d79310b95254170c580f4b4b78ec1f2c9b410d2a26e898ec377c293f042c6283f2456d78a469507e6260ab2d29df9dca9b23fb58453c0f9d54419d077e11187f6b7c4ef876ed062f35974fb8420f96f1a4d67c";
//        str1 = new TestUtil().CreateKey(url_key2);
//        System.out.println("key=="+str1);
//
//        String url_key3 = "11+——）（）（）（）";
//         str1 = new TestUtil().CreateKey(url_key3);
//        System.out.println("key=="+str1);
//
//        String url_key4 = "？212121fb70bdb0af2eede9ac36253ab0c/c1_22810";
//         str1 = new TestUtil().CreateKey(url_key4);
//        System.out.println("key=="+str1);
//
//        String url_key5 = "02121";
//        str1 = new TestUtil().CreateKey(url_key5);
//        System.out.println("key=="+str1);

        long nowTime = System.currentTimeMillis()/1000;
        System.out.println(nowTime);
            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.DATE, 0);// 设为当前月的1号
            calendar.add(Calendar.MONTH, -3);// 0表示当前月，-2就是当前月-2
//            calendar.set(Calendar.HOUR_OF_DAY, 0);
//            calendar.set(Calendar.MINUTE, 0);
//            calendar.set(Calendar.SECOND, 0);
//            calendar.set(Calendar.MILLISECOND, 0);
            long time =  calendar.getTimeInMillis()/1000;
        System.out.println(time);

    }

    /**
     * 传入str，获取redis分层key，wash-0   wash-15
     * @param str
     * @return
     */
    public static String CreateKey(String str){
        if(str == null || "".equals(str)){
            return "wash-1";
        }
        long washNum_1 = Crc64.crc64Long(str);
        long washNum_2 = (washNum_1 & 0X0F);
        return "wash-"+washNum_2;
    }

}
