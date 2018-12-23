# elasticjob-spring-boot-starter
自定义elasticjob的starter

## 如何接入
1. 添加maven依赖

    ```
    <dependency>
        <groupId>com.amy.pie</groupId>
        <artifactId>elasticjob-spring-boot-starter</artifactId>
        <version>2.0.0</version>
    </dependency>

    ```

2. 配置文件中加入如下配置即可使用

 ```
zookeeper.serverLists=XXX
zookeeper.namespace=XXX

或者
zookeeper：
  serverLists:XXX
  namespace:XXX
 ```
 ## 如果使用
 1. 创建任务类，继承`BusinessBatchElasticTask` 重写相应方法
 2. 添加注解`@JobConfig`设置相关的信息, 添加 `@Component`