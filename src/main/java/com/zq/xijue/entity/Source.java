package com.zq.xijue.entity;

import java.util.Date;

public class Source {
    private Long id;

    private String name;

    private String category;

    private String imgUrl;

    private String imgDownloadUrl;

    private Integer downloadTimes;

    private Integer viewTimes;

    private Integer collectTimes;

    private Integer score;

    private String software;

    private String suffix;

    private Long size;

    private String provider;

    private String portraiture;

    private String sourceDesc;

    private String tagName;

    private String sourceStyle;

    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgDownloadUrl() {
        return imgDownloadUrl;
    }

    public void setImgDownloadUrl(String imgDownloadUrl) {
        this.imgDownloadUrl = imgDownloadUrl;
    }

    public Integer getDownloadTimes() {
        return downloadTimes;
    }

    public void setDownloadTimes(Integer downloadTimes) {
        this.downloadTimes = downloadTimes;
    }

    public Integer getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(Integer viewTimes) {
        this.viewTimes = viewTimes;
    }

    public Integer getCollectTimes() {
        return collectTimes;
    }

    public void setCollectTimes(Integer collectTimes) {
        this.collectTimes = collectTimes;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getPortraiture() {
        return portraiture;
    }

    public void setPortraiture(String portraiture) {
        this.portraiture = portraiture;
    }

    public String getSourceDesc() {
        return sourceDesc;
    }

    public void setSourceDesc(String sourceDesc) {
        this.sourceDesc = sourceDesc;
    }

    public String getTagDesc() {
        return tagName;
    }

    public void setTagDesc(String tagDesc) {
        tagName = tagDesc;
    }

    public String getSourceStyle() {
        return sourceStyle;
    }

    public void setSourceStyle(String sourceStyle) {
        this.sourceStyle = sourceStyle;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}