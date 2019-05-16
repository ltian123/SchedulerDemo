package cn.ltian.washUrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 执行jar，处理二进制文件
 * ltian
 */
public class AXMLPrinter2 {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger("AXMLPrinter2");
    /**
     *  处理二进制文件，利用AXMLPrinter2.jar，执行，解析为txt格式，
     * @param where 二进制文件位置
     * @param toWhere 需要存放的txt文件位置
     * @param jarurl jar位置
     */
    public  void dealXML(String where,String toWhere,String jarurl){

        Process process;
        try {
            System.out.println("java -jar"+jarurl+"  "+where);
            process = Runtime.getRuntime().exec(  "java -jar"+jarurl+"  "+where);
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader =
                    new BufferedReader(inputStreamReader);
            String line;
            File file1 = new File(toWhere);
            Writer writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(file1), "UTF-8"));
            while ((line=bufferedReader.readLine()) != null) {
                writer.write(line+"\n",0,(line+"\n").length());
                writer.flush();
            }
            inputStreamReader.close();
            bufferedReader.close();
            writer.close();
            process.waitFor();
        } catch (IOException e) {
            log.error("!!!dealXML()-IOException,error={}",e.getMessage());
        } catch (InterruptedException e) {
            log.error("!!!dealXML()-InterruptedException,error={}",e.getMessage());
        }
    }


}
