package com.example.demo.vo.report;

import com.example.demo.po.report.Report;
import lombok.Data;

@Data
public class SimilarReportVO {
    Integer reportId;
    String  title;
    Double  similarityMeasure;
    String  percentage;
    String  workerName;

    public SimilarReportVO(){}
    public SimilarReportVO(Report report,Double similarity,String percentage){
        this.reportId=report.getId();
        this.title=report.getTitle();
        this.similarityMeasure=similarity;
        this.percentage=percentage;
        this.workerName=report.getWorkerName();
    }
}
