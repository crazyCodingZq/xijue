package com.zq.xijue.mapper;

import com.zq.xijue.entity.SysUserRole;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserRoleMapper {
    @Select("SELECT * FROM sys_user_role WHERE user_id = #{userId}")
    List<SysUserRole> listByUserId(Integer userId);

    @Insert("insert into sys_user_role(user_id,role_id) values (#{userId},#{roleId})")
    void insert(SysUserRole sysUserRole);
}
