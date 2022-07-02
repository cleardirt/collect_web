package com.example.demo.mapperservice.report;

import com.example.demo.po.report.RatingReportList;
import java.util.List;

public interface RatingReportListMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RatingReportList record);

    RatingReportList selectByPrimaryKey(Integer id);

    RatingReportList selectByUid(Integer uid);

    List<RatingReportList> selectAll();

    int updateByPrimaryKey(RatingReportList record);
}