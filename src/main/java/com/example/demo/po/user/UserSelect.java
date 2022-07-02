package com.example.demo.po.user;

import com.example.demo.vo.user.UserSelectVO;

public class UserSelect {
    private Integer id;

    private Integer userId;

    private Integer functionTest;

    private Integer performanceTest;

    private Integer bugExplore;

    private Integer usecaseExecution;

    private Integer deviceConnection;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFunctionTest() {
        return functionTest;
    }

    public void setFunctionTest(Integer functionTest) {
        this.functionTest = functionTest;
    }

    public Integer getPerformanceTest() {
        return performanceTest;
    }

    public void setPerformanceTest(Integer performanceTest) {
        this.performanceTest = performanceTest;
    }

    public Integer getBugExplore() {
        return bugExplore;
    }

    public void setBugExplore(Integer bugExplore) {
        this.bugExplore = bugExplore;
    }

    public Integer getUsecaseExecution() {
        return usecaseExecution;
    }

    public void setUsecaseExecution(Integer usecaseExecution) {
        this.usecaseExecution = usecaseExecution;
    }

    public Integer getDeviceConnection() {
        return deviceConnection;
    }

    public void setDeviceConnection(Integer deviceConnection) {
        this.deviceConnection = deviceConnection;
    }

    public UserSelect(){}

    public UserSelect(UserSelectVO userSelect){
        this.id=userSelect.getId();
        this.userId=userSelect.getUserId();
        this.functionTest=userSelect.getFunctionTest();
        this.performanceTest=userSelect.getPerformanceTest();
        this.bugExplore=userSelect.getBugExplore();
        this.usecaseExecution=userSelect.getUsecaseExecution();
        this.deviceConnection=userSelect.getDeviceConnection();
    }
}