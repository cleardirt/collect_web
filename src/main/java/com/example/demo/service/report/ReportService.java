package com.example.demo.service.report;

import com.example.demo.vo.ResultVO;
import com.example.demo.vo.report.*;
import com.example.demo.vo.task.TaskProgressVO;
import com.example.demo.vo.user.UserRatingVO;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReportService {
    ResultVO<ReportDetailVO> createReport(ReportDetailVO reportDetailVO);

    ResultVO<ReportVO> forkReportByFatherRid(Integer fatherRid, ReportDetailVO reportDetailVO);

    ReportDetailVO getReport(Integer report_id);

    PageInfo<ReportVO> getReportList(Integer currPage, Integer pageSize, Integer uid, Integer task_id);

    TaskProgressVO getReportStatus(Integer tester_id, Integer task_id);

    ResultVO<BugScreenshotVO> uploadShot(MultipartFile multipartFile)throws IOException;

    ResultVO<BugScreenshotVO> deletePic(Integer shotId);

    ResultVO<ReportDetailVO> modifyReport(Integer reportId);

    ResultVO<ReportDetailVO> deleteReport(Integer reportId);

    ResultVO<UserRatingVO> createRating(Integer reportId, Integer testerId, Integer score, String comment);

    PageInfo<ReportVO> getMyReports(Integer page, Integer reportPageSize, Integer uid, Integer task_id);

    ResultVO<List<SimilarReportVO>> getSimilarReports(ReportDetailVO reportDetailVO);

    ReportTreeNodeVO getFatherNode(Integer reportId);

    ResultVO<UserRatingVO> getMyRating(Integer reportId, Integer testerId);

    ResultVO<List<SimilarReportVO>> getSimilarReports(Integer reportId);

    List<ReportVO> getUserReports(Integer uid);
}
