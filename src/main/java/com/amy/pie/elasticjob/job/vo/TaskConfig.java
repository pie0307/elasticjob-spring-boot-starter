package com.amy.pie.elasticjob.job.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * 任务线程配置
 */
@Getter
@Setter
@ToString
public class TaskConfig implements Serializable {
    private int threadNum = 5;
    private int datasize = 500;
    private Map<String, String> data;
}
