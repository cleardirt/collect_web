package com.example.demo.websocket;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.po.Notice;
import com.example.demo.service.NoticeService;
import com.example.demo.service.report.RatingReportListService;
import com.example.demo.util.Constant;
import com.example.demo.vo.NoticeVO;
import com.example.demo.vo.report.RatingTaskVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/webSocket/{uid}")
@Slf4j
@Component
public class WebSocket {

    public static NoticeService noticeService;

    public static RatingReportListService ratingReportListService;

    private static int onlineCount = 0;
    private static Map<Integer, WebSocket> clients = new ConcurrentHashMap<>();
    private Session session;
    private Integer uid;


    @OnOpen
    public void onOpen(@PathParam("uid") Integer uid, Session session) {
        this.uid = uid;
        this.session = session;
        WebSocket.onlineCount++;
        System.out.println(onlineCount);
        clients.put(uid, this);

        // 一连接就发送评审任务
        sendRatingTaskNotice(uid);

        List<Notice> notices=noticeService.getUnSent(uid);
        for(Notice notice:notices){
            sendMessage2AUser(uid,notice.getContent(),notice.getType());
        }

    }

    @OnClose
    public void onClose() {
        clients.remove(uid);
        WebSocket.onlineCount--;
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println(message);
        // 标记已发送消息
        if (message.matches("[0-9]+")){
            if(Integer.parseInt(message,10)!=0 && !message.equals("NAN"))
            noticeService.markSentNotice(Integer.parseInt(message,10));
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("WebSocket发生错误：" + throwable.getMessage());
        throwable.printStackTrace();
    }

    public static void sendMessage(String message) {
        // 向所有连接websocket的客户端发送消息
        // 可以修改为对某个客户端发消息
        for (WebSocket item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

    public static void sendMessage2AUser(Integer uid, String message,String type){
        WebSocket item = clients.get(uid);
        item.session.getAsyncRemote().sendText(type+message);
    }

    public static Notice prepareMessage(Integer uid, String message, String type){
            Notice notice=new Notice();
            notice.setContent(message);
            notice.setUid(uid);
            notice.setType(type);
            notice.setSentDate(new Date());
            notice.setIsRead(false);
            noticeService.storeNotice(notice);
            return notice;
    }

    public void sendRatingTaskNotice(Integer uid){
        List<RatingTaskVO> noticeList = ratingReportListService.getUnsentRatingTaskNotice(uid);
        if (noticeList==null){
            return;
        }
        List<Integer> noticeIds = new ArrayList<>();
        for (RatingTaskVO ratingTaskVO:noticeList){
            noticeIds.add(noticeService.storeNotice(new Notice(new NoticeVO(uid,ratingTaskVO))));
        }
        JSONObject jo = new JSONObject();
        for (int i = 0; i<noticeIds.size(); i++){
            jo.put(noticeIds.get(i).toString(),noticeList.get(i));
        }

        sendMessage2AUser(uid,jo.toJSONString(), Constant.REVIEW_REPORT+"");
    }

    public static List<Integer> getOnlineUsers(){
        System.out.println(clients.keySet());
        return new ArrayList<>(clients.keySet());
    }

}


