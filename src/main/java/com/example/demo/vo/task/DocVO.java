package com.example.demo.vo.task;

import com.example.demo.po.task.Doc;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class DocVO {
    private Integer id;

    private Integer taskId;

    private String fileName;

    private String url;

    private String fileSize;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadTime;


    public DocVO(Doc doc) {
        this.id = doc.getId();
        this.taskId = doc.getTaskId();
        this.fileName = doc.getFileName();
        this.url = doc.getUrl();
        this.fileSize = doc.getFileSize();
        this.uploadTime = doc.getUploadTime();
    }

    public DocVO() {
    }
}
