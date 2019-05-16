package cn.ltian.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @Title : PropertiesUtils.java
 * @Package : com.huagao.officesys.core.utils
 * @Description: Properties文件加载工具类
 * @author ltian
 * @date 2018年7月2日
 * @version v1.0
 */
public class PropertiesUtils {
	
	/**
	 * 加载 porperties文件
	 */
	public static Properties loadPropertyInstance(String messagePropertiesFilePath) {
		
		Properties props = null;
		InputStream is = PropertiesUtils.class.getResourceAsStream(messagePropertiesFilePath);
		props = new Properties();
		
        try {
			props.load(new InputStreamReader(is, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			throw new RunningErrorException(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RunningErrorException(e);
		}
        
        try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RunningErrorException(e);
		}
		
        return props;
    }
}
