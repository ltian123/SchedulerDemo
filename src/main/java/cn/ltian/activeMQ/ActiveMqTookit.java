package cn.ltian.activeMQ;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import java.util.ArrayList;

/**
 * 消息队列辅助类
 * 提供从消息队列里读取、写入
 * mq地址正式环境从配置文件里读取
 *
 */
public class ActiveMqTookit {

    private   Connection connection = null;

    // private String subject = "TOOL.DEFAULT"; "tcp://localhost:61616"

    public static String mqBroker = "tcp://localhost:61616";//mq地址
    public static String spiderStatus="spiderStatus";//爬虫状态subject
    protected BrokerService brokerService;

    /**
     * 以下为正式环境数据库配置文件读取
     */
    // try {
    // InputStream is =
    // JdbcUtil.class.getClassLoader().getResourceAsStream("classpath*:mq.properties");
    // Properties pro = new Properties();
    // pro.load(is);
    // mqBroker = pro.getProperty("activemq.url");
    // } catch (IOException e1) {
    // // TODO Auto-generated catch block
    // e1.printStackTrace();
    // }


    /**
     * 服务端active 写入消息队列
     *
     * @param msgList
     * @param subject
     * @param socketStr
     */
    public void writeMsgToActiveMq(ArrayList<String> msgList, String subject,
                                   String socketStr) {
        try {
            ActiveMQConnectionFactory connectionFactory = null;
            Session session = null;
            session = getSession(socketStr, connectionFactory, session);
            // 创建destination
            Destination destination = session.createQueue(subject);
            // 创建producer
            MessageProducer producer = session.createProducer(destination);
            // 设置JMS的持久性
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            for (String s : msgList) {
                TextMessage message = session.createTextMessage(s);
                // 发生消息message
                producer.send(message);
                // 关闭资源
                message.clearProperties();
            }
            session.close();
            connection.stop();
            connection.close();
            System.out.println("msgList的长度是："+msgList.size());
            System.out.println("关闭资源。。。。");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    /**
     *  服务端active json数据类型  写入消息队列
     * @param msgJsonList
     * @param subject
     * @param socketStr
     */
    public void writeMsgToActiveMqByJson(ArrayList<JSONObject> msgJsonList, String subject,
                                         String socketStr){
        ArrayList<String> msgList=new ArrayList<String>();
        for(JSONObject obj:msgJsonList){
            msgList.add(obj.toString());
        }
        writeMsgToActiveMq(msgList,subject,socketStr);
    }

    /**
     *  服务端active json数据类型  写入消息队列
     * @param array
     * @param subject
     * @param socketStr
     */
    public  void writeMsgToActiveMqByJson(JSONArray array, String subject,
                                          String socketStr){
        ArrayList<String> msgList=new ArrayList<String>();
        for(int i=0;i<array.size();i++){
            msgList.add(array.getJSONObject(i).toString());
        }
        writeMsgToActiveMq(msgList,subject,socketStr);
    }

    /**
     * 获取消息队列session对象
     *
     * @param socketStr
     * @param connectionFactory
     * @param session
     * @return
     */
    public Session getSession(String socketStr,
                              ActiveMQConnectionFactory connectionFactory, Session session) {
        try {
            if (null == connectionFactory) {
                connectionFactory = new ActiveMQConnectionFactory(socketStr);
            }
            // 创建connection
            if (null == connection) {
                connection = connectionFactory.createConnection();

            }
            // 创建session，设置消息确认机制
            if (null == session) {
                connection.start();
                session = connection.createSession(false,
                        Session.AUTO_ACKNOWLEDGE);
            }
            return session;
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 服务端active 读取消息队列
     * @param readCount
     * @param socketUrl
     * @param subject
     * @return
     */
    public   ArrayList<String> readMsgFromActiveMq(int readCount,
                                                   String socketUrl, String subject) {
        ArrayList<String> msgList = new ArrayList<String>();
        ActiveMQConnectionFactory connectionFactory = null;
        Session session = null;
        try {
            // 创建session
            session = getSession(socketUrl, connectionFactory, session);
            // 创建destination
            Destination destination = session.createQueue(subject);
            MessageConsumer consumer = session.createConsumer(destination);
            for (int i = 0; i < readCount; i++) {
                Message msg = consumer.receive(3000);
                if(msg==null){
                    break;
                }
                String str = ((TextMessage) msg).getText();
                msgList.add(str);
                msg.clearProperties();
            }
            return msgList;
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            try {
                session.close();
                connection.stop();
                connection.close();
            } catch (JMSException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 从mq中读取数据
     * @param subject
     * @param readCount
     * @return
     */
    public static ArrayList<String> readFromMq(String subject,int readCount){
        ActiveMqTookit activeMqTookit = new ActiveMqTookit();
        ArrayList<String> resultList = activeMqTookit.readMsgFromActiveMq(readCount, ActiveMqTookit.mqBroker, subject);
        return resultList;
    }

    /**
     * 写入数据到mq
     * @param jsonArray
     * @param subject
     * @return
     */
    public static void writeToMq(JSONArray jsonArray,String subject){
        ActiveMqTookit activeMqTookit = new ActiveMqTookit();
        activeMqTookit.writeMsgToActiveMqByJson(jsonArray, subject,ActiveMqTookit.mqBroker);
    }

    public static void writeToMq(JSONObject object,String subject){
        ActiveMqTookit activeMqTookit = new ActiveMqTookit();
        ArrayList<String> msgList = new ArrayList<String>();
        msgList.add(object.toString());
        activeMqTookit.writeMsgToActiveMq(msgList, subject, ActiveMqTookit.mqBroker);
    }

}
