package com.zq.xijue.mapper;

import com.zq.xijue.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    int insert(Category record);

    int insertSelective(Category record);

    List<Category> queryCategoryList(String id);

}