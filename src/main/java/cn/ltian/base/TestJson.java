package cn.ltian.base;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestJson {



    public static void main(String[] args) {


//        String str = "{\"code\":0,\"msg\":\"SUCCESS\",\"data\":[]}";
//        JSONObject jsonObject=(JSONObject) JSONObject.parse(str);
//        System.out.println(jsonObject);
//        Integer code = jsonObject.getInteger("code");
//        System.out.println("code:"+code);
//        if(code != null && code == 0){
//            JSONArray array = jsonObject.getJSONArray("data");
//            if(array != null && array.size() > 0){
//                JSONObject object = (JSONObject)array.get(0);
//                Integer dataId = object.getInteger("dataId");
//                String customerUuid = object.getString("customerUuid");
//                System.out.println("dataId:"+dataId+";customerUuid:"+customerUuid);
//            }
//        }

//        String str = "\"https://c5-appstore.nubia.com/Developer/ap/2019/03/12/140717/75deee7ac11246c8bc8e3151c1542738.png$#$https://c5-appstore.nubia.com/Developer/ap/2019/03/12/140717/75deee7ac11246c8bc8e3151c1542738.png$#$https://c5-appstore.nubia.com/Developer/ap/2019/03/12/140717/75deee7ac11246c8bc8e3151c1542738.png$#$";
//        String [] copys = str.split("\\$#\\$");
//        if(copys.length >= 1){
//            System.out.println("111:");
//            System.out.println(copys[0]);
//        }
//        if(copys.length >= 2){
//            System.out.println("222:");
//            System.out.println(copys[1]);
//        }
//        if(copys.length >= 3){
//            System.out.println(copys[2]);
//        }
//        if(copys.length >= 4){
//            System.out.println(copys[3]);
//        }
//        if(copys.length >= 5){
//            System.out.println(copys[4]);
//        }


        String str = "https://c5-appstore.nubia.com/Developer/ac/2019/02/28/144843/79f72b8a92cc4abf8d0bcefca9f8dfa7.jpg$#$https://c5-appstore.nubia.com/Developer/ac/2019/02/28/144843/79f72b8a92cc4abf8d0bcefca9f8dfa7.jpg#2$#$https://c5-appstore.nubia.com/Developer/ac/2019/02/28/154910/35cd935b8ff74b2490762f9896719971.jpg#6";
        String [] copys = str.split("\\$#\\$");
        for (int i = 0; i < copys.length; i++) {
            if(copys[i].split("#").length == 1){
                System.out.println("默认1");
                continue;
            }
            String index = copys[i].split("#")[1];
            String url = copys[i].split("#")[0];


            if("1".equals(index)){
                System.out.println("111:"+url);
            }
            if("2".equals(index)){
                System.out.println("2:"+url);
            }
            if("3".equals(index)){
                System.out.println("3:"+url);
            }
            if("4".equals(index)){
                System.out.println("4:"+url);
            }
            if("5".equals(index)){
                System.out.println("5:"+url);
            }
            if("6".equals(index)){
                System.out.println("6:"+url);
            }
            Date date = new Date();

            SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd-hh.mm.ss");
            String str1 = "2019.4.03-11.00.00";
            Date date2= new Date();
            try {
                date2 = df.parse(str1);
                System.out.println(date2.getTime());
            }catch (ParseException e){
                System.out.println("Unparseable using"+df);
            }
        if(Integer.parseInt("1585872000") > System.currentTimeMillis()/1000){
            System.out.println("hah"+Integer.parseInt(System.currentTimeMillis()/1000+""));
        }
            System.out.println("12333333:"+Integer.parseInt(System.currentTimeMillis()/1000+""));
        }

    }



}
