package com.example.demo.vo.report;

import com.example.demo.po.report.Report;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RatingTaskVO {
    private Integer reportId;

    private Integer taskId;

    private String reportTitle;

    private String reportDeviceInfo;

    private BigDecimal reportScore;

    private String authorName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reportCreateTime;

    public RatingTaskVO(){}

    public RatingTaskVO(Report report){
        this.reportId = report.getId();
        this.taskId = report.getTaskId();
        this.reportTitle = report.getTitle();
        this.reportDeviceInfo = report.getDeviceInfo();
        this.reportScore = report.getScore();
        this.authorName = report.getWorkerName();
        this.reportCreateTime = report.getCreateTime();
    }
}
