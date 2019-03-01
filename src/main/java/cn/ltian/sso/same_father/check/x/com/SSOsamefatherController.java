package cn.ltian.sso.same_father.check.x.com;

import cn.ltian.sso.same_father.check.x.com.util.SSOsamefatherCheck;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/ssof")
public class SSOsamefatherController {

    @RequestMapping(value = "/doLogin")
    public String doLogin(Model model, String username, String password, String gotoUrl, HttpServletResponse response) {
        boolean ok = SSOsamefatherCheck.checkLogin(username, password);
        if (ok) {
            //一般只有程序自己才知道cookie意义，这边是演示
            Cookie cookie = new Cookie("ssocookie", "sso");
            //设置在父域
            cookie.setDomain(".x.com");
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:"+gotoUrl;
        }
        model.addAttribute("msg", "密码错误");
        return "login";
    }

    @RequestMapping(value = "/checkCookie")
    public void checkCookie( HttpServletResponse response,String cookieName,String cookieValue) throws IOException {
        boolean ok = SSOsamefatherCheck.checkCookie(cookieName,cookieValue);
        String result = "0";
        if(ok){
            result = "1";
        }
        response.getWriter().print(result);
        response.getWriter().close();
    }


}
