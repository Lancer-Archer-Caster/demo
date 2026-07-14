package com.jd.tech.stack.study.serverdemo.web.controller;

import com.jd.tech.stack.study.serverdemo.client.price.api.PriceCompareService;
import com.jd.tech.stack.study.serverdemo.client.price.dto.PriceCompareDTO;
import com.jd.tech.stack.study.serverdemo.client.price.dto.CompetitorPrice;
import com.jd.tech.stack.study.serverdemo.client.price.param.PriceCompareParam;
import com.jd.tech.stack.study.serverdemo.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description: 价格对比Controller
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Controller
@RequestMapping("/price")
public class PriceCompareController {

    @Autowired
    private PriceCompareService priceCompareService;

    @RequestMapping("/compare")
    @ResponseBody
    public Result<PriceCompareDTO> comparePrice(@RequestBody PriceCompareParam param) {
        return new Result<>(priceCompareService.comparePrice(param));
    }

    @RequestMapping("/competitors")
    @ResponseBody
    public Result<List<CompetitorPrice>> getCompetitorPrices(@RequestParam("skuId") String skuId) {
        return new Result<>(priceCompareService.getCompetitorPrices(skuId));
    }

    @RequestMapping("/talkpoint")
    @ResponseBody
    public Result<String> generateTalkpoint(@RequestParam("skuId") String skuId) {
        return new Result<>(priceCompareService.generateTalkpoint(skuId));
    }
}