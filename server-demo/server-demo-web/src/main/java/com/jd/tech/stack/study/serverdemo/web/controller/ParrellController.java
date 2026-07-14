package com.jd.tech.stack.study.serverdemo.web.controller;


import com.jd.tech.stack.study.serverdemo.client.order.api.ParrellService;
import com.jd.tech.stack.study.serverdemo.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.concurrent.ExecutionException;

/**
 * Description: DongThread Demo
 *
 * @author DongBoot
 * @see <a href="https://joyspace.jd.com/pages/QC6YAkxyJI9MDudZksjh">DongThread使用说明</a>
 */
@Controller
@RequestMapping("/parrell")
public class ParrellController {

    @Autowired
    private ParrellService parrellService;

    @RequestMapping("/get")
    @ResponseBody
    public Result<String> getFromScheduledThreadPool(@RequestParam(name = "text", defaultValue = "demo") String text) throws ExecutionException, InterruptedException {
        return new Result<>(parrellService.getFromThreadPool(text) + ", " + parrellService.getFromScheduledThreadPool(text) + ", " +
                parrellService.getFromThreadPoolTask(text));
    }

}
