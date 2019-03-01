package cn.ltian.PRM.util;

import cn.ltian.PRM.RunningErrorException;

import javax.net.ssl.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.X509Certificate;

/**
 * @Title : HttpsUtils.java
 * @Package : com.huagao.officesys.core.utils.http
 * @Description: HTTPS 工具类
 * @author ltian
 * @date 2018年
 * @version v1.0
 */
public class HttpsUtils {
	
	/**
	 * 发送https请求
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * @return 返回微信服务器响应的信息
	 */
	public static String requestByUrlConnection(String requestUrl, String requestMethod, String outputStr) {
		// 创建SSLContext对象，并使用我们指定的信任管理器初始化
		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			throw new RunningErrorException(e);
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			throw new RunningErrorException(e);
		}
		try {
			sslContext.init(null, tm, new java.security.SecureRandom());
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			throw new RunningErrorException(e);
		}
		
		// 从上述SSLContext对象中得到SSLSocketFactory对象
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		URL url = null;
		try {
			url = new URL(requestUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			throw new RunningErrorException(e);
		}
		
		HttpsURLConnection conn = null;
		try {
			conn = (HttpsURLConnection) url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RunningErrorException(e);
		}
		conn.setSSLSocketFactory(ssf);
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		
		// 设置请求方式（GET/POST）
		try {
			conn.setRequestMethod(requestMethod);
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			throw new RunningErrorException(e);
		}
		conn.setRequestProperty("content-type", "application/x-www-form-urlencoded"); 
		
		// 当outputStr不为null时向输出流写数据
		if (null != outputStr) {
			OutputStream outputStream;
			try {
				outputStream = conn.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new RunningErrorException(e);
			}
			// 注意编码格式
			try {
				outputStream.write(outputStr.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				throw new RunningErrorException(e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new RunningErrorException(e);
			}
			
			try {
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new RunningErrorException(e);
			}
		}
		
		// 从输入流读取返回内容
		InputStream inputStream = null;
		try {
			inputStream = conn.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RunningErrorException(e);
		}
		
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			throw new RunningErrorException(e);
		}
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		
		String str = null;
		StringBuffer buffer = new StringBuffer();
		
		try {
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RunningErrorException(e);
		}
		
		// 释放资源
		try {
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RunningErrorException(e);
		}
		
		try {
			inputStreamReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RunningErrorException(e);
		}
		
		try {
			inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RunningErrorException(e);
		}
		
		inputStream = null;
		conn.disconnect();
		return buffer.toString();
	}
	
	/**
	 * Https 信任管理器
	 * @author GeNing
	 * @since  2016.05.12
	 */
	static class MyX509TrustManager implements X509TrustManager {

		// 检查客户端证书
		public void checkClientTrusted(X509Certificate[] chain, String authType) { }

		// 检查服务器端证书
		public void checkServerTrusted(X509Certificate[] chain, String authType) { }

		// 返回受信任的X509证书数组
		public X509Certificate[] getAcceptedIssuers() { return null; }
	}
}
