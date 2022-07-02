package com.example.demo.po.report;

import com.example.demo.vo.report.ReportVO;

import java.math.BigDecimal;
import java.util.Date;

public class Report {
    private Integer id;

    private String title;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo == null ? null : deviceInfo.trim();
    }

    public Integer getFatherId() {
        return fatherId;
    }

    public void setFatherId(Integer fatherId) {
        this.fatherId = fatherId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getBugDescription() {
        return bugDescription;
    }

    public void setBugDescription(String bugDescription) {
        this.bugDescription = bugDescription;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Integer getRealRaterNumber() {
        return realRaterNumber;
    }

    public void setRealRaterNumber(Integer realRaterNumber) {
        this.realRaterNumber = realRaterNumber;
    }


    public BigDecimal getWeightedRaterNumber() {
        return weightedRaterNumber;
    }

    public void setWeightedRaterNumber(BigDecimal weightedRaterNumber) {
        this.weightedRaterNumber = weightedRaterNumber;
    }

    public String getStepExplanation() {
        return stepExplanation;
    }

    public void setStepExplanation(String stepExplanation) {
        this.stepExplanation = stepExplanation == null ? null : stepExplanation.trim();
    }

    public Report() {
    }

    public Report(ReportVO report) {
        this.id = report.getId();
        this.createTime = report.getCreateTime();
        this.title = report.getTitle();
        this.workerId = report.getWorkerId();
        this.taskId = report.getTaskId();
        this.deviceInfo=report.getDeviceInfo();
        this.fatherId=report.getFatherId();
        this.workerName=report.getWorkerName();
        this.bugDescription=report.getBugDescription();
        this.score=report.getScore();
        this.realRaterNumber =report.getRealRaterNumber();
        this.weightedRaterNumber=report.getWeightedRaterNumber();
        this.stepExplanation=report.getStepExplanation();
    }
}