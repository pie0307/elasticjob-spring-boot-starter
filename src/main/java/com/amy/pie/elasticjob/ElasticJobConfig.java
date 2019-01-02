package com.amy.pie.elasticjob;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class ElasticJobConfig {

    @Resource
    private ZookeeperProperties zookeeperProperties;

    @Bean(initMethod = "init", destroyMethod = "close")
    public CoordinatorRegistryCenter regCenter() {

        ZookeeperConfiguration zookeeperConfiguration =
                new ZookeeperConfiguration(zookeeperProperties.getServerLists(), zookeeperProperties.getNamespace());

        return new ZookeeperRegistryCenter(zookeeperConfiguration);
    }
}
