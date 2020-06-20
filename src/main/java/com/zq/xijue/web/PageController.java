package com.zq.xijue.web;

import com.zq.xijue.core.cache.SysCache;
import com.zq.xijue.core.security.SecurityConstants;
import com.zq.xijue.entity.Category;
import com.zq.xijue.entity.Source;
import com.zq.xijue.entity.Tag;
import com.zq.xijue.service.CategoryService;
import com.zq.xijue.service.SourceService;
import com.zq.xijue.service.TagService;
import com.zq.xijue.util.SysConfig;
import com.zq.xijue.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
public class PageController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SourceService sourceService;

    @Autowired
    public TagService tagService;

    /**
     * 跳转到登陆成功页
     */
    @RequestMapping(SecurityConstants.LOGIN_SUCCESS_URL)
    public String showSuccessPage(Model model) {
        buildData(model);
        return "index";
    }

    /**
     * 跳转到登录页
     */
    @RequestMapping("/login")
    public String showAuthenticationPage(HttpSession session, Model model, String username) {
        // 错误信息
        Object errorMsg = session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        if (errorMsg != null) {
            model.addAttribute("errorMsg", "用户名或密码错误，请重新输入");
            SysCache.put(username);
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        // 登录类型
        model.addAttribute("mobile", username);
        model.addAttribute("login", "2");
        return "login4";
    }

    @RequestMapping("/index")
    public String index(Model model) {
        buildData(model);
        return "index";
    }

    private void buildData(Model model) {
        List<Tag> tagList = tagService.queryTagList();
        String username = UserUtils.getLoginName();
        model.addAttribute("username", username);
        model.addAttribute("tagList", tagList);
        List<Source> one = sourceService.querySourceByPid("1");
        List<Source> two = sourceService.querySourceByPid("2");
        List<Source> three = sourceService.querySourceByPid("3");
        List<Source> four = sourceService.querySourceByPid("4");
        List<Source> five = sourceService.querySourceByPid("5");
        model.addAttribute("one", one);
        model.addAttribute("two", two);
        model.addAttribute("three", three);
        model.addAttribute("four", four);
        model.addAttribute("five", five);
        List<Category> list = categoryService.queryCategoryList(0 + "");
        // 图片链接
        SysConfig config = new SysConfig();
        model.addAttribute("mainLoop1", config.getMainLoop1());
        model.addAttribute("mainLoop2", config.getMainLoop2());
        model.addAttribute("mainLoop3", config.getMainLoop3());
        model.addAttribute("mainLoop4", config.getMainLoop4());
        model.addAttribute("mainLoop5", config.getMainLoop5());

        model.addAttribute("mainBottom1", config.getMainBottom1());
        model.addAttribute("mainBottom2", config.getMainBottom2());
        model.addAttribute("mainBottom3", config.getMainBottom3());
        model.addAttribute("mainBottom4", config.getMainBottom4());

        model.addAttribute("loginBottom", config.getLoginBottom());
        model.addAttribute("categoryList", list);
    }
}
