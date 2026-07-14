package com.jd.tech.stack.study.serverdemo.app.order.api.impl;

import com.jd.tech.stack.study.serverdemo.client.order.api.ParrellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description: DongThread Demo
 *
 * @author DongBoot
 * @see <a href="https://joyspace.jd.com/pages/QC6YAkxyJI9MDudZksjh">DongThread使用说明</a>
 */
@Service
public class ParrellServiceImpl implements ParrellService {

    @Autowired
    @Qualifier("threadPoolExecutor")
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    @Qualifier("scheduledThreadPoolExecutor")
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;


    @Autowired
    @Qualifier("threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private Random random = new Random();

    private String printNo = " from No.";


    /**
     * 使用普通线程池
     *
     * @param name
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     * @see <a href="https://joyspace.jd.com/pages/4tzNx0gR7o5NrrlX91v1">动态调整线程池</a>
     */
    @Override
    public String getFromThreadPool(String name) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor exe = this.threadPoolExecutor;
        return exe.submit(() -> "get " + name + printNo + "1" + " " + exe.getClass().getSimpleName()).get();
    }

    /**
     * 使用调度线程池
     *
     * @param name
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     * @see <a href="https://joyspace.jd.com/pages/4tzNx0gR7o5NrrlX91v1">动态调整线程池</a>
     */
    @Override
    public String getFromScheduledThreadPool(String name) throws ExecutionException, InterruptedException {
        ScheduledThreadPoolExecutor exe = this.scheduledThreadPoolExecutor;
        return exe.schedule(() -> "get " + name + printNo + "2" + " " + exe.getClass().getSimpleName(), 500, TimeUnit.MICROSECONDS).get();
    }

    @Override
    public String getFromThreadPoolTask(String name) throws ExecutionException, InterruptedException {
        ThreadPoolTaskExecutor exe = this.threadPoolTaskExecutor;
        return exe.submit(() -> "get " + name + printNo + "3" + " " + exe.getClass().getSimpleName()).get();
    }
}
