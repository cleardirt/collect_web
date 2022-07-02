package com.example.demo.po.task;

import com.example.demo.vo.task.AppVO;

import java.util.Date;

public class App {
    private Integer id;

    private Integer taskId;

    private String fileName;

    private String url;

    private String fileSize;

    private Date uploadTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize == null ? null : fileSize.trim();
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public App(){}

    public App(AppVO app) {
        this.id = app.getId();
        this.taskId = app.getTaskId();
        this.fileName = app.getFileName();
        this.url = app.getUrl();
        this.fileSize = app.getFileSize();
        this.uploadTime = app.getUploadTime();
    }
}