package com.example.demo.vo.task;

import com.example.demo.po.task.Task;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class TaskVO {
    private Integer id;

    private String title;

    private String briefIntro;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date testStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date testEndTime;

    private Boolean isOpen;

    private Integer workerNum;

    private String testType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deleteTime;

    private Integer auftraggeberId;

    private String auftraggeberName;

    private Integer selectState;

    private Integer inProgress;

    private Integer difficulty;

    public TaskVO(Task task) {
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

    public TaskVO() {
    }
}
