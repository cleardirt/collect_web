package com.example.demo.util;

import com.example.demo.mapperservice.AchievementMapper;
import com.example.demo.po.Achievement;
import com.example.demo.po.Notice;
import com.example.demo.websocket.WebSocket;

import javax.annotation.Resource;
import java.util.Date;


public class AchievementManage {
    @Resource
    static AchievementMapper achievementMapper;
    public static void addAchievements(int uid,String type,String content){
        Achievement achievement=new Achievement();
        achievement.setContent(content);
        achievement.setType(type);
        achievement.setGetTime(new Date());
        achievement.setUid(uid);
        achievementMapper.insert(achievement);
        Notice notice=WebSocket.prepareMessage(uid,"恭喜您获得成就："+content,Constant.SYSTEM_NOTIFICATION+"");
        WebSocket.sendMessage2AUser(uid,notice.getId()+"_"+"恭喜您获得成就："+content,Constant.SYSTEM_NOTIFICATION+"");
    }
}
