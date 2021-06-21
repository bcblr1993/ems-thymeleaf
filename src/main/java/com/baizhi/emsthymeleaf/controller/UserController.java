package com.baizhi.emsthymeleaf.controller;

import com.baizhi.emsthymeleaf.entity.User;
import com.baizhi.emsthymeleaf.service.UserService;
import com.baizhi.emsthymeleaf.utils.VerifyCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class UserController {


    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @RequestMapping("login")
    public String login(User user, HttpSession session, RedirectAttributes redirectAttributes) {
        User userDB = userService.login(user);
        if (userDB == null) {
            redirectAttributes.addFlashAttribute("msg", "用户名不存在!");
            return "redirect:/loginview";
        }
        session.setAttribute("user", userDB);
        return "redirect:/lists";
    }

    /**
     * 用户注册
     *
     * @param user
     * @param code
     * @param session
     * @return
     */
    @RequestMapping("register")
    public String register(User user, String code, HttpSession session) {
        //1.比较验证码
        String sessionCode = session.getAttribute("code").toString();
        log.debug("session中验证为: {}", sessionCode);
        log.debug("接收到验证为: {}", code);
        try {
            if (sessionCode.equalsIgnoreCase(code)) {
                //2.注册用户
                userService.save(user);
                return "redirect:/loginview";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/registview";
    }

    /**
     * 生成验证码
     *
     * @param response
     * @param request
     * @throws IOException
     */
    @RequestMapping("getImage")
    public void getImage(HttpServletResponse response, HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        //1.生成随机数字
        String code = VerifyCodeUtils.generateVerifyCode(4);
        //2.放入session作用域
        session.setAttribute("code", code);
        //3.生成图片
        response.setContentType("image/png");
        VerifyCodeUtils.outputImage(220, 80, response.getOutputStream(), code);
    }
}
