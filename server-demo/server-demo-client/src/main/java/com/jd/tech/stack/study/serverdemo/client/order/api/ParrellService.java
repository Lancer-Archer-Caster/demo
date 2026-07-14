package com.jd.tech.stack.study.serverdemo.client.order.api;

import java.util.concurrent.ExecutionException;

public interface ParrellService {

    String getFromThreadPool(String name) throws ExecutionException, InterruptedException;

    String getFromScheduledThreadPool(String name) throws ExecutionException, InterruptedException;

    String getFromThreadPoolTask(String name) throws ExecutionException, InterruptedException;
}
