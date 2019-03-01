package cn.ltian.PRM;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Title : MD5Utils.java
 * @Package : com.huagao.assetsys.core.utils.encrypt
 * @Description: MD加密
 * @author GeNing
 * @date 2018年7月2日
 * @version v1.0
 */
public class MD5Utils {
	
	public static String MD5Encode(String origin, String charsetname) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String resultString = null;
		resultString = new String(origin);
		MessageDigest md = null;
		md = MessageDigest.getInstance("MD5");
		
		if (charsetname == null || "".equals(charsetname)) 
			resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
		else 
			resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
			
		return resultString;
	}

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
}
