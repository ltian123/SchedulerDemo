package cn.ltian.sso.cross_domain.www.b.com;


import cn.ltian.sso.cross_domain.www.a.com.Demo1Toolcross;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/demo2c")
public class Login2crossController {

    @RequestMapping(value = "/main")
    public String toList(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("ssocookie")){
                    Map<String,String> map = new HashMap<String, String>();
                    map.put("cookieName",cookie.getName());
                    map.put("cookieValue",cookie.getValue());
                    String result = Demo1Toolcross.doGet("http://check.x.com:8080/ssoc/checkCookie",map);
                    if(result.equals("1")){
                        return "success2cross";
                    }
                }
            }
        }
        model.addAttribute("gotoUrl", "/demo2c/main");
        model.addAttribute("path", "/demo2c");
        return "login";
    }
    @RequestMapping(value = "/doLogin")
    public String doLogin(Model model,String username,String password){
        Map<String,String> map = new HashMap<String, String>();
        map.put("username",username);
        map.put("password",password);
        String result = Demo2Toolcross.doGet("http://www.x.com:8080/ssoc/doLogin",map);
        if(result.equals("1")){
            ArrayList hiddenUrl = new ArrayList<String>();
            hiddenUrl.add("http://www.a.com:8080/demo1c/addCookie");
            hiddenUrl.add("http://www.b.com:8080/demo2c/addCookie");
            model.addAttribute("hiddenUrl",hiddenUrl);
            return "success2cross";
        }else if(result.equals("0")){
            model.addAttribute("msg", "密码错误");
            return "login";
        }
        return "login";
    }

    @RequestMapping(value = "/addCookie")
    public  void addcookie(HttpServletResponse response){
        Cookie cookie = new Cookie("ssocookie","sso");
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
