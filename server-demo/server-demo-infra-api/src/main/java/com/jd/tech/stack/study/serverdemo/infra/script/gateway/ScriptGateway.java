package com.jd.tech.stack.study.serverdemo.infra.script.gateway;

import com.jd.tech.stack.study.serverdemo.infra.script.dataobject.ScriptDO;
import com.jd.tech.stack.study.serverdemo.infra.script.dataobject.ScriptNodeDO;

import java.util.List;

/**
 * Description: 脚本网关接口
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public interface ScriptGateway {

    ScriptDO insert(ScriptDO scriptDO);

    ScriptDO findById(Long id);

    ScriptDO findBySessionId(Long sessionId);

    ScriptDO update(ScriptDO scriptDO);

    ScriptDO generate(Long sessionId, String skuId);

    Boolean updateNodeStatus(Long nodeId, Integer status);

    List<ScriptNodeDO> listNodesByScriptId(Long scriptId);
}