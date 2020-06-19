package com.zq.xijue.service;

import com.github.pagehelper.Page;
import com.zq.xijue.entity.Source;
import com.zq.xijue.mapper.SourceMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author: zhangQ
 * @Date: 2020/4/19 16:12
 */
@Service("sourceService")
public class SourceService {
    private final Logger log = LoggerFactory.getLogger(SourceService.class);
    @Autowired
    public SourceMapper sourceMapper;

    public Source querySourceById(String id) {
        return sourceMapper.querySourceById(id);
    }

    public List<Source> lookingForLike(String pid, String style) {
        Source source = new Source();
        source.setCategory(pid);
        source.setSourceStyle(style);
        return sourceMapper.lookingForLike(source);
    }

    public List<Source> querySourceByPid(String pid) {
        return sourceMapper.querySourceByPid(pid);
    }

    public Page<Source> querySourcePage(String pid, String style) {
        Source source = new Source();
        source.setCategory(pid);
        if (!StringUtils.isEmpty(style)) {
            source.setSourceStyle(style);
        }
        Page<Source> page = (Page<Source>) sourceMapper.querySourcePage(source);
        return page;
    }

    public Source querySourceDownLoadUrlById(String id) {
        return sourceMapper.querySourceDownLoadUrlById(id);
    }

    public void saveSource(MultipartFile imgFile, Source source) {
        String fileName = imgFile.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String staticPath = getClass().getClassLoader().getResource("static").getFile();
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        String filePath = getDateString() + File.separator + fileName;
        File dest = new File(staticPath + File.separator + filePath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            imgFile.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        source.setImgUrl("/static/" + filePath);
        source.setCreateDate(new Date());
        source.setSize(imgFile.getSize());
        source.setSourceStyle(source.getTagName());
        sourceMapper.insertSelective(source);
    }

    private String getDateString() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        return date.format(formatter);
    }
}
