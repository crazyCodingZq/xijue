package com.zq.xijue.service;

import com.zq.xijue.entity.SysUser;
import com.zq.xijue.entity.SysUserRole;
import com.zq.xijue.mapper.SysUserMapper;
import com.zq.xijue.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserService {
    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    public SysUser getById(Integer id) {
        return userMapper.selectById(id);
    }

    public SysUser getByName(String name) {
        return userMapper.selectByName(name);
    }

    public SysUser getByMobile(String mobile) {
        return userMapper.selectByMobile(mobile);
    }

    public List<SysUser> queryUserList() {
        return userMapper.queryUserList();
    }

    public void register(String mobile, String password) {
        SysUser sysUser = new SysUser();
        sysUser.setName(mobile);
        sysUser.setMobile(mobile);
        sysUser.setPassword(password);
        userMapper.insert(sysUser);
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(mobile);
        sysUserRole.setRoleId(2);
        sysUserRoleMapper.insert(sysUserRole);
    }

    public void updateUser(SysUser sysUser) {
        userMapper.update(sysUser);
    }
}
