package cn.ltian.zxing;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

public class QRCode {

    /**
     * 创建二维码，写入文件
     * @param content 文本信息
     * @param format 图片格式
     * @param width 宽度
     * @param heigth 高度
     * @param filePath 文件地址
     */
    public static void CreateQRCode(String content,String format,int width,int heigth,String filePath){

//        int width = 300;
//        int heigth = 300;
//        String format = "png";
//        String content = "www.baidu.com";

        //定义二维码的参数
        HashMap hints = new HashMap();
        hints.put(EncodeHintType.CHARACTER_SET,"UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN,2);

        try{

            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE,width,heigth,hints);

            Path file;
            file = new File(filePath).toPath();

//            writeToFile(bitMatrix,format,file);
            MatrixToImageWriter.writeToPath(bitMatrix,format,file);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
