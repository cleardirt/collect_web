package com.example.demo.po.report;

import com.example.demo.vo.report.TextHashVO;

public class TextHash {
    private Integer id;

    private Integer reportId;

    private Integer taskId;

    private String titleHash;

    private String descriptionHash;

    private String stepHash;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTitleHash() {
        return titleHash;
    }

    public void setTitleHash(String titleHash) {
        this.titleHash = titleHash;
    }

    public String getDescriptionHash() {
        return descriptionHash;
    }

    public void setDescriptionHash(String descriptionHash) {
        this.descriptionHash = descriptionHash;
    }

    public String getStepHash() {
        return stepHash;
    }

    public void setStepHash(String stepHash) {
        this.stepHash = stepHash;
    }

    public TextHash(){}

    public TextHash(TextHashVO textHash){
        this.id=textHash.getId();
        this.reportId= textHash.getReportId();
        this.taskId= textHash.getTaskId();;
        this.titleHash=textHash.getTitleHash();
        this.descriptionHash=textHash.getDescriptionHash();
        this.stepHash=textHash.getStepHash();
    }

}