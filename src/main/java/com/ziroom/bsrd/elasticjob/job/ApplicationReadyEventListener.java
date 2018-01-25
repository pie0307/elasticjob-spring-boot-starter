package com.ziroom.bsrd.elasticjob.job;

import com.dangdang.ddframe.job.api.JobScheduler;
import com.dangdang.ddframe.job.api.config.JobConfigurationFactory;
import com.dangdang.ddframe.job.api.config.impl.SimpleJobConfiguration;
import com.dangdang.ddframe.reg.zookeeper.ZookeeperRegistryCenter;
import com.ziroom.bsrd.elasticjob.job.annotation.JobConfig;
import com.ziroom.bsrd.elasticjob.job.vo.JobInfo;
import com.ziroom.bsrd.log.ApplicationLogger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Spring Boot 上下文创建完成后执行的事件监听器
 *
 * @author chengys4
 *         2017-08-21 18:47
 **/
@Component
public class ApplicationReadyEventListener implements ApplicationListener<ContextRefreshedEvent> {

    private ApplicationContext applicationContext;

    @Resource
    private ZookeeperRegistryCenter zookeeperRegistryCenter;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationLogger.info("ApplicationStartedEventListener is starting");

        // 获取ApplicationContext
        setApplicationContext(contextRefreshedEvent.getApplicationContext());

        initJob();

        ApplicationLogger.info("ApplicationStartedEventListener is started");
    }

    private void initJob() {
        // 获取Zookeeper注册中心

        List<JobInfo> data = getJobConfig();

        // 启动作业
        for (JobInfo jobInfo : data) {
            SimpleJobConfiguration simpleJobConfiguration = JobConfigurationFactory.createSimpleJobConfigurationBuilder(jobInfo.getJobName(),
                    jobInfo.getJobClass(), jobInfo.getShardingTotalCount(), jobInfo.getCron()).overwrite(jobInfo.isOverwrite()).description(jobInfo.getDescription()).failover(true).build();
            new JobScheduler(zookeeperRegistryCenter, simpleJobConfiguration).init();
        }

    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private List<JobInfo> getJobConfig() throws BeansException {
        List<JobInfo> jobInfos = new LinkedList();
        Map<String, Object> beanDefinitionNames = getApplicationContext().getBeansWithAnnotation(JobConfig.class);
        Iterator<Map.Entry<String, Object>> set = beanDefinitionNames.entrySet().iterator();
        while (set.hasNext()) {
            JobInfo jobInfo = new JobInfo();
            Map.Entry<String, Object> objectEntry = set.next();
            JobConfig jobConfig = objectEntry.getValue().getClass().getAnnotation(JobConfig.class);
            if (jobConfig.jobName() == null || jobConfig.jobName().trim().length() == 0) {
                jobInfo.setJobName(objectEntry.getValue().getClass().getSimpleName());
            } else {
                jobInfo.setJobName(jobConfig.jobName());
            }
            jobInfo.setCron(jobConfig.value());
            jobInfo.setJobClass(objectEntry.getValue().getClass());
            jobInfo.setJobType(jobConfig.jobType());
            jobInfo.setShardingTotalCount(jobConfig.shardingTotalCount());
            jobInfo.setOverwrite(jobConfig.overwrite());
            jobInfo.setDescription(jobConfig.description());
            jobInfos.add(jobInfo);
        }
        return jobInfos;
    }
}
