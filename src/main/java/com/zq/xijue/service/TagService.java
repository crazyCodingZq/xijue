package com.zq.xijue.service;

import com.zq.xijue.entity.Tag;
import com.zq.xijue.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zhangQ
 * @Date: 2020/4/19 14:45
 */
@Service("tagService")
public class TagService {
    @Autowired
    public TagMapper tagMapper;

    public List<Tag> queryTagList() {
        return tagMapper.queryTagList();
    }

    public Tag queryByName(String tagName) {
        return tagMapper.queryByName(tagName);
    }

    public Long addTag(String tag) {
        Tag t = new Tag();
        t.setTagName(tag);
        tagMapper.insert(t);
        return t.getId();
    }

    public void delTag(String id) {
        tagMapper.delTag(id);
    }
}
