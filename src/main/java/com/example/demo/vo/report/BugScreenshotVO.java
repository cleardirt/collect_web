package com.example.demo.vo.report;

import com.example.demo.po.report.BugScreenshot;
import lombok.Data;

@Data
public class BugScreenshotVO {
    private Integer id;

    private String screenshot;

    private Integer reportId;

    private byte[] fingerprint;

    public BugScreenshotVO(BugScreenshot bugScreenshot) {
        this.id = bugScreenshot.getId();
        this.screenshot = bugScreenshot.getScreenshot();
        this.reportId = bugScreenshot.getReportId();
        this.fingerprint=bugScreenshot.getFingerprint();
    }

    public BugScreenshotVO() {
    }
}
