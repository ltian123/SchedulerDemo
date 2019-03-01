package cn.ltian.activeMQ;

import net.sf.json.JSONObject;

public class TookitTest {
    public static void main(String[] args) {
        JSONObject object = new JSONObject();
        object.put("xiaowang ","hah");
        ActiveMqTookit.writeToMq(object,"xiaoming1");
        //read
//        ArrayList<String> list =  ActiveMqTookit.readFromMq("xiaoming",1);
//        for(String s:list){
//            System.out.println("xiaoming ==>read message:"+s);
//        }
    }
}
