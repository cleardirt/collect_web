package com.example.demo.mapperservice.task;

import com.example.demo.po.task.TaskProgress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskProgressMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByUidAndTaskId(@Param("uid") Integer uid,@Param("taskId") Integer taskId);

    int insert(TaskProgress record);

    TaskProgress selectByPrimaryKey(Integer id);

    List<TaskProgress> selectAll();

    List<TaskProgress> selectByTaskId(Integer taskId);

    int updateByPrimaryKey(TaskProgress record);

    TaskProgress selectByUidAndTaskId(@Param("uid") Integer uid,@Param("taskId") Integer taskId);

    int updateStatusByIds(@Param("task_id") Integer task_id,@Param("uid") Integer uid);

    int cancelStatusByIds(@Param("task_id") Integer task_id,@Param("uid") Integer uid);
}