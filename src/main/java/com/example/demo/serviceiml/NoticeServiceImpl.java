package com.example.demo.serviceiml;

import com.example.demo.mapperservice.NoticeMapper;
import com.example.demo.mapperservice.report.ReportMapper;
import com.example.demo.po.Notice;
import com.example.demo.po.report.Report;
import com.example.demo.service.NoticeService;
import com.example.demo.vo.NoticeVO;
import com.example.demo.vo.report.RatingTaskVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Resource
    NoticeMapper noticeMapper;
    @Resource
    ReportMapper reportMapper;

    @Override
    public List<NoticeVO> getUserNotices(Integer uid) {
        List<Notice> notices = noticeMapper.selectByUid(uid);
        notices.sort(Comparator.comparing(Notice::getSentDate));
        List<NoticeVO> result = new ArrayList<>();
        for (Notice notice:notices){
            result.add(new NoticeVO(notice));
        }
        return result;
    }

    @Override
    public void markSentNotice(Integer noticeID) {
        Notice notice = noticeMapper.selectByPrimaryKey(noticeID);
        System.out.println(noticeID);
        if(notice!=null) {
            notice.setIsRead(true);
            notice.setSentDate(new Date());
            noticeMapper.updateByPrimaryKey(notice);
        }
    }

    @Override
    public Integer storeNotice(Notice notice) {
        noticeMapper.insert(notice);
        return notice.getId();
    }

    @Override
    public List<Notice> getUnSent(Integer uid) {
        List<Notice> notices=noticeMapper.selectByUidUnsent(uid);
        List<Notice> res=new ArrayList<>();
        for(Notice notice:notices){
            if(!notice.getType().equals("rating_task")) {
                notice.setContent(notice.getId() + "_" + notice.getContent());
                res.add(notice);
            }
        }
        return res;
    }
}
