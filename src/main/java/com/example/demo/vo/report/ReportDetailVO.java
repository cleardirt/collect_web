package com.example.demo.vo.report;

import lombok.Data;

import java.util.List;

@Data
public class ReportDetailVO {

    private ReportVO generalInfo;

    private List<BugScreenshotVO> screenshotList;

    public ReportDetailVO(){}

    public ReportDetailVO(ReportVO generalInfo,
                          List<BugScreenshotVO> screenshotList){
        this.generalInfo = generalInfo;
        this.screenshotList = screenshotList;
    }
}
