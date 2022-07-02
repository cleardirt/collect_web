package com.example.demo;

import com.example.demo.mapperservice.report.BugScreenshotMapper;
import com.example.demo.mapperservice.report.RatingReportListMapper;
import com.example.demo.mapperservice.report.ReportMapper;
import com.example.demo.mapperservice.task.TaskMapper;
import com.example.demo.mapperservice.task.TaskProgressMapper;
import com.example.demo.mapperservice.user.*;
import com.example.demo.po.report.BugScreenshot;
import com.example.demo.po.report.RatingReportList;
import com.example.demo.po.report.Report;
import com.example.demo.po.task.Task;
import com.example.demo.po.task.TaskProgress;
import com.example.demo.po.user.User;
import com.example.demo.serviceiml.report.ReportServiceImpl;
import com.example.demo.util.Constant;
import com.example.demo.util.DateHelper;
import com.example.demo.util.ReportTree;
import com.example.demo.vo.ResultVO;
import com.example.demo.vo.report.BugScreenshotVO;
import com.example.demo.vo.report.ReportDetailVO;
import com.example.demo.vo.report.ReportVO;
import com.example.demo.vo.report.SimilarReportVO;
import com.example.demo.vo.task.TaskProgressVO;
import com.github.pagehelper.PageInfo;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MapperScan(basePackages = {"com.example.demo.mapperservice"})
public class ReportServiceTest {
    @Autowired
    private ReportServiceImpl reportServiceImpl;
    @Resource
    BugScreenshotMapper bugScreenshotMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    TaskMapper taskMapper;
    @Resource
    TaskProgressMapper taskProgressMapper;
    @Resource
    UserRatingMapper userRatingMapper;
    @Resource
    UserRecommendationMapper userTaskRecommendationMapper;
    @Resource
    UserSelectMapper userSelectMapper;
    @Resource
    UserDeviceMapper userDeviceMapper;
    @Resource
    RatingForScanMapper ratingForScanMapper;
    @Resource
    ReportMapper reportMapper;
    @Resource
    RatingReportListMapper ratingReportListMapper;

    @Autowired
    ReportTree reportTree;
    @Test
    public void getReport(){
        Integer reportId = createOneReport(2,2,600,601,602).getData().getGeneralInfo().getId();
        ReportDetailVO reportVO1 = reportServiceImpl.getReport(reportId);
        assert (reportVO1!=null);
        reportServiceImpl.deleteReport(reportId);
        taskProgressMapper.deleteByUidAndTaskId(2,2);
    }

    @Test
    public void getCommandList(){
        Integer reportId = createOneReport(2,2,600,601,602).getData().getGeneralInfo().getId();
        List<Report> reportIds=reportMapper.selectEvaluableReport(2,new Date());
        List<Integer> evaluated=userRatingMapper.selectByUid(2);
        List<Integer> resReportId=new ArrayList<>();
        for(int i=0;i<reportIds.size()&&resReportId.size()<=2;i++){
            if (!evaluated.contains(reportIds.get(i).getId()))
                resReportId.add(reportIds.get(i).getId());
        }
        StringBuilder commandList=new StringBuilder();
        for(Integer id:resReportId){
            commandList.append(id).append("_");
        }
        String res= commandList.substring(0,commandList.length()-1);
        System.out.println(res);
        reportServiceImpl.deleteReport(reportId);
        taskProgressMapper.deleteByUidAndTaskId(2,2);
    }

    @Test
    public void rate(){
        Integer reportId = createOneReport(2,2,600,601,602).getData().getGeneralInfo().getId();
        createOneTester(200);
        reportServiceImpl.createRating(reportId,200,4,"不太行");
        System.out.println(reportServiceImpl.getMyRating(reportId,200));
        assert (reportServiceImpl.getMyRating(reportId,200)!=null);
        userRatingMapper.deleteByRUid(reportId,200);
        deleteOneTester(200);
        reportServiceImpl.deleteReport(reportId);
        taskProgressMapper.deleteByUidAndTaskId(2,2);
    }

    @Test
    public void getReportStatus(){
        Integer reportId = createOneReport(2,2,600,601,602).getData().getGeneralInfo().getId();
        TaskProgressVO taskProgressVO = reportServiceImpl.getReportStatus(2,2);
        assert (taskProgressVO.getIsFinished());
        reportServiceImpl.deleteReport(reportId);
        taskProgressMapper.deleteByUidAndTaskId(2,2);
    }

    @Test
    public void getMyReports(){
        createOneTester(100);
        createOneTester(200);
        createOneTask(2000);

        Integer id1 = createOneReport(2000,100,88000,89000,90000).getData().getGeneralInfo().getId();

        Integer id2 = createOneReport(2000,200,100000,101000,102000).getData().getGeneralInfo().getId();

        PageInfo<ReportVO> pageInfo = reportServiceImpl.getMyReports(1, Constant.REPORT_PAGE_SIZE,100,2000);
        assert (pageInfo.getList().size()!=0);
        reportServiceImpl.deleteReport(id1);
        reportServiceImpl.deleteReport(id2);

        taskProgressMapper.deleteByUidAndTaskId(100,2000);
        taskProgressMapper.deleteByUidAndTaskId(200,2000);

        deleteOneTask(2000);
        deleteOneTester(100);
        deleteOneTester(200);
    }

    @Test
    public void getReportList(){
        createOneTester(100);
        createOneTester(200);
        createOneTask(2000);

        Integer id1 = createOneReport(2000,100,88000,89000,90000).getData().getGeneralInfo().getId();

        Integer id2 = createOneReport(2000,200,100000,101000,102000).getData().getGeneralInfo().getId();

        PageInfo<ReportVO> pageInfo = reportServiceImpl.getReportList(1, Constant.REPORT_PAGE_SIZE,100,2000);
        assert (pageInfo.getList().size()!=0);
        reportServiceImpl.deleteReport(id1);
        reportServiceImpl.deleteReport(id2);

        taskProgressMapper.deleteByUidAndTaskId(100,2000);
        taskProgressMapper.deleteByUidAndTaskId(200,2000);

        deleteOneTask(2000);
        deleteOneTester(100);
        deleteOneTester(200);

    }

    public ResultVO<ReportDetailVO> createOneReport(Integer taskId, Integer workerId,
                                                    Integer picId1, Integer picId2,
                                                    Integer picId3){
        ReportVO reportVO = new ReportVO();
        reportVO.setCreateTime(new Date());
        reportVO.setTitle("hh");
        reportVO.setTaskId(taskId);
        reportVO.setWorkerId(workerId);
        reportVO.setDeviceInfo("Android");
        reportVO.setBugDescription("只有一个BUG!!!");
        reportVO.setDeviceInfo("report选了一个device");
        reportVO.setStepExplanation("这里有一个复现步骤");

        BugScreenshot bugScreenshot = new BugScreenshot();
        bugScreenshot.setId(picId1);
        bugScreenshot.setScreenshot("jj");
        bugScreenshot.setFingerprint(new byte[32]);
        bugScreenshotMapper.insert(bugScreenshot);
        bugScreenshot.setId(picId2);
        bugScreenshot.setScreenshot("kk");
        bugScreenshotMapper.insert(bugScreenshot);
        bugScreenshot.setId(picId3);
        bugScreenshot.setScreenshot("tt");
        bugScreenshotMapper.insert(bugScreenshot);

        BugScreenshotVO bugScreenshotVO = new BugScreenshotVO();
        bugScreenshotVO.setId(picId1);
        BugScreenshotVO bugScreenshotVO1 = new BugScreenshotVO();
        bugScreenshotVO1.setId(picId2);
        BugScreenshotVO bugScreenshotVO2 = new BugScreenshotVO();
        bugScreenshotVO2.setId(picId3);

        List<BugScreenshotVO> list = new ArrayList<>();
        list.add(bugScreenshotVO);
        list.add(bugScreenshotVO1);
        list.add(bugScreenshotVO2);

        ReportDetailVO reportDetailVO = new ReportDetailVO();
        reportDetailVO.setGeneralInfo(reportVO);
        reportDetailVO.setScreenshotList(list);
        TaskProgress tp = new TaskProgress();
        tp.setTaskId(taskId);
        tp.setWorkerId(workerId);
        tp.setIsFinished(false);
        taskProgressMapper.insert(tp);
        ResultVO<ReportDetailVO> rdv = reportServiceImpl.createReport(reportDetailVO);

        return rdv;
    }

    public ResultVO<ReportDetailVO> createOneReport(Integer taskId, Integer workerId,
                                                    Integer picId1){
        ReportVO reportVO = new ReportVO();
        reportVO.setCreateTime(new Date());
        reportVO.setTitle("hh");
        reportVO.setTaskId(taskId);
        reportVO.setWorkerId(workerId);
        reportVO.setBugDescription("这也是一个天大的Bug太可怕了" +
                "人怎么能够这样好好补偿奶茶你愤怒和付出预估一个环境比较好关于就不那么关于北京");
        reportVO.setDeviceInfo("ios");
        reportVO.setStepExplanation("再次模拟一个步骤复现呜哇五阿瓦我不会的代表性");
        TaskProgress tp = new TaskProgress();
        tp.setTaskId(taskId);
        tp.setWorkerId(workerId);
        tp.setIsFinished(false);
        taskProgressMapper.insert(tp);

        BugScreenshotVO bugScreenshotVO = new BugScreenshotVO();
        bugScreenshotVO.setId(picId1);

        List<BugScreenshotVO> list = new ArrayList<>();
        list.add(bugScreenshotVO);

        ReportDetailVO reportDetailVO = new ReportDetailVO();
        reportDetailVO.setGeneralInfo(reportVO);
        reportDetailVO.setScreenshotList(list);
        ResultVO<ReportDetailVO> rdv = reportServiceImpl.createReport(reportDetailVO);

        return rdv;
    }

    public ResultVO<ReportDetailVO> createAnotherReport(Integer taskId, Integer workerId,
                                                    Integer picId1){
        ReportVO reportVO = new ReportVO();
        reportVO.setCreateTime(new Date());
        reportVO.setTitle("噢噢这是第二个report测试similar用");
        reportVO.setTaskId(taskId);
        reportVO.setWorkerId(workerId);
        reportVO.setBugDescription("发现一个大大的Bug");
        reportVO.setDeviceInfo("ios");
        reportVO.setStepExplanation("再次模拟一个步骤复现");


        BugScreenshotVO bugScreenshotVO = new BugScreenshotVO();
        bugScreenshotVO.setId(picId1);

        List<BugScreenshotVO> list = new ArrayList<>();
        list.add(bugScreenshotVO);

        ReportDetailVO reportDetailVO = new ReportDetailVO();
        reportDetailVO.setGeneralInfo(reportVO);
        reportDetailVO.setScreenshotList(list);
        ResultVO<ReportDetailVO> rdv = reportServiceImpl.createReport(reportDetailVO);
        return rdv;
    }

    public ResultVO<ReportDetailVO> createThirdReport(Integer taskId, Integer workerId,
                                                        Integer picId1){
        ReportVO reportVO = new ReportVO();
        reportVO.setCreateTime(new Date());
        reportVO.setTitle("噢噢这是第二个report测试多用用哈哈哈如果你需要的话这样这样");
        reportVO.setTaskId(taskId);
        reportVO.setWorkerId(workerId);
        reportVO.setBugDescription("这也是一个天大的Bug太可怕了人怎么能够这样好好补偿奶茶你愤怒和付出");
        reportVO.setDeviceInfo("ios");
        reportVO.setStepExplanation("再次模拟一个步骤复现");


        BugScreenshotVO bugScreenshotVO = new BugScreenshotVO();
        bugScreenshotVO.setId(picId1);

        List<BugScreenshotVO> list = new ArrayList<>();
        list.add(bugScreenshotVO);

        ReportDetailVO reportDetailVO = new ReportDetailVO();
        reportDetailVO.setGeneralInfo(reportVO);
        reportDetailVO.setScreenshotList(list);
        ResultVO<ReportDetailVO> rdv = reportServiceImpl.createReport(reportDetailVO);
        return rdv;
    }

    public ResultVO<ReportVO> forkReport(Integer fatherId, Integer taskId,
                                               Integer workerId, Integer picId1){
        ReportVO reportVO = new ReportVO();
        reportVO.setCreateTime(new Date());
        reportVO.setTitle("噢噢这是第二个report测试多用用哈哈哈如果你需要的话这样这样" +
                "给关于不会不会结婚薄荷健康不会根据该");
        reportVO.setTaskId(taskId);
        reportVO.setWorkerId(workerId);
        reportVO.setBugDescription("这也是一个天大的Bug太可怕了" +
                "人怎么能够这样好好补偿奶茶你愤怒和付出预估一个环境比较好关于就不那么关于北京");
        reportVO.setDeviceInfo("ios");
        reportVO.setStepExplanation("这是一个fork再次模拟一个步骤复现官方粤方言和放入忽而拿分拿分拿分");


        BugScreenshotVO bugScreenshotVO = new BugScreenshotVO();
        bugScreenshotVO.setId(picId1);

        List<BugScreenshotVO> list = new ArrayList<>();
        list.add(bugScreenshotVO);

        ReportDetailVO reportDetailVO = new ReportDetailVO();
        reportDetailVO.setGeneralInfo(reportVO);
        reportDetailVO.setScreenshotList(list);
        ResultVO<ReportVO> rdv = reportServiceImpl.forkReportByFatherRid(fatherId,
                reportDetailVO);
        return rdv;
    }

    public void createOneTester(Integer workerId){
        User user = new User();
        user.setId(workerId);
        user.setCreateTime(new Date());
        user.setPhone("jj");
        user.setUsername("bbb");
        user.setUserpass("jhjh");
        user.setUserRole("TESTER");
        user.setProfessionalAbility(BigDecimal.ZERO);
        user.setActivity(0);
        user.setRatingAbility(BigDecimal.ZERO);
        user.setCredit(BigDecimal.TEN);
        userMapper.insert(user);
    }

    public void deleteOneTester(Integer workerId){
        userTaskRecommendationMapper.deleteByUid(workerId);
        userSelectMapper.deleteByUid(workerId);
        userDeviceMapper.deleteByUid(workerId);
        ratingForScanMapper.deleteByUid(workerId);
        userMapper.deleteByPrimaryKey(workerId);
    }

    public void createOneTask(Integer tid){
        Task task = new Task();
        task.setId(tid);
        task.setTitle("jjjj");
        task.setTestStartTime(new Date());
        task.setTestEndTime(DateHelper.getTomorrow(new Date()));
        task.setIsOpen(true);
        task.setWorkerNum(100);
        task.setTestType("function");
        task.setCreateTime(new Date());
        task.setAuftraggeberId(3);
        task.setDifficulty(1);
        taskMapper.insert(task);
    }

    public void deleteOneTask(Integer tid){
        taskMapper.deleteByPrimaryKey(tid);
    }

    public BugScreenshotVO uploadLocalPic(){
        BugScreenshotVO bugScreenshotVO = null;
        File file = new File(
                "src/test/java/com/example/demo/GreatCormorants_ZH-CN6811149253_1920x1080.jpg");
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        MultipartFile mulFile = null;
        try {
            mulFile = new MockMultipartFile(
                    "butterflies.jpg", //文件名
                    "butterflies.jpg", //originalName 相当于上传文件在客户机上的文件名
                    ContentType.APPLICATION_OCTET_STREAM.toString(), //文件类型
                    fileInputStream //文件流
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bugScreenshotVO = reportServiceImpl.uploadShot(mulFile).getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bugScreenshotVO;
    }



    @Test
    public void getSimilarReports(){
        BugScreenshotVO bugScreenshotVO1 = uploadLocalPic();
        ReportDetailVO reportDetailVO1 =
                createOneReport(1,2,bugScreenshotVO1.getId()).getData();

        BugScreenshotVO bugScreenshotVO = uploadLocalPic();
        ReportDetailVO reportDetailVO2 =
                createAnotherReport(1,2,bugScreenshotVO.getId()).getData();

        BugScreenshotVO bugScreenshotVO3 = uploadLocalPic();
        ReportDetailVO reportDetailVO3 =
                createThirdReport(1,2,bugScreenshotVO3.getId()).getData();

        BugScreenshotVO bugScreenshotVO2 = uploadLocalPic();
        List<BugScreenshotVO> sslist = new ArrayList<>();
        sslist.add(bugScreenshotVO2);
        ReportVO reportVO = new ReportVO();
        reportVO.setStepExplanation("再次模拟一个步骤复现");
        reportVO.setBugDescription("Bug");
        reportVO.setTaskId(1);
        reportVO.setWorkerId(2);
        reportVO.setTitle("测试相似度用报告");
        reportVO.setDeviceInfo("ios");
        ReportDetailVO reportDetailVO = new ReportDetailVO(reportVO,sslist);
        ResultVO<List<SimilarReportVO>> result =
                reportServiceImpl.getSimilarReports(reportDetailVO);
        String msg = result.getMsg();
        System.out.println(msg);
        System.out.println(result.getData().size());
        List<SimilarReportVO> similarReportVOList = result.getData();
        for (SimilarReportVO reportVO1:similarReportVOList){
            System.out.println("以下为相似报告:");
            System.out.println(reportVO1.getPercentage());
        }

        reportServiceImpl.deleteReport(reportDetailVO1.getGeneralInfo().getId());
        reportServiceImpl.deleteReport(reportDetailVO2.getGeneralInfo().getId());
        reportServiceImpl.deleteReport(reportDetailVO3.getGeneralInfo().getId());
        reportServiceImpl.deletePic(bugScreenshotVO2.getId());

        taskProgressMapper.deleteByUidAndTaskId(2,1);
    }

    public ResultVO<ReportDetailVO> createFourthReport(
            Integer taskId, Integer workerId, Integer picId1){
        ReportVO reportVO = new ReportVO();
        reportVO.setCreateTime(new Date());
        reportVO.setTitle("测试相似度用报告");
        reportVO.setTaskId(taskId);
        reportVO.setWorkerId(workerId);
        reportVO.setBugDescription("Bug");
        reportVO.setDeviceInfo("ios");
        reportVO.setStepExplanation("再次模拟一个步骤复现");


        BugScreenshotVO bugScreenshotVO = new BugScreenshotVO();
        bugScreenshotVO.setId(picId1);

        List<BugScreenshotVO> list = new ArrayList<>();
        list.add(bugScreenshotVO);

        ReportDetailVO reportDetailVO = new ReportDetailVO();
        reportDetailVO.setGeneralInfo(reportVO);
        reportDetailVO.setScreenshotList(list);
        ResultVO<ReportDetailVO> rdv = reportServiceImpl.createReport(reportDetailVO);
        TaskProgress tp = new TaskProgress();
        tp.setTaskId(taskId);
        tp.setWorkerId(workerId);
        tp.setIsFinished(true);
        taskProgressMapper.insert(tp);
        return rdv;
    }

    @Test
    public void getSimilarByReportId(){
        BugScreenshotVO bugScreenshotVO1 = uploadLocalPic();
        ReportDetailVO reportDetailVO1 =
                createOneReport(1,2,bugScreenshotVO1.getId()).getData();

        BugScreenshotVO bugScreenshotVO = uploadLocalPic();
        ReportDetailVO reportDetailVO2 =
                createAnotherReport(1,2,bugScreenshotVO.getId()).getData();

        BugScreenshotVO bugScreenshotVO3 = uploadLocalPic();
        ReportDetailVO reportDetailVO3 =
                createThirdReport(1,2,bugScreenshotVO3.getId()).getData();

        BugScreenshotVO bugScreenshotVO2 = uploadLocalPic();
//        ReportDetailVO reportDetailVO = createFourthReport(1,2,
//                bugScreenshotVO2.getId()).getData();
        ReportVO reportVO = forkReport(reportDetailVO3.getGeneralInfo().getId(),
                1,2,bugScreenshotVO2.getId()).getData();

        BugScreenshotVO bugScreenshotVO4 = uploadLocalPic();
        ReportVO reportVO4 = forkReport(reportVO.getId(),
                1,2,bugScreenshotVO4.getId()).getData();

        System.out.println(reportVO.getId());

        ResultVO<List<SimilarReportVO>> result =
                reportServiceImpl.getSimilarReports(
                        reportVO.getId());
        String msg = result.getMsg();
        System.out.println(msg);
        System.out.println(result.getData().size());
        List<SimilarReportVO> similarReportVOList = result.getData();
        for (SimilarReportVO reportVO1:similarReportVOList){
            System.out.println("以下为相似报告:");
            System.out.println(reportVO1.getPercentage());
            System.out.println(reportVO1.getReportId());
        }
        reportServiceImpl.deleteReport(reportDetailVO1.getGeneralInfo().getId());
        reportServiceImpl.deleteReport(reportDetailVO2.getGeneralInfo().getId());
        reportServiceImpl.deleteReport(reportDetailVO3.getGeneralInfo().getId());
        reportServiceImpl.deleteReport(reportVO.getId());
        reportServiceImpl.deleteReport(reportVO4.getId());

        taskProgressMapper.deleteByUidAndTaskId(2,1);
    }

    @Test
    public void getFatherNode(){
        BugScreenshotVO bugScreenshotVO1 = uploadLocalPic();
        ReportDetailVO reportDetailVO1 =
                createOneReport(1,2,bugScreenshotVO1.getId()).getData();

        BugScreenshotVO bugScreenshotVO2 = uploadLocalPic();
        ReportVO reportVO2 = forkReport(reportDetailVO1.getGeneralInfo().getId(),
                1,2,bugScreenshotVO2.getId()).getData();

        assert (reportServiceImpl.getFatherNode(reportVO2.getId()).getReportId()
                .equals(reportDetailVO1.getGeneralInfo().getId()));

        reportServiceImpl.deleteReport(reportVO2.getId());
        reportServiceImpl.deleteReport(reportDetailVO1.getGeneralInfo().getId());
        taskProgressMapper.deleteByUidAndTaskId(2,1);
    }

    @Test
    public void uploadShot() throws IOException {
        ResultVO<BugScreenshotVO> resultVO =
                reportServiceImpl.uploadShot(new MockMultipartFile(
                "butterflies.jpg", //文件名
                "butterflies.jpg", //originalName 相当于上传文件在客户机上的文件名
                ContentType.APPLICATION_OCTET_STREAM.toString(), //文件类型
                new FileInputStream("src/test/java/com/example/demo/GreatCormorants_ZH-CN6811149253_1920x1080.jpg") //文件流
        ));
        BugScreenshotVO bugScreenshotVO = resultVO.getData();
        assert (bugScreenshotVO!=null);
        bugScreenshotMapper.deleteByPrimaryKey(bugScreenshotVO.getId());
    }
}
