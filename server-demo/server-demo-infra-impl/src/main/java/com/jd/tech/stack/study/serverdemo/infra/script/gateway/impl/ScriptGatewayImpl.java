package com.jd.tech.stack.study.serverdemo.infra.script.gateway.impl;

import com.jd.tech.stack.study.serverdemo.infra.script.dataobject.ScriptDO;
import com.jd.tech.stack.study.serverdemo.infra.script.dataobject.ScriptNodeDO;
import com.jd.tech.stack.study.serverdemo.infra.script.gateway.ScriptGateway;
import com.jd.tech.stack.study.serverdemo.infra.script.mapper.ScriptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 脚本网关实现
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Service
public class ScriptGatewayImpl implements ScriptGateway {

    @Autowired
    private ScriptMapper scriptMapper;

    @Override
    public ScriptDO insert(ScriptDO scriptDO) {
        scriptMapper.insert(scriptDO);
        return scriptDO;
    }

    @Override
    public ScriptDO findById(Long id) {
        return scriptMapper.getById(id);
    }

    @Override
    public ScriptDO findBySessionId(Long sessionId) {
        return scriptMapper.getBySessionId(sessionId);
    }

    @Override
    public ScriptDO update(ScriptDO scriptDO) {
        scriptMapper.update(scriptDO);
        return scriptDO;
    }

    @Override
    public ScriptDO generate(Long sessionId, String skuId) {
        ScriptDO scriptDO = new ScriptDO();
        scriptDO.setSessionId(sessionId);
        scriptDO.setSkuId(skuId);
        scriptDO.setTitle("生成的话术脚本");
        scriptDO.setContent("JoyCue 本地模板生成的直播话术");
        scriptDO.setAiGenerated(true);
        scriptDO.setStatus("DRAFT");
        scriptMapper.insert(scriptDO);
        insertLocalNode(scriptDO.getId(), 1, "开场暖场", "家人们好，今天为大家介绍 SKU " + skuId + "。先看核心卖点，再看价格与服务保障。", "开场,商品介绍");
        insertLocalNode(scriptDO.getId(), 2, "核心卖点", "这款商品的关键优势包括性能、续航和使用体验，请结合现场实物逐项演示。", "核心卖点,场景演示");
        insertLocalNode(scriptDO.getId(), 3, "价格说明", "当前演示价来自本地样例库，请以页面展示价格为准，不使用无法验证的绝对化表述。", "价格,合规");
        insertLocalNode(scriptDO.getId(), 4, "收尾引导", "需要的朋友可以查看商品详情，理性选择适合自己的配置，感谢大家观看。", "收尾,互动");
        return scriptDO;
    }

    private void insertLocalNode(Long scriptId, int index, String type, String content, String keywords) {
        ScriptNodeDO node = new ScriptNodeDO();
        node.setScriptId(scriptId);
        node.setNodeIndex(index);
        node.setNodeType(type);
        node.setContent(content);
        node.setKeywords(keywords);
        node.setSortOrder(index);
        node.setStatus("0");
        scriptMapper.insertNode(node);
    }

    @Override
    public Boolean updateNodeStatus(Long nodeId, Integer status) {
        scriptMapper.updateNodeStatus(nodeId, String.valueOf(status));
        return true;
    }

    @Override
    public List<ScriptNodeDO> listNodesByScriptId(Long scriptId) {
        return scriptMapper.getNodesByScriptId(scriptId);
    }
}
