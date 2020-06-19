package com.zq.xijue.mapper;

import com.zq.xijue.entity.Source;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SourceMapper {
    int insert(Source record);

    int insertSelective(Source record);

    Source querySourceById(String id);

    List<Source> querySourceByPid(String id);

    List<Source> lookingForLike(Source record);

    List<Source> querySourcePage(Source record);

    Source querySourceDownLoadUrlById(String id);
}