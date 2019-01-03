package com.amy.pie.elasticjob.job.core;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.util.concurrent.CountDownLatch;

@Getter
@Setter
@Slf4j
public abstract class SuperTask implements Runnable {

    private String taskCode;

    private CountDownLatch countDownLatch;

    private StopWatch stopWatch;

    protected void workStart() {
        stopWatch.start(taskCode);
    }

    protected void workDone() {
        this.countDownLatch.countDown();
        this.stopWatch.stop();
        log.info("prettyPrint ---> " + stopWatch.prettyPrint());
    }
}
