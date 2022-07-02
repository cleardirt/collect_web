package com.example.demo.po;

import com.example.demo.vo.NoticeVO;

import java.util.Date;

public class Notice {
    private Integer id;

    private Integer uid;

    private String type;

    private String content;

    private Boolean isRead;

    private Date sentDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public Notice(){}

    public Notice(NoticeVO noticeVO){
        this.uid = noticeVO.getUid();
        this.type = noticeVO.getType();
        this.content = noticeVO.getContent();
        this.isRead = noticeVO.getIsRead();
        this.sentDate = noticeVO.getSentDate();
    }
}