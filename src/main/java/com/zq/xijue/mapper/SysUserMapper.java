package com.zq.xijue.mapper;

import com.zq.xijue.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserMapper {
    @Select("SELECT * FROM sys_user WHERE id = #{id}")
    SysUser selectById(Integer id);

    @Select("SELECT * FROM sys_user WHERE name = #{name}")
    SysUser selectByName(String name);

    @Select("SELECT * FROM sys_user WHERE mobile = #{mobile}")
    SysUser selectByMobile(String mobile);

    void insert(SysUser sysUser);

    void update(SysUser sysUser);

    List<SysUser> queryUserList();
}
