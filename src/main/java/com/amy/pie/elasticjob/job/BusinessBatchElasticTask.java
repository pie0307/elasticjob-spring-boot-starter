package com.amy.pie.elasticjob.job;

import com.amy.pie.elasticjob.job.core.TaskRunner;
import com.amy.pie.elasticjob.job.itf.ITaskExecutor;
import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.amy.pie.elasticjob.job.core.AbstractBusinessSimpleElasticJob;
import com.amy.pie.elasticjob.job.vo.ElasticTaskItem;
import com.amy.pie.elasticjob.job.vo.TaskConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 批量处理任务
 */
@Slf4j
public abstract class BusinessBatchElasticTask extends AbstractBusinessSimpleElasticJob {

    /**
     * 处理单个任务
     */
    @Override
    protected abstract boolean processOne(ElasticTaskItem taskItem);

    protected TaskConfig getTaskConfig() {
        TaskConfig taskConfig = new TaskConfig();
        return taskConfig;
    }

    /**
     * 返回任务的编码
     */
    @Override
    protected abstract String getTaskCode();

    /**
     * 是否批量处理 默认单个处理
     */
    @Override
    protected boolean isProcessAll() {
        return false;
    }

    /**
     * 批量批量任务
     */
    @Override
    protected void processAll(TaskConfig taskConfig, List<ElasticTaskItem> taskItems) {
        long start = System.currentTimeMillis();
        CountDownLatch latch = null;
        log.info(getTaskCode() + " handle : " + taskItems.size());
        try {
            Map<Integer, List<ElasticTaskItem>> data = parseData(taskConfig.getThreadNum(), taskItems);
            latch = new CountDownLatch(data.size());
            Iterator<Map.Entry<Integer, List<ElasticTaskItem>>> alltask = data.entrySet().iterator();
            while (alltask.hasNext()) {
                Map.Entry<Integer, List<ElasticTaskItem>> item = alltask.next();
                TaskRunner taskRunner = new TaskRunner(getRunnerContext(), item.getValue(), getTaskExecutorHandler());
                taskRunner.setCountDownLatch(latch);
                Future<?> future = getExecutorService().submit(taskRunner);
            }
        } catch (Exception e) {
            log.error("bath handle fail：", e);
        } finally {
            try {
                latch.await(1, TimeUnit.HOURS);
            } catch (InterruptedException e) {
                log.error("CountDownLatch await error ", e);
            }
            log.info(getTaskCode() + " costtime " + (System.currentTimeMillis() - start) / 1000 + " s");
        }
    }

    /**
     * 获取需要处理任务的数据，数据需要包装成<code>ElasticTaskItem</code>
     */
    @Override
    protected abstract List<ElasticTaskItem> getAllProcessData(TaskConfig taskConfig, JobExecutionMultipleShardingContext shardingContext) throws Exception;

    /**
     * 多线程处理时，单个任务的处理器<br/>
     * 任务放在处理器中执行 批量处理时需要重写
     */
    public ITaskExecutor getTaskExecutorHandler() {
        if (isProcessAll()) {
            throw new RuntimeException(" isProcessAll is true and imp getTaskExecutorHandler method ");
        }
        return null;
    }


    /**
     * 批量处理时需要重写<br/>
     * 多线程处理时返回线程池
     */
    public ThreadPoolTaskExecutor getExecutorService() {
        if (isProcessAll()) {
            throw new RuntimeException(" isProcessAll is true and imp getExecutorService method ");
        }
        return null;
    }

    /**
     * 把要处理的数据添加的分组
     */
    private void addToMap(Map<Integer, List<ElasticTaskItem>> map, int index, ElasticTaskItem item) {
        List<ElasticTaskItem> data = map.get(Integer.valueOf(index));
        if (data == null) {
            data = new ArrayList<ElasticTaskItem>();
        }
        data.add(item);
        map.put(Integer.valueOf(index), data);
    }

    /**
     * 任务分发
     */
    private Map<Integer, List<ElasticTaskItem>> parseData(int threadnum, List<ElasticTaskItem> handledata) {
        Map<Integer, List<ElasticTaskItem>> map = new HashMap<Integer, List<ElasticTaskItem>>();
        for (int i = 0; i < handledata.size(); i++) {
            for (int index = 0; index < threadnum; index++) {
                if (i % threadnum == index) {
                    addToMap(map, index, handledata.get(i));
                }
            }
        }
        return map;
    }
}
