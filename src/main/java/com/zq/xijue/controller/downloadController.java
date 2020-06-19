package com.zq.xijue.controller;

import com.zq.xijue.core.ResultCode;
import com.zq.xijue.core.ResultVO;
import com.zq.xijue.entity.Source;
import com.zq.xijue.entity.SysUser;
import com.zq.xijue.service.SourceService;
import com.zq.xijue.service.SysUserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @Author: zhangQ
 * @Date: 2020/5/25 22:01
 */
@Controller
@RequestMapping("/download")
public class downloadController {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(AlipayController.class);
    @Autowired
    private SourceService sourceService;
    @Autowired
    private SysUserService userService;

    @ResponseBody
    @RequestMapping(value = "/source", method = RequestMethod.POST)
    public ResultVO downloadSource(String id, Authentication authentication) {
        try {
            //SysUser user = userService.getByMobile(authentication.getName());
            SysUser user = userService.getByName(authentication.getName());
            if (user.getVipLevel() == 0) {
                return new ResultVO(false, ResultCode.NOT_VIP, "开通VIP，下载全站资源~");
            }
            if (user.getVipLevel() == 1 && user.getVipLimit().before(new Date())) {
                return new ResultVO(false, ResultCode.VIP_EXPIRE, "您的vip已过期，请续费~");
            }
            if (user.getVipLevel() == 1 && user.getVipLimit().after(new Date())) {
                Source source = sourceService.querySourceDownLoadUrlById(id);
                return new ResultVO(source.getImgDownloadUrl());
            }
            if (user.getVipLevel() == 20) {
                Source source = sourceService.querySourceDownLoadUrlById(id);
                return new ResultVO(source.getImgDownloadUrl());
            }
        } catch (Exception e) {
            logger.error("downloadSource error:", e);
        }
        return new ResultVO(false, ResultCode.NOT_VIP, "开通VIP，下载全站资源~");
    }
}
