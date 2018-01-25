# techplatform-spring-boot-starter
自定义techplatform工具starter

1. 添加maven依赖

    ```
    <dependency>
        <groupId>com.ziroom.bsrd</groupId>
        <artifactId>techplatform-spring-boot-starter</artifactId>
        <version>1.0.0</version>
    </dependency>

    ```

2. 配置文件中加入如下配置即可使用

 ```
techplatform:
  storage:
      url: 对应环境的配置（http://storage.t.ziroom.com）
  mail:
        url: 对应环境的配置（http://message.t.ziroom.com）
        token: 对应环境的配置（DPSK6R4eRem98jyydaSomA）
  sms:
        url: 对应环境的配置（http://message.t.ziroom.com）
        token: 对应环境的配置（YRQ4NqJ5Ra2nYuMGRmcSlQ）

 ```