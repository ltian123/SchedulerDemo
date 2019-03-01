package cn.ltian.PRM.PRMnew;

import java.util.HashMap;
import java.util.Map;

public class Test {


    /**
     * Description: 测试post带请求头带请求参数
     *
     * @throws Exception
     */
    public void testPost() throws Exception {
        //设置请求头
        Map<String, String> headers = HttpUtil.PRMheads();
        //设置请求参数
        Map<String, String> params = new HashMap<String, String>();

        params.put("action","");
        params.put("username", "读取配置文件username");

        HttpClientResult result = HttpUtil.doPost("http://127.0.0.1:8080/hello/post", headers, params);
        System.out.println(result);
    }




}
