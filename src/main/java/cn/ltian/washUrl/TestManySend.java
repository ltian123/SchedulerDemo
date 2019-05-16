package cn.ltian.washUrl;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class TestManySend {

    public static void main(String[] args) throws Exception{
        String IP = "http://127.0.0.1:8081";
        String CONNECT = "/api/wash/drainage";
        long now = System.currentTimeMillis();//当前时间
//        long now = 1551938937326L;
        HttpPost post = new HttpPost(IP + CONNECT);
        post.setHeader("accept", "application/json");
        post.setHeader("Accept-Charset", "UTF-8");
        // 设置CookieStore
        BasicCookieStore cookieStore = new BasicCookieStore();
        // 构造HttpClient
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();

        List<String> urls = new ArrayList<>();
//        urls.add("https://89fe13943415c965441647de8437ca63.dd.cdntips.com/imtt.dd.qq.com/16891/F8ED63E12FB45B0EA8B6943EA6C1A37F.apk?mkey=5cd0660b314dce75&f=0c27&fsname=cn.gov.tax.its_1.1.8_10108.apk&csr=1bbd&cip=49.77.232.128&proto=https");
//        urls.add("http://ucdl.25pp.com/fs07/2016/04/06/5/106_1913f7505c25d7fb953f385981f4a93b.apk?cc=3918859047&fname=%E9%9D%9E%E5%A3%B9%E7%94%B5%E8%A7%86&packageid=324811&pkg=air.nexti.android.applenews&vcode=3000010&yingid=pp_wap_ppcn&vh=52268f1c9ab6a916c3c092041a67895d&sf=487479&sh=10&appid=579201&apprd=579201&iconUrl=http%3A%2F%2Fandroid-artworks.25pp.com%2Ffs08%2F2016%2F06%2F06%2F4%2F1_6fda27e8b0f0ea774c85bb2c08ccfefb_con.png&did=598c402bdeb31f5a31d3c760f13d0625&md5=4e6270e3af0380bdef166b0899363f34");
//        urls.add("http://p.gdown.baidu.com/42981f5f78ff650b5bbf86b040ff18aeb1476897d074009fe743157826a558c55096b952498a7a09932dc1d1cfb7cf8d86a80a1b4fb33a64a211f81f53be856182144e5f75ee3956f37ea3b79115d4c0d9acbf1572c1facae9d23a3c521fb3ee4dd4e14dc8d79310b95254170c580f4b4b78ec1f2c9b410d2a26e898ec377c293f042c6283f2456d78a469507e6260ab2d29df9dca9b23fb58453c0f9d54419d077e11187f6b7c4ef876ed062f35974fb8420f96f1a4d67c");
//        urls.add("http://shouji.360tpcdn.com/160601/5d14efb70bdb0af2eede9ac36253ab0c/com.lazyswipe_22810.apk");
//        urls.add("https://alissl.ucdl.pp.uc.cn/fs08/2019/04/30/3/106_038e76ac4b0bfcf73be3a1eab5432556.apk?yingid=wdj_web&fname=%E5%A4%84CP&pos=wdj_web%2Fdetail_normal_dl%2F0&appid=7615296&packageid=100499808&apprd=7615296&iconUrl=http%3A%2F%2Fandroid-artworks.25pp.com%2Ffs08%2F2018%2F02%2F06%2F0%2F110_0a9577bd7948f4f1627e1e2b6178aa55_con.png&pkg=me.ddkj.cp&did=07a310ac1ec34c46bd0056b25fdaf357&vcode=155&md5=174bb7c12fb4388f8264d37e4fdf72af");
//        urls.add("http://b9.market.xiaomi.com/download/AppStore/01f3d51a70e3705e58fabfcbf73389bbb4c40440b/com.wandoujia.eyepetizer.apk");
//        urls.add("https://c.tapimg.com/pub2/201903/cab7f5b2021a9d3a73a44667be1b7e5c.apk?_upd=com.taptap_2.1.1.apk");
//        urls.add("http://m.appstore.nubia.com/123.apk");
//        urls.add("http://p.gdown.baidu.com/13b30ad1752d605fc288a541b12daf8291aece6d9575e7de1160cc1063c03dfdaf850492637b02c94d42b7f6b43fbb21d48d2a5a4635367f7fc053fe3469aead274a25cb14979c52ce114c00857e5b3f4dd4e14dc8d79310d9d508d00f3454da114e208366d952e0b3d865a58cb0931246138fd6056e746d30a42d11124d0665e3a599880c3b4ffe81864967c2b3a163555fb80aeeeb07fe9ca1f1ed13745d3f");
//        urls.add("http://p.gdown.baidu.com/2caba47f636538f0239d35e13a2baa4944f81543d89c5c372522a891daab843cda642b4ad794d7b78dbdda945d22b1c62dd2ebd3f07116b35900cd0d6bc1f76ce727cdb3ccf9fc8877fa5495d1b40d5345a77a80bbe75c3436cd94b4d8faae0643f394b78c7c39cf859bbb9a50e101ff16aaf263591239a09f23af3581fcbcefb38183b485c93c695b265c098796950a8d51663650ffb31ecafb0a0f389c51e0a26330e7c0b0ce4eeb7de74ed6a33695c58f29b5df2eeb1132a42f60aae154ae880f4a0e30c218477bc9171903174e5cd053dcd4a151108cc797690ec73e83052a34b98e88398e0be03ef86c608c51871286fc348a0bd2038a4a86dcc9732eeef91fbb74d64b81c29001e48946b2f7ee");
        urls.add("http://p.gdown.baidu.com/3b3afdf385180179d1422689e2def5c42b9ff67611f1d9027092fa5266360603738d97984d64ddcf640037c221b511579ae3c7ca2ff8612f7b5c6380c8eafa9339b322ce986e547848efa956e4f0a47f2f49ecbc700e046502eb3e57dab8a641ed57badb61ffba081bfa11229d159c2e9a4d51064fea838698bc70f96afefd817c791ff736877e4c7ea96cf6f8ac04c332334c3a21d880d9cbc364f5dfb5f0ad474eae82e899b27e9a77232351cab8352de5dc89dab5d94a6c327ed0e23d557675469aee02c3959291adece9eaae282662587da774ef2fbe068ec4746fed6a83cccf97bede3f2e0fdafeecf6edd1397ff2737bcacc28152b13809f8c37646b6b5a10119df6b866610497f87613a8749ebe38d1dd51252debb186a4c5291f45c472c3d1ce08c9a2496508b24935a5f7d6d52d39709aebe4174b02975e878b1dc75241188279336373");
        for (int i = 0; i < urls.size(); i++) {
            List<NameValuePair> list5 = new ArrayList<NameValuePair>();
            list5.add(new BasicNameValuePair("realUrl",urls.get(i)));
            System.out.println(urls.get(i));
            post.setEntity(new UrlEncodedFormEntity(list5, "UTF-8"));
            HttpResponse response5 = httpClient.execute(post);
            System.out.println("接口:请求 后的数据："+ EntityUtils.toString(response5.getEntity())+"请求 后的数据状态码："+ response5.getStatusLine());
            System.out.println("======================================");
        }

//        String path = new TestManySend().getClass().getClassLoader().getResource("").getPath();
//        System.out.println(path);




    }
}
