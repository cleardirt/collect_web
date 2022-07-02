package com.example.demo.mapperservice.report;

import com.example.demo.po.report.TextHash;
import java.util.List;

public interface TextHashMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByReportId(Integer reportId);

    int insert(TextHash record);

    TextHash selectByPrimaryKey(Integer id);

    List<TextHash> selectAll();

    int updateByPrimaryKey(TextHash record);

    TextHash selectByReportId(Integer reportId);
}