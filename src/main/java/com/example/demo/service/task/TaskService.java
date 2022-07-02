package com.example.demo.service.task;

import com.example.demo.vo.ResultVO;
import com.example.demo.vo.report.DeviceInfoVO;
import com.example.demo.vo.task.RuleVO;
import com.example.demo.vo.task.TaskProgressVO;
import com.example.demo.vo.task.TaskVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface TaskService {
    ResultVO<TaskVO> creatTask(TaskVO taskVO, List<String> devices);

    ResultVO<TaskProgressVO> selectTask(Integer taskId, Integer uid);

    ResultVO<TaskVO> cancelSelectTask(Integer taskId, Integer uid);

    PageInfo<TaskVO> getTasks(Integer page, Integer coursePageSize, Integer uid, String type);

    PageInfo<TaskVO> getTasksInProgress(Integer page, Integer PageSize, Integer uid,String type);

    ResultVO<TaskVO> deleteTask(TaskVO taskVo);

    TaskVO getTask(Integer taskId);

    Integer getTasksSelectedNum(Integer taskId);

    Boolean judgePermission(Integer uid, Integer taskId);

    PageInfo<TaskVO> getSelectedTasks(Integer page, Integer task_page_size, Integer uid, Integer mode, String type);

    List<DeviceInfoVO> getDevices(Integer taskId);

    ResultVO<RuleVO> setRule(Integer ruleId);

    RuleVO getCurrentRules();

    ResultVO<RuleVO> createRule(RuleVO ruleVO);

    List<RuleVO> getAllRule();

    ResultVO<RuleVO> deleteRule(Integer ruleId);
}
