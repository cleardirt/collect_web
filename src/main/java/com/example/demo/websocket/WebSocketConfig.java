package com.example.demo.websocket;

import com.example.demo.service.NoticeService;
import com.example.demo.service.report.RatingReportListService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.Resource;

@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Resource
    public void setNoticeService(NoticeService noticeService){
        WebSocket.noticeService = noticeService;
    }

    @Resource
    public void setRatingReportListService(RatingReportListService ratingReportListService){
        WebSocket.ratingReportListService = ratingReportListService;
    }


}
