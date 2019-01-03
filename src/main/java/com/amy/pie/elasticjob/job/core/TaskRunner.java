package com.amy.pie.elasticjob.job.core;


import com.amy.pie.elasticjob.job.itf.ITaskExecutor;
import com.amy.pie.elasticjob.job.vo.ElasticTaskItem;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 任务线程执行器
 */
@Slf4j
public class TaskRunner extends SuperTask {

    private List<? extends ElasticTaskItem> value;

    private ElasticTaskItem.RunnerContext runnerContext;

    private ITaskExecutor taskExecutorHandler;

    public TaskRunner(ElasticTaskItem.RunnerContext runnerContext, List<? extends ElasticTaskItem> value, ITaskExecutor taskExecutorHandler) {
        this.runnerContext = runnerContext;
        this.value = value;
        this.taskExecutorHandler = taskExecutorHandler;
    }

    public TaskRunner(ElasticTaskItem.RunnerContext runnerContext) {
        this.runnerContext = runnerContext;
    }

    public TaskRunner(List<? extends ElasticTaskItem> value) {
        this.value = value;
    }

    @Override
    public void run() {

        String threadName = runnerContext.getTaskCode() + "_" + Thread.currentThread().getName();
        log.info(threadName + " start ");
        if (runnerContext == null) {
            log.info(" runnerContextThreadLocal is null");
            return;
        }
        if (value == null || value.isEmpty()) {
            log.info(threadName + " handle size=0 ");
            return;
        }

        long start = System.currentTimeMillis();
        StringBuilder stringBuilder = new StringBuilder(threadName + " handle size=" + value.size());

        try {
            workStart();
            for (ElasticTaskItem item : value) {
                try {
                    taskExecutorHandler.handle(item, runnerContext.getTaskCode());
                } catch (Exception e) {
                    log.error(threadName + " run error ", e);
                }
            }
        } catch (Exception e) {
            log.error(threadName + " run error ", e);
        } finally {
            workDone();
            long times = (System.currentTimeMillis() - start);
            stringBuilder.append(" costtime ").append(times).append(" ms");
            log.info(stringBuilder.toString());
        }
    }

    public ITaskExecutor getTaskExecutorHandler() {
        return taskExecutorHandler;
    }

    public void setTaskExecutorHandler(ITaskExecutor taskExecutorHandler) {
        this.taskExecutorHandler = taskExecutorHandler;
    }
}
