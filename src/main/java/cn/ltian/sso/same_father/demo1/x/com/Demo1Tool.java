package cn.ltian.sso.same_father.demo1.x.com;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Demo1Tool {

    public static String doGet(String url,String cookieName,String cookieValue){
        HttpURLConnection httpURLConnection = null;
        StringBuffer sb = new StringBuffer();
        try{

            URL urls = new URL(url+
                    "?cookieName="+cookieName+"&cookieValue="+cookieValue);
            httpURLConnection = (HttpURLConnection)urls.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            InputStream in = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String temp = null ;
            while((temp = br.readLine()) != null){
                sb.append(temp);
            }
            br.close();
            isr.close();
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }
}
