package cn.ltian.PRM;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


public class RequestConfig {
	private static Logger logger = LoggerFactory.getLogger(RequestConfig.class);

	static {
		try {
			Properties requestConfigProperties = PropertiesUtils.loadPropertyInstance("/request-config.properties");

			// 初始化 RequestConfig对象
			RequestConfig.setToken(requestConfigProperties.getProperty("token"));
			RequestConfig.setSecret(requestConfigProperties.getProperty("secret"));
			RequestConfig.setPublicKeyStr(requestConfigProperties.getProperty("publicKeyStr"));
			RequestConfig.setIp(requestConfigProperties.getProperty("ip"));
			RequestConfig.setUsername(requestConfigProperties.getProperty("username"));
		}catch(Exception e){
			logger.info("初始化request-config.properties 异常");
		}

	}
	//
	private static String charset;
	
	//
	private static String time;
	
	//
	private static String token;
	
	//
	private static String username;
	
	//
	private static String ip;
	
	// 应用秘钥
	private static String secret;
	
	// 应用公钥
	private static String publicKeyStr;


	public static String getCharset() {
		return charset;
	}

	public static String getTime() {
		return time;
	}

	public static String getToken() {
		return token;
	}

	public static String getUsername() {
		return username;
	}

	public static String getIp() {
		return ip;
	}

	public static String getSecret() {
		return secret;
	}

	public static String getPublicKeyStr() {
		return publicKeyStr;
	}

	public static void setRandomnum(String charset) {
		RequestConfig.charset = charset;
	}

	public static void setTime(String time) {
		RequestConfig.time = time;
	}

	public static void setToken(String token) {
		RequestConfig.token = token;
	}

	public static void setUsername(String username) {
		RequestConfig.username = username;
	}

	public static void setIp(String ip) {
		RequestConfig.ip = ip;
	}

	public static void setSecret(String secret) {
		RequestConfig.secret = secret;
	}

	public static void setPublicKeyStr(String publicKeyStr) {
		RequestConfig.publicKeyStr = publicKeyStr;
	}
}
