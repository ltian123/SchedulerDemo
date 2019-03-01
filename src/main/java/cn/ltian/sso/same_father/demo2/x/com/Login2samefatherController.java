package cn.ltian.sso.same_father.demo2.x.com;


import cn.ltian.sso.same_father.demo1.x.com.Demo1Tool;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/demo2f")
public class Login2samefatherController {

    @RequestMapping(value = "/main")
    public String toList(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("ssocookie")){
                    String result = Demo1Tool.doGet("http://check.x.com:8080/ssof/checkCookie",cookie.getName(),cookie.getValue());
                    if(result.equals("1")){
                        return "success2";
                    }
                }
            }
        }
        model.addAttribute("gotoUrl", "/demo2f/main");
        return "login";
    }

}
