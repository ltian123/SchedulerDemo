package cn.ltian.PRM.util;

import cn.ltian.PRM.MD5Utils;
import cn.ltian.PRM.RequestConfig;
import cn.ltian.PRM.StringUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Title : RequestUtils.java
 * @Package : com.huagao.officesys.core.utils.http
 * @Description: 请求工具类
 * @author
 * @date 2018年
 * @version v1.0
 */
public class RequestUtils {

	private static Logger logger = LoggerFactory.getLogger(RequestUtils.class);
	/**
     * 获取访问IP
     * @param request
     * @return
     */
    public static String getRemoteHost(HttpServletRequest request) {
    	String ip = request.getHeader("X-Forwarded-For");
    	if (!StringUtils.isNull(ip) && !"unKnown".equalsIgnoreCase(ip)) {
    		//多次反向代理后会有多个ip值，第一个ip才是真实ip
    		int index = ip.indexOf(",");
    		if(index != -1)
    			return ip.substring(0,index);
    		else
    			return ip;
    	}
    	
    	ip = request.getHeader("X-Real-IP");
    	if (!StringUtils.isNull(ip) && !"unKnown".equalsIgnoreCase(ip))
    		return ip;
    	
    	return request.getRemoteAddr();
    }
    
    /**
     * 获取所有请求参数
     * @param request
     * @return
     */
    public static SortedMap<String, String> getRequestAllParams(HttpServletRequest request) {
    	
    	SortedMap<String, String> map = null;
    	
    	Enumeration<?> paramNames = request.getParameterNames();
    	
    	if (paramNames == null) 
    		return null;
    	else
    		map =  new TreeMap<String, String>();
    	
    	while (paramNames.hasMoreElements()) {
    		
    		String paramName = (String) paramNames.nextElement();
    		String paramValue = request.getParameter(paramName);
    		
    		if (!StringUtils.isNull(paramValue))
    			map.put(paramName, paramValue);
    	}
    	
    	return map;
    }

	/**
	 * PRM 同步请求
	 * @param paramsMap
	 * @return
	 */
	public static String baseRequest(SortedMap<String, String> paramsMap) {

		//

//		paramsMap.put("username", RequestConfig.getUsername());
		paramsMap.put("ip", RequestConfig.getIp());
//		paramsMap.put("secret", RequestConfig.getSecret());

		String randomNum = RandomStringUtils.random(5,true,true);
		paramsMap.put("randomnum", randomNum);
		String time = String.valueOf(new Date().getTime());
		paramsMap.put("time", time);



		try {
			String token = "";
			String key = MD5Utils.MD5Encode( RequestConfig.getSecret(),"UTF-8");
			String []str = new String[]{randomNum,time,key};
			Arrays.sort(str);

			token = MD5Utils.MD5Encode(str[0] + str[1] + str[2],"UTF-8");
			paramsMap.put("token", token);



		} catch (Exception e) {
			logger.info("token MD5encode error");
			return null;
		}


		System.out.println(paramsMap);
		return "";
//		return HttpUtils.postByHttpClient(Constants.SERVER_GATEWAY_URL, paramsMap, RequestConfig.getCharset());
	}


	public static void main(String[] args) {
		SortedMap<String, String> paramsMap = new TreeMap<String, String>();
		RequestUtils.baseRequest(paramsMap);

	}

}
