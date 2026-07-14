package com.jd.tech.stack.study.serverdemo.client;

import com.jd.tech.stack.study.serverdemo.client.order.api.ParrellService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.concurrent.ExecutionException;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ParrellServiceTest {

    @Autowired
    private ParrellService parrellService;

    @Test
    public void testParrellService() throws ExecutionException, InterruptedException {
        String result = parrellService.getFromThreadPool("demo");
        assertEquals("get demo from No.1 ThreadPoolExecutor", result);
    }

    @Test
    public void testParrellThreadPoolTaskExecutorBean() throws ExecutionException, InterruptedException {
        String result = parrellService.getFromThreadPoolTask("demo");
        assertEquals("get demo from No.3 ThreadPoolTaskExecutor", result);
    }

    @Test
    public void testParrellThreadPoolExecutor() throws ExecutionException, InterruptedException {
        String result = parrellService.getFromScheduledThreadPool("demo");
        assertEquals("get demo from No.2 ScheduledThreadPoolExecutor", result);
    }
}
