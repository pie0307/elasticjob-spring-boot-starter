package com.ziroom.bsrd.elasticjob.job.core;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public abstract class SuperTask<T> implements Callable<T> {

    public static final String SuperTaskStatus_SUCCESS = "SUCCESS";

    public static final String SuperTaskStatus_ERROR = "ERROR";

    private CountDownLatch countDownLatch;

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    protected void workStart() {

    }

    protected void workDone() {
        this.countDownLatch.countDown();
    }


}
