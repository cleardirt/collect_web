package com.example.demo.vo.report;

import com.example.demo.po.report.TextHash;
import lombok.Data;

@Data
public class TextHashVO {
    private Integer id;

    private Integer reportId;

    private Integer taskId;

    private String titleHash;

    private String descriptionHash;

    private String stepHash;

    public TextHashVO(){}

    public TextHashVO(TextHash textHash){
        this.id=textHash.getId();
        this.reportId= textHash.getReportId();
        this.taskId= textHash.getTaskId();;
        this.titleHash=textHash.getTitleHash();
        this.descriptionHash=textHash.getDescriptionHash();
        this.stepHash=textHash.getStepHash();
    }
}
