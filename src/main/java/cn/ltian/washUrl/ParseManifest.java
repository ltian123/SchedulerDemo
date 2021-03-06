package cn.ltian.washUrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析manifest.xml文件，获取包名
 * ltian
 */
public class ParseManifest {
    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger("ParseManifest");

    private String appPackage;
    private List<String> permissions = new ArrayList();
    private List<String> activities = new ArrayList();



    /**
     * 解析入口activity
     * @param doc
     * @return
     */
    public  String findLaucherActivity(Document doc){
        Node activity = null;
        String sTem = "";
        NodeList categoryList = doc.getElementsByTagName("category");
        for(int i = 0; i < categoryList.getLength(); i++){
            Node category = categoryList.item(i);
            NamedNodeMap attrs  =category.getAttributes();
            for(int j = 0; j < attrs.getLength(); j++){
                if(attrs.item(j).getNodeName() == "android:name"){
                    if(attrs.item(j).getNodeValue().equals("android.intent.category.LAUNCHER")){
                        activity = category.getParentNode().getParentNode();
                        break;
                    }
                }
            }
        }
        if(activity != null){
            NamedNodeMap attrs  =activity.getAttributes();
            for(int j = 0; j < attrs.getLength(); j++){
                if(attrs.item(j).getNodeName() == "android:name"){
                    sTem = attrs.item(j).getNodeValue();
                }
            }
        }
        return sTem;
    }

    /**
     * 解析入口
     * @param xmlStr
     */
    public  void xmlHandle(String xmlStr){
        Document document = null;
        try {

            xmlStr = new String(xmlStr.getBytes(),"UTF-8");
            StringReader sr = new StringReader(xmlStr);
            InputSource is = new InputSource(sr);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            document = builder.parse(is);

            //加载xml文件
            NodeList permissionList = document.getElementsByTagName("uses-permission");
            NodeList activityAll = document.getElementsByTagName("activity");

            //获取权限列表
            for (int i = 0; i < permissionList.getLength(); i++) {
                Node permission = permissionList.item(i);
                permissions.add((permission.getAttributes()).item(0).getNodeValue());
            }

            //获取activity列表
            appPackage = (findPackage(document));
            for(int i = 0; i < activityAll.getLength(); i++){
                Node activity = activityAll.item(i);
                NamedNodeMap attrs  =activity.getAttributes();
                for(int j = 0; j < attrs.getLength(); j++){
                    if(attrs.item(j).getNodeName() == "android:name"){
                        String sTem = attrs.item(j).getNodeValue();
                        if(sTem.startsWith(".")){
                            sTem = appPackage+sTem;
                        }
                        activities.add(sTem);
                    }
                }
            }
            String s = findLaucherActivity(document);
            if(s.startsWith(".")){
                s = appPackage+s;
            }
            //移动入口类至首位
            activities.remove(s);
            activities.add(0, s);
        } catch (ParserConfigurationException e) {
            log.error("!!!xmlHandle()-ParserConfigurationException,error={}",e.getMessage());
        } catch (SAXException e) {
            log.error("!!!xmlHandle()-SAXException,error={}",e.getMessage());
        } catch (IOException e) {
            log.error("!!!xmlHandle()-IOException,error={}",e.getMessage());
        }
    }
    public static String output(ParseManifest a){
        return a.appPackage;
    }
    /**
     * 解析包名
     *
     * @param doc
     * @return
     */
    public static String findPackage(Document doc) {
        Node node = doc.getFirstChild();
        NamedNodeMap attrs = node.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            if (attrs.item(i).getNodeName() == "package") {
                return attrs.item(i).getNodeValue();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        new ParseManifest().xmlHandle("D:\\ltian\\tomcat\\tomcat-7\\webapps\\WEB-INF\\classes\\dealDir\\1557882432070\\1557882432070-3");
    }
}
