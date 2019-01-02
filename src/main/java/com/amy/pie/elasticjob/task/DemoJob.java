package com.amy.pie.elasticjob.task;

import com.amy.pie.elasticjob.job.annotation.JobConfig;
import com.amy.pie.elasticjob.job.core.AbstractBusinessSimpleElasticJob;
import com.amy.pie.elasticjob.job.vo.ElasticTaskItem;
import com.amy.pie.elasticjob.job.vo.TaskConfig;
import com.dangdang.ddframe.job.api.ShardingContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author : Administrator.
 * @Description :
 * @Date : Created in 2019/1/2 22:36
 * @Modified By :
 */
@Component
@JobConfig(value = "0 */1 * * * ?", shardingTotalCount = 2, jobName = "dailyTestPushJob")
public class DemoJob extends AbstractBusinessSimpleElasticJob {
    @Override
    protected TaskConfig getTaskConfig() {
        return null;
    }

    @Override
    protected boolean processOne(ElasticTaskItem taskItem) {
        return false;
    }

    @Override
    protected String getTaskCode() {
        return null;
    }

    @Override
    protected List<ElasticTaskItem> getAllProcessData(TaskConfig taskConfig, ShardingContext shardingContext) throws Exception {
        return null;
    }
}
