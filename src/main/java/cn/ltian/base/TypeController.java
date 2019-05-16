package cn.ltian.base;

import cn.ltian.PRM.MD5Utils;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping(value = "/manager/type")
public class TypeController {

	@RequestMapping(value = "/toList")
	public String toList(Model model) {
		model.addAttribute("leftTip", "type");
		System.out.println("tiaozhuanyemian ");
		return "welcome";
	}

	@RequestMapping(value = "/publictest")
	@ResponseBody
	public String publictest (Model model, HttpServletRequest request) {

		String randomnum = request.getHeader("randomnum");
		String time = request.getHeader("time");
		String token = request.getHeader("token");
		String userkey = request.getHeader("userkey");
		//验证随机数
		if (StringUtils.isNull(randomnum)) {
			return ResultFactory.createJsonResult("10001", false, null).toJson2();
		}
		//验证时间
		if (StringUtils.isNull(time)) {
			return ResultFactory.createJsonResult("10002", false, null).toJson2();
		}
		//判断超时
		if(!validateTime(time)){
			return ResultFactory.createJsonResult("10005", false, null).toJson2();
		}

		//验证userkey
		if (StringUtils.isNull(userkey)) {
			return ResultFactory.createJsonResult("10004", false, null).toJson2();
		}
		//校验userkey是否存在
		//TODO
		//验证token
		if (StringUtils.isNull(token)) {
			return ResultFactory.createJsonResult("10003", false, null).toJson2();
		}
		String []str = new String []{randomnum,time,userkey};
		Arrays.sort(str);
		String tokenvalidate = "";
		try {
			tokenvalidate = MD5Utils.MD5Encode(str[0]+str[1]+str[2],"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultFactory.createJsonResult("10006", false, null).toJson2();
		}
		if(!tokenvalidate.equals(token)){
			return ResultFactory.createJsonResult("10007", false, null).toJson2();
		}


		System.out.println("校验通过 ");
		 return ResultFactory.createJsonResult("0", true, null).toJson2();
	}

	//10分钟为超时 超时为false
	public static boolean validateTime(String time){
		System.currentTimeMillis();//获取当前时间戳
		if((System.currentTimeMillis() - Long.valueOf(time)) / (1000 * 60) > 10){
			return false;
		}else{
			return true;
		}
	}

	@RequestMapping(value = "/publictestpic")
	@ResponseBody
	public String publictestpic (Model model, HttpServletRequest request) {
		MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		MultipartHttpServletRequest multipartHttpServletRequest = resolver.resolveMultipart(request);
		MultipartFile file = multipartHttpServletRequest.getFile("uploadFile");

		// 文件不为空
		if(!file.isEmpty()) {
			// 文件存放路径
			String path = request.getServletContext().getRealPath("/");
			// 文件名称
			String name = String.valueOf(new Date().getTime()+"_"+file.getOriginalFilename());
			File destFile = new File(path,name);
			// 转存文件
			try {
				file.transferTo(destFile);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
			// 访问的url
			String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+ request.getContextPath() + "/" + name;
			return ResultFactory.createJsonResult("0", true, "{url:"+url+"}").toJson2();
		}

		return ResultFactory.createJsonResult("10008", false, null).toJson2();
	}


	@RequestMapping(value = "/testCookie")
	@ResponseBody
	public String testCookie (Model model, HttpServletRequest request, HttpServletResponse response,String keyString) throws Exception{
		//获取请求entity中的json
		StringBuilder responseStrBuilder = new StringBuilder();
		try {
			BufferedReader streamReader = new BufferedReader( new InputStreamReader(request.getInputStream(), "UTF-8"));

			String inputStr;
			while ((inputStr = streamReader.readLine()) != null)
				responseStrBuilder.append(inputStr);

			System.out.println(responseStrBuilder.toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取json出错");
		}
		System.out.println("entity==》json："+responseStrBuilder.toString());

		//获取request中的cookies，验证cookies
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for(Cookie cookie:cookies){
				if(cookie.getName().equals("rsa")){
					Map<String,String> map = new HashMap<String, String>();
					map.put("cookieName",cookie.getName());
					map.put("cookieValue",cookie.getValue());
					if(cookie.getName().equals("rsa")&&cookie.getValue().equals("jiami")){
						String userid = request.getSession().getAttribute("userid").toString();
						System.out.println("addCookie：jiami+userid:"+userid);
						return ResultFactory.createJsonResult("0", true, null).toJson2();
					}
				}
			}
		}

		//cookies验证不通过，也就是没有登陆，验证sign
		//验证1：判断json是否合法，是否存在
		if(StringUtils.isNull(responseStrBuilder.toString())){
			//json不存在
			return ResultFactory.createJsonResult("10001", false, null).toJson2();
		}
		if(!isJson(responseStrBuilder.toString())){
			//不是 json
			return ResultFactory.createJsonResult("10001", false, null).toJson2();
		}
		JSONObject obj = JSONObject.fromObject(responseStrBuilder.toString());
		System.out.println("clientId："+obj.getString("clientId"));
		System.out.println("time："+obj.getString("time"));
		System.out.println("sign："+obj.getString("sign"));
		//验证2：三个参数是否存在
		if (obj.getString("clientId") == null && obj.getString("time") == null && obj.getString("sign") == null) {
			return ResultFactory.createJsonResult("10001", false, null).toJson2();
		}

		//验证3：私钥解密sign，与clientId+time 比较
		String clientId = obj.getString("clientId"); //拷贝的客户端ID
		String priKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAI3vr9WhLq4+HAQKnfUNKmkmR23x10uRm26AsFZVZhAKRCdDiJNUpf3cPmYi1C+n8sCYp5j9vdNDB4xwAW6rZSSai5csfy3caMTO67mRvs8O6+AH+ia5M8sfcJ8nMvmbQAESbx8EEhqtRKArW9tVkmEisVIdpyWgBjv1xZV/jHANAgMBAAECgYAtnfYuO7JsD8wjRGJF6uhRiRr16/8c63xABJ4n8SRvTU1gLSVM7Ky4rTtkyhbWBi1P1wAufIawyl83tJvRPMaxhWMukO/SHoxRrYetgjXPcV5eVgUcde0NKvy/Ei9iwPkGTi+NRzpXtuOjiWdTaqbRtSUg1sRpg67mk6YZN8WAwQJBAMPe+aFcznSrXKawlIdraMRJ37vn4E5IqyYao9KaqDOwjXGwRuO6LmQtWhBuBsthJVpeJYnjZdrSiOrtfZ341TECQQC5gh0Gh35O89eJ0s1QY8mTVAlobvSrVzWbjnkQhubrOjyLGdXw+MH8mfX2CxMxq/j46ywkFA81MjajrDHRxIGdAkBy8huU8p3GIfpRaDcB8aqd5qyB3WXpCwRFbETPhytGikm3ejdf1Rb8exDrq2YZXH1LNwzYirZvYDYxiAW7+xdBAkAOqCMW8vmdz1JGR2uFYHz6sPcVUz7tkrRfmAAkuCPijfVeoCnxIhZhmOCAEhvwHsBLGnmgWB1jfJYolGBTTI1ZAkAmbJJLQAa4axErfsKpDg1xLTeCZTEjCrTV8JelqONBGjbWwP4qFx0rH+0ZNYcL0kCe49qdkEz7fRzU2j/m1CAX";//拷贝的私钥
		String pubKey ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCN76/VoS6uPhwECp31DSppJkdt8ddLkZtugLBWVWYQCkQnQ4iTVKX93D5mItQvp/LAmKeY/b3TQweMcAFuq2UkmouXLH8t3GjEzuu5kb7PDuvgB/omuTPLH3CfJzL5m0ABEm8fBBIarUSgK1vbVZJhIrFSHacloAY79cWVf4xwDQIDAQAB";
		String content = clientId + obj.getString("time");
		byte[] newSourcepri_pub = null;
		try {
			newSourcepri_pub = RSACoder.decryptByPrivateKey( Base64.decodeBase64(obj.getString("sign")), Base64.decodeBase64(priKey));
		}  catch (Exception e) {
			//解密出错
			System.out.println("123"+e.getMessage());
			return ResultFactory.createJsonResult("10001", false, null).toJson2();
		}
		System.out.println("newSourcepri_pub："+new String(newSourcepri_pub));
		if(!content.equals(new String(newSourcepri_pub))){
			System.out.println("newSourcepri_pub：");
			return ResultFactory.createJsonResult("10001", false, null).toJson2();
		}

		//解密，验证成功，配置cookie，配置session
		System.out.println("222：");
		Cookie cookie = new Cookie("rsa","jiami");
		cookie.setPath("/");
		response.addCookie(cookie);
		request.getSession().setAttribute("userid","123");
		System.out.println("addCookie：");
		request.getSession().setMaxInactiveInterval(30 * 60);//设置单位为秒，设置为-1永不过期
		return ResultFactory.createJsonResult("0", false, null).toJson2();
	}

	/**
	 * 判断是否为json字符串
	 * @param content
	 * @return
	 */
	public  static boolean isJson(String content){
		try {
			JSONObject.fromObject(content);
			return  true;
		} catch (Exception e) {
			return false;
		}
	}


}
