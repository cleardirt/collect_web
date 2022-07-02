package com.example.demo.serviceiml.report;

import com.example.demo.mapperservice.report.RatingReportListMapper;
import com.example.demo.mapperservice.user.RatingForScanMapper;
import com.example.demo.mapperservice.user.UserDeviceMapper;
import com.example.demo.mapperservice.user.UserRatingMapper;
import com.example.demo.mapperservice.report.BugScreenshotMapper;
import com.example.demo.mapperservice.report.ReportMapper;
import com.example.demo.mapperservice.report.TextHashMapper;
import com.example.demo.mapperservice.task.TaskMapper;
import com.example.demo.mapperservice.task.TaskProgressMapper;
import com.example.demo.mapperservice.user.UserMapper;
import com.example.demo.po.Notice;
import com.example.demo.po.report.BugScreenshot;
import com.example.demo.po.report.RatingReportList;
import com.example.demo.po.report.Report;
import com.example.demo.po.report.TextHash;
import com.example.demo.po.task.Task;
import com.example.demo.po.user.RatingForScan;
import com.example.demo.po.user.User;
import com.example.demo.po.user.UserDevice;
import com.example.demo.po.user.UserRating;
import com.example.demo.service.report.ReportService;
import com.example.demo.util.*;
import com.example.demo.util.ComparisonIml.FingerPrint;
import com.example.demo.util.ComparisonIml.TextComparison;
import com.example.demo.util.Comparson.ImageComparisonStrategies;
import com.example.demo.util.Comparson.TextComparisonStrategy;
import com.example.demo.vo.ResultVO;
import com.example.demo.vo.report.*;
import com.example.demo.vo.task.TaskProgressVO;
import com.example.demo.vo.user.UserRatingVO;
import com.example.demo.websocket.WebSocket;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    @Resource
    ReportMapper reportMapper;
    @Resource
    BugScreenshotMapper screenshotMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    TaskProgressMapper progressMapper;
    @Resource
    TaskMapper taskMapper;
    @Resource
    UserRatingMapper userRatingMapper;
    @Resource
    TextHashMapper textHashMapper;
    @Resource
    UserDeviceMapper userDeviceMapper;
    @Resource
    ReportTree reportTree;
    @Resource
    RatingForScanMapper ratingForScanMapper;
    @Resource
    RatingReportListMapper ratingReportListMapper;

    static ImageComparisonStrategies imageComparisonStrategies = new FingerPrint();

    TextComparisonStrategy textComparison = new TextComparison();

    @Override
    public ResultVO<ReportDetailVO> createReport(ReportDetailVO reportDetailVO) {
        // set report create_time
        reportDetailVO.getGeneralInfo().setCreateTime(new Date());
        Report report = new Report(reportDetailVO.getGeneralInfo());
        report.setWorkerName(userMapper.selectByPrimaryKey(
                report.getWorkerId()).getUsername());
        report.setWeightedRaterNumber(BigDecimal.valueOf(0));
        report.setScore(BigDecimal.valueOf(0));
        report.setRealRaterNumber(0);

        Task task = taskMapper.selectByPrimaryKey(reportDetailVO.getGeneralInfo().getTaskId());
        if (task.getTestEndTime().before(new Date()))
            return new ResultVO<>(Constant.REQUEST_FAIL, "任务已结束，无法提交报告");

        if ((report.getId() != null)
                && reportMapper.selectByPrimaryKey(report.getId()) != null) {
        } else {
            reportMapper.insert(report);
        }

        ReportDetailVO rdv = new ReportDetailVO();

        int task_id = report.getTaskId();
        int uid = report.getWorkerId();

        // update on report_id,bug_id...after inserting...
        int real_report_id = report.getId();

        TextHash textHash = new TextHash();
        textHash.setReportId(real_report_id);
        textHash.setTaskId(task_id);

        TextComparisonStrategy textComparison = new TextComparison(report.getTitle());
        String titleHash = BigIntegerStringConvertion.bigInteger2String(
                textComparison.getStrSimHash());
        textHash.setTitleHash(titleHash);

        textComparison = new TextComparison(report.getBugDescription());
        String bugHash = BigIntegerStringConvertion.bigInteger2String(
                textComparison.getStrSimHash());
        textHash.setDescriptionHash(bugHash);

        textComparison = new TextComparison(report.getStepExplanation());
        String stepHash = BigIntegerStringConvertion.bigInteger2String(
                textComparison.getStrSimHash()
        );
        textHash.setStepHash(stepHash);

        textHashMapper.insert(textHash);

        rdv.setGeneralInfo(new ReportVO(report));

        List<BugScreenshotVO> shotList = reportDetailVO.getScreenshotList();

        for (BugScreenshotVO shot : shotList) {
            BugScreenshot bugScreenshot = screenshotMapper.selectByPrimaryKey(
                    shot.getId()
            );
            bugScreenshot.setReportId(real_report_id);
            screenshotMapper.updateByPrimaryKey(bugScreenshot);
        }

        rdv.setScreenshotList(shotList);


        boolean isFinished = progressMapper.selectByUidAndTaskId(uid, task_id).getIsFinished();
        // update task_progress on is_finished when tester creates a report
        progressMapper.updateStatusByIds(task_id, uid);

        // update the professionalAbility of author

        int difficulty = task.getDifficulty();
        User user = userMapper.selectByPrimaryKey(uid);
        BigDecimal competence = user.getProfessionalAbility();
        // competence += task_difficulty/denominator
        int denominator = 2;
        int bonus = 5;
        competence = competence.add(BigDecimal.valueOf(difficulty / denominator));
        user.setProfessionalAbility(competence);
        user.setActivity(user.getActivity() + bonus);
        if (!isFinished) //第一次完成才改信誉值
        {
            BigDecimal credit = user.getCredit();
            if (credit.doubleValue() >= 9.5)
                credit = BigDecimal.TEN;
            else
                credit = credit.add(new BigDecimal("0.5"));
            user.setCredit(credit);
        }
        userMapper.updateByPrimaryKey(user);

        // update the devices of author
        UserDevice userDevice = userDeviceMapper.selectByUserId(uid);
        switch (reportDetailVO.getGeneralInfo().getDeviceInfo()) {
            case "Windows":
                userDevice.setWindows(1);
                break;
            case "Linux":
                userDevice.setLinux(1);
                break;
            case "MacOS":
                userDevice.setMacos(1);
                break;
            case "Harmony":
                userDevice.setHarmonyos(1);
                break;
            case "iOS":
                userDevice.setIos(1);
                break;
            default:
                break;
        }
        userDeviceMapper.updateByPrimaryKey(userDevice);


        reportTree.insertNode(rdv.getGeneralInfo().getTaskId(), rdv.getGeneralInfo().getId(), null);
        return new ResultVO<>(Constant.REQUEST_SUCCESS, "报告创建成功", rdv);
    }

    @Override
    public ResultVO<ReportVO> forkReportByFatherRid(Integer fatherRid, ReportDetailVO reportDetailVO) {
        List<BugScreenshot> fatherPicList = screenshotMapper.selectByReportId(fatherRid);
//        for (BugScreenshot pic:fatherPicList){
//            pic.setId(null);
//            screenshotMapper.insert(pic);
//        }
        List<Integer> fatherPicIds = new ArrayList<>();
        for (BugScreenshot bugScreenshot : fatherPicList) {
            fatherPicIds.add(bugScreenshot.getId());
        }
        List<BugScreenshotVO> sonPicVOList = reportDetailVO.getScreenshotList();
        for (BugScreenshotVO bugScreenshotVO : sonPicVOList) {
            int id = bugScreenshotVO.getId();
            if (fatherPicIds.contains(id)) {
                BugScreenshot pic = screenshotMapper.selectByPrimaryKey(id);
                pic.setId(null);
                screenshotMapper.insert(pic);
            }
        }
        reportDetailVO.getGeneralInfo().setFatherId(fatherRid);
        ReportDetailVO rdv = createReport(reportDetailVO).getData();

        reportTree.insertNode(reportDetailVO.getGeneralInfo().getTaskId(), rdv.getGeneralInfo().getId(), fatherRid);//插入报告协作树

        boolean isFinished = progressMapper.selectByUidAndTaskId(reportDetailVO.getGeneralInfo().getWorkerId(), reportDetailVO.getGeneralInfo().getTaskId()).getIsFinished();
        if (!isFinished) //第一次完成才改信誉值
        {
            User user = userMapper.selectByPrimaryKey(reportDetailVO.getGeneralInfo().getWorkerId());

            BigDecimal credit = user.getCredit();
            if (credit.doubleValue() >= 9.5)
                credit = BigDecimal.TEN;
            else
                credit = credit.add(new BigDecimal("0.5"));
            user.setCredit(credit);

            progressMapper.updateStatusByIds(reportDetailVO.getGeneralInfo().getTaskId(), reportDetailVO.getGeneralInfo().getWorkerId());
        }

        return new ResultVO<>(Constant.REQUEST_SUCCESS, "报告协作成功", rdv.getGeneralInfo());
    }

    @Override
    public ReportDetailVO getReport(Integer report_id) {
        Report report = reportMapper.selectByPrimaryKey(report_id);
        ReportVO reportVO = new ReportVO(report);

        // add shotList
        List<BugScreenshotVO> shotList = POVOListGeneratorUtil
                .convert(screenshotMapper.selectByReportId(report_id),
                        BugScreenshotVO.class);

        return new ReportDetailVO(reportVO, shotList);
    }

    public boolean judgeUserAuthority(Integer uid, Integer task_id) {
        return (!userMapper.selectByPrimaryKey(uid).getUserRole().equals("TESTER")
                || progressMapper.selectByUidAndTaskId(uid, task_id) != null) &&
                (!userMapper.selectByPrimaryKey(uid).getUserRole().equals("AUFTRA")
                        || taskMapper.selectTaskOfAuftra(uid, task_id) != null);
    }

    @Override
    public PageInfo<ReportVO> getReportList(Integer currPage, Integer pageSize,
                                            Integer uid, Integer task_id) {

        if (judgeUserAuthority(uid, task_id)) {
            if (currPage == null || currPage < 1) currPage = 1;
            PageHelper.startPage(currPage, pageSize);
            List<Report> reportList = reportMapper.selectByTaskId(task_id);
            PageInfo<Report> reportPageInfo = new PageInfo<>(reportList);
            return PageInfoUtil.convert(reportPageInfo, ReportVO.class);
        } else {
            return new PageInfo<>();
        }
    }

    @Override
    public PageInfo<ReportVO> getMyReports(Integer page, Integer reportPageSize,
                                           Integer uid, Integer task_id) {
        if (judgeUserAuthority(uid, task_id)) {
            if (page == null || page < 1) page = 1;
            PageHelper.startPage(page, reportPageSize);
            List<Report> myReportList = reportMapper.selectByTUid(task_id, uid);
            PageInfo<Report> reportPageInfo = new PageInfo<>(myReportList);
//            for (Report report:myReportList){
//                System.out.println(report.getCreateTime());
//            }
            return PageInfoUtil.convert(reportPageInfo, ReportVO.class);
        } else {
            return new PageInfo<>();
        }
    }

    @Override
    public ResultVO<List<SimilarReportVO>> getSimilarReports(ReportDetailVO reportDetailVO) {
        List<SimilarReportVO> similarReportVOList = new ArrayList<>();
        boolean createdSuccess = true;

        Double similarity;
        int taskId = reportDetailVO.getGeneralInfo().getTaskId();
        String selfDevice = reportDetailVO.getGeneralInfo().getDeviceInfo();
        List<Report> sameTaskReportList = reportMapper.selectByTidDev(taskId, selfDevice);
        if (sameTaskReportList.size() == 0) {
//            createReport(reportDetailVO);
            return new ResultVO<>(Constant.REQUEST_SUCCESS, "报告创建成功", new ArrayList<>());
        }

        for (Report report : sameTaskReportList) {
            similarity = calculateSimilarity(reportDetailVO,
                    0.15, 0.15, 0.3, 0.4,
                    report.getId());
            double failureBoundary = 0.9;
            if (similarity > failureBoundary) createdSuccess = false;

            double similarBoundary = 0.7;
            createSimilarReportList(similarReportVOList, similarity, report, similarBoundary);
        }
        List<SimilarReportVO> sorted = similarReportVOList.stream()
                .sorted(Comparator.comparing(SimilarReportVO::getSimilarityMeasure).reversed())
                .collect(Collectors.toList());

        if (!createdSuccess) {
//            deleteReport(selfReportId);
            return new ResultVO<>(Constant.REQUEST_FAIL, "此报告相似度过高，无法创建", sorted);
        }
//        deleteReport(selfReportId);
        return new ResultVO<>(Constant.REQUEST_SUCCESS, "此报告相似度合格，可以创建", sorted);
    }

    @Override
    public ReportTreeNodeVO getFatherNode(Integer reportId) {
        Report report = reportMapper.selectByPrimaryKey(reportId);
        if (report.getFatherId() == null)
            return reportTree.getNode(report.getTaskId(), reportId);
        else
            return reportTree.getNode(report.getTaskId(), report.getFatherId());
    }

    @Override
    public ResultVO<UserRatingVO> getMyRating(Integer reportId, Integer testerId) {
        UserRating rating = userRatingMapper.selectByRidUid(reportId, testerId);
        if (rating != null) {
            return new ResultVO<>(Constant.REQUEST_SUCCESS, "已经评分", new UserRatingVO(rating));
        }
        return new ResultVO<>(Constant.REQUEST_FAIL, "尚未评分", new UserRatingVO());
    }

    private void findRelatives(ArrayList<Integer> relatives, ReportTreeNodeVO ancientFather) {
        relatives.add(ancientFather.getReportId());
        if (ancientFather.getChild().size() != 0) {
            for (ReportTreeNodeVO child : ancientFather.getChild()) {
                findRelatives(relatives, child);
            }
        }
    }

    @Override
    public ResultVO<List<SimilarReportVO>> getSimilarReports(Integer reportId) {
        Report report = reportMapper.selectByPrimaryKey(reportId);
        List<BugScreenshot> list = screenshotMapper.selectByReportId(reportId);
        TextHash textHash = textHashMapper.selectByReportId(reportId);
        BigInteger titleHash = BigIntegerStringConvertion.string2BigInteger(
                textHash.getTitleHash()
        );
        BigInteger bugHash = BigIntegerStringConvertion.string2BigInteger(
                textHash.getDescriptionHash()
        );
        BigInteger stepHash = BigIntegerStringConvertion.string2BigInteger(
                textHash.getStepHash()
        );


        List<SimilarReportVO> similarReportVOList = new ArrayList<>();

        Double similarity;

        int taskId = report.getTaskId();
        String selfDevice = report.getDeviceInfo();
        List<Report> sameTaskDifferentReportList =
                reportMapper.selectDifferentByTidDev(reportId, taskId, selfDevice);
        if (sameTaskDifferentReportList.size() == 0) {
            return new ResultVO<>(Constant.REQUEST_FAIL,
                    "尚未有其他报告", new ArrayList<>());
        }

//        int ancientFather = report.getId();
//        Report temp = report;
//        while(temp.getFatherId()!=null){
//            ancientFather = temp.getFatherId();
//            temp = reportMapper.selectByPrimaryKey(temp.getFatherId());
//        }
//        ReportTreeNodeVO relatives = reportTree.getNode(taskId,ancientFather);

        ReportTreeNodeVO tempNode = reportTree.getNode(taskId, reportId);
        ArrayList<Integer> relatives = new ArrayList<>();
        int ancientNode = reportId;

        while (tempNode.getFatherId() != null) {
            tempNode = reportTree.getNode(taskId, tempNode.getFatherId());
            ancientNode = tempNode.getReportId();
        }

        ReportTreeNodeVO wholeTree = reportTree.getNode(taskId, ancientNode);
        findRelatives(relatives, wholeTree);

        for (Report differentReport : sameTaskDifferentReportList) {
            if (relatives.contains(differentReport.getId())) {
                continue;
            }

            similarity = calculateSimilarity(titleHash, bugHash, stepHash, list,
                    0.15, 0.15, 0.3, 0.4,
                    differentReport.getId());

            double similarBoundaryOutTree = 0.5;
            createSimilarReportList(similarReportVOList, similarity, differentReport, similarBoundaryOutTree);
        }
        List<SimilarReportVO> sorted = similarReportVOList.stream()
                .sorted(Comparator.comparing(SimilarReportVO::getSimilarityMeasure).reversed())
                .collect(Collectors.toList());
        if (sorted.size() == 0) {
            return new ResultVO<>(Constant.REQUEST_FAIL,
                    "无相似报告", sorted);
        }
        return new ResultVO<>(Constant.REQUEST_SUCCESS, "有相似报告", sorted);
    }

    @Override
    public List<ReportVO> getUserReports(Integer uid) {
        List<Report> reports = reportMapper.selectByTesterId(uid);
        List<ReportVO> res = new ArrayList<>();
        for (Report report : reports) {
            res.add(new ReportVO(report));
        }
        return res;
    }

    private void createSimilarReportList(List<SimilarReportVO> similarReportVOList, Double similarity, Report differentReport, double similarBoundaryOutTree) {
        if (similarity > similarBoundaryOutTree) {
            NumberFormat nf = NumberFormat.getPercentInstance();
            nf.setMaximumIntegerDigits(3);
            nf.setMinimumFractionDigits(2);
            String percent = nf.format(similarity);
            similarReportVOList.add(
                    new SimilarReportVO(differentReport, similarity, percent));
        }
    }

    public Double calculateSimilarity(BigInteger titleHash1,
                                      BigInteger bugHash1,
                                      BigInteger stepHash1,
                                      List<BugScreenshot> screenshotList1,
                                      Double picWeight, Double titleWeight,
                                      Double descriptionWeight, Double stepWeight,
                                      Integer comparedReportId) {
        TextHash comparedTextHash = textHashMapper.selectByReportId(comparedReportId);
        BigInteger titleHash2 = BigIntegerStringConvertion.string2BigInteger(comparedTextHash.getTitleHash());
        BigInteger bugHash2 = BigIntegerStringConvertion.string2BigInteger(comparedTextHash.getDescriptionHash());
        BigInteger stepHash2 = BigIntegerStringConvertion.string2BigInteger(comparedTextHash.getStepHash());
        Double titleSimilarity = textComparison.getSemblance(titleHash1, titleHash2);
        Double bugSimilarity = textComparison.getSemblance(bugHash1, bugHash2);
        Double stepSimilarity = textComparison.getSemblance(stepHash1, stepHash2);

        List<BugScreenshot> screenshotList2 = screenshotMapper.selectByReportId(comparedReportId);
        int selfPicNum = screenshotList1.size();
        Double[] maxSimilarities = new Double[selfPicNum];
        for (int i = 0; i < selfPicNum; i++) {
            maxSimilarities[i] = 0.0;
        }
        byte[] selfPicHash;
        byte[] comparedPicHash;
        Double picSimilarity = 0.0;
        for (int i = 0; i < selfPicNum; i++) {
            selfPicHash = screenshotList1.get(i).getFingerprint();
            for (BugScreenshot bugScreenshot : screenshotList2) {
                comparedPicHash = bugScreenshot.getFingerprint();
                maxSimilarities[i] = Math.max(imageComparisonStrategies.getResult(selfPicHash, comparedPicHash)
                        , maxSimilarities[i]);
            }
            picSimilarity += maxSimilarities[i];
        }
        picSimilarity /= selfPicNum;

        return picWeight * picSimilarity + titleWeight * titleSimilarity + stepWeight * stepSimilarity +
                descriptionWeight * bugSimilarity;
    }

    public Double calculateSimilarity(ReportDetailVO selfReport, Double picWeight,
                                      Double titleWeight, Double descriptionWeight, Double stepWeight,
                                      Integer comparedReportId) {
        BigInteger titleHash1 = textComparison.getHashOfStr(
                selfReport.getGeneralInfo().getTitle());
        BigInteger bugHash1 = textComparison.getHashOfStr(
                selfReport.getGeneralInfo().getBugDescription());
        BigInteger stepHash1 = textComparison.getHashOfStr(
                selfReport.getGeneralInfo().getStepExplanation());
        List<BugScreenshot> screenshotList1 = new ArrayList<>();
        List<BugScreenshotVO> screenshotVOS = selfReport.getScreenshotList();
        for (BugScreenshotVO bugScreenshotVO : screenshotVOS) {
            screenshotList1.add(screenshotMapper.selectByPrimaryKey(bugScreenshotVO.getId()));
        }

        TextHash comparedTextHash = textHashMapper.selectByReportId(comparedReportId);
        BigInteger titleHash2 = BigIntegerStringConvertion.string2BigInteger(comparedTextHash.getTitleHash());
        BigInteger bugHash2 = BigIntegerStringConvertion.string2BigInteger(comparedTextHash.getDescriptionHash());
        BigInteger stepHash2 = BigIntegerStringConvertion.string2BigInteger(comparedTextHash.getStepHash());
        Double titleSimilarity = textComparison.getSemblance(titleHash1, titleHash2);
        Double bugSimilarity = textComparison.getSemblance(bugHash1, bugHash2);
        Double stepSimilarity = textComparison.getSemblance(stepHash1, stepHash2);

        List<BugScreenshot> screenshotList2 = screenshotMapper.selectByReportId(comparedReportId);
        int selfPicNum = screenshotList1.size();
        Double[] maxSimilarities = new Double[selfPicNum];
        for (int i = 0; i < selfPicNum; i++) {
            maxSimilarities[i] = 0.0;
        }
        byte[] selfPicHash;
        byte[] comparedPicHash;
        Double picSimilarity = 0.0;
        for (int i = 0; i < selfPicNum; i++) {
            selfPicHash = screenshotList1.get(i).getFingerprint();
            for (BugScreenshot bugScreenshot : screenshotList2) {
                comparedPicHash = bugScreenshot.getFingerprint();
                maxSimilarities[i] = Math.max(imageComparisonStrategies.getResult(selfPicHash, comparedPicHash)
                        , maxSimilarities[i]);
            }
            picSimilarity += maxSimilarities[i];
        }
        picSimilarity /= selfPicNum;

        return picWeight * picSimilarity + titleWeight * titleSimilarity + stepWeight * stepSimilarity +
                descriptionWeight * bugSimilarity;
    }

    @Override
    public TaskProgressVO getReportStatus(Integer tester_id, Integer task_id) {
        return new TaskProgressVO(progressMapper.selectByUidAndTaskId(tester_id, task_id));
    }

    @Override
    public ResultVO<BugScreenshotVO> uploadShot(MultipartFile multipartFile) throws IOException {
        BugScreenshotVO screenshotVO = new BugScreenshotVO();

        screenshotVO.setReportId(null); // 现在没有reportId

        String url = OssHelper.uploadPic(multipartFile, "pic");
        byte[] fp = OssHelper.getFingerPrint();
        screenshotVO.setFingerprint(fp);

        // 图片名不需要，图片大小、上传时间均未设置

        screenshotVO.setScreenshot(url); //screenshot String

        BugScreenshot shot = new BugScreenshot(screenshotVO);
        screenshotMapper.insert(shot);
        screenshotVO = new BugScreenshotVO(shot);

        return new ResultVO<>(Constant.REQUEST_SUCCESS, "图片上传成功", screenshotVO);
    }

    @Override
    public ResultVO<BugScreenshotVO> deletePic(Integer shotId) {
        screenshotMapper.deleteByPrimaryKey(shotId);
        return new ResultVO<>(Constant.REQUEST_SUCCESS, "图片删除成功", new BugScreenshotVO());
    }

    @Override
    public ResultVO<ReportDetailVO> modifyReport(Integer reportId) {
        return null;
    }

    @Override
    public ResultVO<ReportDetailVO> deleteReport(Integer reportId) {
        // not delete rating...

        Report report = reportMapper.selectByPrimaryKey(reportId);

        List<BugScreenshot> shotList = screenshotMapper.selectByReportId(reportId);

        for (BugScreenshot shot : shotList) {
            screenshotMapper.deleteByPrimaryKey(shot.getId());
        }
        textHashMapper.deleteByReportId(reportId);
        reportMapper.deleteByPrimaryKey(reportId);

        ReportDetailVO reportDetailVO = new ReportDetailVO();
        reportDetailVO.setGeneralInfo(new ReportVO(report));
        reportDetailVO.setScreenshotList(POVOListGeneratorUtil.convert(
                shotList, BugScreenshotVO.class
        ));

        progressMapper.cancelStatusByIds(report.getTaskId(), report.getWorkerId());

        return new ResultVO<>(Constant.REQUEST_SUCCESS, "报告删除成功", new ReportDetailVO());
    }

    @Override
    public ResultVO<UserRatingVO> createRating(Integer reportId, Integer testerId, Integer score, String comment) {
        UserRatingVO userRatingVO=new UserRatingVO();
        if (userRatingMapper.selectByRidUid(reportId, testerId) == null) {
            UserRating userRating = new UserRating();
            userRating.setReportId(reportId);
            userRating.setUserId(testerId);
            userRating.setRating(score);
            userRating.setComment(comment);
            userRatingMapper.insert(userRating);


            Report report = reportMapper.selectByPrimaryKey(reportId);
            int raterNum = report.getRealRaterNumber();
            report.setRealRaterNumber(raterNum + 1);
            BigDecimal weightedRaterNum = report.getWeightedRaterNumber();
            User rater = userMapper.selectByPrimaryKey(testerId);
            double raterCompetence = rater.getRatingAbility().doubleValue();
            if (raterCompetence == 0)
                raterCompetence = 2;
            BigDecimal weightedNum = BigDecimal.valueOf(raterCompetence / 2);
            BigDecimal afterNum = weightedRaterNum.add(weightedNum);
            report.setWeightedRaterNumber(afterNum);
            BigDecimal initialScore = report.getScore();
            BigDecimal afterScore = initialScore.multiply(weightedRaterNum).add(
                    weightedNum.multiply(BigDecimal.valueOf(score))
            ).divide(afterNum, 2, RoundingMode.HALF_UP);
            report.setScore(afterScore);
            reportMapper.updateByPrimaryKey(report);

            RatingForScan rating = new RatingForScan();
            rating.setRatingTime(report.getCreateTime());
            rating.setRating(score);
            rating.setReportId(reportId);
            rating.setUserId(testerId);
            rating.setReportEndTime(taskMapper.selectByPrimaryKey(report.getTaskId()).getTestEndTime());
            ratingForScanMapper.insert(rating);

            double parameter = 0.2;
            User author = userMapper.selectByPrimaryKey(
                    report.getWorkerId());
            double professionalAbility = author.getRatingAbility().doubleValue(); //初始专业能力是1，通过权重后得分改变
            if (professionalAbility == 0)
                professionalAbility = 1;
            author.setProfessionalAbility(BigDecimal.valueOf(afterScore.doubleValue() * parameter + professionalAbility * (1 - professionalAbility)));
            userMapper.updateByPrimaryKey(author);

            RatingReportList ratingReportList = ratingReportListMapper.selectByUid(testerId);
            if (ratingReportList != null) {
                List<String> mission = Arrays.asList(ratingReportList.getReportToRate().split("_"));
                if (mission.contains(reportId + "")) {
                    ratingReportList.setRatedReport(ratingReportList.getReportToRate() + "_" + reportId);
                    ratingReportListMapper.updateByPrimaryKey(ratingReportList);
                }
            }
            if(author.getId()!=2) {
                String message = rater.getUsername() + "刚刚给你的报告打了" + score + "分";
                if (comment != null)
                    message += "并评论到：" + comment;
                Notice notice=WebSocket.prepareMessage(author.getId(), message, Constant.RELATED_TO_ME + "");
                WebSocket.sendMessage2AUser(author.getId(), notice.getId()+"_"+message, Constant.RELATED_TO_ME + "");
            }
            userRatingVO.setRating(score);
            userRatingVO.setComment(comment);
            userRatingVO.setUserId(testerId);
            userRatingVO.setReportId(reportId);

        } else {
            return new ResultVO<>(Constant.REQUEST_FAIL, "请勿重复评分");
        }
        return new ResultVO<>(Constant.REQUEST_SUCCESS, "报告评分成功", userRatingVO);
    }
}