package tager.imagemaster.entity.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskPre {
    public String taskName; // 任务名

    public int people; // 任务所需人数

    public double reward; // 奖励积分

    public double consume;

    public String description; // 任务描述

    public String label; // 标签
}
