package com.example.demo.vo.report;

import com.example.demo.po.report.DeviceInfo;
import lombok.Data;

@Data
public class DeviceInfoVO {
    private Integer id;

    private Integer taskId;

    private String explanation;

    public DeviceInfoVO(DeviceInfo deviceInfo) {
        this.id = deviceInfo.getId();
        this.taskId = deviceInfo.getTaskId();
        this.explanation = deviceInfo.getExplanation();
    }

    public DeviceInfoVO() {
    }
}
