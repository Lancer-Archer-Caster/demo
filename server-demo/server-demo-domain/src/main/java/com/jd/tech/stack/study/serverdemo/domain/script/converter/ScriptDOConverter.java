package com.jd.tech.stack.study.serverdemo.domain.script.converter;

import com.jd.tech.stack.study.serverdemo.domain.script.bo.ScriptBO;
import com.jd.tech.stack.study.serverdemo.domain.script.bo.ScriptNodeBO;
import com.jd.tech.stack.study.serverdemo.infra.script.dataobject.ScriptDO;
import com.jd.tech.stack.study.serverdemo.infra.script.dataobject.ScriptNodeDO;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 话术DO转换器
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class ScriptDOConverter {

    public static ScriptBO convert(ScriptDO scriptDO) {
        if (scriptDO == null) {
            return null;
        }
        ScriptBO bo = new ScriptBO();
        bo.setId(scriptDO.getId());
        bo.setSessionId(scriptDO.getSessionId());
        bo.setSkuId(scriptDO.getSkuId());
        bo.setContent(scriptDO.getContent());
        bo.setAiGenerated(scriptDO.getAiGenerated());
        // nodes handled separately via ScriptNodeDO
        return bo;
    }

    public static ScriptDO convert(ScriptBO bo) {
        if (bo == null) {
            return null;
        }
        ScriptDO scriptDO = new ScriptDO();
        scriptDO.setId(bo.getId());
        scriptDO.setSessionId(bo.getSessionId());
        scriptDO.setSkuId(bo.getSkuId());
        scriptDO.setContent(bo.getContent());
        scriptDO.setAiGenerated(bo.getAiGenerated());
        // nodes handled separately via ScriptNodeDO
        return scriptDO;
    }

    public static ScriptNodeBO convertNode(ScriptNodeDO nodeDO) {
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

    public static ScriptNodeDO convertNode(ScriptNodeBO bo) {
        if (bo == null) {
            return null;
        }
        ScriptNodeDO nodeDO = new ScriptNodeDO();
        nodeDO.setId(bo.getId());
        nodeDO.setNodeIndex(bo.getNodeIndex());
        nodeDO.setContent(bo.getContent());
        nodeDO.setKeywords(bo.getKeywords());
        nodeDO.setStatus(bo.getStatus() != null ? String.valueOf(bo.getStatus()) : null);
        return nodeDO;
    }

    public static List<ScriptNodeBO> convertNodeList(List<ScriptNodeDO> doList) {
        if (doList == null) {
            return new ArrayList<>();
        }
        List<ScriptNodeBO> boList = new ArrayList<>();
        for (ScriptNodeDO nodeDO : doList) {
            ScriptNodeBO bo = convertNode(nodeDO);
            if (bo != null) {
                boList.add(bo);
            }
        }
        return boList;
    }
}