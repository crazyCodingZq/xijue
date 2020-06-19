package com.zq.xijue.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import lombok.Data;

@Data
public class SysPermission implements Serializable {
    static final long serialVersionUID = 1L;

    private Integer id;

    private String url;

    private Integer roleId;

    private String permission;

    private List permissions;

    public List getPermissions() {
        return Arrays.asList(permission.trim().split(","));
    }
}
