package com.example.demo.vo.user;

import com.example.demo.po.user.UserDevice;
import lombok.Data;

@Data
public class UserDeviceVO {
    private Integer id;

    private Integer userId;

    private Integer windows;

    private Integer linux;

    private Integer macos;

    private Integer harmonyos;

    private Integer ios;

    private Integer android;

    public UserDeviceVO(){}

    public UserDeviceVO(UserDevice userDevice){
        this.id=userDevice.getId();
        this.userId=userDevice.getUserId();
        this.windows=userDevice.getWindows();
        this.linux=userDevice.getLinux();
        this.macos=userDevice.getMacos();
        this.harmonyos=userDevice.getHarmonyos();
        this.ios=userDevice.getIos();
        this.android=userDevice.getAndroid();
    }
}
