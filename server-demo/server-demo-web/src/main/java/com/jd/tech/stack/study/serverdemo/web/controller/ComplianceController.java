package com.jd.tech.stack.study.serverdemo.web.controller;

import com.jd.tech.stack.study.serverdemo.client.compliance.api.ComplianceService;
import com.jd.tech.stack.study.serverdemo.client.compliance.dto.ComplianceResultDTO;
import com.jd.tech.stack.study.serverdemo.client.compliance.param.ComplianceCheckParam;
import com.jd.tech.stack.study.serverdemo.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description: 合规Controller
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Controller
@RequestMapping("/compliance")
public class ComplianceController {

    @Autowired
    private ComplianceService complianceService;

    @RequestMapping("/check")
    @ResponseBody
    public Result<ComplianceResultDTO> checkCompliance(@RequestBody ComplianceCheckParam param) {
        return new Result<>(complianceService.checkCompliance(param));
    }

    @RequestMapping("/words")
    @ResponseBody
    public Result<List<String>> getComplianceWords() {
        return new Result<>(complianceService.getComplianceWords());
    }
}