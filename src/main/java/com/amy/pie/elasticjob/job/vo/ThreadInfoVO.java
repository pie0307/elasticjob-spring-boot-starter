package com.amy.pie.elasticjob.job.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.Future;

@Getter
@Setter
@NoArgsConstructor
public class ThreadInfoVO {
    private String threadName;
    private Future<?> future;
    private long startTime;

    public ThreadInfoVO(String threadName) {
        this.threadName = threadName;
    }
}
