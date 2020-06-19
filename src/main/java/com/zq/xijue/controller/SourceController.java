package com.zq.xijue.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zq.xijue.core.ResultVO;
import com.zq.xijue.entity.Category;
import com.zq.xijue.entity.Source;
import com.zq.xijue.entity.Tag;
import com.zq.xijue.service.CategoryService;
import com.zq.xijue.service.SourceService;
import com.zq.xijue.service.TagService;
import com.zq.xijue.util.UserUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: zhangQ
 * @Date: 2020/4/19 16:12
 */
@Controller
@RequestMapping("/source")
public class SourceController {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(AlipayController.class);
    @Autowired
    private SourceService sourceService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    public TagService tagService;

    @RequestMapping("/toDetail")
    public String toDetail(Model model, String id) {
        model.addAttribute("username", UserUtils.getLoginName());
        Source source = sourceService.querySourceById(id);
        String[] tagArr = source.getTagDesc().split(",");
        List<Source> likeList = sourceService.lookingForLike(source.getCategory(), source.getSourceStyle());
        model.addAttribute("source", source);
        model.addAttribute("tagArr", tagArr);
        model.addAttribute("likeList", likeList);
        List<Category> categoryList = categoryService.queryCategoryList(0 + "");
        model.addAttribute("categoryList", categoryList);
        return "detail";
    }

    @RequestMapping("/toCategory")
    public String toCategory(Model model, String categoryId, String style) {
        model.addAttribute("username", UserUtils.getLoginName());
        if (categoryId == null && style == null) {
            categoryId = "1";
        }
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("style", style);
        List<Category> categoryList = categoryService.queryCategoryList(0 + "");
        model.addAttribute("categoryList", categoryList);
        return "category";
    }

    @RequestMapping("/search")
    public String search(Model model, String categoryId, String style) {
        model.addAttribute("username", UserUtils.getLoginName());
        if (categoryId == null && style == null) {
            categoryId = "1";
        }
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("style", style);
        List<Category> categoryList = categoryService.queryCategoryList(0 + "");
        model.addAttribute("categoryList", categoryList);
        return "category";
    }

    @ResponseBody
    @RequestMapping(value = "/querySourceById", method = RequestMethod.POST)
    public ResultVO querySourceById(String id) {
        return new ResultVO(sourceService.querySourceById(id));
    }

    @ResponseBody
    @RequestMapping(value = "/lookingForLike", method = RequestMethod.POST)
    public ResultVO lookingForLike(String pid, String style) {
        return new ResultVO(sourceService.lookingForLike(pid, style));
    }

    @ResponseBody
    @RequestMapping(value = "/querySourcePage", method = RequestMethod.POST)
    public ResultVO querySourcePage(String pid, String style, String pageNum, String pageSize) {
        try {
            if (pageNum == null || pageNum == "") {
                pageNum = "1";
            }
            if (pageSize == null || pageSize == "") {
                pageSize = "40";
            }
            PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
//            //当前页
//            private int pageNum;
//            //每页的数量
//            private int pageSize;
//            //当前页的数量
//            private int size;
//            //由于startRow和endRow不常用，这里说个具体的用法
//            //可以在页面中"显示startRow到endRow 共size条数据"
//            //当前页面第一个元素在数据库中的行号
//            private int startRow;
//            //当前页面最后一个元素在数据库中的行号
//            private int endRow;
//            //总页数
//            private int pages;
//            //前一页
//            private int prePage;
//            //下一页
//            private int nextPage;
//            //是否为第一页
//            private boolean isFirstPage = false;
//            //是否为最后一页
//            private boolean isLastPage = false;
//            //是否有前一页
//            private boolean hasPreviousPage = false;
//            //是否有下一页
//            private boolean hasNextPage = false;
//            //导航页码数
//            private int navigatePages;
//            //所有导航页号
//            private int[] navigatepageNums;
//            //导航条上的第一页
//            private int navigateFirstPage;
//            //导航条上的最后一页
//            private int navigateLastPage;
            PageInfo<Source> pageInfo = new PageInfo<>(sourceService.querySourcePage(pid, style));
            return new ResultVO(pageInfo);
        } catch (Exception e) {
            logger.error("querySourcePage error,e={}", e);
            return new ResultVO(false, "参数错误");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/addSource")
    public String addSource(Model model) {
        List<Category> categoryList = categoryService.queryCategoryList(null);
        model.addAttribute("categoryList", categoryList);
        List<Tag> tagList = tagService.queryTagList();
        model.addAttribute("tagList", tagList);
        return "addSource";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/saveSource")
    public String saveSource(Model model, MultipartFile imgFile, Source source) {
        try {
            List<Category> categoryList = categoryService.queryCategoryList(null);
            model.addAttribute("categoryList", categoryList);
            List<Tag> tagList = tagService.queryTagList();
            model.addAttribute("tagList", tagList);
            sourceService.saveSource(imgFile, source);
        } catch (Exception e) {
            logger.error("saveSource error:", e);
        }
        return "addSource";
    }
}
