package com.example.demo.controller.task;

import com.example.demo.service.NoticeService;
import com.example.demo.vo.NoticeVO;
import com.example.demo.vo.ResultVO;
import com.example.demo.vo.report.RatingTaskVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Resource
    NoticeService noticeService;

    @GetMapping("/get")
    public List<NoticeVO> getNoticesByUid(@RequestParam Integer uid){
        return noticeService.getUserNotices(uid);
    }
}
