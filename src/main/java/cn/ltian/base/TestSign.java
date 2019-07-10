package cn.ltian.base;

import cn.ltian.PRM.MD5Utils;

import java.util.*;

public class TestSign {

    /**
     * 开发者平台接口签名算法，test
     * @param args
     */
    public static void main(String[] args) {
        //随机数
        long time = System.currentTimeMillis()/1000;
//        long time = 1553063176;
        System.out.println("随机数："+time);

        //设置参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("time",time+"");
        //有多少个参数就在params中加入
//        params.put("pageSize","10");
        params.put("uId","7195047");
//        params.put("developerId","5572702");
//        params.put("pageNo","1");

        //key排序
        List list = new ArrayList();
        Set set1 = params.keySet();
        Iterator iter1 = set1.iterator();
        while (iter1.hasNext()) {
            String key = (String) iter1.next();
            list.add(key);
        }
        String[] strings = new String[list.size()];
        list.toArray(strings);
        Arrays.sort(strings);

        //字符串拼接
        String str = "";
        String canshu = "";
        Set set2 = params.keySet();
        for (int i = 0; i < strings.length; i++) {
            String key2 = strings[i];
            str+=key2+"="+params.get(key2);
            canshu += "&"+key2+"="+params.get(key2);
        }
        //GjWj8pc4bYq19NDkQ86fd6MtlW650Tki
        String secretKey = "GjWj8pc4bYq19NDkQ86fd6MtlW650Tki";
        str+=secretKey;

        //sign
        String sign = "";
        try{
            System.out.println("str："+str);
             sign = MD5Utils.MD5Encode(str,"UTF-8");
        }catch (Exception e){
            System.out.println("MD5错误："+e.getMessage());
        }
        System.out.println("sign："+sign);

        System.out.println("参数拼接：===》?sign="+sign+canshu);

    }
}
