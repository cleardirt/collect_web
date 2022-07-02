package com.example.demo.mapperservice.user;

import com.example.demo.po.user.UserDevice;
import java.util.List;

public interface UserDeviceMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByUid(Integer user_id);

    int insert(UserDevice record);

    UserDevice selectByPrimaryKey(Integer id);

    UserDevice selectByUserId(Integer id);

    List<UserDevice> selectAll();

    int updateByPrimaryKey(UserDevice record);
}