package com.jd.tech.stack.study.serverdemo.app.script.api.impl;

import com.jd.tech.stack.study.serverdemo.app.script.converter.ScriptBOConverter;
import com.jd.tech.stack.study.serverdemo.client.script.api.ScriptService;
import com.jd.tech.stack.study.serverdemo.client.script.dto.ScriptDTO;
import com.jd.tech.stack.study.serverdemo.client.script.dto.ScriptNodeDTO;
import com.jd.tech.stack.study.serverdemo.client.script.param.ScriptGenerateParam;
import com.jd.tech.stack.study.serverdemo.domain.script.bo.ScriptBO;
import com.jd.tech.stack.study.serverdemo.domain.script.bo.ScriptNodeBO;
import com.jd.tech.stack.study.serverdemo.domain.script.service.ScriptDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 脚本服务实现
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class ScriptServiceImpl implements ScriptService {

    private static final Logger log = LoggerFactory.getLogger(ScriptServiceImpl.class);

    @Autowired
    private ScriptDomainService scriptDomainService;

    @Override
    public ScriptDTO generateScript(ScriptGenerateParam param) {
        log.info("generateScript sessionId={}, skuId={}", param.getSessionId(), param.getSkuId());
        ScriptBO bo = scriptDomainService.generateScript(param.getSessionId(), param.getSkuId());
        return ScriptBOConverter.convert(bo);
    }

    @Override
    public ScriptDTO getScript(Long scriptId) {
        log.info("getScript scriptId={}", scriptId);
        ScriptBO bo = scriptDomainService.getScript(scriptId);
        return ScriptBOConverter.convert(bo);
    }

    @Override
    public Boolean updateNodeStatus(Long nodeId, Integer status) {
        log.info("updateNodeStatus nodeId={}, status={}", nodeId, status);
        return scriptDomainService.updateNodeStatus(nodeId, status);
    }

    @Override
    public List<ScriptNodeDTO> getScriptNodes(Long scriptId) {
        log.info("getScriptNodes scriptId={}", scriptId);
        List<ScriptNodeBO> boList = scriptDomainService.getScriptNodes(scriptId);
        return ScriptBOConverter.convertNodeList(boList);
    }
}