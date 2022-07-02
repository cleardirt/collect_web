package com.example.demo.vo.user;

import com.example.demo.po.user.UserSelect;
import lombok.Data;

@Data
public class UserSelectVO {

    private Integer id;

    private Integer userId;

    private Integer functionTest;

    private Integer performanceTest;

    private Integer bugExplore;

    private Integer usecaseExecution;

    private Integer deviceConnection;

    public UserSelectVO(){}

    public UserSelectVO(UserSelect userSelect){
        this.id=userSelect.getId();
        this.userId=userSelect.getUserId();
        this.functionTest=userSelect.getFunctionTest();
        this.performanceTest=userSelect.getPerformanceTest();
        this.bugExplore=userSelect.getBugExplore();
        this.usecaseExecution=userSelect.getUsecaseExecution();
        this.deviceConnection=userSelect.getDeviceConnection();
    }
}
