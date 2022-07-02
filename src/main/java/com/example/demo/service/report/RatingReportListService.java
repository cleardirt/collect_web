package com.example.demo.service.report;

import com.example.demo.vo.report.RatingTaskVO;

import java.util.List;

public interface RatingReportListService {
    List<RatingTaskVO> getUnsentRatingTaskNotice(Integer uid);
}
