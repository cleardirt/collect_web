package com.example.demo.po.report;

import com.example.demo.vo.report.BugScreenshotVO;

public class BugScreenshot {
    private Integer id;

    private String screenshot;

    private Integer reportId;

    private byte[] fingerprint;

    public BugScreenshot() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot == null ? null : screenshot.trim();
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public byte[] getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(byte[] fingerprint) {
        this.fingerprint = fingerprint;
    }

    public BugScreenshot(BugScreenshotVO bugScreenshot) {
        this.id = bugScreenshot.getId();
        this.screenshot = bugScreenshot.getScreenshot();
        this.reportId = bugScreenshot.getReportId();
        this.fingerprint=bugScreenshot.getFingerprint();
    }
}