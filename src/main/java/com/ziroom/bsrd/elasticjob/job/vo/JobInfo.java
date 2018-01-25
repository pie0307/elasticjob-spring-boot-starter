package com.ziroom.bsrd.elasticjob.job.vo;


import com.dangdang.ddframe.job.api.config.impl.JobType;

/**
 * job 配置
 */
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

    public String getJobParameter() {
        return jobParameter;
    }

    public void setJobParameter(String jobParameter) {
        this.jobParameter = jobParameter;
    }

    private boolean overwrite;

    private String description;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public int getShardingTotalCount() {
        return shardingTotalCount;
    }

    public Class getJobClass() {
        return jobClass;
    }

    public void setJobClass(Class jobClass) {
        this.jobClass = jobClass;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public String getDescription() {
        return description;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setShardingTotalCount(int shardingTotalCount) {
        this.shardingTotalCount = shardingTotalCount;
    }
}