package com.example.demo.aspect;

import com.example.demo.mapperservice.user.UserMapper;
import com.example.demo.po.user.User;
import com.example.demo.po.user.UserRecommendation;
import com.example.demo.util.Constant;
import com.example.demo.vo.ResultVO;
import com.example.demo.vo.report.ReportDetailVO;
import com.example.demo.vo.task.TaskProgressVO;
import com.example.demo.vo.task.TaskVO;
import com.example.demo.vo.user.UserVO;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Aspect
@Component
public class UpdateActivityAspect {
    @Resource
    UserMapper userMapper;

    @Pointcut("execution(public * com.example.demo.controller.user.UserController.login(..)))")
    public void login(){}
    @Pointcut("execution(public * com.example.demo.controller.task.TaskController.selectTask(..)))")
    public void selectTask(){}
    @Pointcut("execution(public * com.example.demo.controller.report.ReportController.createReport(..)))")
    public void creatReport(){}
    @AfterReturning(returning = "obj",pointcut = "login()")
    public void updateActivityAfterLogin(ResultVO<UserVO> obj){
        UserVO userVO=obj.getData();
        if (userVO!=null) {
            if (userVO.getActivity() != null)
                userVO.setActivity(userVO.getActivity() + 1);
            else
                userVO.setActivity(1);
            userMapper.updateByPrimaryKey(new User(userVO));
        }
    }

    @AfterReturning(returning = "obj",pointcut = "selectTask()")
    public void updateActivityAfterSelect(ResultVO<TaskProgressVO> obj){
        TaskProgressVO taskProgressVO=obj.getData();
        User user=userMapper.selectByPrimaryKey(taskProgressVO.getWorkerId());
        UserVO userVO=new UserVO(user);
        if (userVO.getActivity()!=null)
            userVO.setActivity(userVO.getActivity()+1);
        else
            userVO.setActivity(1);
        userMapper.updateByPrimaryKey(new User(userVO));
    }

    @AfterReturning(returning = "obj",pointcut = "creatReport()")
    public void updateActivityAfterFinish(ResultVO<ReportDetailVO> obj){
        if (obj.getCode().equals(Constant.REQUEST_SUCCESS)) {
            User user=userMapper.selectByPrimaryKey(obj.getData().getGeneralInfo().getWorkerId());
            UserVO userVO=new UserVO(user);
            if (userVO.getActivity()!=null)
                userVO.setActivity(userVO.getActivity()+1);
            else
                userVO.setActivity(1);
        }
    }
}
