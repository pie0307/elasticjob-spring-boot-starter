package com.amy.pie.elasticjob.job.itf;


import com.amy.pie.elasticjob.job.vo.ElasticTaskItem;

/**
 * 任务执行器
 */
public interface ITaskExecutor {

    String handle(ElasticTaskItem elasticTaskItem, String taskName);
}
