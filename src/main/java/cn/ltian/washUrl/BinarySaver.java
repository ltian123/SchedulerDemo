package cn.ltian.washUrl;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 未用
 */
public class BinarySaver {

    private final static String url = "http://b9.market.xiaomi.com/download/AppStore/01f3d51a70e3705e58fabfcbf73389bbb4c40440b/com.wandoujia.eyepetizer.apk";

    public static void main(String[] args) {
        try {
            URL root = new URL(url);
            saveBinary(root);
        } catch (MalformedURLException e) {
            // TODO: handle exception
            System.out.println(url + "is not URL");
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println(e);
        }
    }

    public static void saveBinary(URL u) throws IOException {
        // TODO Auto-generated method stub
        URLConnection uc = u.openConnection();
        String contentType = uc.getContentType();
        int contentLength = uc.getContentLength();
        /*
         * 可以限制不下载哪种文本文件
        if (contentType.startsWith("text/") || contentLength == -1) {
            throw new IOException("This is not a binary file.");
        }*/
        System.out.println("file size is:"+uc.getContentLength());//打印文件长度
        try (InputStream raw = uc.getInputStream()) {
            InputStream in = new BufferedInputStream(raw);
//            Reader r = new InputStreamReader(in);

            byte[] data = new byte[contentLength];
            int offset = 0;
            while (offset < contentLength) {
                int bytesRead = in.read(data, offset, data.length - offset);
                if (bytesRead == -1) {
                    break;
                }
                offset += bytesRead;
            }

            if (offset != contentLength) {
                throw new IOException("Only read " + offset
                        + " bytes; Expected " + contentLength + " bytes");
            }
            String filename = "D:\\ltian\\test\\存储位置1.apk";
            try (FileOutputStream fout = new FileOutputStream(filename)) {
                fout.write(data);
                fout.flush();
            }
        }
    }

}
