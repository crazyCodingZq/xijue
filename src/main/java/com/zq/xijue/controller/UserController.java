package com.zq.xijue.controller;

import com.zq.xijue.core.ResultVO;
import com.zq.xijue.core.cache.SysCache;
import com.zq.xijue.entity.SysUser;
import com.zq.xijue.service.SysUserService;
import com.zq.xijue.util.UserUtils;
import com.zq.xijue.util.ValidationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


@Controller
public class UserController {
    private final Logger log = LoggerFactory.getLogger(AlipayController.class);
    @Autowired
    private SysUserService userService;

    @RequestMapping("/sso")
    public String login(HttpSession session, Model model, String error) {
        // 错误信息
        Object errorMsg = session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        String mobile = "";
        Map<String, String> map = (Map<String, String>) session.getAttribute("smsCode");
        if (map != null) {
            model.addAttribute("errorMsg", "验证码错误");
            mobile = map.get("mobile");
            SysCache.put(mobile);
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        // 登录类型
        model.addAttribute("mobile", mobile);
        model.addAttribute("login", "1");
        return "login4";
    }

    /**
     * 调换到注册页面
     */
    @RequestMapping("/toRegister")
    public String toRegister() {
        return "register4";
    }

    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResultVO register(HttpSession session, String mobile, String password, String code) {
        try {
            if (!ValidationUtils.isMobile(mobile)) {
                return new ResultVO(false, "手机号码格式错误");
            }
            if (!ValidationUtils.isPassword(password)) {
                return new ResultVO(false, "密码格式错误");
            }
            String cacheCode = SysCache.mobileCodeCache.getIfPresent(mobile);
            if (StringUtils.isEmpty(cacheCode) || !cacheCode.equals(code)) {
                return new ResultVO(false, "验证码错误");
            }
            userService.register(mobile, password);
        } catch (Exception e) {
            return new ResultVO(false, "系统异常，请稍后再试");
        }
        return new ResultVO(true, "注册成功");
    }

    @RequestMapping("/vip")
    public String vip(Model model) {
        String username = UserUtils.getLoginName();
        model.addAttribute("username", username);
        return "vip";
    }

    @ResponseBody
    @RequestMapping(value = "/checkCode", method = RequestMethod.POST)
    public ResultVO checkCode(HttpSession session, String code) {
        String validateCode = (String) session.getAttribute("validateCode");
        log.info("验证码：{}, 用户输入：{}", validateCode, code);
        if (!validateCode.toLowerCase().equals(code.toLowerCase())) {
            //手动设置异常
            //request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION",new DisabledException("验证码输入错误"));
            return new ResultVO(false, "验证码输入错误，请重试");
        }
        return new ResultVO("验证码输入正确");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/toUserMgmt")
    public String toUserMgmt(Model model) {
        List<SysUser> userList = userService.queryUserList();
        model.addAttribute("userList", userList);
        return "userMgmt";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    public ResultVO updateUser(Model model, SysUser sysUser) {
        try {
            if (!ValidationUtils.isMobile(sysUser.getMobile())) {
                return new ResultVO(false, "手机号码格式错误");
            }
            if (!StringUtils.isEmpty(sysUser.getPassword()) && !ValidationUtils.isPassword(sysUser.getPassword())) {
                return new ResultVO(false, "密码格式错误");
            }
            userService.updateUser(sysUser);
            return new ResultVO(true, "修改成功");
        } catch (Exception e) {
            return new ResultVO(false, "系统异常，请稍后再试");
        }
    }

}
