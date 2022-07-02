package com.example.demo.mapperservice;

import com.example.demo.po.Achievement;
import java.util.List;

public interface AchievementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Achievement record);

    Achievement selectByPrimaryKey(Integer id);

    List<Achievement> selectByUid(Integer id);

    List<Achievement> selectAll();

    int updateByPrimaryKey(Achievement record);
}