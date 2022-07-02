package com.example.demo.aspect;

import com.example.demo.mapperservice.report.ReportMapper;
import com.example.demo.mapperservice.user.UserMapper;
import com.example.demo.mapperservice.user.UserRatingMapper;
import com.example.demo.po.user.User;
import com.example.demo.util.AchievementManage;
import com.example.demo.util.Constant;
import com.example.demo.vo.ResultVO;
import com.example.demo.vo.report.ReportDetailVO;
import com.example.demo.vo.user.UserRatingVO;
import com.example.demo.vo.user.UserVO;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Component
public class UpdateAchievement {
    //定义一个公用方法
    @Resource
    UserMapper userMapper;
    @Resource
    UserRatingMapper userRatingMapper;
    @Resource
    ReportMapper reportMapper;
    @Pointcut("execution(public * com.example.demo.controller.report.ReportController.createReport(..)))")
    public void creatReport(){}
    @Pointcut("execution(public * com.example.demo.job.ScoringAbilityCalculation.configureTasks(..)))")
    public void configureTasks(){}
    @Pointcut("execution(public * com.example.demo.controller.report.ReportController.rateReportById(..)))")
    public void rate(){}

    @AfterReturning(returning = "obj",pointcut = "creatReport()")
    public void updateAchievementAfterFinish(ResultVO<ReportDetailVO> obj){
        if (obj.getCode().equals(Constant.REQUEST_SUCCESS)) {
            int userID=obj.getData().getGeneralInfo().getWorkerId();
            if(obj.getCode().equals(Constant.REQUEST_SUCCESS)&&reportMapper.selectByTesterId(userID).size()==5)
                AchievementManage.addAchievements(userID,"任务","报告达人");
            else if (obj.getCode().equals(Constant.REQUEST_SUCCESS)&&reportMapper.selectByTesterId(userID).size()==10)
                AchievementManage.addAchievements(userID,"任务","报告超人");
        }
    }

    @AfterReturning(returning = "obj",pointcut = "rate()")
    public void updateAchievementAfterRate(ResultVO<UserRatingVO> obj){
        if (obj.getCode().equals(Constant.REQUEST_SUCCESS)) {
            int userID=obj.getData().getUserId();
            if(obj.getCode().equals(Constant.REQUEST_SUCCESS)&&userRatingMapper.selectByUid(userID).size()==5)
                AchievementManage.addAchievements(userID,"评审","评分爱好者");
            else if (obj.getCode().equals(Constant.REQUEST_SUCCESS)&&userRatingMapper.selectByUid(userID).size()==10)
                AchievementManage.addAchievements(userID,"评审","评分达人");
        }
    }
}
