package com.example.demo.vo.report;

import com.example.demo.po.report.RatingReportList;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RatingReportListVO {
    private Integer id;

    private Integer uid;

    private String reportToRate;

    private String ratedReport;

    private BigDecimal ratingWillingness;

    public RatingReportListVO(){}

    public RatingReportListVO(RatingReportList ratingReportList){
        this.id = ratingReportList.getId();
        this.uid = ratingReportList.getUid();
        this.reportToRate = ratingReportList.getReportToRate();
        this.ratedReport = ratingReportList.getRatedReport();
        this.ratingWillingness = ratingReportList.getRatingWillingness();
    }
}
