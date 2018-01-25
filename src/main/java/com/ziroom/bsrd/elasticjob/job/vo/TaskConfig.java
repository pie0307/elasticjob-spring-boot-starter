package com.ziroom.bsrd.elasticjob.job.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * 任务线程配置
 */
@Getter
@Setter
public class TaskConfig implements Serializable {
    private int threadNum = 5;
    private int datasize = 500;
    private Map<String, String> data;

    @Override
    public String toString() {
        return "TaskConfig{" +
                "threadNum=" + threadNum +
                ", datasize=" + datasize +
                ", data=" + data +
                '}';
    }
}
