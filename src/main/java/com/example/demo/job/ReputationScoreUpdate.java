package com.example.demo.job;

import com.example.demo.mapperservice.report.ReportMapper;
import com.example.demo.mapperservice.task.TaskMapper;
import com.example.demo.mapperservice.task.TaskProgressMapper;
import com.example.demo.mapperservice.user.UserMapper;
import com.example.demo.po.Notice;
import com.example.demo.po.task.Task;
import com.example.demo.po.task.TaskProgress;
import com.example.demo.po.user.User;
import com.example.demo.util.Constant;
import com.example.demo.util.DateHelper;
import com.example.demo.websocket.WebSocket;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling   // 2.开启定时任务
public class ReputationScoreUpdate {
    @Resource
    ReportMapper reportMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    TaskMapper taskMapper;
    @Resource
    TaskProgressMapper taskProgressMapper;

    @Scheduled(cron = "0 0 0 1-31 * ? ")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    //https://cron.qqe2.com/   cron在线生成
    private void configureTasks() {
        double parameter = 0.2;
        List<Integer> tasksIds = taskMapper.selectEndsToday(DateHelper.removeTime(new Date()), DateHelper.getYesterday(new Date()));
        for (Integer task : tasksIds) {
            List<TaskProgress> taskProgresses = taskProgressMapper.selectByTaskId(task);
            for (TaskProgress taskProgress : taskProgresses) {
                if (!taskProgress.getIsFinished()) {
                    User user = userMapper.selectByPrimaryKey(taskProgress.getWorkerId());
                    BigDecimal credit = user.getCredit();
                    if (credit.doubleValue() <= 1) {
                        credit = BigDecimal.ZERO;
                        Notice notice=WebSocket.prepareMessage(user.getId(),"您的信誉值过低，将无法参加新任务，请尽快完成已参加任务！", Constant.SYSTEM_NOTIFICATION+"");
                        WebSocket.sendMessage2AUser(user.getId(),notice.getId()+"_您的信誉值过低，将无法参加新任务，请尽快完成已参加任务！", Constant.SYSTEM_NOTIFICATION+"");
                    }
                    else
                        credit = credit.subtract(new BigDecimal("1"));
                    user.setCredit(credit);
                    userMapper.updateByPrimaryKey(user);
                }
            }
        }
    }


}
