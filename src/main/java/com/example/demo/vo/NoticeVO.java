package com.example.demo.vo;

import com.example.demo.po.Notice;
import com.example.demo.vo.report.RatingTaskVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class NoticeVO {
    private Integer id;

    private Integer uid;

    private String type;

    private String content;

    private Boolean isRead;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sentDate;

    public NoticeVO(){}

    public NoticeVO(Notice notice){
        this.id = notice.getId();
        this.type = notice.getType();
        this.content = notice.getContent();
        this.uid = notice.getUid();
        this.isRead = notice.getIsRead();
        this.sentDate = notice.getSentDate();
    }

    public NoticeVO(Integer uid, RatingTaskVO ratingTaskVO){
        this.type = "rating_task";
        this.content = ratingTaskVO.getReportId().toString();
        this.uid = uid;
        this.isRead = false;
        this.sentDate=new Date();
    }
}
