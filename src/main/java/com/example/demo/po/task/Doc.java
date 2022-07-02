package com.example.demo.po.task;

import com.example.demo.vo.task.DocVO;

import java.util.Date;

public class Doc {
    private Integer id;

    private Integer taskId;

    private String fileName;

    private String url;

    private String fileSize;

    private Date uploadTime;

    public Doc() {
    }

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

    public Doc(DocVO doc) {
        this.id = doc.getId();
        this.taskId = doc.getTaskId();
        this.fileName = doc.getFileName();
        this.url = doc.getUrl();
        this.fileSize = doc.getFileSize();
        this.uploadTime = doc.getUploadTime();
    }
}