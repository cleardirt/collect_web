package com.example.demo.po.task;

import com.example.demo.vo.task.TaskVO;

import java.util.Date;

public class Task {
    private Integer id;

    private String title;

    private String briefIntro;

    private Date testStartTime;

    private Date testEndTime;

    private Boolean isOpen;

    private Integer workerNum;

    private String testType;

    private Date createTime;

    private Date deleteTime;

    private Integer auftraggeberId;

    private String auftraggeberName;

    private Integer selectState;

    private Integer inProgress;

    private Integer difficulty;

    public Task() {
        
    }

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

    public String getBriefIntro() {
        return briefIntro;
    }

    public void setBriefIntro(String briefIntro) {
        this.briefIntro = briefIntro == null ? null : briefIntro.trim();
    }

    public Date getTestStartTime() {
        return testStartTime;
    }

    public void setTestStartTime(Date testStartTime) {
        this.testStartTime = testStartTime;
    }

    public Date getTestEndTime() {
        return testEndTime;
    }

    public void setTestEndTime(Date testEndTime) {
        this.testEndTime = testEndTime;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public Integer getWorkerNum() {
        return workerNum;
    }

    public void setWorkerNum(Integer workerNum) {
        this.workerNum = workerNum;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType == null ? null : testType.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Integer getAuftraggeberId() {
        return auftraggeberId;
    }

    public void setAuftraggeberId(Integer auftraggeberId) {
        this.auftraggeberId = auftraggeberId;
    }

    public String getAuftraggeberName() {
        return auftraggeberName;
    }

    public void setAuftraggeberName(String auftraggeberName) {
        this.auftraggeberName = auftraggeberName == null ? null : auftraggeberName.trim();
    }

    public Integer getSelectState() {
        return selectState;
    }

    public void setSelectState(Integer selectState) {
        this.selectState = selectState;
    }

    public Integer getInProgress() {
        return inProgress;
    }

    public void setInProgress(Integer inProgress) {
        this.inProgress = inProgress;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Task(TaskVO task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.briefIntro = task.getBriefIntro();
        this.testStartTime = task.getTestStartTime();
        this.testEndTime = task.getTestEndTime();
        this.isOpen = task.getIsOpen();
        this.workerNum = task.getWorkerNum();
        this.testType = task.getTestType();
        this.createTime = task.getCreateTime();
        this.deleteTime = task.getDeleteTime();
        this.auftraggeberId = task.getAuftraggeberId();
        this.auftraggeberName = task.getAuftraggeberName();
        this.selectState = task.getSelectState();
        this.inProgress = task.getInProgress();
        this.difficulty=task.getDifficulty();
    }
}