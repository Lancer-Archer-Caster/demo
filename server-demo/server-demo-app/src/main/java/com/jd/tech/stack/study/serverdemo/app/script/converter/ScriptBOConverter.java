package com.jd.tech.stack.study.serverdemo.app.script.converter;

import com.jd.tech.stack.study.serverdemo.client.script.dto.ScriptDTO;
import com.jd.tech.stack.study.serverdemo.client.script.dto.ScriptNodeDTO;
import com.jd.tech.stack.study.serverdemo.domain.script.bo.ScriptBO;
import com.jd.tech.stack.study.serverdemo.domain.script.bo.ScriptNodeBO;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 脚本BO转换器
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public class ScriptBOConverter {

    public static ScriptDTO convert(ScriptBO bo) {
        if (bo == null) {
            return null;
        }
        ScriptDTO dto = new ScriptDTO();
        dto.setId(bo.getId());
        dto.setSessionId(bo.getSessionId());
        dto.setSkuId(bo.getSkuId());
        dto.setContent(bo.getContent());
        dto.setAiGenerated(bo.getAiGenerated());
        dto.setNodes(convertNodeList(bo.getNodes()));
        return dto;
    }

    public static ScriptNodeDTO convertNode(ScriptNodeBO bo) {
        if (bo == null) {
            return null;
        }
        ScriptNodeDTO dto = new ScriptNodeDTO();
        dto.setId(bo.getId());
        dto.setNodeIndex(bo.getNodeIndex());
        dto.setContent(bo.getContent());
        dto.setKeywords(bo.getKeywords());
        dto.setStatus(bo.getStatus());
        return dto;
    }

    public static List<ScriptNodeDTO> convertNodeList(List<ScriptNodeBO> boList) {
        if (boList == null) {
            return null;
        }
        List<ScriptNodeDTO> dtoList = new ArrayList<>();
        for (ScriptNodeBO bo : boList) {
            dtoList.add(convertNode(bo));
        }
        return dtoList;
    }
}