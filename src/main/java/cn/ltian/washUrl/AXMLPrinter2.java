package cn.ltian.washUrl;

import android.content.res.AXmlResourceParser;
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


            System.out.println("java -jar "+jarurl+"  "+where);
            process = Runtime.getRuntime().exec(  "java -jar "+jarurl+"  "+where);
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


    private static final float[] RADIX_MULTS = {
            0.0039063F, 3.051758E-005F, 1.192093E-007F, 4.656613E-010F };

    private static final String[] DIMENSION_UNITS = {
            "px", "dip", "sp", "pt", "in", "mm", "", "" };

    private static final String[] FRACTION_UNITS = {
            "%", "%p", "", "", "", "", "", "" };

    public  String  dealXMLReturn(byte[] zip)
    {
        StringBuffer str = new StringBuffer();
//        if (StringUtil.isEmpty(where)) {
//            log("Usage: AXMLPrinter <binary xml file>", new Object[0]);
//            return null;
//        }
        try {
            AXmlResourceParser parser = new AXmlResourceParser();
            InputStream fis =  new ByteArrayInputStream(zip);
            parser.open(fis);
            StringBuilder indent = new StringBuilder(10);
            String indentStep = "\t";
            while (true) {
                int type = parser.next();
                if (type == 1) {
                    break;
                }
                switch (type)
                {
                    case 0:
                        str.append(log("<?xml version=\"1.0\" encoding=\"utf-8\"?>", new Object[0]));
                        break;
                    case 2:
                        str.append(log("%s<%s%s", new Object[] { indent,
                                getNamespacePrefix(parser.getPrefix()), parser.getName() }));
                        indent.append("\t");

                        int namespaceCountBefore = parser.getNamespaceCount(parser.getDepth() - 1);
                        int namespaceCount = parser.getNamespaceCount(parser.getDepth());
                        for (int i = namespaceCountBefore; i != namespaceCount; i++) {
                            str.append(log("%sxmlns:%s=\"%s\"", new Object[] {
                                    indent,
                                    parser.getNamespacePrefix(i),
                                    parser.getNamespaceUri(i) }));
                        }

                        for (int i = 0; i != parser.getAttributeCount(); i++) {
                            str.append(log("%s%s%s=\"%s\"", new Object[] { indent,
                                    getNamespacePrefix(parser.getAttributePrefix(i)),
                                    parser.getAttributeName(i),
                                    getAttributeValue(parser, i) }));
                        }
                        str.append(log("%s>", new Object[] { indent }));
                        break;
                    case 3:
                        indent.setLength(indent.length() - "\t".length());
                        str.append(log("%s</%s%s>", new Object[] { indent,
                                getNamespacePrefix(parser.getPrefix()),
                                parser.getName() }));
                        break;
                    case 4:
                        str.append(log("%s%s", new Object[] { indent, parser.getText() }));
                    case 1:
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return str.toString();
    }

    private static String getNamespacePrefix(String prefix) {
        if ((prefix == null) || (prefix.length() == 0)) {
            return "";
        }
        return prefix + ":";
    }

    private static String getAttributeValue(AXmlResourceParser parser, int index) {
        int type = parser.getAttributeValueType(index);
        int data = parser.getAttributeValueData(index);
        if (type == 3) {
            return parser.getAttributeValue(index);
        }
        if (type == 2) {
            return String.format("?%s%08X", new Object[] { getPackage(data), Integer.valueOf(data) });
        }
        if (type == 1) {
            return String.format("@%s%08X", new Object[] { getPackage(data), Integer.valueOf(data) });
        }
        if (type == 4) {
            return String.valueOf(Float.intBitsToFloat(data));
        }
        if (type == 17) {
            return String.format("0x%08X", new Object[] { Integer.valueOf(data) });
        }
        if (type == 18) {
            return data != 0 ? "true" : "false";
        }
        if (type == 5) {
            return Float.toString(complexToFloat(data)) +
                    DIMENSION_UNITS[(data & 0xF)];
        }
        if (type == 6) {
            return Float.toString(complexToFloat(data)) +
                    FRACTION_UNITS[(data & 0xF)];
        }
        if ((type >= 28) && (type <= 31)) {
            return String.format("#%08X", new Object[] { Integer.valueOf(data) });
        }
        if ((type >= 16) && (type <= 31)) {
            return String.valueOf(data);
        }
        return String.format("<0x%X, type 0x%02X>", new Object[] { Integer.valueOf(data), Integer.valueOf(type) });
    }

    private static String getPackage(int id) {
        if (id >>> 24 == 1) {
            return "android:";
        }
        return "";
    }

    private  String log(String format, Object[] arguments) {
        String str = String.format(format, arguments);
        return str;
//        System.out.printf(format, arguments);
//        System.out.println();
    }

    public static float complexToFloat(int complex)
    {
        return (complex & 0xFFFFFF00) * RADIX_MULTS[(complex >> 4 & 0x3)];
    }


}
