package cn.ltian.zxing;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

public class ReadQRCode {

    /**
     * 读取二维码信息
     * @param filePath 文件地址
     * @return
     */
    public static Result readQRcode(String filePath) {
        Result result = null;
        try {
            MultiFormatReader multiFormatReader = new MultiFormatReader();

            File file = new File(filePath);

            BufferedImage image = ImageIO.read(file);

            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));

            HashMap hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            result = multiFormatReader.decode(binaryBitmap, hints);


            System.out.println("解析结果：" + result.toString());
            System.out.println("二维码格式类型：" + result.getBarcodeFormat());
            System.out.println("二维码文本类型：" + result.getText());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
