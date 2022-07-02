package com.example.demo.po;

import com.example.demo.vo.user.AchievementVO;

import java.util.Date;

public class Achievement {
    private Integer id;

    private Integer uid;

    private String type;

    private String content;

    private Date getTime;

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

    public Date getGetTime() {
        return getTime;
    }

    public void setGetTime(Date getTime) {
        this.getTime = getTime;
    }

    public Achievement(){}

    public Achievement(AchievementVO achievement){
        this.id=achievement.getId();
        this.uid=achievement.getUid();
        this.type=achievement.getType();
        this.content=achievement.getContent();
        this.getTime=achievement.getGetTime();
    }
}