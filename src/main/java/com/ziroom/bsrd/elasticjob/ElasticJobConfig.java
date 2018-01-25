package com.ziroom.bsrd.elasticjob;

import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticJobConfig {

    @Autowired
    ZookeeperProperties zookeeperProperties;

    @Bean(initMethod = "init", destroyMethod = "close")
    public CoordinatorRegistryCenter regCenter() {
        ZookeeperConfiguration zookeeperConfiguration =
                new ZookeeperConfiguration(zookeeperProperties.getServerLists(), zookeeperProperties.getNamespace());


        return new ZookeeperRegistryCenter(zookeeperConfiguration);
    }
}
