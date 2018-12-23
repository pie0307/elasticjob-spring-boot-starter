package com.amy.pie.elasticjob.job.annotation;


import com.dangdang.ddframe.job.api.config.impl.JobType;

import java.lang.annotation.*;

/**
 * 任务配置注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JobConfig {
    /**
     * 表达式
     */
    String value();

    /**
     * 任务类型
     */
    JobType jobType() default JobType.SIMPLE;

    /**
     * 任务的名称 默认执行类类名首字母小写
     */
    String jobName() default "";

    /**
     * 任务的编码 默认执行类类名首字母小写
     */
    String jobCode() default "";

    /**
     * 分片数
     */
    int shardingTotalCount() default 2;

    /**
     * 是否覆盖注册中心配置
     */
    boolean overwrite() default false;

    /**
     * 任务描述
     */
    String description() default "";
}
