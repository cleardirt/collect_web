package com.example.demo.controller.task;

import com.example.demo.service.task.TaskService;
import com.example.demo.util.Constant;
import com.example.demo.vo.ResultVO;
import com.example.demo.vo.report.DeviceInfoVO;
import com.example.demo.vo.task.RuleVO;
import com.example.demo.vo.task.TaskProgressVO;
import com.example.demo.vo.task.TaskVO;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Resource
    TaskService taskService;

    @PostMapping("/create")
    public ResultVO<TaskVO> creatTask(@RequestBody TaskVO taskVo,@RequestParam List<String> devices){
        return taskService.creatTask(taskVo,devices);
    }

    @PostMapping("/delete")
    public ResultVO<TaskVO> deleteTask(@RequestBody TaskVO taskVo){
        return taskService.deleteTask(taskVo);
    }

    @PostMapping("/select/{taskId}")
    public ResultVO<TaskProgressVO> selectTask(@RequestParam Integer uid, @PathVariable Integer taskId){
        return taskService.selectTask(taskId,uid);
    }

    @PostMapping("/cancelSelect/{taskId}")
    public ResultVO<TaskVO> cancelSelectTask(@RequestParam Integer uid, @PathVariable Integer taskId){
        return taskService.cancelSelectTask(taskId,uid);
    }

    @GetMapping("/{taskId}")
    public TaskVO getTasksById(@PathVariable Integer taskId) {
        return taskService.getTask(taskId);
    }

    @GetMapping("/workernum/{taskId}")
    public Integer getTasksSelectedNum(@PathVariable Integer taskId) {
        return taskService.getTasksSelectedNum(taskId);
    }

    @GetMapping("/all/{page}")
    public PageInfo<TaskVO> getTasksByIdOrType(@RequestParam(required = false, defaultValue = "") Integer uid,@RequestParam(required = false, defaultValue = "") String type,
                                              @PathVariable Integer page) {
        return taskService.getTasks(page, Constant.Task_PAGE_SIZE, uid,type);
    }

    @GetMapping("/selected/{page}")
    public PageInfo<TaskVO> getSelectedTasks(@RequestParam Integer uid, @RequestParam Integer mode, @RequestParam(required = false, defaultValue = "") String type, @PathVariable Integer page) {
        return taskService.getSelectedTasks(page, Constant.Task_PAGE_SIZE, uid,mode,type);
    }

    @GetMapping("/missionsquare/{page}")
    public PageInfo<TaskVO> getTasksByIdOrTypeInProgress(@RequestParam Integer uid,@RequestParam(required = false, defaultValue = "") String type,
                                               @PathVariable Integer page) {
        return taskService.getTasksInProgress(page, Constant.Task_PAGE_SIZE, uid,type);
    }

    @GetMapping("/judge/{taskId}")
    public Boolean judgeByUTid(@RequestParam Integer uid, @PathVariable Integer taskId){
        return taskService.judgePermission(uid,taskId);
    }

    @GetMapping("/devices/{taskId}")
    public List<DeviceInfoVO> getDevices(@PathVariable Integer taskId){
        return taskService.getDevices(taskId);
    }

    @PostMapping("/setRule/{ruleId}")
    public ResultVO<RuleVO> setRule(@PathVariable Integer ruleId){
        return taskService.setRule(ruleId);
    }

    @GetMapping("/currentRules")
    public RuleVO getCurrentRules(){
        return taskService.getCurrentRules();
    }

    @PostMapping("/createRule")
    public ResultVO<RuleVO> createRule(@RequestBody RuleVO ruleVO){
        return taskService.createRule(ruleVO);
    }

    @GetMapping("/allRule")
    public List<RuleVO> getAllRule(){
        return taskService.getAllRule();
    }

    @PostMapping("/deleteRule/{ruleId}")
    public ResultVO<RuleVO> deleteRule(@PathVariable Integer ruleId){
        return taskService.deleteRule(ruleId);
    }
}
