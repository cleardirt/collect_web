package com.example.demo.mapperservice.user;

import com.example.demo.po.user.UserSelect;
import java.util.List;

public interface UserSelectMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByUid(Integer user_id);

    int insert(UserSelect record);

    UserSelect selectByPrimaryKey(Integer id);

    UserSelect selectByUserId(Integer id);

    List<UserSelect> selectAll();

    int updateByPrimaryKey(UserSelect record);
}