package com.jd.tech.stack.study.serverdemo.web.controller;

import com.jd.tech.stack.study.serverdemo.client.review.api.ReviewService;
import com.jd.tech.stack.study.serverdemo.client.review.dto.ReviewDTO;
import com.jd.tech.stack.study.serverdemo.client.review.param.ReviewQueryParam;
import com.jd.tech.stack.study.serverdemo.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description: 复盘Controller
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @RequestMapping("/session")
    @ResponseBody
    public Result<ReviewDTO> getSessionReview(@RequestBody ReviewQueryParam param) {
        return new Result<>(reviewService.getSessionReview(param));
    }

    @RequestMapping("/backflow")
    @ResponseBody
    public Result<Boolean> backflowKnowledge(@RequestParam("sessionId") Long sessionId) {
        return new Result<>(reviewService.backflowKnowledge(sessionId));
    }

    @RequestMapping("/export")
    @ResponseBody
    public Result<String> exportReport(@RequestParam("sessionId") Long sessionId) {
        return new Result<>(reviewService.exportReport(sessionId));
    }
}