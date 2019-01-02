package com.amy.pie.elasticjob.job.core;

import com.amy.pie.elasticjob.job.vo.ElasticTaskItem;
import com.amy.pie.elasticjob.job.vo.TaskConfig;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 批量处理数据任务
 */
@Slf4j
public abstract class AbstractBusinessSimpleElasticJob implements SimpleJob {

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
    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("分片项000000 -----   " + shardingContext.getShardingItem() + "-----" + Thread.currentThread().getName());
        if (!jobStart(shardingContext)) {
            return;
        }

        try {

            log.info(getTaskCode() + "_begin");

            TaskConfig taskConfig = getTaskConfig();

            runnerContextThreadLocal.set(new ElasticTaskItem.RunnerContext(getTaskCode(), taskConfig));

            List<ElasticTaskItem> taskItems = getAllProcessData(taskConfig, shardingContext);

            //not find data
            if (taskItems == null || taskItems.size() == 0) {
                log.info("not find data");
                return;
            }

            //处理分片项和实际数据的对应关系
            List<ElasticTaskItem> realToBeExecutedIdList = Lists.newArrayList();

            for (int i = 0; i < taskItems.size(); i++) {

                if (taskItems.get(i).getTaskId() == 0) {
                    throw new Exception(" taskId is not allow 0 ");
                }

                Long shardingItem = taskItems.get(i).getTaskId() % shardingContext.getShardingTotalCount();
                if (shardingContext.getShardingItem() == shardingItem) {
                    realToBeExecutedIdList.add(taskItems.get(i));
                }
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
            log.info(getTaskCode() + "_end");
            jobEnd(shardingContext);
        }
    }

    private boolean jobStart(ShardingContext shardingContext) {
        return true;
    }

    protected void jobEnd(ShardingContext shardingContext) {

    }

    /**
     * 返回任务的线程配置，处理数据量信息
     */
    protected abstract TaskConfig getTaskConfig();

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
    protected abstract List<ElasticTaskItem> getAllProcessData(TaskConfig taskConfig, ShardingContext shardingContext) throws Exception;
}
