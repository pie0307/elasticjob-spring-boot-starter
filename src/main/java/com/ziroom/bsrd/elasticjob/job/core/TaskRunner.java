package com.ziroom.bsrd.elasticjob.job.core;


import com.ziroom.bsrd.elasticjob.job.itf.ITaskExecutor;
import com.ziroom.bsrd.elasticjob.job.vo.ElasticTaskItem;
import com.ziroom.bsrd.log.ApplicationLogger;

import java.util.List;

/**
 * 任务线程执行器
 */
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
    public Object call() {

        String threadName = runnerContext.getTaskCode() + "_" + Thread.currentThread().getName();
        ApplicationLogger.info(threadName + " start ");
        if (runnerContext == null) {
            ApplicationLogger.info_api(" runnerContextThreadLocal is null");
            return SuperTaskStatus_ERROR;
        }
        if (value == null || value.isEmpty()) {
            ApplicationLogger.info(threadName + " handle size=0 ");
            return SuperTaskStatus_SUCCESS;
        }

        long start = System.currentTimeMillis();
        StringBuilder stringBuilder = new StringBuilder(threadName + " handle size=" + value.size());

        try {
            workStart();
            for (ElasticTaskItem item : value) {
                try {
                    taskExecutorHandler.handle(item, runnerContext.getTaskCode());
                } catch (Exception e) {
                    ApplicationLogger.error(threadName + " run error ", e);
                }
            }
            return SuperTaskStatus_SUCCESS;
        } catch (Exception e) {
            ApplicationLogger.error(threadName + " run error ", e);
            return SuperTaskStatus_ERROR;
        } finally {
            workDone();
            long times = (System.currentTimeMillis() - start);
            stringBuilder.append(" costtime ").append(times).append(" ms");
            ApplicationLogger.info(stringBuilder.toString());
        }
    }

    public ITaskExecutor getTaskExecutorHandler() {
        return taskExecutorHandler;
    }

    public void setTaskExecutorHandler(ITaskExecutor taskExecutorHandler) {
        this.taskExecutorHandler = taskExecutorHandler;
    }
}
