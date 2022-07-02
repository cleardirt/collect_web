package com.example.demo.po.report;

import java.math.BigDecimal;

public class RatingReportList {
    private Integer id;

    private Integer uid;

    private String reportToRate;

    private String ratedReport;

    private BigDecimal ratingWillingness;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getReportToRate() {
        return reportToRate;
    }

    public void setReportToRate(String reportToRate) {
        this.reportToRate = reportToRate == null ? null : reportToRate.trim();
    }

    public String getRatedReport() {
        return ratedReport;
    }

    public void setRatedReport(String ratedReport) {
        this.ratedReport = ratedReport == null ? null : ratedReport.trim();
    }

    public BigDecimal getRatingWillingness() {
        return ratingWillingness;
    }

    public void setRatingWillingness(BigDecimal ratingWillingness) {
        this.ratingWillingness = ratingWillingness;
    }

    public RatingReportList(){}
}