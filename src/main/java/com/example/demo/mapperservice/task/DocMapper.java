package com.example.demo.mapperservice.task;

import com.example.demo.po.task.Doc;


import java.util.List;

public interface DocMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Doc record);

    Doc selectByPrimaryKey(Integer id);

    List<Doc> selectByTaskId(Integer id);

    List<Doc> selectAll();

    int updateByPrimaryKey(Doc record);
}