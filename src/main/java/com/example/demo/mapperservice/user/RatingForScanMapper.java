package com.example.demo.mapperservice.user;

import com.example.demo.po.user.RatingForScan;
import java.util.List;

public interface RatingForScanMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RatingForScan record);

    RatingForScan selectByPrimaryKey(Integer id);

    List<RatingForScan> selectAll();

    int updateByPrimaryKey(RatingForScan record);

    int deleteByUid(Integer id);
}