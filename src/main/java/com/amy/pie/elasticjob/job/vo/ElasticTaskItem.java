package com.amy.pie.elasticjob.job.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 每个任务项
 */
@Getter
@Setter
public class ElasticTaskItem implements Serializable {

    /**
     * 任务id 必须唯一，做为分片标识
     */
    private long taskId;

    /**
     * 任务状态
     */
    private TaskItemStatus status = TaskItemStatus.ACTIVE;

    /**
     * 任务数据信息
     */
    private Object object;

    public ElasticTaskItem() {

    }

    public ElasticTaskItem(Object data) {
        object = data;
    }

    public enum TaskItemStatus {
        ACTIVE,
        INACTIVE
    }

    /**
     * 线程执行器的上下文
     **/
    @Getter
    @Setter
    @ToString
    public static class RunnerContext implements Serializable {
        private String taskCode;
        private TaskConfig taskConfig;

        public RunnerContext(String taskCode, TaskConfig taskConfig) {
            this.taskCode = taskCode;
            this.taskConfig = taskConfig;
        }
    }
}