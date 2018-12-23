package com.amy.pie.elasticjob.job.core;

import com.amy.pie.elasticjob.job.vo.ElasticTaskItem;
import com.amy.pie.elasticjob.job.vo.TaskConfig;
import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.exception.JobException;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量处理数据任务
 */
@Slf4j
public abstract class AbstractBusinessSimpleElasticJob extends AbstractSimpleElasticJob {

    private ThreadLocal<ElasticTaskItem.RunnerContext> runnerContextThreadLocal = new ThreadLocal<>();

    /**
     * 获取当前任务的上下文数据
     */
    public ElasticTaskItem.RunnerContext getRunnerContext() {
        return runnerContextThreadLocal.get();
    }

    /**
     * 任务调度入口  由ElasticJob 触发
     */
    public void process(final JobExecutionMultipleShardingContext shardingContext) {

        jobStart(shardingContext);

        StringBuilder logSB = new StringBuilder();
        boolean isPrintLog = false;
        try {

            logSB.append(getTaskCode() + "_task Start").append("-");

            log.info(getTaskCode() + "_begin");

            TaskConfig taskConfig = getTaskConfig();

            runnerContextThreadLocal.set(new ElasticTaskItem.RunnerContext(getTaskCode(), taskConfig));

            List<ElasticTaskItem> taskItems = getAllProcessData(taskConfig, shardingContext);
            //得到分片项
            List<Integer> shardingItems = shardingContext.getShardingItems();
            if (shardingItems == null || shardingItems.isEmpty()) {
                logSB.append("No sharding items");
                log.info(logSB.toString());//警告，该服务器未分配到分片项
                return;
            }

            //处理分片项和实际数据的对应关系
            List<ElasticTaskItem> realToBeExecutedIdList = new ArrayList<>();
            int shardingTotalCount = shardingContext.getShardingTotalCount();//分片总数
            //not find data
            if (taskItems == null || taskItems.size() == 0) {
                log.info("not find data");
                return;
            }

            for (int i = 0; i < taskItems.size(); i++) {
                if (taskItems.get(i).getTaskId() == 0) {
                    throw new Exception(" taskId is not allow 0 ");
                }
                int shardingItem = (int) taskItems.get(i).getTaskId() % shardingTotalCount;
                if (shardingItems.contains(shardingItem)) {
                    realToBeExecutedIdList.add(taskItems.get(i));
                }
            }

            logSB.append("Execute job count:" + realToBeExecutedIdList.size()).append("|");
            if (realToBeExecutedIdList.size() > 0) {
                isPrintLog = true;
            }
            List<ElasticTaskItem> taskItems_ = Lists.newArrayList();
            for (ElasticTaskItem taskItem : realToBeExecutedIdList) {
                if (!isProcessAll()) {
                    this.processOne(taskItem);
                } else {
                    taskItems_.add(taskItem);
                }
            }

            if (isProcessAll()) {
                processAll(taskConfig, taskItems_);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (isPrintLog) {
                log.info(logSB.toString());
            }
            log.info(getTaskCode() + "_end");
            jobEnd(shardingContext);
        }

    }

    private void jobStart(JobExecutionMultipleShardingContext shardingContext) {
    }

    protected void jobEnd(JobExecutionMultipleShardingContext shardingContext) {

    }

    /**
     * 返回任务的线程配置，处理数据量信息
     */
    protected abstract TaskConfig getTaskConfig();

    /**
     * 异常处理
     */
    @Override
    public void handleJobExecutionException(final JobException jobException) {
        log.error("[" + getTaskCode() + "] execute error", jobException);
    }

    /**
     * 处理一个任务
     */
    protected abstract boolean processOne(ElasticTaskItem taskItem);

    /**
     * 是否批量处理
     */
    protected boolean isProcessAll() {
        return false;
    }

    /**
     * 批量批量任务
     */
    protected void processAll(TaskConfig taskConfig, List<ElasticTaskItem> taskItems) {
        throw new RuntimeException(" imp processAll method in subclass and imp isProcessAll method ");
    }

    /**
     * 任务名称
     */
    protected abstract String getTaskCode();

    /**
     * 获取符合条件的惹怒
     */
    protected abstract List<ElasticTaskItem> getAllProcessData(TaskConfig taskConfig, JobExecutionMultipleShardingContext shardingContext) throws Exception;
}
