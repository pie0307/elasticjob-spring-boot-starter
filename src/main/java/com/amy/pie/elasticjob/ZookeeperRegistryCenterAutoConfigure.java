package com.amy.pie.elasticjob;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;

@Configuration
@ConditionalOnClass(ZookeeperRegistryCenter.class)
@EnableConfigurationProperties(ZookeeperProperties.class)
public class ZookeeperRegistryCenterAutoConfigure {

    @Resource
    private ZookeeperProperties zookeeperProperties;

    @Bean(initMethod = "init", destroyMethod = "close")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = {"zookeeper.serverLists", "zookeeper.namespace"})
    public ZookeeperRegistryCenter zookeeperRegistryCenter() {

        ZookeeperConfiguration zookeeperConfiguration =
                new ZookeeperConfiguration(zookeeperProperties.getServerLists(), zookeeperProperties.getNamespace());

        return new ZookeeperRegistryCenter(zookeeperConfiguration);
    }
}
