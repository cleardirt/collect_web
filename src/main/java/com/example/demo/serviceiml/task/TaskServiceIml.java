package com.example.demo.serviceiml.task;

import com.example.demo.aspect.UpdateRecommendationAspect;
import com.example.demo.enums.UserRole;
import com.example.demo.mapperservice.task.RuleMapper;
import com.example.demo.mapperservice.user.UserRecommendationMapper;
import com.example.demo.mapperservice.user.UserSelectMapper;
import com.example.demo.mapperservice.report.DeviceInfoMapper;
import com.example.demo.mapperservice.report.ReportMapper;
import com.example.demo.mapperservice.task.TaskMapper;
import com.example.demo.mapperservice.task.TaskProgressMapper;
import com.example.demo.mapperservice.user.UserMapper;
import com.example.demo.po.report.DeviceInfo;
import com.example.demo.po.task.Rule;
import com.example.demo.po.task.Task;
import com.example.demo.po.task.TaskProgress;
import com.example.demo.po.user.User;
import com.example.demo.po.user.UserRecommendation;
import com.example.demo.po.user.UserSelect;
import com.example.demo.service.task.TaskService;
import com.example.demo.util.Constant;
import com.example.demo.util.POVOListGeneratorUtil;
import com.example.demo.util.PageInfoUtil;
import com.example.demo.vo.ResultVO;
import com.example.demo.vo.report.DeviceInfoVO;
import com.example.demo.vo.task.RuleVO;
import com.example.demo.vo.task.TaskProgressVO;
import com.example.demo.vo.task.TaskVO;
import com.example.demo.vo.user.UserVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class TaskServiceIml implements TaskService {
    @Resource
    UserMapper userMapper;
    @Resource
    TaskMapper taskMapper;
    @Resource
    TaskProgressMapper taskProgressMapper;
    @Resource
    ReportMapper reportMapper;
    @Resource
    DeviceInfoMapper deviceInfoMapper;
    @Resource
    UserSelectMapper userSelectMapper;
    @Resource
    UserRecommendationMapper userRecommendationMapper;
    @Resource
    UpdateRecommendationAspect updateRecommendationAspect;
    @Resource
    RuleMapper ruleMapper;


    @Override
    public ResultVO<TaskVO> creatTask(TaskVO taskVO, List<String> devices) {
        User user=userMapper.selectByPrimaryKey(taskVO.getAuftraggeberId());
        if (!UserRole.valueOf(user.getUserRole()).equals(UserRole.AUFTRA)){
            return new ResultVO<>(Constant.REQUEST_FAIL,"?????????????????????????????????");
        }
        taskVO.setCreateTime(new Date());
        Task task=new Task(taskVO);
        taskMapper.insert(task);
        taskVO=new TaskVO(task);
        for (String device : devices) {
            DeviceInfoVO deviceInfoVO = new DeviceInfoVO();
            deviceInfoVO.setTaskId(taskVO.getId());
            deviceInfoVO.setExplanation(device);
            deviceInfoMapper.insert(new DeviceInfo(deviceInfoVO));
        }
        return new ResultVO<>(Constant.REQUEST_SUCCESS,"????????????",taskVO);
    }

    @Override
    public ResultVO<TaskProgressVO> selectTask(Integer taskId, Integer uid) {
        Task task=taskMapper.selectByPrimaryKey(taskId);
        if (taskProgressMapper.selectByTaskId(taskId).size()>=task.getWorkerNum())
            return new ResultVO<>(Constant.REQUEST_FAIL,"????????????");
        taskMapper.updateByPrimaryKey(task);
        double credit=userMapper.selectByPrimaryKey(uid).getCredit().doubleValue();
        if (credit<1)
            return new ResultVO<>(Constant.REQUEST_FAIL,"??????????????????????????????????????????");

        UserSelect userSelect=userSelectMapper.selectByUserId(uid);
        if (userSelect==null) {
            userSelect = new UserSelect();
            userSelect.setUserId(uid);
            switch (task.getTestType()){
                case "????????????":
                    userSelect.setFunctionTest(1);
                    break;
                case "BUG??????":
                    userSelect.setBugExplore(1);
                    break;
                case  "????????????":
                    userSelect.setPerformanceTest(1);
                    break;
                case "????????????":
                    userSelect.setUsecaseExecution(1);
                    break;
                case "????????????":
                    userSelect.setDeviceConnection(1);
                    break;
            }
        }
        else {
            switch (task.getTestType()){
                case "????????????":
                    userSelect.setFunctionTest(userSelect.getFunctionTest()+1);
                    break;
                case "BUG??????":
                    userSelect.setBugExplore(userSelect.getBugExplore()+1);
                    break;
                case  "????????????":
                    userSelect.setPerformanceTest(userSelect.getPerformanceTest()+1);
                    break;
                case "????????????":
                    userSelect.setUsecaseExecution(userSelect.getUsecaseExecution()+1);
                    break;
                case "????????????":
                    userSelect.setDeviceConnection(userSelect.getDeviceConnection()+1);
                    break;
            }
            userSelectMapper.updateByPrimaryKey(userSelect);
        }



        TaskProgress taskProgress=new TaskProgress();
        if(taskProgressMapper.selectByUidAndTaskId(uid,taskId)!=null)
            return new ResultVO<>(Constant.REQUEST_FAIL,"??????????????????");
        taskProgress.setTaskId(taskId);
        taskProgress.setWorkerId(uid);
        taskProgress.setIsFinished(false);
        taskProgressMapper.insert(taskProgress);


        return new ResultVO<>(Constant.REQUEST_SUCCESS,"????????????",new TaskProgressVO(taskProgress));
    }

    @Override
    public ResultVO<TaskVO> cancelSelectTask(Integer taskId, Integer uid) {
        Task task=taskMapper.selectByPrimaryKey(taskId);
        taskProgressMapper.deleteByUidAndTaskId(uid,taskId);
        UserSelect userSelect=userSelectMapper.selectByUserId(uid);
        switch (task.getTestType()){
            case "????????????":
                userSelect.setFunctionTest(userSelect.getFunctionTest()-1);
                break;
            case "BUG??????":
                userSelect.setBugExplore(userSelect.getBugExplore()-1);
                break;
            case  "????????????":
                userSelect.setPerformanceTest(userSelect.getPerformanceTest()-1);
                break;
            case "????????????":
                userSelect.setUsecaseExecution(userSelect.getUsecaseExecution()-1);
                break;
            case "????????????":
                userSelect.setDeviceConnection(userSelect.getDeviceConnection()-1);
                break;
        }
        userSelectMapper.updateByPrimaryKey(userSelect);
        return new ResultVO<>(Constant.REQUEST_SUCCESS,"??????????????????",new TaskVO(task));
    }



    @Override
    public PageInfo<TaskVO> getTasks(Integer page, Integer PageSize, Integer uid,String type) {
        User user=userMapper.selectByPrimaryKey(uid);
        if(page==null || page<1) page=1;
            PageHelper.startPage(page, PageSize);
        PageInfo<Task> po;
        List<Task> list;
        if (uid==null) {
            if (type!=null&& !type.equals(""))
                list=taskMapper.selectAllByType(type);
            else
                list = taskMapper.selectAll();
        }
        else {
            if (!UserRole.valueOf(user.getUserRole()).equals(UserRole.AUFTRA)) {
                if (type!=null && !type.equals(""))
                    list = taskMapper.selectUserAllAndType(uid,type);
                else
                    list = taskMapper.selectUserAll(uid);
            }
            else {
                if (type!=null && !type.equals(""))
                    list=taskMapper.selectAuftarAllAndType(uid,type);
                else
                    list = taskMapper.selectAuftarAll(uid);
            }
        }
        //todo ??????????????????????????????list??????
        po=new PageInfo<>(list);

        PageInfo<TaskVO> voPageInfo=getTaskVOPageInfo(po);

        for (int i = 0; i < voPageInfo.getList().size(); i++) {
            if (voPageInfo.getList().get(i).getTestStartTime().after(new Date()))
                voPageInfo.getList().get(i).setInProgress(0);
            else if (voPageInfo.getList().get(i).getTestEndTime().before(new Date()))
                voPageInfo.getList().get(i).setInProgress(2);
            else
                voPageInfo.getList().get(i).setInProgress(1);
        }

        return voPageInfo;

    }

    @Override
    public PageInfo<TaskVO> getTasksInProgress(Integer page, Integer PageSize, Integer uid, String type) {
        User user=userMapper.selectByPrimaryKey(uid);
        if(page==null || page<1) page=1;
            PageHelper.startPage(page, PageSize);
        PageInfo<Task> po;
        List<Task> list;
        if (type==null || type.equals(""))
            list=taskMapper.selectInProgressAll(new Date());
        else
            list=taskMapper.selectInProgressAllBYType(new Date(),type);
        PageInfo<TaskVO> voPageInfo = null;
        if (UserRole.valueOf(user.getUserRole()).equals(UserRole.TESTER)){
            list=recommendedSorting(userRecommendationMapper.selectByUserId(uid).getRecommendList(),list);//????????????????????????
            po=new PageInfo<>(list);
            voPageInfo=getTaskVOPageInfo(po);
            for (int i = 0; i < voPageInfo.getList().size(); i++) {
                if (taskProgressMapper.selectByUidAndTaskId(uid,voPageInfo.getList().get(i).getId())!=null) {
                    if(reportMapper.selectByTUid(voPageInfo.getList().get(i).getId(),uid).size()!=0)
                        voPageInfo.getList().get(i).setSelectState(2);
                    else
                        voPageInfo.getList().get(i).setSelectState(1);
                }
                voPageInfo.getList().get(i).setInProgress(1);
            }
        }
        else if (UserRole.valueOf(user.getUserRole()).equals(UserRole.AUFTRA)){
            po=new PageInfo<>(list);
            voPageInfo=getTaskVOPageInfo(po);
            for (int i = 0; i < voPageInfo.getList().size(); i++) {
                if (voPageInfo.getList().get(i).getAuftraggeberId().equals(uid))
                    voPageInfo.getList().get(i).setSelectState(1);
                voPageInfo.getList().get(i).setInProgress(1);
            }
        }
        return voPageInfo;
    }

    @Override
    public ResultVO<TaskVO> deleteTask(TaskVO taskVo) {
        return null;
    }

    @Override
    public TaskVO getTask(Integer taskId) {
        return new TaskVO(taskMapper.selectByPrimaryKey(taskId));
    }

    @Override
    public Integer getTasksSelectedNum(Integer taskId) {
        return taskProgressMapper.selectByTaskId(taskId).size();
    }

    @Override
    public PageInfo<TaskVO> getSelectedTasks(Integer page, Integer PageSize, Integer uid, Integer mode,String type) {
        if(page==null || page<1) page=1;
        PageHelper.startPage(page, PageSize);
        PageInfo<Task> po;
        List<Task> list;
        if (type==null||type.equals("")) {
            if (mode==0)
                list = taskMapper.selectUserAllFinished(uid);
            else
                list = taskMapper.selectUserAllUnfinished(uid);
        }
        else {
            if (mode==0)
                list = taskMapper.selectUserAllAndTypeFinished(uid, type);
            else
                list=taskMapper.selectUserAllAndTypeUnFinished(uid,type);
        }
        po=new PageInfo<>(list);
        PageInfo<TaskVO> voPageInfo=getTaskVOPageInfo(po);
        for (int i = 0; i < voPageInfo.getList().size(); i++) {
            if (voPageInfo.getList().get(i).getTestStartTime().after(new Date()))
                voPageInfo.getList().get(i).setInProgress(0);
            else if (voPageInfo.getList().get(i).getTestEndTime().before(new Date()))
                voPageInfo.getList().get(i).setInProgress(2);
            else
                voPageInfo.getList().get(i).setInProgress(1);
            if (mode==0)
                voPageInfo.getList().get(i).setSelectState(2);
            else
                voPageInfo.getList().get(i).setSelectState(1);
        }
        return voPageInfo;
    }

    @Override
    public List<DeviceInfoVO> getDevices(Integer taskId) {
        ArrayList<DeviceInfoVO> res=new ArrayList<>();
        ArrayList<DeviceInfo> deviceInfos= (ArrayList<DeviceInfo>) deviceInfoMapper.selectByTaskId(taskId);
        for (DeviceInfo deviceInfo : deviceInfos) {
            res.add(new DeviceInfoVO(deviceInfo));
        }
        return res;
    }

    @Override
    public ResultVO<RuleVO> setRule(Integer ruleId) {
        Rule rule=ruleMapper.selectCurrentRule();
        if (rule.getId().equals(ruleId))
            return new ResultVO<>(Constant.REQUEST_FAIL,"?????????????????????");
        rule.setSelected(false);
        ruleMapper.updateByPrimaryKey(rule);

        Rule CurRule=ruleMapper.selectByPrimaryKey(ruleId);
        CurRule.setSelected(true);
        ruleMapper.updateByPrimaryKey(CurRule);

        updateRecommendationAspect.setRule(new RuleVO(CurRule));
        List<User> users=userMapper.selectAllByRole("TESTER");
        for (User user:users) {
            UserRecommendation userRecommendation=userRecommendationMapper.selectByUserId(user.getId());
            userRecommendation.setRecommendList(updateRecommendationAspect.updateRecommendation(new UserVO(user)));
            userRecommendationMapper.updateByPrimaryKey(userRecommendation);
        }
        return new ResultVO<>(Constant.REQUEST_SUCCESS,"????????????",new RuleVO(CurRule));
    }

    @Override
    public RuleVO getCurrentRules() {
        RuleVO ruleVO=updateRecommendationAspect.getRule();
        return ruleVO;
    }

    @Override
    public ResultVO<RuleVO> createRule(RuleVO ruleVO) {
        Rule rule=new Rule(ruleVO);
        rule.setSelected(false);
        ruleMapper.insert(rule);
        return new ResultVO<>(Constant.REQUEST_SUCCESS,"??????????????????",new RuleVO(rule));
    }

    @Override
    public List<RuleVO> getAllRule() {
        List<RuleVO> res=POVOListGeneratorUtil.convert(ruleMapper.selectAll(),RuleVO.class);
        return res;
    }

    @Override
    public ResultVO<RuleVO> deleteRule(Integer ruleId) {
        if (ruleMapper.selectByPrimaryKey(ruleId).getSelected())
            return new ResultVO<>(Constant.REQUEST_FAIL,"???????????????????????????????????????");
        ruleMapper.deleteByPrimaryKey(ruleId);
        return new ResultVO<>(Constant.REQUEST_SUCCESS,"????????????");
    }

    @Override
    public Boolean judgePermission(Integer uid, Integer taskId) {
        if (userMapper.selectByPrimaryKey(uid).getUserRole().equals("TESTER")){
            if (taskProgressMapper.selectByUidAndTaskId(uid,taskId)==null) return false;
        }else if (userMapper.selectByPrimaryKey(uid).getUserRole().equals("AUFTRA")){
            if (taskMapper.selectTaskOfAuftra(uid,taskId)==null) return false;
        }
        return true;
    }

    private PageInfo<TaskVO> getTaskVOPageInfo(PageInfo<Task> po) {
        PageInfo<TaskVO> result = PageInfoUtil.convert(po, TaskVO.class);
        return result;
    }

    private List<Task> recommendedSorting(String recommendation,List<Task> tasks){
        String[] re=recommendation.split(",");
        HashMap<Integer, Integer> hashMap=new HashMap<>();
        for (int i = 0; i < re.length; i++) {
            hashMap.put(Integer.parseInt(re[i]),i);
        }
        Task[] res=new Task[re.length];
        for (Task task : tasks) {
            res[hashMap.get(task.getId())] = task;
        }
        List<Task> ans=new ArrayList<>();
        for (Task task : res) {
            if (task != null)
                ans.add(task);
        }
        return ans;
    }

}
