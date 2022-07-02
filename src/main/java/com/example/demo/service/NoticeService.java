package com.example.demo.service;

import com.example.demo.po.Notice;
import com.example.demo.vo.NoticeVO;
import com.example.demo.vo.report.RatingTaskVO;

import java.util.List;

public interface NoticeService {
    List<NoticeVO> getUserNotices(Integer uid);
    void markSentNotice(Integer noticeID);
    Integer storeNotice(Notice notice);
    List<Notice> getUnSent(Integer uid);
}
