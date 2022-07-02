package com.example.demo.po.report;

import com.example.demo.vo.report.DeviceInfoVO;

public class DeviceInfo {
    public DeviceInfo() {
    }

    private Integer id;

    private Integer taskId;

    private String explanation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation == null ? null : explanation.trim();
    }

    public DeviceInfo(DeviceInfoVO deviceInfo) {
        this.id = deviceInfo.getId();
        this.taskId = deviceInfo.getTaskId();
        this.explanation = deviceInfo.getExplanation();
    }
}