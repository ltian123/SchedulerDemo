package cn.ltian.sso.cross_domain.www.x.com.util;

public class SSOcrossCheck {

    public static final String USERNAME = "user";
    public static final String PASSWORD = "123";

    public static boolean checkLogin(String username,String password){
        if(username.equals(USERNAME) && password.equals(PASSWORD))
            return true;
            return false;
    }

    public static boolean checkCookie(String cookieName,String cookieValue){

         if(cookieName.equals("ssocookie")&&cookieValue.equals("sso")){
             return true;
         }
        return false;
    }
}
