package cn.ltian.sso.cross_domain.www.x.com;

import cn.ltian.sso.cross_domain.www.x.com.util.SSOcrossCheck;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/ssoc")
public class SSOcrossController {

    @RequestMapping(value = "/doLogin")
    public void doLogin(Model model, String username, String password, String gotoUrl, HttpServletResponse response) throws IOException {
        boolean ok = SSOcrossCheck.checkLogin(username, password);
        String result = "0";
        if (ok) {
            result = "1";
        }
        response.getWriter().print(result);
        response.getWriter().close();
    }

    @RequestMapping(value = "/checkCookie")
    public void checkCookie( HttpServletResponse response,String cookieName,String cookieValue) throws IOException {
        boolean ok = SSOcrossCheck.checkCookie(cookieName,cookieValue);
        String result = "0";
        if(ok){
            result = "1";
        }
        response.getWriter().print(result);
        response.getWriter().close();
    }


}
