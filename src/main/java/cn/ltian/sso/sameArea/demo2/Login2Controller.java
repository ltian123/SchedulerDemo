package cn.ltian.sso.sameArea.demo2;


import cn.ltian.sso.sameArea.util.SSOCheck;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/demo2")
public class Login2Controller {

    @RequestMapping(value = "/main")
    public String toList(Model model, HttpServletRequest request) {
        if(SSOCheck.checkCookie(request)){
            return "success2";
        }
        model.addAttribute("gotoUrl", "/demo2/main");
        return "login";
    }

}
