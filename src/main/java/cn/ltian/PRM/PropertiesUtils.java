package cn.ltian.PRM;

import java.io.*;
import java.util.Properties;

/**
 * @Title : PropertiesUtils.java
 * @Package : com.huagao.assetsys.core.utils
 * @Description: Properties文件加载工具类
 * @author
 * @date 2018年
 * @version v1.0
 */
public class PropertiesUtils {
	
	/**
	 * 加载 porperties文件
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	public static Properties loadPropertyInstance(String messagePropertiesFilePath) throws UnsupportedEncodingException, IOException {
		
		Properties props = null;
		InputStream is = PropertiesUtils.class.getResourceAsStream(messagePropertiesFilePath);
		props = new Properties();

		props.load(is);
		is.close();
		
        return props;
    }
}
