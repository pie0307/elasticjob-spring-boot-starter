# elasticjob-spring-boot-starter
自定义elasticjob的starter


1. 添加maven依赖

    ```
    <dependency>
        <groupId>com.ziroom.bsrd</groupId>
        <artifactId>elasticjob-spring-boot-starter</artifactId>
        <version>1.0.0</version>
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