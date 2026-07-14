package com.jd.tech.stack.study.serverdemo.web.controller;

import com.jd.tech.stack.study.serverdemo.client.sku.api.SkuService;
import com.jd.tech.stack.study.serverdemo.client.sku.dto.SkuDTO;
import com.jd.tech.stack.study.serverdemo.client.sku.dto.SkuCompareDTO;
import com.jd.tech.stack.study.serverdemo.client.sku.param.SkuQueryParam;
import com.jd.tech.stack.study.serverdemo.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description: 商品Controller
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Controller
@RequestMapping("/sku")
public class SkuController {

    @Autowired
    private SkuService skuService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<SkuDTO> querySkuDetail(@RequestBody SkuQueryParam param) {
        return new Result<>(skuService.querySkuDetail(param));
    }

    @RequestMapping("/batch")
    @ResponseBody
    public Result<List<SkuDTO>> batchQuerySkus(@RequestBody List<String> skuIds) {
        return new Result<>(skuService.batchQuerySkus(skuIds));
    }

    @RequestMapping("/compare")
    @ResponseBody
    public Result<SkuCompareDTO> compareSkuParams(@RequestBody SkuQueryParam param) {
        return new Result<>(skuService.compareSkuParams(param));
    }
}