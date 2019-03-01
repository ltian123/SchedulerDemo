package cn.ltian.zxing;

public class Test {


    public static void main(String[] args) {
        //生成二维码测试
        int width = 300;
        int heigth = 300;
        String format = "png";
        String content = "https://www.baidu.com";
        String filePath = "D://ltian/test/test2.png";
        QRCode.CreateQRCode(content,format,width,heigth,filePath);


        //读取二维码测试
//        String filePath1 = "D://ltian/test/test1.png";
//        ReadQRCode.readQRcode(filePath1);
    }
}
