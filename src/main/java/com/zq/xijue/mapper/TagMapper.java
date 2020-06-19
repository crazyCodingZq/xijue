package com.zq.xijue.mapper;

import com.zq.xijue.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagMapper {
    int insert(Tag record);

    int insertSelective(Tag record);

    List<Tag> queryTagList();

    void delTag(String id);

    Tag queryByName(String tagName);
}