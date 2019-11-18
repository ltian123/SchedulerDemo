package cn.ltian.hospital;

import cn.ltian.base.ResultFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;

public class RegisterLuHui {

    private static String url = ResultFactory.getResultMsg("9999");
    private static String canshu = ResultFactory.getResultMsg("99990");

    static String jsonString1 = "{\"header\":{\"resultCode\":\"0\",\"resultMsg\":\"成功\",\"hisCode\":null,\"hisMsg\":null},\"content\":[{\"CurDate\":\"2019-11-15\",\"DateTypeID\":\"100501\",\"LimitAmout\":\"60\",\"OrgID_Used\":\"01010101035603\",\"DateTypeName\":\"星期五上午\",\"ResourceID\":\"1021\",\"ResourceName\":\"陆辉\",\"ResourceTypeID\":\"002000\",\"RemainAmount\":\"1\",\"TotAmount\":\"150.00\",\"OrgName\":\"甲状腺外科门诊\",\"OrgAddr\":\"门诊楼三层4区\"},{\"CurDate\":\"2019-11-15\",\"DateTypeID\":\"100503\",\"LimitAmout\":\"60\",\"OrgID_Used\":\"01010101035603\",\"DateTypeName\":\"星期五下午\",\"ResourceID\":\"1021\",\"ResourceName\":\"陆辉\",\"ResourceTypeID\":\"002000\",\"RemainAmount\":\"0\",\"TotAmount\":\"150.00\",\"OrgName\":\"甲状腺外科门诊\",\"OrgAddr\":\"门诊楼三层4区\"}]}";


    private static HttpPost post;

    private static  CloseableHttpClient httpClient;

    public static HttpPost createPost(){
        if(post == null){
            post = new HttpPost(url);
            post.setHeader("accept", "application/json");
            post.setHeader("Accept-Charset", "UTF-8");
            post.setHeader("Content-Type", "application/json;charset=UTF-8");
            StringEntity entity = new StringEntity(canshu, Charset.forName("UTF-8"));
            post.setEntity(entity);
        }
        if(httpClient == null){
            // 设置CookieStore
            BasicCookieStore cookieStore = new BasicCookieStore();
//        // 构造HttpClient
            httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
        }
        return post;
    }


    public String getMessage() throws Exception{
        if(post == null){
            createPost();
        }
        if(httpClient == null){
            createPost();
        }

        HttpResponse response1 = null;
        String result = "";
        try{

             response1 = httpClient.execute(post);
              result = EntityUtils.toString(response1.getEntity());
            //result = jsonString1;
            System.out.println("123456请求 后的数据："+ result+"<====请求 后的数据状态码："+ response1.getStatusLine());

        }finally {
//            try {
//                httpClient.close();//关闭httpclient
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        return result;
    }



    public String parseJson(String jsons){

        JSONObject jsonObject = JSONObject.parseObject(jsons);
        JSONArray content = jsonObject.getJSONArray("content");
        for(int i=0;i<content.size();i++){
            JSONObject o = (JSONObject) content.get(0);
            Entity peoplePo1 = JSONObject.parseObject(o.toString(), Entity.class);
            System.out.println(peoplePo1);
            int  yupiao = Integer.parseInt(peoplePo1.getRemainAmount());
            if(yupiao > 0){
                return "有票了，快去；剩余票数："+yupiao+";星期："+peoplePo1.getDateTypeName()
                        +";医生："+peoplePo1.getResourceName()+";时间："+peoplePo1.getCurDate()
                        +";费用："+peoplePo1.getTotAmount()+";门诊："+peoplePo1.getOrgName()+
                        ";就医地址："+peoplePo1.getOrgAddr();
            }
        }

        return null;
    }
}
