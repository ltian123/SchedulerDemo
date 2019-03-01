package cn.ltian.sso.same_father.demo1.x.com;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/demo1f")
public class Login1samefatherController {

    @RequestMapping(value = "/main")
    public String toList(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("ssocookie")){
                    String result = Demo1Tool.doGet("http://check.x.com:8080/ssof/checkCookie",cookie.getName(),cookie.getValue());
                    if(result.equals("1")){
                        return "success1";
                    }
                }
            }
        }
        model.addAttribute("gotoUrl", "/demo1f/main");
        return "login";
    }

}
