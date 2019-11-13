package cn.ltian.hospital;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Test {

    static String json = "{\"header\":{\"resultCode\":\"0\",\"resultMsg\":\"成功\",\"hisCode\":null,\"hisMsg\":null},\"content\":[{\"CurDate\":\"2019-11-15\",\"DateTypeID\":\"100501\",\"LimitAmout\":\"60\",\"OrgID_Used\":\"01010101035603\",\"DateTypeName\":\"星期五上午\",\"ResourceID\":\"1021\",\"ResourceName\":\"陆辉\",\"ResourceTypeID\":\"002000\",\"RemainAmount\":\"1\",\"TotAmount\":\"150.00\",\"OrgName\":\"甲状腺外科门诊\",\"OrgAddr\":\"门诊楼三层4区\"},{\"CurDate\":\"2019-11-15\",\"DateTypeID\":\"100503\",\"LimitAmout\":\"60\",\"OrgID_Used\":\"01010101035603\",\"DateTypeName\":\"星期五下午\",\"ResourceID\":\"1021\",\"ResourceName\":\"陆辉\",\"ResourceTypeID\":\"002000\",\"RemainAmount\":\"0\",\"TotAmount\":\"150.00\",\"OrgName\":\"甲状腺外科门诊\",\"OrgAddr\":\"门诊楼三层4区\"}]}";

    public static void main(String[] args) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        Date d  = null;
////这里会有一个异常，所以要用try catch捕获异常
//        try {
//            d  = sdf.parse("2017-11-06 18:10:01");
//            int a = d.getMinutes();
//            System.out.println(a);
//            if(a == 0){
//                System.out.println("2zhengdian");
//
//            }
//            panduanzhengdian(d.getTime());
//        }catch (Exception e) {
//            e.printStackTrace();
//        }

        while(true){
            try {
                new Test().runhah();
                TimeUnit.SECONDS.sleep(60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }



    }

    public void runhah(){
        RegisterLuHui registerLuHui = new RegisterLuHui();

        try {
            String result = registerLuHui.getMessage();
            System.out.println(result);
            String ofter = registerLuHui.parseJson(result);
            System.out.println("jiexi结果："+ofter);
            if(ofter != null){
                //发邮件
                System.out.println("发送邮件");
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setMsg(ofter);
                messageInfo.setTitle("！！！陆辉的号：有消息了，快去（不是定时任务）");
                EmailSend.send(messageInfo);
            }else{

                if(panduanzhengdian()){
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setMsg("整点报时；任务启动中");
                    messageInfo.setTitle("陆辉的号:没有消息（定时任务）");
                    EmailSend.send(messageInfo);
                }
                System.out.println("没有票，哈哈，囧");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean panduanzhengdian(){
       //产生一个当前的毫秒，这个毫秒其实就是自1970年1月1日0时起的毫秒数，用来计算当前毫秒与1970年1月1日之间的毫秒差。
        //那么我们就可以long time=System.currentTimeMillis();
        //然后换算一下小时，发现1小时等于3600s等于3600000ms。
        //那么我们把这个long类型的time进行一次取余运算。
        //int min=time%3600000;
        //那么只要min的值小于一分钟，即60000ms即可。
        //if(min<60000)System.out.println("当前时间为整点.");
        //else println("非整点.");

        //整体简化代码如下:
        Date d = new Date();
        int min = d.getMinutes();
        System.out.println("小时数："+d.getHours());
        System.out.println("分钟数："+min);
        System.out.println("秒数："+d.getSeconds());
        if(min == 30){
            System.out.println("当前时间为整点.");
            return true;
        }
        else {
            System.out.println("非整点");
            return false;
        }

    }
}
