package cn.ltian.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TestRes {

    public static void main(String[] args) throws Exception {

        /*
        公钥私钥
         */
        String priKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKEtPxYiHIDXUVPNJ68plqur2MSd+vF70I3TKDaMcTQz+VjXbdR1UUboQwIwdbs3XzcnSH7tjNaHKBm/W49tjpZ389k0WI+KcEGrSWDrUL0pngbtKwr/3DD0qQ/VCggJlHB52Ln32N2yjraqJ0s3l8cWmydZoAU+TPRzyMXmHB03AgMBAAECgYEAgm2FAbo06qZTb8cy9/JltSJpViKpscUKLdrUTeA0s5PjkdszS5xsWz8VCq6AdzIF4THu3pgJQok5ECzkQRrdKOlg7XKIn+cNQPRMff8ICcAe0+FF5IiM/WZKzKlu7YiFA8BVWy/msAZdGxdqef6LIA9A5kz38PIowfdI1RGuTPECQQDVLRS9JDUC+gQ1RSUi/8fuaKPh5qQhRAJEXbN0CeQ7GgHGYqITYg5e4OSPyFG+zhgavCb+iXnbUDjHleFqy0XzAkEAwY4EIzjv8FlYVyUnIm2giMvizpd8lttFAMBa9WgZ4n0db2xEhv4uYvTDJWrfDZUVqfhTk1UJUzQ+ehvp8YjIrQJAHezmLGcYygIu9Qtv2yns9xrZZqzp/YF+j/pXZsK8t08/UIooFnB9c/Q+IbENSKvF1nrzPmguIwtv7RStQECo4QJAUaeYF0Pt9Xe7zQffeqbFEvDwvjcqWHoooiiENwOz59jgkiEax9hzuUuJCEAN2a0LdqlMmNtHE9L2iI0+JKeQAQJAMyzmJ7VrOE/3+UnRYIpyHiSGsW2xqMOXiLbYXafzbic3vquijYOkAmdvnEkxQ6Cq0OlKsAti2bu9WSpKZ4zceg==";//拷贝的私钥
        String pubKey ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQChLT8WIhyA11FTzSevKZarq9jEnfrxe9CN0yg2jHE0M/lY123UdVFG6EMCMHW7N183J0h+7YzWhygZv1uPbY6Wd/PZNFiPinBBq0lg61C9KZ4G7SsK/9ww9KkP1QoICZRwedi599jdso62qidLN5fHFpsnWaAFPkz0c8jF5hwdNwIDAQAB";

        /*

         */
        String IP = "http://deveniutesttmp.nubia.com";
        String CONNECT = "/dev/publish/connect";
        long now = System.currentTimeMillis();//当前时间
//        long now = 1551938937326L;
        HttpPost post = new HttpPost(IP + CONNECT);
        StringKey str = new StringKey();
        str.setTime(now);
        str.setClientId("5584594");//开发者reference_id
        String content = "5584594" + now;
        //公钥加密，
        byte[] signpri_pub =RSACoder.encryptByPublicKey(content.getBytes(), Base64.decodeBase64(pubKey));
//        byte[] signpri_pub =encryptByRSA1(Base64.decodeBase64(priKey),Base64.decodeBase64(content));
        System.out.println("ltian Sign公钥加密："+Base64.encodeBase64String(signpri_pub)+"-------");
        str.setSign(Base64.encodeBase64String(signpri_pub));

        //私钥解密，
        byte[] newSourcepri_pub = RSACoder.decryptByPrivateKey(Base64.decodeBase64(str.getSign()), Base64.decodeBase64(priKey));
        System.out.println("ltian Sign私钥解密："+new String(newSourcepri_pub));
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();
        System.out.println("ltian jiemi 后的数据："+gson.toJson(str));
        StringEntity entity = new StringEntity(gson.toJson(str), Charset.forName("UTF-8"));


        post.setEntity(entity);
        post.setHeader("accept", "application/json");
        post.setHeader("Accept-Charset", "UTF-8");

        // 设置CookieStore
        BasicCookieStore cookieStore = new BasicCookieStore();
        // 构造HttpClient
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
        // 发起post请求
        HttpResponse response = httpClient.execute(post);
        System.out.println("请求1 后的数据："+ EntityUtils.toString(response.getEntity())+"请求1 后的数据状态码："+ response.getStatusLine());

//        new  TestRes().getRSA(now,"728706",pubKey);
        /**
         * 应用类别查询
         */
//        HttpGet post2 = new HttpGet(IP+"/dev/publish/category?layerId=15000&categoryLayer=2");
//        post2.setHeader("accept", "application/json");
//        post2.setHeader("Accept-Charset", "UTF-8");
////        post2.setHeader("Content-Type", "application/json;charset=UTF-8");
//
////        List<NameValuePair> list = new ArrayList<NameValuePair>();
////        list.add(new BasicNameValuePair("layerId","15000"));
////        list.add(new BasicNameValuePair("categoryLayer","2"));
////        post2.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
//
//        HttpResponse response1 = httpClient.execute(post2);
//        System.out.println("应用类别查询:请求2 后的数据："+EntityUtils.toString(response1.getEntity())+"请求2 后的数据状态码："+ response1.getStatusLine());
        /**
         * 应用详情查询
         */
//        HttpGet post3 = new HttpGet(IP+"/dev/publish/queryApp?packageName=io.dcloud.H503FEB96");
//        post3.setHeader("accept", "application/json");
//        post3.setHeader("Accept-Charset", "UTF-8");
//
////        List<NameValuePair> list3 = new ArrayList<NameValuePair>();
////        list3.add(new BasicNameValuePair("packageName","io.dcloud.H52D97B4D"));
////        post3.setEntity(new UrlEncodedFormEntity(list3, "UTF-8"));
//
//        HttpResponse response3 = httpClient.execute(post3);
//        System.out.println("应用详情查询:请求3 后的数据："+EntityUtils.toString(response3.getEntity())+"请求3 后的数据状态码："+ response3.getStatusLine());
        /**
         * 1.5应用审核状态查询接口
         */
//        HttpGet post4 = new HttpGet(IP+"/dev/publish/queryAppAudit?packageName=io.dcloud.H503FEB96");
//        post4.setHeader("accept", "application/json");
//        post4.setHeader("Accept-Charset", "UTF-8");
//
////        List<NameValuePair> list4 = new ArrayList<NameValuePair>();
////        list4.add(new BasicNameValuePair("packageName","io.dcloud.H52D97B4D"));
////        post4.setEntity(new UrlEncodedFormEntity(list4, "UTF-8"));
//
//        HttpResponse response4 = httpClient.execute(post4);
//        System.out.println("应用审核状态查询接口:请求4 后的数据："+EntityUtils.toString(response4.getEntity())+"请求4 后的数据状态码："+ response4.getStatusLine());
        /**
 * 1.6应用新增接口
 */
//        HttpPost post5 = new HttpPost(IP+"/dev/publish/addApp");
//        post5.setHeader("accept", "application/json");
//        post5.setHeader("Accept-Charset", "UTF-8");
//
//        List<NameValuePair> list5 = new ArrayList<NameValuePair>();
//        list5.add(new BasicNameValuePair("packageName","i.am.from.jiagnsu.nanjing.hexi.nubia"));
//        post5.setEntity(new UrlEncodedFormEntity(list5, "UTF-8"));
//
//        HttpResponse response5 = httpClient.execute(post5);
//        System.out.println("应用审核状态查询接口:请求5 后的数据："+EntityUtils.toString(response5.getEntity())+"请求5 后的数据状态码："+ response5.getStatusLine());
        /**
         * 1.7应用更新接口
         */
//        HttpPost post6 = new HttpPost(IP+"/dev/publish/updateApp");
//        post6.setHeader("accept", "application/json");
//        post6.setHeader("Accept-Charset", "UTF-8");
//
//        ScreenShot [] screenShot = new ScreenShot[2];
//        ScreenShot s1 = new ScreenShot();
//        s1.setScreenName("720x1280.jpg");
//        s1.setScreenUrl("https://developer-appstore.nubia.com/Developer/as/2018/02/06/091736/d11739bf31b24d6285f3396b7f0d5e3d.jpg");
//        s1.setScreenHeight(1280);
//        s1.setScreenSize(96956);
//        s1.setScreenWidth(720);
//        s1.setSortIndex(1);
//        s1.setSmallScreenUrl("https://developer-appstore.nubia.com/Developer/as/2018/02/06/091736/d11739bf31b24d6285f3396b7f0d5e3d.jpg");
//        ScreenShot s2 = new ScreenShot();
//        s2.setScreenName("720x1280.jpg");
//        s2.setScreenUrl( "https://developer-appstore.nubia.com/Developer/as/2018/02/06/091739/3dfe201942ed4be4aaf3f2290499a45c.jpg");
//        s2.setScreenHeight(1280);
//        s2.setScreenSize(96956);
//        s2.setScreenWidth(720);
//        s2.setSortIndex(2);
//        s2.setSmallScreenUrl("https://developer-appstore.nubia.com/Developer/as/2018/02/06/091739/3dfe201942ed4be4aaf3f2290499a45c.jpg");
//        screenShot[0] = s1;
//        screenShot[1] = s2;
//        List<NameValuePair> list6 = new ArrayList<NameValuePair>();
//        list6.add(new BasicNameValuePair("packageName","io.dcloud.H503FEB96"));
//        list6.add(new BasicNameValuePair("synchroType","2"));
//        list6.add(new BasicNameValuePair("publishTime","1554260400"));
//        list6.add(new BasicNameValuePair("appType","2"));
//        list6.add(new BasicNameValuePair("type","1"));
////        list6.add(new BasicNameValuePair("apkUrl","https://c5-appstore.nubia.com/Developer/app/2019/04/02/204040/8f457a9079914215a22c3dff6adcb219.apk"));
////        list6.add(new BasicNameValuePair("apkTransferUrl","/0.110111/H596A3870_0402203927.apk"));
//        list6.add(new BasicNameValuePair("appIcon","https://developer-appstore.nubia.com/Developer/ai/2018/02/06/091734/7868f4fe0735466f89159d59ca6913f9.png"));
//        list6.add(new BasicNameValuePair("screenshots",gson.toJson(screenShot)));
//        list6.add(new BasicNameValuePair("publicityIcon","https://c5-appstore.nubia.com/Developer/ap/2019/03/12/140717/75deee7ac11246c8bc8e3151c1542738.png"));
//        list6.add(new BasicNameValuePair("appName","测试0402031"));
//        list6.add(new BasicNameValuePair("categoryLayer1","2"));
//        list6.add(new BasicNameValuePair("categoryLayer2","15000"));
//        list6.add(new BasicNameValuePair("categoryLayer3","9000"));
//        list6.add(new BasicNameValuePair("gameType","2"));
//        list6.add(new BasicNameValuePair("languages","中文1"));
//        list6.add(new BasicNameValuePair("summary","一句话简介12"));
//        list6.add(new BasicNameValuePair("introduction","应用简介12"));
//        list6.add(new BasicNameValuePair("updateDescription","更新内容12"));
//        list6.add(new BasicNameValuePair("managerName","大王"));
//        list6.add(new BasicNameValuePair("managerEmail","16412@qq.com"));
//        list6.add(new BasicNameValuePair("managerPhone","13337747472"));
//        list6.add(new BasicNameValuePair("managerQQ","16458972"));
//
//        list6.add(new BasicNameValuePair("appCopyRightIcon1","https://c5-appstore.nubia.com/Developer/ap/2019/03/12/140717/75deee7ac11246c8bc8e3151c1542738.png"));
//        list6.add(new BasicNameValuePair("appCopyRightIcon2","https://c5-appstore.nubia.com/Developer/ac/2019/02/25/165714/e6fba64b6b674a309db158062e65c64f.jpg"));
//        list6.add(new BasicNameValuePair("appCopyRightIcon3","https://c5-appstore.nubia.com/Developer/ac/2019/02/26/140451/84f8c4ac5d0547bba504fcb2f82e9404.jpg"));
//        list6.add(new BasicNameValuePair("appCopyRightIcon5","https://c5-appstore.nubia.com/Developer/ap/2019/03/12/140717/75deee7ac11246c8bc8e3151c1542738.png"));
//        list6.add(new BasicNameValuePair("appNetLicense","111222333"));
//        list6.add(new BasicNameValuePair("appQualification1","https://c5-appstore.nubia.com/Developer/lp/2019/04/04/145518/34f62e1336874765a74e69f947d5d194.jpg"));
//        list6.add(new BasicNameValuePair("appQualification5","https://c5-appstore.nubia.com/Developer/ap/2019/03/12/140717/75deee7ac11246c8bc8e3151c1542738.png"));
//        post6.setEntity(new UrlEncodedFormEntity(list6, "UTF-8"));
//
//        HttpResponse response6 = httpClient.execute(post6);
//        System.out.println("应用更新接口:请求6 后的数据："+EntityUtils.toString(response6.getEntity())+"请求6 后的数据状态码："+ response6.getStatusLine());
//        System.out.println(gson.toJson(screenShot));

        /**
         * 1.8上传图片接口
         */
//        HttpPost post7 = new HttpPost(IP+"/dev/publish/uploadImg");
//        File file = new File("C:\\Users\\ltian\\Pictures\\_20190221163837.jpg");
//        FileBody fileBody = new FileBody(file);
////        String fileType = CheckFileFormat.getFileType("C:\\Users\\ltian\\Pictures\\bug\\2.png");
//        StringBody fileSize = new StringBody(file.length()+"");
//        StringBody fileName = new StringBody(file.getName());
//        StringBody type = new StringBody("lp");
//        StringBody lastModified = new StringBody(file.lastModified()+"");
//        MultipartEntity entity1 = new MultipartEntity();
//        entity1.addPart("uploadFile", fileBody);
//        entity1.addPart("fileSize", fileSize);
//        entity1.addPart("fileName", fileName);
//        entity1.addPart("type", type);
//        entity1.addPart("lastModified", lastModified);
//        post7.setEntity(entity1);
//        post7.setHeader("Accept-Charset", "UTF-8");
//        post7.setHeader("accept", "application/json");
//        post7.setHeader("contentType", "multipart/form-data;charset='UTF-8'");
//        post7.setHeader("dataType", "json");
//        HttpResponse response7 = httpClient.execute(post7);
//        System.out.println("上传图片接口:请求7 后的数据："+EntityUtils.toString(response7.getEntity(),"UTF-8")+"请求7 后的数据状态码："+ response7.getStatusLine());

        /**
         * 1.9上传分片接口
         */
//        HttpPost post8 = new HttpPost(IP+"/dev/publish/chunkUploadFile");
//        FileBody fileBody = new FileBody(new File("D:\\ltian\\test\\测试0423\\unpackage\\release\\H5B5E483B_0423112016.apk"));
////        String fileType = CheckFileFormat.getFileType("C:\\Users\\ltian\\Pictures\\bug\\2.png");
////        StringBody fileSize = new StringBody(new File("C:\\Users\\ltian\\Pictures\\bug\\2.png").length()+"");
////        StringBody fileName = new StringBody(new File("C:\\Users\\ltian\\Pictures\\bug\\2.png").getName());
//        StringBody uid = new StringBody("0.1234567899");
//        StringBody chunks = new StringBody("1");
//        StringBody chunk = new StringBody("0");
////        StringBody lastModified = new StringBody(new File("C:\\Users\\ltian\\Pictures\\bug\\2.png").lastModified()+"");
//        MultipartEntity entity1 = new MultipartEntity();
//        entity1.addPart("guid", uid);
//        entity1.addPart("chunks", chunks);
//        entity1.addPart("chunk", chunk);
////        entity1.addPart("fileSize", fileSize);
////        entity1.addPart("fileName", fileName);
//        entity1.addPart("file", fileBody);
//        post8.setEntity(entity1);
//        post8.setHeader("Accept-Charset", "UTF-8");
//        post8.setHeader("accept", "application/json");
//        post8.setHeader("contentType", "multipart/form-data;charset='UTF-8'");
//        post8.setHeader("dataType", "json");
//        HttpResponse response7 = httpClient.execute(post8);
//        System.out.println("上传分片接口:请求8 后的数据："+EntityUtils.toString(response7.getEntity())+"请求8 后的数据状态码："+ response7.getStatusLine());
        /**
         * 1.10 合并文件
         */
        HttpPost post8 = new HttpPost(IP+"/dev/publish/chunkMergeFiles");
        List<NameValuePair> list8 = new ArrayList<NameValuePair>();
        list8.add(new BasicNameValuePair("guid","1557717989040"));
        list8.add(new BasicNameValuePair("chunks","1"));
        list8.add(new BasicNameValuePair("fileName","H5B5E483B_0423112016.apk"));
        post8.setEntity(new UrlEncodedFormEntity(list8, "UTF-8"));
        post8.setHeader("Accept-Charset", "UTF-8");
        post8.setHeader("accept", "application/json");
        post8.setHeader("contentType", "multipart/form-data;charset='UTF-8'");
        post8.setHeader("dataType", "json");
        HttpResponse response6 = httpClient.execute(post8);
        System.out.println("合并文件接口:请求8 后的数据："+EntityUtils.toString(response6.getEntity())+"请求8 后的数据状态码："+ response6.getStatusLine());
        /**
         * 1.11 新建应用
         */
//        HttpPost post9 = new HttpPost(IP+"/dev/publish/addApp");
//        ScreenShot [] screenShot = new ScreenShot[2];
//        ScreenShot s1 = new ScreenShot();
//        s1.setScreenName("720x1280.jpg");
//        s1.setScreenUrl("https://developer-appstore.nubia.com/Developer/as/2018/02/06/091736/d11739bf31b24d6285f3396b7f0d5e3d.jpg");
//        s1.setScreenHeight(1280);
//        s1.setScreenSize(96956);
//        s1.setScreenWidth(720);
//        s1.setSortIndex(1);
//        s1.setSmallScreenUrl("https://developer-appstore.nubia.com/Developer/as/2018/02/06/091736/d11739bf31b24d6285f3396b7f0d5e3d.jpg");
//        ScreenShot s2 = new ScreenShot();
//        s2.setScreenName("IMG_8780.JPG");
//        s2.setScreenUrl( "https://c5-appstore.nubia.com/Developer/as/2019/04/03/202549/fe4dbdba1b124f6e990fbc119863af97.JPG");
//        s2.setScreenHeight(3024);
//        s2.setScreenSize(2613697);
//        s2.setScreenWidth(4032);
//        s2.setSortIndex(0);
//        s2.setSmallScreenUrl("https://c5-appstore.nubia.com/Developer/as/2019/04/03/202549/fe4dbdba1b124f6e990fbc119863af97.JPG");
//        screenShot[0] = s1;
//        screenShot[1] = s2;
//        List<NameValuePair> list6 = new ArrayList<NameValuePair>();
//        list6.add(new BasicNameValuePair("packageName","io.dcloud.H5B5E483B"));
////        list6.add(new BasicNameValuePair("synchroType","2"));
//        list6.add(new BasicNameValuePair("appType","2"));
//        list6.add(new BasicNameValuePair("type","1"));
//        list6.add(new BasicNameValuePair("appIcon","https://c5-appstore.nubia.com/Developer/ai/2019/04/04/104227/ed4a76d846ef4e498981717bdae4eed5.png"));
//        list6.add(new BasicNameValuePair("apkUrl","https://c5-appstore.nubia.com/Developer/app/2019/04/23/133404/27d5572dcfcf44a4932e6fa40a5e079a.apk"));
//        list6.add(new BasicNameValuePair("apkTransferUrl","/0.1234567899/H5B5E483B_0423112016.apk"));
////io.dcloud.H5587B309.nubia
//        list6.add(new BasicNameValuePair("screenshots",gson.toJson(screenShot)));
//        list6.add(new BasicNameValuePair("publicityIcon","https://c5-appstore.nubia.com/Developer/ap/2019/03/12/140717/75deee7ac11246c8bc8e3151c1542738.png"));
//        list6.add(new BasicNameValuePair("appCopyRightIcon1","https://c5-appstore.nubia.com/Developer/ac/2019/02/25/165714/e6fba64b6b674a309db158062e65c64f.jpg"));
//        list6.add(new BasicNameValuePair("appCopyRightIcon2","https://c5-appstore.nubia.com/Developer/ac/2019/02/26/140451/84f8c4ac5d0547bba504fcb2f82e9404.jpg"));
//        list6.add(new BasicNameValuePair("appCopyRightIcon5","https://c5-appstore.nubia.com/Developer/ap/2019/03/12/140717/75deee7ac11246c8bc8e3151c1542738.png"));
//        list6.add(new BasicNameValuePair("appNetLicense","11ddssaa"));
//        list6.add(new BasicNameValuePair("appQualification1","https://developer-appstore.nubia.com/Developer/as/2018/04/27/152555/51d1e53c4462499cb4023c5ff7fe14c0.jpg"));
//        list6.add(new BasicNameValuePair("appQualification3","https://c5-appstore.nubia.com/Developer/lp/2019/04/04/145518/34f62e1336874765a74e69f947d5d194.jpg"));
//        list6.add(new BasicNameValuePair("appQualification5","https://c5-appstore.nubia.com/Developer/ap/2019/03/12/140717/75deee7ac11246c8bc8e3151c1542738.png"));
//
//        list6.add(new BasicNameValuePair("appName","测试0423"));
//        list6.add(new BasicNameValuePair("categoryLayer1","2"));
//        list6.add(new BasicNameValuePair("categoryLayer2","15000"));
//        list6.add(new BasicNameValuePair("categoryLayer3","15060"));
//        list6.add(new BasicNameValuePair("gameType","2"));
//        list6.add(new BasicNameValuePair("languages","中文,英文"));
//        list6.add(new BasicNameValuePair("summary","一句话简介"));
//        list6.add(new BasicNameValuePair("introduction","应用简介"));
////        list6.add(new BasicNameValuePair("updateDescription","i.am.from.shanxi.yanan.nubiai.am.from.shanxi.yanan.nubia"));
//        list6.add(new BasicNameValuePair("managerName","田测试"));
//        list6.add(new BasicNameValuePair("managerEmail","666@qq.com"));
//        list6.add(new BasicNameValuePair("managerPhone","13337747666"));
//        list6.add(new BasicNameValuePair("managerQQ","666667"));
//        list6.add(new BasicNameValuePair("deriverRation","30"));
//        list6.add(new BasicNameValuePair("channelCostRate","3"));
//        list6.add(new BasicNameValuePair("notifyUrl","www.baidu.com"));
////        list6.add(new BasicNameValuePair("carries","中国移动-南京游戏基地,中国移动-广州MM基地"));
//        //publishTime
//        post9.setEntity(new UrlEncodedFormEntity(list6, "UTF-8"));
////
////
////
////
////
//        post9.setHeader("Accept-Charset", "UTF-8");
//        post9.setHeader("accept", "application/json");
//        post9.setHeader("contentType", "multipart/form-data;charset='UTF-8'");
//        post9.setHeader("dataType", "json");
//        HttpResponse response9 = httpClient.execute(post9);
//        System.out.println("新建应用接口:请求9 后的数据："+EntityUtils.toString(response9.getEntity())+"请求9 后的数据状态码："+ response9.getStatusLine());



//        https://c5-appstore.nubia.com/Developer/app/2019/03/28/093000/5c1f32ee2c5d4d17a819a57b325aab84.apk
//        2、使用JSONArray
//        JSONArray jsonArray= JSONArray.fromObject(gson.toJson(screenShot));
//        //获得jsonArray的第一个元素
//        System.out.println("length:"+jsonArray.size());
//        Object o=jsonArray.get(0);
//        JSONObject jsonObject2= JSONObject.fromObject(o);
//        ScreenShot stu2=(ScreenShot)JSONObject.toBean(jsonObject2, ScreenShot.class);
//        System.out.println("stu2:"+stu2);

    }


    public String getRSA(Long time,String clientId,String pubKey) throws Exception{
        StringKey str = new StringKey();
        str.setTime(time);
        str.setClientId("728706");//开发者reference_id
        String content = "728706" + time;
        //公钥加密，
        byte[] signpri_pub =RSACoder.encryptByPublicKey(content.getBytes(), Base64.decodeBase64(pubKey));
//        byte[] signpri_pub =encryptByRSA1(Base64.decodeBase64(priKey),Base64.decodeBase64(content));
        System.out.println("ltian Sign公钥加密："+Base64.encodeBase64String(signpri_pub)+"-------");
        str.setSign(Base64.encodeBase64String(signpri_pub));

        //私钥解密，
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();
        System.out.println("ltian 公钥加密 后的数据："+gson.toJson(str));
        return gson.toJson(str);

    }
}
