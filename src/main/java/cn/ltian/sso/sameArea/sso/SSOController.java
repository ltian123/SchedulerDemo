package cn.ltian.sso.sameArea.sso;

import cn.ltian.sso.sameArea.util.SSOCheck;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/sso")
public class SSOController {

    @RequestMapping(value = "/doLogin")
    public String doLogin(Model model, String username, String password, String gotoUrl, HttpServletResponse response) {
        boolean ok = SSOCheck.checkLogin(username, password);
        if (ok) {
            //一般只有程序自己才知道cookie意义，这边是演示
            Cookie cookie = new Cookie("ssocookie", "sso");
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:"+gotoUrl;
        }
        model.addAttribute("msg", "密码错误");
        return "login";
    }
}
