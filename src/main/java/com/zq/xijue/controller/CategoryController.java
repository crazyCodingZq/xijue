package com.zq.xijue.controller;

import com.zq.xijue.core.ResultVO;
import com.zq.xijue.entity.Category;
import com.zq.xijue.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: zhangQ
 * @Date: 2020/4/16 21:50
 */
@Controller
@RequestMapping("/category")
public class CategoryController {
    private final Logger logger = LoggerFactory.getLogger(AlipayController.class);
    @Autowired
    private CategoryService categoryService;

    @ResponseBody
    @RequestMapping(value = "/queryCategoryList", method = RequestMethod.POST)
    public ResultVO queryCategoryList(Model model, String id) {
        return new ResultVO(categoryService.queryCategoryList(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/toCateMgmt")
    public String categoryMgmt(Model model) {
        List<Category> cateList = categoryService.queryCategoryList(null);
        model.addAttribute("categoryList", cateList);
        return "addCategory";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @RequestMapping(value = "/addCategory", method = RequestMethod.POST)
    public ResultVO addCategory(Model model, Category category) {
        try {
            categoryService.insert(category);
            return new ResultVO(category);
        } catch (Exception e) {
            logger.error("addCategory error:", e);
        }
        return new ResultVO(false, "系统错误");
    }

}
