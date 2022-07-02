package com.example.demo;

import com.example.demo.enums.UserRole;
import com.example.demo.mapperservice.report.DeviceInfoMapper;
import com.example.demo.mapperservice.task.RuleMapper;
import com.example.demo.mapperservice.task.TaskMapper;
import com.example.demo.mapperservice.task.TaskProgressMapper;
import com.example.demo.mapperservice.user.UserMapper;
import com.example.demo.mapperservice.user.UserSelectMapper;
import com.example.demo.po.task.Task;
import com.example.demo.po.user.User;
import com.example.demo.po.user.UserSelect;
import com.example.demo.serviceiml.task.AppServiceIml;
import com.example.demo.serviceiml.task.TaskServiceIml;
import com.example.demo.serviceiml.user.UserServiceIml;
import com.example.demo.vo.ResultVO;
import com.example.demo.vo.task.AppVO;
import com.example.demo.vo.task.RuleVO;
import com.example.demo.vo.task.TaskProgressVO;
import com.example.demo.vo.task.TaskVO;
import com.example.demo.vo.user.UserVO;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MapperScan(basePackages = {"com.example.demo.mapperservice"})
public class TaskServiceTest {
    @Autowired
    private TaskServiceIml taskServiceIml;
    @Autowired
    private AppServiceIml appServiceIml;
    @Autowired
    private UserServiceIml userServiceIml;
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private TaskProgressMapper taskProgressMapper;
    @Resource
    private DeviceInfoMapper deviceInfoMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private RuleMapper ruleMapper;
    @Resource
    private UserSelectMapper userSelectMapper;

    @Test
    public void getTaskTest(){
        taskServiceIml.selectTask(1,1);
        taskServiceIml.selectTask(2,1);
        PageInfo<TaskVO> pageInfo= taskServiceIml.getTasks(1,2,null,null);
        assert(pageInfo.getList().size()==2);
        List<Task> tasks=taskMapper.selectUserAllUnfinished(1);
        assert (tasks!=null);
        taskProgressMapper.deleteByUidAndTaskId(1,2);
        taskProgressMapper.deleteByUidAndTaskId(1,1);
    }

    @Test
    public void createTaskTest(){
        ResultVO<TaskVO> resultVO = createTask();
        assert (resultVO.getData().getId()!=null);
        taskMapper.deleteByPrimaryKey(resultVO.getData().getId());
        assert (taskMapper.selectByPrimaryKey(resultVO.getData().getId())==null);
        deviceInfoMapper.deleteByTaskId(resultVO.getData().getId());
    }

    public ResultVO<TaskVO> createTask(){
        TaskVO taskVO=new TaskVO();
        taskVO.setAuftraggeberId(3);
        taskVO.setTitle("test");
        taskVO.setTestStartTime(new Date());
        taskVO.setTestEndTime(new Date());
        taskVO.setIsOpen(false);
        taskVO.setWorkerNum(1);
        taskVO.setTestType("功能测试");
        taskVO.setDifficulty(1);
        List<String> devices = new ArrayList<>();
        devices.add("iOS");
        devices.add("Linux");
        return taskServiceIml.creatTask(taskVO, devices);
    }

    @Test
    public void getTaskInprogressTest(){
        PageInfo<TaskVO> resultVO=taskServiceIml.getTasksInProgress(
                1,5,2,"");
//        assert (resultVO.getList().size()!=0);
        assert (resultVO!=null);
    }

    @Test
    public void getApp(){
        List<AppVO> res=appServiceIml.getApp(1);
        assert(res!=null);
    }

    @Test
    public void selectTask(){
        TaskProgressVO taskProgressVO = taskServiceIml.selectTask(1,2).getData();
        assert (!taskProgressVO.getIsFinished());
        taskServiceIml.cancelSelectTask(1,2);
        assert (taskProgressMapper.selectByUidAndTaskId(2,1)==null);

        UserSelect userSelect = userSelectMapper.selectByUserId(2);
        userSelect.setPerformanceTest(0);
        userSelect.setFunctionTest(0);
        userSelectMapper.updateByPrimaryKey(userSelect);
    }

    @Test
    public void getOneTaskTest(){
        ResultVO<TaskVO> resultVO = createTask();
        assert (taskServiceIml.getTask(resultVO.getData().getId())!=null);
        taskMapper.deleteByPrimaryKey(resultVO.getData().getId());
        deviceInfoMapper.deleteByTaskId(resultVO.getData().getId());
    }

    @Test
    public void getTaskSelectedNumTest(){
        taskServiceIml.selectTask(1,2);
        taskServiceIml.selectTask(1,3);
        assert (taskServiceIml.getTasksSelectedNum(1).compareTo(1) > 0);
        taskProgressMapper.deleteByUidAndTaskId(2,1);
        taskProgressMapper.deleteByUidAndTaskId(3,1);

        UserSelect userSelect = userSelectMapper.selectByUserId(2);
        userSelect.setPerformanceTest(0);
        userSelect.setFunctionTest(0);
        userSelectMapper.updateByPrimaryKey(userSelect);
    }

    private int createAuftra(){
        User auftra = new User();
        auftra.setCreateTime(new Date());
        auftra.setUserRole(UserRole.AUFTRA.toString());
        auftra.setPhone("18752774855");
        auftra.setUsername("hh");
        auftra.setUserpass("123456");
        UserVO userVO = userServiceIml.userRegister(new UserVO(auftra)).getData();
        return userVO.getId();
    }

    @Test
    public void judgePermissionTest(){
        assert (!taskServiceIml.judgePermission(2,1));
        taskServiceIml.selectTask(1,2);
        assert (taskServiceIml.judgePermission(2,1));
        taskServiceIml.cancelSelectTask(1,2);
        UserSelect userSelect = userSelectMapper.selectByUserId(2);
        userSelect.setPerformanceTest(0);
        userSelect.setFunctionTest(0);
        userSelectMapper.updateByPrimaryKey(userSelect);

        assert (taskServiceIml.judgePermission(3,1));

        assert (taskServiceIml.judgePermission(1,2));

        int auftraId = createAuftra();
        assert (!taskServiceIml.judgePermission(auftraId,1));
        userMapper.deleteByPrimaryKey(auftraId);
    }

    @Test
    public void getSelectedTasksTest(){
        taskServiceIml.selectTask(1,2);
        taskServiceIml.selectTask(2,2);
        assert (taskServiceIml.
                getSelectedTasks(1,2,2,1,"功能测试").getList()
        .size()>0);
        assert (taskServiceIml.
                getSelectedTasks(1,2,2,0,"功能测试")!=null);
        assert (taskServiceIml.
                getSelectedTasks(1,2,2,1,"性能测试").getList()
                .size()>0);
        assert (taskServiceIml.
                getSelectedTasks(1,2,2,0,"性能测试").getList()
                !=null);

        taskServiceIml.cancelSelectTask(1,2);
        taskServiceIml.cancelSelectTask(2,2);
        UserSelect userSelect = userSelectMapper.selectByUserId(2);
        userSelect.setPerformanceTest(0);
        userSelect.setFunctionTest(0);
        userSelectMapper.updateByPrimaryKey(userSelect);
    }

    @Test
    public void getDevicesTest(){
        TaskVO taskVO = createTask().getData();
        assert (taskServiceIml.getDevices(taskVO.getId()).size()==2);
        taskMapper.deleteByPrimaryKey(taskVO.getId());
        deviceInfoMapper.deleteByTaskId(taskVO.getId());
    }

    @Test
    public void RuleTest(){
//        assert (taskServiceIml.setRule(1).getCode()==0||taskServiceIml.setRule(1).getCode()==1);
        if (ruleMapper.selectByStatus(false,false,true)!=null){
            taskServiceIml.deleteRule(ruleMapper.selectByStatus(false,false,true).getId());
        }
        assert (taskServiceIml.getCurrentRules().getId()==1);
        RuleVO ruleVO = new RuleVO();
        ruleVO.setSelected(false);
        ruleVO.setProfessionalAbilityRanking(false);
        ruleVO.setSortByTaskCategory(false);
        ruleVO.setTestEquipmentSorting(true);

        RuleVO r = taskServiceIml.createRule(ruleVO).getData();
        assert (!r.getSelected());
        assert (taskServiceIml.setRule(r.getId()).getCode()==1);
        assert (taskServiceIml.getAllRule().size()>=2);
        taskServiceIml.setRule(1);
        taskServiceIml.deleteRule(r.getId());
    }

}
