package com.example.demo.vo.task;

import com.example.demo.po.task.App;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class AppVO {
    private Integer id;

    private Integer taskId;

    private String fileName;

    private String url;

    private String fileSize;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadTime;


    public AppVO(App app) {
        this.id = app.getId();
        this.taskId = app.getTaskId();
        this.fileName = app.getFileName();
        this.url = app.getUrl();
        this.fileSize = app.getFileSize();
        this.uploadTime = app.getUploadTime();
    }

    public AppVO() {

    }
}
