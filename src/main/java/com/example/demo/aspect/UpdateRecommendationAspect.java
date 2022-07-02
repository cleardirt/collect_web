package com.example.demo.aspect;

import com.example.demo.enums.UserRole;
import com.example.demo.mapperservice.report.DeviceInfoMapper;
import com.example.demo.mapperservice.report.RatingReportListMapper;
import com.example.demo.mapperservice.user.UserDeviceMapper;
import com.example.demo.mapperservice.user.UserRecommendationMapper;
import com.example.demo.mapperservice.user.UserSelectMapper;
import com.example.demo.mapperservice.task.TaskMapper;
import com.example.demo.mapperservice.user.UserMapper;
import com.example.demo.po.report.RatingReportList;
import com.example.demo.vo.task.RuleVO;
import com.example.demo.vo.task.TaskVO;
import com.example.demo.vo.user.UserRecommendationVO;
import com.example.demo.po.report.DeviceInfo;
import com.example.demo.po.task.Task;
import com.example.demo.po.user.User;
import com.example.demo.po.user.UserDevice;
import com.example.demo.po.user.UserRecommendation;
import com.example.demo.po.user.UserSelect;
import com.example.demo.util.Constant;
import com.example.demo.vo.ResultVO;
import com.example.demo.vo.report.ReportDetailVO;
import com.example.demo.vo.task.TaskProgressVO;
import com.example.demo.vo.user.UserVO;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Aspect
@Component
public class UpdateRecommendationAspect {

    @Resource
    TaskMapper taskMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    UserSelectMapper userSelectMapper;
    @Resource
    UserRecommendationMapper userRecommendationMapper;
    @Resource
    UserDeviceMapper userDeviceMapper;
    @Resource
    DeviceInfoMapper deviceInfoMapper;
    @Resource
    RatingReportListMapper ratingReportListMapper;

//    private boolean professionalAbilityRanking=true;
//
//    private boolean sortByTaskCategory=true;
//
//    private boolean testEquipmentSorting=true;

    private RuleVO ruleVO;

    //定义一个公用方法
    @Pointcut("execution(public * com.example.demo.controller.user.UserController.register(..))")
    public void log(){}
    @Pointcut("execution(public * com.example.demo.controller.task.TaskController.selectTask(..)))")
    public void selectTask(){}
    @Pointcut("execution(public * com.example.demo.controller.report.ReportController.createReport(..)))")
    public void creatReport(){}
    @Pointcut("execution(public * com.example.demo.controller.task.TaskController.creatTask(..)))")
    public void createTask(){}

    @After("log()")
    public void doAfter(){
    }

    @AfterReturning(returning="obj", pointcut = "log()")
    public void doAfterReturning(ResultVO<UserVO> obj){
        UserVO userVO=obj.getData();
        if (userVO!=null&&userVO.getUserRole().equals(UserRole.TESTER.toString())) {
            int id = userVO.getId();
            ArrayList<Integer> defaultQueue = (ArrayList<Integer>) taskMapper.selectIdInProgressAll(new Date());
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < defaultQueue.size(); i++) {
                stringBuilder.append(defaultQueue.get(i));
                if (i != defaultQueue.size() - 1)
                    stringBuilder.append(",");
            }
            String res = stringBuilder.toString();
            UserRecommendationVO userRecommendationVO = new UserRecommendationVO();
            userRecommendationVO.setRecommendList(res);
            userRecommendationVO.setUid(id);
            userRecommendationMapper.insert(new UserRecommendation(userRecommendationVO));


            UserSelect userSelect=new UserSelect();
            userSelect.setUserId(id);
            userSelect.setDeviceConnection(0);
            userSelect.setUsecaseExecution(0);
            userSelect.setPerformanceTest(0);
            userSelect.setBugExplore(0);
            userSelect.setFunctionTest(0);
            userSelectMapper.insert(userSelect);

            UserDevice userDevice=new UserDevice();
            userDevice.setUserId(id);
            userDevice.setAndroid(0);
            userDevice.setHarmonyos(0);
            userDevice.setIos(0);
            userDevice.setLinux(0);
            userDevice.setMacos(0);
            userDevice.setWindows(0);
            userDeviceMapper.insert(userDevice);

            RatingReportList ratingReportList=new RatingReportList();
            ratingReportList.setReportToRate("");
            ratingReportList.setRatedReport("");
            ratingReportList.setRatingWillingness(BigDecimal.valueOf(5.0));
            ratingReportList.setUid(id);
            ratingReportListMapper.insert(ratingReportList);
        }
    }

    @AfterReturning(returning = "obj",pointcut = "createTask()")
    public void updateAllAfterNewTask(ResultVO<TaskVO> obj){
        List<UserRecommendation> userRecommendations=userRecommendationMapper.selectAll();
        for(UserRecommendation userRecommendation:userRecommendations){
            UserVO userVO=new UserVO(userMapper.selectByPrimaryKey(userRecommendation.getUid()));
            userRecommendation.setRecommendList(updateRecommendation(userVO));
            userRecommendationMapper.updateByPrimaryKey(userRecommendation);
        }
    }

    @AfterReturning(returning = "obj",pointcut = "selectTask()")
    public void updateActivityAfterSelect(ResultVO<TaskProgressVO> obj){
        TaskProgressVO taskProgressVO=obj.getData();
        User user=userMapper.selectByPrimaryKey(taskProgressVO.getWorkerId());
        UserVO userVO=new UserVO(user);
        UserRecommendation userRecommendation=userRecommendationMapper.selectByUserId(taskProgressVO.getWorkerId());
        userRecommendation.setRecommendList(updateRecommendation(userVO));
        userRecommendationMapper.updateByPrimaryKey(userRecommendation);
    }

    @AfterReturning(returning = "obj",pointcut = "creatReport()")
    public void updateActivityAfterFinish(ResultVO<ReportDetailVO> obj){
        if (obj.getCode().equals(Constant.REQUEST_SUCCESS)) {
            String update = updateRecommendation(new UserVO(userMapper.selectByPrimaryKey(obj.getData().getGeneralInfo().getWorkerId())));
            UserRecommendation userRecommendation=userRecommendationMapper.selectByUserId(obj.getData().getGeneralInfo().getWorkerId());
            userRecommendation.setRecommendList(update);
            userRecommendationMapper.updateByPrimaryKey(userRecommendation);
        }
    }

    public void setRule(RuleVO ruleVO){
        this.ruleVO=ruleVO;
//        this.professionalAbilityRanking= ruleVO.isProfessionalAbilityRanking();
//        this.sortByTaskCategory= ruleVO.isTestEquipmentSorting();
//        this.testEquipmentSorting= ruleVO.isTestEquipmentSorting();
    }

    public RuleVO getRule(){
//        RuleVO ruleVO=new RuleVO(professionalAbilityRanking,sortByTaskCategory,testEquipmentSorting);
        return this.ruleVO;
    }

    public String updateRecommendation(UserVO userVO){
        ArrayList<Task> taskArrayList= (ArrayList<Task>) taskMapper.selectInProgressAll(new Date());
        HashMap<String, Integer> categoryMap = new HashMap<>();
        HashMap<String, Integer> deviceMap = new HashMap<>();

        if (ruleVO.isSortByTaskCategory()) {
            UserSelect userSelect = userSelectMapper.selectByUserId(userVO.getId());

            TreeMap<String, Integer> treeMap = new TreeMap<>();
            treeMap.put("BUG探索", userSelect.getBugExplore());
            treeMap.put("硬件连通", userSelect.getDeviceConnection());
            treeMap.put("功能测试", userSelect.getFunctionTest());
            treeMap.put("性能测试", userSelect.getPerformanceTest());
            treeMap.put("用例执行", userSelect.getUsecaseExecution());

            List<Map.Entry<String, Integer>> treeMapList = new ArrayList<>(treeMap.entrySet());
            treeMapList.sort(Comparator.comparingInt(Map.Entry::getValue));//升序


            int start = 0;//最低权重
            int pre = treeMapList.get(0).getValue();
            for (Map.Entry<String, Integer> stringIntegerEntry : treeMapList) {
                if (stringIntegerEntry.getValue() != pre)
                    start++;
                pre = stringIntegerEntry.getValue();
                stringIntegerEntry.setValue(start * 3);//变成最小公倍数12
//            System.out.println(stringIntegerEntry.getKey()+":"+stringIntegerEntry.getValue());

                categoryMap.put(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
            }//hashMap中存放不同的类别对应的权重（0-12）}
        }

            if (ruleVO.isTestEquipmentSorting()) {
                UserDevice userDevice = userDeviceMapper.selectByUserId(userVO.getId());
                TreeMap<String, Integer> deviceTreeMap = new TreeMap<>();
                deviceTreeMap.put("windows", userDevice.getWindows());
                deviceTreeMap.put("Android", userDevice.getAndroid());
                deviceTreeMap.put("Harmonyos", userDevice.getHarmonyos());
                deviceTreeMap.put("Macos", userDevice.getMacos());
                deviceTreeMap.put("Ios", userDevice.getIos());
                deviceTreeMap.put("Linux", userDevice.getLinux());
                List<Map.Entry<String, Integer>> deviceTreeMapList = new ArrayList<>(deviceTreeMap.entrySet());
                deviceTreeMapList.sort(Comparator.comparingInt(Map.Entry::getValue));//升序
                //按测试设备加权重
                int d_start = 0;//最低权重
                int d_pre = deviceTreeMapList.get(0).getValue();
                for (Map.Entry<String, Integer> stringIntegerEntry : deviceTreeMapList) {
                    if (stringIntegerEntry.getValue() != d_pre)
                        d_start++;
                    d_pre = stringIntegerEntry.getValue();
                    stringIntegerEntry.setValue(d_start * 2);//变成最小公倍数12
                    deviceMap.put(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
                }
            }
        //专业能力
        TreeMap<Integer, Integer> taskMap=new TreeMap<>();
        List<Map.Entry<Integer, Integer>> taskList;
        for (Task task :
                taskArrayList) {
            int value=0;
            if (ruleVO.isSortByTaskCategory()) //任务类别
                value+=categoryMap.get(task.getTestType());
            if (ruleVO.isTestEquipmentSorting()) { //测试设备
                List<DeviceInfo> deviceInfos = deviceInfoMapper.selectByTaskId(task.getId());
                int max = 0;
                for (DeviceInfo deviceInfo : deviceInfos) {
                    max = Math.max(max, deviceMap.getOrDefault(deviceInfo.getExplanation(), 0));
                }
                value += max;
            }

            if (ruleVO.isProfessionalAbilityRanking()) //专业能力
            {
//            System.out.println(task.getId()+"专业能力的value："+Math.abs(userVO.getProfessionalAbility() - task.getDifficulty())+","+(3 - Math.abs(userVO.getProfessionalAbility() - task.getDifficulty())) * 4);
                value += (3 - Math.abs(userVO.getProfessionalAbility().doubleValue() - task.getDifficulty())) * 4;
            }//变成最小公倍数12
//            System.out.println(task.getId()+":"+value);
            taskMap.put(task.getId(),value);
        }//按专业能力和难度计算权重（0-12）


        taskList=new ArrayList<>(taskMap.entrySet());
        taskList.sort((o1, o2) -> (o2.getValue() - o1.getValue()));//倒序存放推荐队列
        StringBuilder stringBuilder=new StringBuilder();
        for (int i = 0; i < taskList.size(); i++) {
//            System.out.println(taskList.get(i).getKey()+":"+taskList.get(i).getValue());
            stringBuilder.append(taskList.get(i).getKey());
            if (i!=taskList.size()-1)
                stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }
}
