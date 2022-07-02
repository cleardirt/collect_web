package com.example.demo.controller.report;

import com.example.demo.service.report.ReportService;
import com.example.demo.util.Constant;
import com.example.demo.vo.ResultVO;
import com.example.demo.vo.report.*;
import com.example.demo.vo.task.TaskProgressVO;
import com.example.demo.vo.user.UserRatingVO;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Resource
    ReportService reportService;

    @PostMapping("/create")
    public ResultVO<ReportDetailVO> createReport(@RequestBody ReportDetailVO reportDetailVO) {
        return reportService.createReport(reportDetailVO);
    }

    // 协作fork
    @PostMapping("/fork/{fatherRId}")
    public ResultVO<ReportVO> forkReport(@PathVariable Integer fatherRId, @RequestBody ReportDetailVO
            reportDetailVO) {
        return reportService.forkReportByFatherRid(fatherRId, reportDetailVO);
    }

    @GetMapping("/{report_id}")
    public ReportDetailVO getReportByRid(@PathVariable Integer report_id) {
        return reportService.getReport(report_id);
    }

    // 任务详情页展示报告列表
    @GetMapping("/{task_id}/report_list/{page}")
    public PageInfo<ReportVO> getReportsByTid(
            @RequestParam(required = false, defaultValue = "") Integer uid,
            @PathVariable Integer task_id,
            @PathVariable Integer page) {
        return reportService.getReportList(page, Constant.REPORT_PAGE_SIZE, uid, task_id);
    }

    @GetMapping("/{task_id}/mine/{page}")
    public PageInfo<ReportVO> getMineByUid(@RequestParam(required = false, defaultValue = "") Integer uid,
                                           @PathVariable Integer task_id,
                                           @PathVariable Integer page) {
        return reportService.getMyReports(page, Constant.REPORT_PAGE_SIZE, uid, task_id);
    }

    @GetMapping("/{userId}/mine/")
    public List<ReportVO> getMineByUid(@RequestParam(required = false, defaultValue = "") Integer uid) {
        return reportService.getUserReports(uid);
    }

    @PostMapping("/picture/upload")
    public ResultVO<BugScreenshotVO> uploadPic(@RequestParam("file") MultipartFile multipartFile)
            throws IOException {
        return reportService.uploadShot(multipartFile);
    }

    // TESTER填写报告的过程中，删除上传的截图
    @PostMapping("/picture/delete/{shotId}")
    public ResultVO<BugScreenshotVO> deletePicByPid(@PathVariable Integer shotId) {
        return reportService.deletePic(shotId);
    }

    // 检测tester完成状态
    @GetMapping("/status/{task_id}")
    public TaskProgressVO getStatusByIds(@RequestParam Integer tester_id, @PathVariable Integer task_id) {
        return reportService.getReportStatus(tester_id, task_id);
    }

    @PostMapping("/modify/{reportId}")
    public ResultVO<ReportDetailVO> modifyReportById(@PathVariable Integer reportId) {
        // todo
        return null;
    }

    @PostMapping("/rate/{reportId}")
    public ResultVO<UserRatingVO> rateReportById(@PathVariable Integer reportId,
                                                 @RequestParam Integer testerId,
                                                 @RequestParam Integer score,
                                                 @RequestParam String comment) {
        return reportService.createRating(reportId, testerId, score, comment);
    }

    // 根据尚未提交的报告内容，找出数据库内相似度较高的同任务报告列表
    @PostMapping("/similar")
    public ResultVO<List<SimilarReportVO>> getSimilarByDetailVO(
            @RequestBody ReportDetailVO reportDetailVO) {
        return reportService.getSimilarReports(reportDetailVO);
    }

    @GetMapping("/CollaborationTree/{reportId}")
    public ReportTreeNodeVO getFatherNode(@PathVariable Integer reportId) {
        return reportService.getFatherNode(reportId);
    }

    @GetMapping("/myRating/{reportId}")
    public ResultVO<UserRatingVO> getMyRatingByUid(@PathVariable Integer reportId,
                                                   @RequestParam Integer testerId) {
        return reportService.getMyRating(reportId, testerId);
    }


    @GetMapping("/similar/{reportId}")
    public ResultVO<List<SimilarReportVO>> getSimilarByReportId(
            @PathVariable Integer reportId
    ) {
        return reportService.getSimilarReports(reportId);
    }
}
