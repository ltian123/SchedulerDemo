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

    public static void main(String[] args) {

        String url = "http://p.gdown.baidu.com/42981f5f78ff650b5bbf86b040ff18aeb1476897d074009fe743157826a558c55096b952498a7a09932dc1d1cfb7cf8d86a80a1b4fb33a64a211f81f53be856182144e5f75ee3956f37ea3b79115d4c0d9acbf1572c1facae9d23a3c521fb3ee4dd4e14dc8d79310b95254170c580f4b4b78ec1f2c9b410d2a26e898ec377c293f042c6283f2456d78a469507e6260ab2d29df9dca9b23fb58453c0f9d54419d077e11187f6b7c4ef876ed062f35974fb8420f96f1a4d67c";
        String dealDir="D:\\ltian\\test\\";
        String jarurl = " D:\\ltian\\test\\0509\\AXMLPrinter2.jar";
        byte[] b = new byte[4];
        ArcSyncHttpClient arcSyncHttpClient = new ArcSyncHttpClient();
        log.info("url:"+url);
        log.info("开始读取前4个字节，判断是不是apk包");
        System.out.println("startTime:==================>"+System.currentTimeMillis());
        arcSyncHttpClient.downloadRead(url,b,0,3);
        boolean isApk = arcSyncHttpClient.isApk(b);
        if(isApk == false){
            log.info(url+":不是apk下载地址");
            return;
        }
        log.info("is apk");
        log.info("开始处理");
        String name = arcSyncHttpClient.dealApkGetName(url,dealDir,jarurl);
        System.out.println("endTime:==================>"+System.currentTimeMillis());
        log.info("packagename："+name);

    }

    /**
     * 处理apk下载地址，返回包名
     * @param downloadUrl
     * @param dealDir
     * @param jarurl
     * @return
     */
    public String dealApkGetName(String downloadUrl,String dealDir,String jarurl){

        String name = null;
        String salt = System.currentTimeMillis()+"";
        dealDir = dealDir+salt+"\\";
        try {
            File file =new File(dealDir);
            file .mkdir();
            log.info("开始读取压缩文件部分，获取AndroidManifest.xml");
            new ZFile(downloadUrl).readData("AndroidManifest.xml",dealDir,salt);
            log.info("开始处理二进制AndroidManifest.xml");
            new AXMLPrinter2().dealXML(dealDir+salt+"-2",dealDir+salt+"-3",jarurl);
            ParseManifest a = new ParseManifest();
            log.info("开始解析AndroidManifest.xml");
            a.xmlHandle(dealDir+salt+"-3");
            name =ParseManifest.output(a);
            //执行删除文件夹

        } catch (Exception e) {
            log.error("!!!dealApkGetName(),error={}",e.getMessage());
        }finally {
            FileUtil.deleteDirectory(dealDir);
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
