package cn.ltian.sso.sameArea.demo1;


import cn.ltian.sso.sameArea.util.SSOCheck;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/demo1")
public class Login1Controller {

    @RequestMapping(value = "/main")
    public String toList(Model model, HttpServletRequest request) {
        if(SSOCheck.checkCookie(request)){
            return "success1";
        }
        model.addAttribute("gotoUrl", "/demo1/main");
        return "login";
    }

}
