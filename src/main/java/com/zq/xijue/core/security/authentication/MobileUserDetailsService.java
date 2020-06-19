package com.zq.xijue.core.security.authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zq.xijue.entity.SysRole;
import com.zq.xijue.entity.SysUser;
import com.zq.xijue.entity.SysUserRole;
import com.zq.xijue.service.SysRoleService;
import com.zq.xijue.service.SysUserRoleService;
import com.zq.xijue.service.SysUserService;

/**
 * 手机登录 UserDetailService，通过手机号读取信息
 * @author jitwxs
 * @since 2019/1/8 23:37
 */
@Service
public class MobileUserDetailsService implements UserDetailsService {
    @Autowired
    private SysUserService userService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysUserRoleService userRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // 从数据库中取出用户信息
        SysUser user = userService.getByMobile(username);

        // 判断用户是否存在
        if (user == null) {
            throw new UsernameNotFoundException("该手机号码尚未注册");
        }

        // 添加权限
        List<SysUserRole> userRoles = userRoleService.listByUserId(user.getId());
        for (SysUserRole userRole : userRoles) {
            SysRole role = roleService.getById(userRole.getRoleId());
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        // 返回UserDetails实现类
        return new User(user.getName(), user.getPassword(), authorities);
    }
}
