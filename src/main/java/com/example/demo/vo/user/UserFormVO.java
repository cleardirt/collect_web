package com.example.demo.vo.user;

import com.example.demo.po.user.User;
import lombok.Data;

@Data
public class UserFormVO {
    private String phone;
    private String userpass;
    private Integer id;
    private String userRole;
    private String token;
    private String username;

    public UserFormVO() {
    }

    public UserFormVO(User user) {
        this.id=user.getId();
        this.phone=user.getPhone();
        this.userRole=user.getUserRole();
        this.username=user.getUsername();
        this.userpass=user.getUserpass();
    }
}