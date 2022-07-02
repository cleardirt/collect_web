package com.example.demo.mapperservice.task;

import com.example.demo.po.task.Task;
import com.example.demo.po.task.TaskProgress;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
public interface TaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Task record);

    Task selectByPrimaryKey(Integer id);

    List<Task> selectAll();

    List<Task> selectAllByType(String type);

    List<Task> selectInProgressAll(Date nowtime);

    List<Integer> selectIdInProgressAll(Date nowtime);

    List<Task> selectInProgressAllBYType(@Param("nowtime")Date nowtime,@Param("type") String type);

    int updateByPrimaryKey(Task record);

    List<Task> selectUserAll(Integer id);

    List<Task> selectUserAllFinished(Integer id);

    List<Task> selectUserAllUnfinished(Integer id);

    List<Task> selectUserAllAndType(@Param("uid") Integer id,@Param("type") String type);

    List<Task> selectUserAllAndTypeFinished(@Param("uid") Integer id,@Param("type") String type);

    List<Task> selectUserAllAndTypeUnFinished(@Param("uid") Integer id,@Param("type") String type);

    List<Task> selectAuftarAll(Integer id);

    List<Task> selectAuftarAllAndType(@Param("uid") Integer id,@Param("type") String type);

    Task selectTaskOfAuftra(@Param("uid") Integer uid, @Param("task_id") Integer task_id);

    List<Integer> selectEndsToday(@Param("today")Date today,@Param("yesterday")Date yesterday);
}