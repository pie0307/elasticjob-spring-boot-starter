package com.amy.pie.elasticjob.job.vo;


import com.dangdang.ddframe.job.api.config.impl.JobType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * job 配置
 */
@Getter
@Setter
@ToString
public class JobInfo {

    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 任务编码
     */
    private String jobCode;
    /**
     * 任务类型
     */
    private JobType jobType;

    /**
     * 执行类
     */
    private Class jobClass;

    /**
     * 表达式
     */
    private String cron;

    /**
     * 分片数
     */
    private int shardingTotalCount;

    /**
     * 任务参数
     */
    private String jobParameter;

    private boolean overwrite;

    private String description;
}