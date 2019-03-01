package cn.ltian.sso.sameArea.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class SSOCheck {

    public static final String USERNAME = "user";
    public static final String PASSWORD = "123";

    public static boolean checkLogin(String username,String password){
        if(username.equals(USERNAME) && password.equals(PASSWORD))
            return true;
            return false;
    }

    public static boolean checkCookie(HttpServletRequest httpServiceRequest){

        Cookie[] cookies = httpServiceRequest.getCookies();
        if(cookies != null){
             for (Cookie cookie:cookies){
                 if(cookie.getName().equals("ssocookie")&&cookie.getValue().equals("sso")){
                     return true;
                 }
             }
        }
        return false;
    }
}
