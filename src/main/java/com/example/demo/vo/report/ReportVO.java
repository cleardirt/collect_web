package com.example.demo.vo.report;

import com.example.demo.po.report.Report;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class ReportVO {
    private Integer id;

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Integer workerId;

    private Integer taskId;

    private String deviceInfo;

    private Integer fatherId;

    private String workerName;

    private String bugDescription;

    private String stepExplanation;

    private BigDecimal score;

    private Integer realRaterNumber;

    private BigDecimal weightedRaterNumber;

    public ReportVO(Report report) {
        this.id = report.getId();
        this.createTime = report.getCreateTime();
        this.title = report.getTitle();
        this.workerId = report.getWorkerId();
        this.taskId = report.getTaskId();
        this.deviceInfo= report.getDeviceInfo();
        this.fatherId=report.getFatherId();
        this.workerName=report.getWorkerName();
        this.bugDescription=report.getBugDescription();
        this.score=report.getScore();
        this.realRaterNumber =report.getRealRaterNumber();
        this.weightedRaterNumber=report.getWeightedRaterNumber();
        this.stepExplanation=report.getStepExplanation();
    }

    public ReportVO() {
    }
}
