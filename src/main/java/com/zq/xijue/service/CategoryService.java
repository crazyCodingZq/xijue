package com.zq.xijue.service;

import com.zq.xijue.entity.Category;
import com.zq.xijue.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zhangQ
 * @Date: 2020/4/16 22:27
 */
@Service("categoryService")
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoryList(String id) {
        return categoryMapper.queryCategoryList(id);
    }

    public Long insert(Category category) {
        categoryMapper.insert(category);
        return category.getId();
    }

}
