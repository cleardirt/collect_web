package com.example.demo.vo.task;

import com.example.demo.po.task.TaskProgress;
import lombok.Data;

@Data
public class TaskProgressVO {
    private Integer id;

    private Integer workerId;

    private Integer taskId;

    private Boolean isFinished;

    public TaskProgressVO(TaskProgress taskProgress) {
        this.id = taskProgress.getId();
        this.workerId = taskProgress.getWorkerId();
        this.taskId = taskProgress.getTaskId();
        this.isFinished = taskProgress.getIsFinished();
    }

    public TaskProgressVO() {
    }
}
