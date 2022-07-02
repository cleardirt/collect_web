package com.example.demo.job;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.websocket.WebSocket;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class WebSocketTest {
    JSONObject jo = new JSONObject();

    @Scheduled(cron = "1-59 1-59 0-23 1-31 * ? ")
    private void testNotify(){
//        jo.put("success",true);
//        WebSocket.sendMessage(jo.toJSONString());
//        System.out.println("a push");
//        WebSocket.getOnlineUsers();
    }

}
