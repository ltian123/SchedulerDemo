package cn.ltian.PRM;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * @Title : StringUtils.java
 * @Package : com.huagao.officesys.core.utils.pack
 * @Description: 字符串处理工具类
 * @author GeNing
 * @date 2018年7月2日
 * @version v1.0
 */
public class StringUtils {
	
	/*
	 * 判断字符是否为空(包括 null,"","null","Null","N/A")
	 */
	public static boolean isNull(String str) {
		boolean isnull = false;
		if (str == null || "".equals(str) || "null".equals(str)
				|| "Null".equals(str) || "N/A".equals(str)) {
			isnull = true;
		}
		return isnull;
	}
	
	/*
	 * 生成随机数纯数字
	 */
	public static String getRandom(int length) {
		Random random = new Random();
		String rr = "";
		Set<Integer> set = new HashSet<Integer>();
		while (set.size() < length) {
			set.add(random.nextInt(10));
		}
		Iterator<Integer> iterator = set.iterator();
		while (iterator.hasNext()) {
			rr = rr + iterator.next();
		}
		return rr;
	}
	
	/*
	 * 生成随机字符
	 */
	public static String getRandomString(int length) { //length表示生成字符串的长度  
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {
	        int number = random.nextInt(base.length());
	        sb.append(base.charAt(number));
	    }
	    return sb.toString();
	 }
	
    /* 
     * 将字符串转换成ASCII码 
     */  
    public static String getCnASCII(String cnStr) {
        StringBuffer strBuf = new StringBuffer();  
        // 将字符串转换成字节序列  
        byte[] bGBK = cnStr.getBytes();  
        for (int i = 0; i < bGBK.length; i++) {  
            // 将每个字符转换成ASCII码  
            strBuf.append(Integer.toHexString(bGBK[i] & 0xff)+" ");  
        }  
        return strBuf.toString();  
    }  
    
    /*
     * 字符串首字母大写
     */
	public static String captureName(String name) {
		// name = name.substring(0, 1).toUpperCase() + name.substring(1);
		// return  name;
		char[] cs = name.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);
	}
	
	/*
	 * 截取字符串后N位
	 */
	public static String subLastLength(String target, int length) {
		try {
			return target.substring(target.length() - length, target.length());
		} catch (Exception e) {
			// TODO: handle exception
			int targetLength = target.length();
			String result = "";
			for (int i = length - targetLength; i < length - 1; i++) {
				result += "0";
			}
			return result + target;
		}
	}
	
	public static String getSerierCardNo(int i) {
		Calendar calendar = Calendar.getInstance();
		String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4);
		String mouth = calendar.get(Calendar.MONTH) <= 9 ? "0" + calendar.get(Calendar.MONTH) : String.valueOf(calendar.get(Calendar.MONTH));
		String day = calendar.get(Calendar.DATE) > 9 ? String.valueOf(calendar.get(Calendar.DATE)) : "0" + calendar.get(Calendar.DATE);
		
		// 业务关键字
		String serviecKey = "LY";
		if (i > 1) {
			switch (i) {
			case 2:
				serviecKey = "LY";
				break;
			default:
				break;
			}
		}
		// 业务编号 1位数字 如：1 / 2 / 3
		String payChannel = String.valueOf(i);
		// 时间信息 6位数字 如：161201
		String timeInfo = year + mouth + day;
		
		return String.valueOf(serviecKey) + payChannel + timeInfo;
	}
}
