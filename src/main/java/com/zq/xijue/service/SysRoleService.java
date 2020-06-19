package com.zq.xijue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zq.xijue.entity.SysRole;
import com.zq.xijue.mapper.SysRoleMapper;

@Service
public class SysRoleService {
    @Autowired
    private SysRoleMapper roleMapper;

    public SysRole getById(Integer id) {
        return roleMapper.selectById(id);
    }

    public SysRole getByName(String roleName) {
        return roleMapper.selectByName(roleName);
    }
}
