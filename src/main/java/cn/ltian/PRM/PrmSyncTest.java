package cn.ltian.PRM;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class PrmSyncTest {


    public static void main(String[] args) throws Exception{
//        ReadExcelUtil readExcelUtil = new ReadExcelUtil();
//        File file = new File("D:\\ltian\\test\\1.xls");
//        List<List<Object>> list = readExcelUtil.read2003Excel(file,0,0);
//        Iterator<List<Object>> it = list.iterator();
//        String str = "";
//        while(it.hasNext()) {
//            String str2 = "";
//            List<Object> l = (List<Object>) it.next();
//            Iterator<Object> it2 = l.iterator();
////            str = str +it2.next().toString()+",";
////            System.out.println(it2.next());
//            str2 = it2.next().toString();
//            System.out.println("id="+str2);
//            System.out.println("休眠2秒");
//            Thread.sleep(2*1000);//休眠2秒
//            sync(str2);
//        }
        System.out.print("779738");
        sync("779738");
    }


    public static  String sync(String str) throws Exception{

        String IP = "http://developer.nubia.com";
        String CONNECT = "/api/component/PRMSync";
        long now = System.currentTimeMillis();//当前时间
//        long now = 1551938937326L;
        HttpGet post = new HttpGet(IP + CONNECT+"?ids="+str);
        post.setHeader("accept", "application/json");
        post.setHeader("Accept-Charset", "UTF-8");
        post.setHeader("Content-Type", "application/json;charset=UTF-8");

//        List<NameValuePair> list = new ArrayList<NameValuePair>();
//        list.add(new BasicNameValuePair("names",str));
//        post.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
//        // 设置CookieStore
        BasicCookieStore cookieStore = new BasicCookieStore();
//        // 构造HttpClient
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
        HttpResponse response1 = httpClient.execute(post);
        System.out.println("同步prm:请求 后的数据："+ EntityUtils.toString(response1.getEntity())+"<====请求 后的数据状态码："+ response1.getStatusLine());
        return "";
    }



}
