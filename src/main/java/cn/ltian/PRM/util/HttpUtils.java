package cn.ltian.PRM.util;

import cn.ltian.PRM.RunningErrorException;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http 工具类
 * @Title : HttpUtils.java
 * @Package : com.huagao.officesys.core.utils.http
 * @Description: TODO(用一句话描述该文件做什么)
 * @author
 * @date 2018年
 * @version v1.0
 */
public class HttpUtils {
	
	/**
	 * 向指定URL发送GET方法的请求
	 * @param url 发送请求的URL
	 * @return String 所代表远程资源的响应结果
	 */
	public static String getByUrlConnection(String url) {
		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);

			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();

			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

			// 建立实际的连接
			connection.connect();

			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

		//	result = in.readLine();
			String line = "";
			while ((line = in.readLine()) != null) {
				result += line + "\n";
			}

		} catch (Exception e) {
			throw new RunningErrorException(e);
		} finally {
			// 使用finally块来关闭输入流
			try {
				if (in != null)
					in.close();
			} catch (Exception e) {
				throw new RunningErrorException(e);
			}
		}
		return result;
	}
	
	/**
	 * HttpClient 发送POST 请求
	 * @param postUrl
	 * @param paramsMap
	 * @param encoding
	 * @return
	 */
	public static String postByHttpClient(String postUrl, Map<String, String> paramsMap, String encoding) {
		CloseableHttpClient client = HttpClients.createDefault();
		String responseText = null;
		CloseableHttpResponse response = null;
		
		try {
			HttpPost method = new HttpPost(postUrl);
			
			if (paramsMap != null) {
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> param : paramsMap.entrySet()) {
					NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
					paramList.add(pair);
				}
				method.setEntity(new UrlEncodedFormEntity(paramList, encoding));
			}
			
			response = client.execute(method);
			HttpEntity entity = response.getEntity();
			
			if (entity != null)
                responseText = EntityUtils.toString(entity);
        
		} catch (Exception e) {
			throw new RunningErrorException(e);
		} finally {
            try {
            	response.close();
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
        return responseText;
	}
	
	/**
	 * HttpClient 发送POST 请求
	 * @param postUrl
	 * @param paramsMap
	 * @param encoding
	 * @return
	 */
	public static CookieStore postByHttpClient(String postUrl, Map<String, String> paramsMap, String encoding, CookieStore cookieStore) {
		
		CloseableHttpClient client = null;
		String responseText = null;
		CloseableHttpResponse response = null;
		
		try 
		{
			if (cookieStore == null) 
				cookieStore = new BasicCookieStore();
			client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
			HttpPost method = new HttpPost(postUrl);
			
			if (paramsMap != null) 
			{
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> param : paramsMap.entrySet()) 
				{
					NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
					paramList.add(pair);
				}
				method.setEntity(new UrlEncodedFormEntity(paramList, encoding));
			}
			
			response = client.execute(method);
			HttpEntity entity = response.getEntity();
			
			if (entity != null)
                responseText = EntityUtils.toString(entity);
			
            List<Cookie> cookies = cookieStore.getCookies();
            for (int i = 0; i < cookies.size(); i++) 
            {
            	System.out.println(cookies.get(i).getValue());
            }
        
		} 
		catch (Exception e) 
		{
			throw new RunningErrorException(e);
		} 
		finally 
		{
            try 
            {
            	response.close();
            } 
            catch (Exception e)
            {
            	e.printStackTrace();
            }
        }
		System.out.println(responseText);
        return cookieStore;
	}
}
