package com.example.demo.job;

import com.example.demo.mapperservice.report.RatingReportListMapper;
import com.example.demo.mapperservice.report.ReportMapper;
import com.example.demo.mapperservice.task.TaskMapper;
import com.example.demo.mapperservice.user.RatingForScanMapper;
import com.example.demo.mapperservice.user.UserMapper;
import com.example.demo.mapperservice.user.UserRatingMapper;
import com.example.demo.po.report.RatingReportList;
import com.example.demo.po.report.Report;
import com.example.demo.po.user.RatingForScan;
import com.example.demo.po.user.User;
import com.example.demo.po.user.UserRating;
import com.example.demo.util.AchievementManage;
import com.example.demo.util.DateHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class ScoringAbilityCalculation {
    @Resource
    RatingForScanMapper ratingForScanMapper;
    @Resource
    ReportMapper reportMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    RatingReportListMapper reportListMapper;
    @Resource
    UserRatingMapper userRatingMapper;

    //3.添加定时任务
    @Scheduled(cron = "0 0 0 1-31 * ? ")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    //https://cron.qqe2.com/   cron在线生成
    public void configureTasks() {
        List<RatingForScan> ratingForScans = ratingForScanMapper.selectAll();
        long nd = 1000 * 24 * 60 * 60;
        double parameter = 0.2;
        // long ns = 1000;
        for (RatingForScan rating :
                ratingForScans) {
            if (rating.getRatingTime().compareTo(new Date()) / nd >= 7 || rating.getReportEndTime().before(new Date())) { //相差七天或者报告日期截止
                User rater = userMapper.selectByPrimaryKey(rating.getUserId());
                Report report = reportMapper.selectByPrimaryKey(rating.getReportId());
                double accuracy = 5 - Math.abs(rating.getRating() - report.getScore().doubleValue());
                double begin = rater.getActivity().doubleValue();
                if (rater.getRatingAbility().doubleValue() == 0)
                    rater.setRatingAbility(BigDecimal.valueOf(2 * (1 - parameter) + parameter * accuracy));//没有评价过的话，初始值设为2
                else
                    rater.setRatingAbility(BigDecimal.valueOf(rater.getRatingAbility().doubleValue() * (1 - parameter) + parameter * accuracy));
                if (rater.getRatingAbility().doubleValue() > begin && rater.getRatingAbility().doubleValue() > 4.8)
                    AchievementManage.addAchievements(rater.getId(), "评审", "齐泽克二代");
                else if (rater.getRatingAbility().doubleValue() > begin && rater.getRatingAbility().doubleValue() > 4.5)
                    AchievementManage.addAchievements(rater.getId(), "评审", "锐评达人");
                else if (rater.getRatingAbility().doubleValue() > begin && rater.getRatingAbility().doubleValue() > 4.0)
                    AchievementManage.addAchievements(rater.getId(), "评审", "评论家");
                userMapper.updateByPrimaryKey(rater);
            }
            if (rating.getReportEndTime().before(new Date())) {
                ratingForScanMapper.deleteByPrimaryKey(rating.getId());
            } else {
                rating.setRatingTime(DateHelper.removeTime(new Date()));
            }
        }
        List<RatingReportList> ratingReportLists = reportListMapper.selectAll();
        for (RatingReportList ratingReportList : ratingReportLists) {
            User user = userMapper.selectByPrimaryKey(ratingReportList.getUid());
            int mission = ratingReportList.getReportToRate().split("_").length;
            int competed = ratingReportList.getRatedReport().split("_").length;
            double completionRate = mission != 0 ? (double) competed / mission * 5 : 5;//如果完成一半则意愿度不变
            if (completionRate >= 10)
                completionRate = 10;
            completionRate = ratingReportList.getRatingWillingness().doubleValue() * 0.8 + completionRate * 0.2;//每次更新的参数占0.2
            ratingReportList.setRatingWillingness(BigDecimal.valueOf(completionRate));
            String update = calCommandList(user, ratingReportList.getRatingWillingness().doubleValue());
            ratingReportList.setRatedReport("");
            ratingReportList.setReportToRate(update);
            reportListMapper.updateByPrimaryKey(ratingReportList);
        }
    }

    private String calCommandList(User user, double willing) {
        double ability = user.getRatingAbility().doubleValue();
        double complex = 0.5 * ability + 0.75 * willing;  //能力是0-5，意愿是0-10
        int amount = (int) complex;
        List<Report> reportId = reportMapper.selectEvaluableReport(user.getId(), new Date());
        List<Integer> evaluated = userRatingMapper.selectByUid(user.getId());
        List<Integer> resReportId = new ArrayList<>();
        for (int i = 0; i < reportId.size() && resReportId.size() <= amount; i++) {
            if (evaluated!=null&&!evaluated.contains(reportId.get(i).getId()))
                resReportId.add(reportId.get(i).getId());
        }
        StringBuilder commandList = new StringBuilder();
        for (Integer id : resReportId) {
            commandList.append(id).append("_");
        }
        String res = commandList.substring(0, commandList.length() - 1);
        return res;
    }

}