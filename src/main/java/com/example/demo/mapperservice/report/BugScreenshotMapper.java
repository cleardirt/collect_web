package com.example.demo.mapperservice.report;

import com.example.demo.po.report.BugScreenshot;


import java.util.List;

public interface BugScreenshotMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BugScreenshot record);

    BugScreenshot selectByPrimaryKey(Integer id);

    List<BugScreenshot> selectAll();

    int updateByPrimaryKey(BugScreenshot record);

    List<BugScreenshot> selectByReportId(Integer report_id);
}