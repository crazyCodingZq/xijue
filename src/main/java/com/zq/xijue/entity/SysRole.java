package com.zq.xijue.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class SysRole implements Serializable {
    static final long serialVersionUID = 1L;

    private Integer id;

    private String name;
}
