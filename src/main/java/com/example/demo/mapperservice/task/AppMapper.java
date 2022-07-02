package com.example.demo.mapperservice.task;

import com.example.demo.po.task.App;


import java.util.List;

public interface AppMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByFileName(String fileName);

    int insert(App record);

    App selectByPrimaryKey(Integer id);

    List<App> selectByTaskId(Integer id);

    List<App> selectAll();

    int updateByPrimaryKey(App record);
}