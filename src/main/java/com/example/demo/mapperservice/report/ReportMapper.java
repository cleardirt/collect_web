package com.example.demo.mapperservice.report;

import com.example.demo.po.report.Report;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ReportMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByUid(Integer uid);

    int insert(Report record);

    Report selectByPrimaryKey(Integer id);

    List<Report> selectAll();

    int updateByPrimaryKey(Report record);

    List<Report> selectByTaskId(Integer task_id);

    // 返回一个任务下某用户创建的报告列表
    List<Report> selectByTUid(@Param("task_id") Integer task_id,
                              @Param("uid") Integer uid);

    // 返回一个用户创建的所有报告，暂未使用
    List<Report> selectByTesterId(Integer uid);

    List<Report> selectByTidDev(@Param("task_id") Integer task_id,
                                @Param("dev") String dev);

    // 返回一个已存在报告的同任务其他报告
    List<Report> selectDifferentByTidDev(@Param("report_id") Integer report_id,
                                         @Param("task_id") Integer task_id,
                                         @Param("dev") String dev);

    // 返回一个已存在报告的同任务其他报告
    List<Report> selectEvaluableReport(@Param("user_id") Integer user_id,@Param("nowtime") Date today);
}