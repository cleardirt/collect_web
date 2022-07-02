package com.example.demo.mapperservice.user;

import com.example.demo.po.user.User;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    User selectByPrimaryKey(Integer id);

    List<User> selectByTaskId(Integer id);

    User selectByPhone(String phone);

    List<User> selectAll();

    List<User> selectAllByRole(String role);

    int updateByPrimaryKey(User record);
}
