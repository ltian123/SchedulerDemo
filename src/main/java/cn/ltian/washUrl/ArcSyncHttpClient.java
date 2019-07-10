package cn.ltian.washUrl;

import cn.ltian.washUrl.adirectmanifest.ZFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 获取http链接中 apk下载类型 包名
 * ltian
 */
public class ArcSyncHttpClient {


    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger("ArcSyncHttpClient");

    public static void main(String[] args) throws Exception{

        String url = "http://b8.market.mi-img.com/download/AppStore/01068d59b91754cb006b933a635314ce44efb610a/com.alibaba.wireless.apk";
        byte[] b = new byte[4];
        ArcSyncHttpClient arcSyncHttpClient = new ArcSyncHttpClient();
        log.info("url:"+url);
        log.info("开始读取前4个字节，判断是不是apk包");
        Long startTime = System.currentTimeMillis();
        System.out.println("startTime:==================>"+startTime);
        arcSyncHttpClient.downloadRead(url,b,0,3);
        boolean isApk = arcSyncHttpClient.isApk(b);
        if(isApk == false){
            log.info(url+":不是apk下载地址");
            return;
        }
        log.info("is apk");
        log.info("开始处理");
        String name = arcSyncHttpClient.dealApkGetName(url);
        System.out.println("time:==================>"+(System.currentTimeMillis()-startTime));
        log.info("packagename："+name);
//        System.setOut(new PrintStream(new File("D:\\ltian\\tomcat\\tomcat-7\\webapps\\WEB-INF\\1.txt")));
//        System.out.println("你好");
//        System.out.println("请进");



    }

    /**
     * 处理apk下载地址，返回包名
     * @param downloadUrl
     * @return
     */
    public String dealApkGetName(String downloadUrl){

        String name = null;
        try {
            log.info("开始读取压缩文件部分，获取AndroidManifest.xml");
            byte[] zip = new ZFile(downloadUrl).readData("AndroidManifest.xml");
            log.info("开始处理二进制AndroidManifest.xml");
//             new AXMLPrinter2().dealXML(dealDir+salt+"-2",dealDir+salt+"-3",jarurl);
            String str = new AXMLPrinter2().dealXMLReturn(zip);
            System.out.println("xml:"+str);
            ParseManifest a = new ParseManifest();
            log.info("开始解析AndroidManifest.xml");
            a.xmlHandle(str);
            name =ParseManifest.output(a);

        } catch (Exception e) {
            log.error("!!!dealApkGetName(),error={}",e.getMessage());
        }finally {
        }

        return name;
    }


    /**
     * 判断是不是apk文件
     * @param b
     * @return
     */
    public boolean isApk(byte[] b){
        if(b == null||b.length<4){
            return false;
        }
        System.out.println(b[0]+",1:"+b[1]+",2:"+b[2]+",3:"+b[3]+",");
        if(b[0] != 80){
            return false;
        }
        if(b[1] != 75){
            return false;
        }
        if(b[2] != 3){
            return false;
        }
        if(b[3] != 4){
            return false;
        }
        return true;
    }



    /**
     * 短点下载
     * @param fileName 文件名
     * @param filePath 文件路径
     * @param remoteUrl 网络地址
     * @param start  开始大小
     * @param end      结束大小
     */
    public  void download( String fileName, String filePath,String remoteUrl, long start ,long end) {
        byte[] buf = new byte[10240];
        HttpURLConnection httpURLConnection;
        URL url;
        BufferedInputStream bis;
        int size;
        RandomAccessFile rndFile;
        // 下载文件
        try {
            url = new URL(remoteUrl);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            // 设置User-Agent
            httpURLConnection.setRequestProperty("User-Agent", "Net");
            // 设置续传开始
            httpURLConnection.setRequestProperty("Range", "bytes=" + start + "-"+ end);
            // 获取输入流
            bis = new BufferedInputStream(httpURLConnection.getInputStream());
            rndFile = new RandomAccessFile(filePath + "\\" + fileName, "rw");
            rndFile.seek(0);
            int i = 0;
            while ((size = bis.read(buf)) != -1) {
            //if (i > 500) break;
                rndFile.write(buf, 0, size);
                i++;
            }
            httpURLConnection.disconnect();
            bis.close();
            rndFile.close();
        } catch (Exception e) {
            log.error("!!!download(),error={}",e.getMessage());
        }
    }

    /**
     * 短点读
     * @param remoteUrl 网络地址
     * @param data  字节数组
     * @param start  开始大小
     * @param end      结束大小
     */
    public  byte[] downloadRead( String remoteUrl,byte[] data, long start ,long end) {
        log.info("start:"+start+",end:"+end);
        HttpURLConnection httpURLConnection;
        URL url;
        BufferedInputStream bis;
        // 下载文件
        try {
            url = new URL(remoteUrl);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            // 设置User-Agent
            httpURLConnection.setRequestProperty("User-Agent", "Net");
            // 设置续传开始
            httpURLConnection.setRequestProperty("Range", "bytes=" + start + "-"+ end);
            //设置超时时间，6秒
            httpURLConnection.setConnectTimeout(6000);
            httpURLConnection.setReadTimeout(6000);
            // 获取输入流
            bis = new BufferedInputStream(httpURLConnection.getInputStream());
            int offset = 0;
            while (offset < data.length) {
                int bytesRead = bis.read(data, offset, data.length - offset);
                if (bytesRead == -1) {
                    break;
                }
                offset += bytesRead;
            }
            httpURLConnection.disconnect();
            bis.close();
        } catch (Exception e) {
            log.error("!!!downloadRead(),error={}",e.getMessage());
        }
        return data;
    }

    /**
     * 读取文件，返回字节数组
     * @param filePath
     * @return
     * @throws IOException
     */
    private  byte[] InputStream2ByteArray(String filePath) throws IOException {

        InputStream in = new FileInputStream(filePath);
        byte[] data = toByteArray(in);
        in.close();

        return data;
    }

    private  byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }
}
