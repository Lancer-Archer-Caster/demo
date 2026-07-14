package com.jd.tech.stack.study.serverdemo.domain.script.service;

import com.jd.tech.stack.study.serverdemo.domain.script.bo.ScriptBO;
import com.jd.tech.stack.study.serverdemo.domain.script.bo.ScriptNodeBO;
import com.jd.tech.stack.study.serverdemo.domain.script.converter.ScriptDOConverter;
import com.jd.tech.stack.study.serverdemo.infra.script.dataobject.ScriptDO;
import com.jd.tech.stack.study.serverdemo.infra.script.dataobject.ScriptNodeDO;
import com.jd.tech.stack.study.serverdemo.infra.script.gateway.ScriptGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 话术领域服务
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class ScriptDomainService {

    private static final Logger log = LoggerFactory.getLogger(ScriptDomainService.class);

    @Autowired
    private ScriptGateway scriptGateway;

    public ScriptBO generateScript(Long sessionId, String skuId) {
        log.info("generateScript sessionId={}, skuId={}", sessionId, skuId);
        ScriptDO scriptDO = scriptGateway.generate(sessionId, skuId);
        ScriptBO scriptBO = ScriptDOConverter.convert(scriptDO);
        scriptBO.setNodes(ScriptDOConverter.convertNodeList(scriptGateway.listNodesByScriptId(scriptDO.getId())));
        return scriptBO;
    }

    public ScriptBO getScript(Long scriptId) {
        log.info("getScript id={}", scriptId);
        ScriptDO scriptDO = scriptGateway.findById(scriptId);
        return ScriptDOConverter.convert(scriptDO);
    }

    public Boolean updateNodeStatus(Long nodeId, Integer status) {
        log.info("updateNodeStatus nodeId={}, status={}", nodeId, status);
        return scriptGateway.updateNodeStatus(nodeId, status);
    }

    public List<ScriptNodeBO> getScriptNodes(Long scriptId) {
        log.info("getScriptNodes scriptId={}", scriptId);
        List<ScriptNodeDO> nodeList = scriptGateway.listNodesByScriptId(scriptId);
        List<ScriptNodeBO> boList = new ArrayList<>();
        if (nodeList != null) {
            for (ScriptNodeDO nodeDO : nodeList) {
                ScriptNodeBO bo = convertNodeDO(nodeDO);
                if (bo != null) {
                    boList.add(bo);
                }
            }
        }
        return boList;
    }

    private ScriptNodeBO convertNodeDO(ScriptNodeDO nodeDO) {
        if (nodeDO == null) {
            return null;
        }
        ScriptNodeBO bo = new ScriptNodeBO();
        bo.setId(nodeDO.getId());
        bo.setNodeIndex(nodeDO.getNodeIndex());
        bo.setContent(nodeDO.getContent());
        bo.setKeywords(nodeDO.getKeywords());
        bo.setStatus(nodeDO.getStatus() != null ? Integer.parseInt(nodeDO.getStatus()) : null);
        return bo;
    }
}
