package com.example.demo.job;

import com.example.demo.mapperservice.report.RatingReportListMapper;
import com.example.demo.po.report.RatingReportList;
import com.example.demo.po.report.Report;
import com.example.demo.websocket.WebSocket;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import com.alibaba.fastjson.JSONObject;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableScheduling
public class RatingTaskNotification {
    @Resource
    RatingReportListMapper reportListMapper;

    JSONObject jo = new JSONObject();

    @Scheduled(cron = "0 0 0,12 1-31 * ? ")
    private void notifyRatersToRate(){
//        List<Integer> onlineUsers = WebSocket.getOnlineUsers();
//        for (Integer uid: onlineUsers){
//            RatingReportList reportList = reportListMapper.selectByUid(uid);
//            String recommendedListStr = reportList.getReportToRate();
//            String finishedListStr = reportList.getRatedReport();
//            String[] recommended = recommendedListStr.split("_");
//            String[] finished = finishedListStr.split("_");
//            List<Integer> recommendedIdList = strList2IntList(recommended);
//            List<Integer> finishedIdList = strList2IntList(finished);
//            List<Integer> newRec = new ArrayList<>();
//            for (Integer id : recommendedIdList) {
//                if (!finishedIdList.contains(id)) {
//                    newRec.add(id);
//                }
//            }
//            jo.put("rating_tasks", newRec);
//            WebSocket.sendMessage(jo.toString());
//        }
    }

    private List<Integer> strList2IntList(String[] strList){
        List<Integer> result = new ArrayList<>();
        for (String s : strList) {
            result.add(Integer.parseInt(s, 10));
        }
        return result;
    }
}
