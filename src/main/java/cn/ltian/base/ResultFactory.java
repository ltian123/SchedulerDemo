package cn.ltian.base;

import java.util.Properties;

/**
 * @Title : ResultFactory.java
 * @Package : com.huagao.officesys.core.dto
 * @Description: Result 工厂
 * @author ltian
 * @date 2018年7月2日
 * @version v1.0
 */
public class ResultFactory {
	
	/**
	 * 初始化 错误信息的Properties文件
	 */
	public static Properties ERROR_PRO = null;
	
	static {
		ERROR_PRO = PropertiesUtils.loadPropertyInstance("/base/publisherror.properties");
	}

	/**
	 * 创建  JsonResult
	 * @param data
	 * @return
	 */
	public static Result createJsonResult(String statusCode,boolean success,Object data) {
		return new JsonResult<Object>(statusCode, true, getResultMsg(statusCode), data);
	}
	
	/**
	 * 获取 错误编码所对应的错误信息
	 * @param error
	 * @return
	 */
	public static String getResultMsg(String error) {
		return ERROR_PRO.getProperty(error);
	}
}
