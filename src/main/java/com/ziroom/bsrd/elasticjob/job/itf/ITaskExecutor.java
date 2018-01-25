package com.ziroom.bsrd.elasticjob.job.itf;


import com.ziroom.bsrd.elasticjob.job.vo.ElasticTaskItem;

/**
 * 任务执行器
 */
public interface ITaskExecutor {

    String handle(ElasticTaskItem elasticTaskItem, String taskName);
}
