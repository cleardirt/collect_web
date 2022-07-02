package com.example.demo.vo.user;

import com.example.demo.po.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserVO {
    private Integer id;

    private String username;

    private String phone;

    private String userpass;

    private String userRole;

    private Integer activity;

    private BigDecimal professionalAbility;

    private BigDecimal ratingAbility;

    private BigDecimal credit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public UserVO() {
    }
    public UserVO(@NonNull User user) {
        id = user.getId();
        username = user.getUsername();
        phone = user.getPhone();
        userpass = user.getUserpass();
        activity = user.getActivity();
        userRole = user.getUserRole();
        createTime = user.getCreateTime();
        professionalAbility = user.getProfessionalAbility();
        ratingAbility=user.getRatingAbility();
        credit=user.getCredit();
    }
}
