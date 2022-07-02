package com.example.demo.util;

import com.example.demo.mapperservice.report.ReportMapper;
import com.example.demo.po.report.Report;
import com.example.demo.vo.report.ReportTreeNodeVO;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public class ReportTree implements ApplicationRunner {
    @Resource
    ReportMapper reportMapper;
    /**
     * 用于指示bean包含在SpringApplication中时应运行的接口。可以定义多个applicationrunner bean
     * 在同一应用程序上下文中，可以使用有序接口或@order注释对其进行排序。
     */
    public HashMap<Integer, HashMap<Integer, ReportTreeNodeVO> >reportTreeNodes;//key代表taskId，下一个HashMap代表reportId和它对应的树节点

    @Override
    public void run(ApplicationArguments args) throws Exception {
        reportTreeNodes=new HashMap<>();
        List<Report> reports=reportMapper.selectAll();
        System.out.println(reports.size());
        for (Report report : reports) {//首先按taskId把report分组
            if (reportTreeNodes.containsKey(report.getTaskId())) {//判断taskId有没有放在map里了
                HashMap<Integer, ReportTreeNodeVO> temp=reportTreeNodes.get(report.getTaskId());
                temp.put(report.getId(),new ReportTreeNodeVO(report.getId(),report.getFatherId(),report.getTaskId()));
                reportTreeNodes.put(report.getTaskId(),temp);
            }
            else {
                HashMap<Integer, ReportTreeNodeVO> temp=new HashMap<>();
                temp.put(report.getId(),new ReportTreeNodeVO(report.getId(),report.getFatherId(),report.getTaskId()));
                reportTreeNodes.put(report.getTaskId(),temp);
            }
        }
        for (Map.Entry<Integer, HashMap<Integer, ReportTreeNodeVO> > entry: reportTreeNodes.entrySet()) {
            for (Map.Entry<Integer, ReportTreeNodeVO> taskEntry: entry.getValue().entrySet()) {//每个taskId对应的report列表
                if (taskEntry.getValue().getFatherId() != null) {
                    ReportTreeNodeVO father = entry.getValue().get(taskEntry.getValue().getFatherId());
                    father.getChild().add(entry.getValue().get(taskEntry.getValue().getReportId()));
                }
            }
        }
    }

    public ReportTreeNodeVO getNode(Integer taskId, Integer reportId){
        return reportTreeNodes.get(taskId).get(reportId);
    }

    public void insertNode(Integer taskId,Integer reportId,Integer fatherId){
        ReportTreeNodeVO newNode=new ReportTreeNodeVO(reportId,fatherId,taskId);
        if (fatherId!=null){
            ReportTreeNodeVO father = reportTreeNodes.get(taskId).get(fatherId);
            father.getChild().add(newNode);
        }
        HashMap<Integer, ReportTreeNodeVO> hashMap=reportTreeNodes.get(taskId);
        if (hashMap==null) {
            hashMap=new HashMap<>();
            hashMap.put(reportId,newNode);
            reportTreeNodes.put(taskId,hashMap);
        }
        else
            hashMap.put(reportId,newNode);
    }
}