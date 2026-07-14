package com.jd.tech.stack.study.serverdemo.client.script.api;

import com.jd.tech.stack.study.serverdemo.client.script.dto.ScriptDTO;
import com.jd.tech.stack.study.serverdemo.client.script.dto.ScriptNodeDTO;
import com.jd.tech.stack.study.serverdemo.client.script.param.ScriptGenerateParam;

import java.util.List;

/**
 * Description: 脚本服务接口
 *
 * @author DongBoot
 * @date 2024-11-29
 */
public interface ScriptService {

    /**
     * 生成脚本
     */
    ScriptDTO generateScript(ScriptGenerateParam param);

    /**
     * 获取脚本详情
     */
    ScriptDTO getScript(Long scriptId);

    /**
     * 更新节点状态
     */
    Boolean updateNodeStatus(Long nodeId, Integer status);

    /**
     * 获取脚本节点列表
     */
    List<ScriptNodeDTO> getScriptNodes(Long scriptId);
}