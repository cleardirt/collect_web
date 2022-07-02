package com.example.demo.vo.user;

import com.example.demo.po.Achievement;
import lombok.Data;

import java.util.Date;
@Data
public class AchievementVO {
    private Integer id;

    private Integer uid;

    private String type;

    private String content;

    private Date getTime;

    public AchievementVO(Achievement achievement){
        this.id=achievement.getId();
        this.uid=achievement.getUid();
        this.type=achievement.getType();
        this.content=achievement.getContent();
        this.getTime=achievement.getGetTime();
    }
}
