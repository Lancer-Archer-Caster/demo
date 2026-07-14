package com.jd.tech.stack.study.serverdemo.web.controller;

import com.jd.tech.stack.study.serverdemo.client.script.api.ScriptService;
import com.jd.tech.stack.study.serverdemo.client.script.dto.ScriptDTO;
import com.jd.tech.stack.study.serverdemo.client.script.dto.ScriptNodeDTO;
import com.jd.tech.stack.study.serverdemo.client.script.param.ScriptGenerateParam;
import com.jd.tech.stack.study.serverdemo.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description: 话术Controller
 *
 * @author DongBoot
 * @date 2024-11-29
 */
@Controller
@RequestMapping("/script")
public class ScriptController {

    @Autowired
    private ScriptService scriptService;

    @RequestMapping("/generate")
    @ResponseBody
    public Result<ScriptDTO> generateScript(@RequestBody ScriptGenerateParam param) {
        return new Result<>(scriptService.generateScript(param));
    }

    @RequestMapping("/get")
    @ResponseBody
    public Result<ScriptDTO> getScript(@RequestParam("scriptId") Long scriptId) {
        return new Result<>(scriptService.getScript(scriptId));
    }

    @RequestMapping("/updateNodeStatus")
    @ResponseBody
    public Result<Boolean> updateNodeStatus(@RequestParam("nodeId") Long nodeId,
                                             @RequestParam("status") Integer status) {
        return new Result<>(scriptService.updateNodeStatus(nodeId, status));
    }

    @RequestMapping("/nodes")
    @ResponseBody
    public Result<List<ScriptNodeDTO>> getScriptNodes(@RequestParam("scriptId") Long scriptId) {
        return new Result<>(scriptService.getScriptNodes(scriptId));
    }
}