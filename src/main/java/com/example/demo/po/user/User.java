package com.example.demo.po.user;

import com.example.demo.vo.user.UserVO;

import java.math.BigDecimal;
import java.util.Date;

public class User {
    private Integer id;

    private String username;

    private String phone;

    private String userpass;

    private String userRole;

    private Date createTime;

    private Integer activity;

    private BigDecimal professionalAbility;

    private BigDecimal ratingAbility;

    private BigDecimal credit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getUserpass() {
        return userpass;
    }

    public void setUserpass(String userpass) {
        this.userpass = userpass == null ? null : userpass.trim();
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole == null ? null : userRole.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getActivity() {
        return activity;
    }

    public void setActivity(Integer activity) {
        this.activity = activity;
    }

    public BigDecimal getProfessionalAbility() {
        return professionalAbility;
    }

    public void setProfessionalAbility(BigDecimal professionalAbility) {
        this.professionalAbility = professionalAbility;
    }

    public BigDecimal getRatingAbility() {
        return ratingAbility;
    }

    public void setRatingAbility(BigDecimal ratingAbility) {
        this.ratingAbility = ratingAbility;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public User() {
    }

    public User(UserVO userVO){
        this.id = userVO.getId();
        this.username = userVO.getUsername();
        this.phone = userVO.getPhone();
        this.userpass = userVO.getUserpass();
        this.activity = userVO.getActivity();
        this.userRole = userVO.getUserRole();
        this.createTime = userVO.getCreateTime();
        this.professionalAbility = userVO.getProfessionalAbility();
        this.ratingAbility=userVO.getRatingAbility();
        this.credit=userVO.getCredit();
    }
}