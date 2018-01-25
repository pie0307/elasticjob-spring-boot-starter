package com.ziroom.bsrd.elasticjob.job.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.Future;

/**
 * @author chengys4
 *         2018-01-11 11:26
 **/
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
