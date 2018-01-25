package com.ziroom.bsrd.elasticjob;

import com.dangdang.ddframe.context.JobApplicationContext;
import com.dangdang.ddframe.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chengys4
 *         2018-01-24 18:25
 **/
@Configuration
@ConditionalOnClass(ZookeeperRegistryCenter.class)
@EnableConfigurationProperties(ZookeeperProperties.class)
public class ZookeeperRegistryCenterAutoConfigure {

    @Autowired
    ZookeeperProperties zookeeperProperties;

    @Bean(initMethod = "init", destroyMethod = "close")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = {"zookeeper.serverLists", "zookeeper.namespace"})
    public ZookeeperRegistryCenter zookeeperRegistryCenter() {
        ZookeeperConfiguration zookeeperConfiguration =
                new ZookeeperConfiguration(zookeeperProperties.getServerLists(), zookeeperProperties.getNamespace());
        return new ZookeeperRegistryCenter(zookeeperConfiguration);
    }

    @Bean
    @ConditionalOnMissingBean
    public JobApplicationContext jobApplicationContext() {
        return new JobApplicationContext();
    }
}
