package com.ziroom.bsrd.elasticjob.job;

import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.ziroom.bsrd.elasticjob.job.core.AbstractBusinessSimpleElasticJob;
import com.ziroom.bsrd.elasticjob.job.core.TaskRunner;
import com.ziroom.bsrd.elasticjob.job.itf.ITaskExecutor;
import com.ziroom.bsrd.elasticjob.job.vo.ElasticTaskItem;
import com.ziroom.bsrd.elasticjob.job.vo.TaskConfig;
import com.ziroom.bsrd.log.ApplicationLogger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 批量处理任务
 */
public abstract class BusinessBatchElasticTask extends AbstractBusinessSimpleElasticJob {


    @Override
    protected abstract boolean processOne(ElasticTaskItem taskItem);

    protected TaskConfig getTaskConfig() {
        TaskConfig taskConfig = new TaskConfig();
        return taskConfig;
    }

    @Override
    protected abstract String getTaskCode();

    /**
     * 是否批量处理
     *
     * @return
     */
    @Override
    protected boolean isProcessAll() {
        return false;
    }

    /**
     * 批量批量任务
     *
     * @param taskConfig
     * @param taskItems
     */
    @Override
    protected void processAll(TaskConfig taskConfig, List<ElasticTaskItem> taskItems) {
        long start = System.currentTimeMillis();
        CountDownLatch latch = null;
        ApplicationLogger.info_api(getTaskCode() + " handle : " + taskItems.size());
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
            ApplicationLogger.error("bath handle fail：", e);
        } finally {
            try {
                latch.await(1, TimeUnit.HOURS);
            } catch (InterruptedException e) {
                ApplicationLogger.error("CountDownLatch await error ", e);
            }
            ApplicationLogger.info_api(getTaskCode() + " costtime " + (System.currentTimeMillis() - start) / 1000 + " s");
        }
    }

    @Override
    protected abstract List<ElasticTaskItem> getAllProcessData(TaskConfig taskConfig, JobExecutionMultipleShardingContext shardingContext) throws Exception;

    /**
     * 获取单个任务的执行器
     *
     * @return
     */
    public ITaskExecutor getTaskExecutorHandler() {
        if (isProcessAll()) {
            throw new RuntimeException(" isProcessAll is true and imp getTaskExecutorHandler method ");
        }
        return null;
    }


    public ThreadPoolTaskExecutor getExecutorService() {
        if (isProcessAll()) {
            throw new RuntimeException(" isProcessAll is true and imp getExecutorService method ");
        }
        return null;
    }

    /**
     * 把要处理的数据添加的分组
     *
     * @param map
     * @param index
     * @param item
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
     * 任务分发 TODO 分组策略需要抽出来 场景 用户会多次看直播  要求同一个用户的记录由同一个线程处理 。
     *
     * @param threadnum
     * @param handledata
     * @return
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
