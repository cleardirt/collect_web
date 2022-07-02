package com.example.demo.serviceiml.report;

import com.example.demo.mapperservice.report.RatingReportListMapper;
import com.example.demo.mapperservice.report.ReportMapper;
import com.example.demo.po.report.RatingReportList;
import com.example.demo.service.report.RatingReportListService;
import com.example.demo.vo.report.RatingTaskVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class RatingReportListServiceImpl implements RatingReportListService {
    @Resource
    RatingReportListMapper reportListMapper;
    @Resource
    ReportMapper reportMapper;


    @Override
    public List<RatingTaskVO> getUnsentRatingTaskNotice(Integer uid) {
        RatingReportList reportList = reportListMapper.selectByUid(uid);
        String recommendedListStr = reportList.getReportToRate();
        System.out.println(recommendedListStr);
        String finishedListStr = reportList.getRatedReport();
        if (recommendedListStr==null||recommendedListStr.equals("")) return null;
        String[] recommended = recommendedListStr.split("_");
        List<Integer> recommendedIdList = strList2IntList(recommended);
        List<Integer> finishedIdList=new ArrayList<>();
        if(!finishedListStr.equals("")) {
            String[] finished = finishedListStr.split("_");
            finishedIdList = strList2IntList(finished);
        }
        List<RatingTaskVO> result = new ArrayList<>();
        for (Integer id : recommendedIdList) {
            if (!finishedIdList.contains(id)) {
                result.add(new RatingTaskVO(reportMapper.selectByPrimaryKey(id)));
            }
        }
        if (result.size()==0) return null;
        return result;
    }

    private List<Integer> strList2IntList(String[] strList){
        List<Integer> result = new ArrayList<>();
        for (String s : strList) {
            result.add(Integer.parseInt(s, 10));
        }
        return result;
    }


}
