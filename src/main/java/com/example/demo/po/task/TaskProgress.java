package com.example.demo.po.task;

import com.example.demo.vo.task.TaskProgressVO;

public class TaskProgress {
    private Integer id;

    private Integer workerId;

    private Integer taskId;

    private Boolean isFinished;

    public TaskProgress() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Boolean getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }

    public TaskProgress(TaskProgressVO taskProgress) {
        this.id = taskProgress.getId();
        this.workerId = taskProgress.getWorkerId();
        this.taskId = taskProgress.getTaskId();
        this.isFinished = taskProgress.getIsFinished();
    }
}