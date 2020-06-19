package com.zq.xijue.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysUser implements Serializable {
    static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String mobile;

    private String password;

    private int vipLevel;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date vipLimit;
}
