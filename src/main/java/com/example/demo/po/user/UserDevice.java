package com.example.demo.po.user;

import com.example.demo.vo.user.UserDeviceVO;

public class UserDevice {
    private Integer id;

    private Integer userId;

    private Integer windows;

    private Integer linux;

    private Integer macos;

    private Integer harmonyos;

    private Integer ios;

    private Integer android;

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

    public Integer getWindows() {
        return windows;
    }

    public void setWindows(Integer windows) {
        this.windows = windows;
    }

    public Integer getLinux() {
        return linux;
    }

    public void setLinux(Integer linux) {
        this.linux = linux;
    }

    public Integer getMacos() {
        return macos;
    }

    public void setMacos(Integer macos) {
        this.macos = macos;
    }

    public Integer getHarmonyos() {
        return harmonyos;
    }

    public void setHarmonyos(Integer harmonyos) {
        this.harmonyos = harmonyos;
    }

    public Integer getIos() {
        return ios;
    }

    public void setIos(Integer ios) {
        this.ios = ios;
    }

    public Integer getAndroid() {
        return android;
    }

    public void setAndroid(Integer android) {
        this.android = android;
    }

    public UserDevice(){}

    public UserDevice(UserDeviceVO userDevice){
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