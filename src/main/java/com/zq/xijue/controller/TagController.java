package com.zq.xijue.controller;

import com.zq.xijue.core.ResultVO;
import com.zq.xijue.entity.Tag;
import com.zq.xijue.service.TagService;
import com.zq.xijue.util.ValidationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: zhangQ
 * @Date: 2020/4/19 14:45
 */
@Controller
@RequestMapping("/tag")
public class TagController {
    @Autowired
    public TagService tagService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/index")
    public String tagIndex(Model model) {
        List<Tag> tagList = tagService.queryTagList();
        model.addAttribute("tagList", tagList);
        return "addTag";
    }

    @ResponseBody
    @RequestMapping("/queryTags")
    public ResultVO queryTagList() {
        ResultVO resultVO = new ResultVO(tagService.queryTagList());
        return resultVO;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @RequestMapping("/addTag")
    public ResultVO addTag(String tag) {
        if (StringUtils.isEmpty(tag) || !ValidationUtils.checkTagName(tag)) {
            return new ResultVO(false, "标签错误，标签只能是中文或英文，且不超过4个汉字");
        }
        Tag t = tagService.queryByName(tag);
        if (t != null) {
            return new ResultVO(false, "标签：" + tag + " 已存在");
        }
        Long id = tagService.addTag(tag);
        return new ResultVO(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @RequestMapping("/delTag")
    public ResultVO delTag(String id) {
        tagService.delTag(id);
        return new ResultVO(true, "删除成功");
    }
}
