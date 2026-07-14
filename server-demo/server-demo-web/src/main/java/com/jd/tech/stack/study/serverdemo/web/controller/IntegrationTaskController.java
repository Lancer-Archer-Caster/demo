package com.jd.tech.stack.study.serverdemo.web.controller;

import com.jd.tech.stack.study.serverdemo.web.vo.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Stable facade for long-running external work (model inference and data import).
 *
 * The controller deliberately exposes a task contract instead of making a page
 * wait for a model or data provider.  The current implementation completes a
 * demo task immediately.  When real systems are available, replace only the
 * two create methods with a message-queue/HTTP adapter and update the task from
 * its callback; the frontend contract stays unchanged.
 */
@RestController
@RequestMapping("/api/v1")
public class IntegrationTaskController {

    private final ConcurrentMap<String, Map<String, Object>> tasks = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Map<String, Object>> replays = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Map<String, Object>> livePreparations = new ConcurrentHashMap<>();

    public IntegrationTaskController() {
        addReplay("replay-001", "双十一特惠家电数码大促专场", "2026-07-12 19:00 - 23:00", "04:00:00", "12.5w", "15.3%", 345000, "ready");
        addReplay("replay-002", "秋季美妆护肤新品发布会", "2026-07-10 20:00 - 22:00", "02:00:00", "8.2w", "12.1%", 128500, "done");
        addReplay("replay-003", "夏末秋初男女装清仓捡漏", "2026-07-08 18:00 - 22:00", "04:00:00", "6.7w", "9.8%", 98600, "failed");
        addReplay("replay-004", "电竞外设全网底价狂欢", "2026-07-06 18:00 - 24:00", "06:00:00", "10.1w", "14.6%", 216800, "processing");
    }

    @RequestMapping(value = "/tasks/script", method = RequestMethod.POST)
    public Result<Map<String, Object>> submitScriptTask(@RequestBody Map<String, Object> payload) {
        String taskId = createTask("SCRIPT_GENERATION", payload);
        return new Result<>(tasks.get(taskId));
    }

    @RequestMapping(value = "/sku/import", method = RequestMethod.POST)
    public Result<Map<String, Object>> importSkuFile(@RequestParam("file") MultipartFile file,
                                                       @RequestParam(value = "sessionId", required = false) Long sessionId) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sessionId", sessionId);
        payload.put("fileName", file.getOriginalFilename());
        payload.put("fileSize", file.getSize());
        String taskId = createTask("SKU_IMPORT", payload);
        return new Result<>(tasks.get(taskId));
    }

    @RequestMapping(value = "/tasks/{taskId}", method = RequestMethod.GET)
    public Result<Map<String, Object>> getTask(@PathVariable("taskId") String taskId) {
        Map<String, Object> task = tasks.get(taskId);
        if (task == null) {
            return new Result<>(404, null, "任务不存在或已过期");
        }
        return new Result<>(task);
    }

    @RequestMapping(value = "/live/metrics", method = RequestMethod.GET)
    public Result<Map<String, Object>> getLiveMetrics(@RequestParam("sessionId") Long sessionId) {
        Map<String, Object> metrics = new LinkedHashMap<>();
        metrics.put("sessionId", sessionId);
        metrics.put("onlineUsers", 12853);
        metrics.put("orderRate", 23);
        metrics.put("totalRevenue", 3456000);
        metrics.put("viewersTrend", "rising");
        metrics.put("source", "local-demo");
        return new Result<>(metrics);
    }

    /**
     * 数据中心聚合契约。当前返回 Fixture；真实环境只需将本方法的数据来源
     * 替换为交易、流量、库存、售后和诊断 Adapter，前端结构不变。
     */
    @RequestMapping(value = "/data-center/overview", method = RequestMethod.GET)
    public Result<Map<String, Object>> getDataCenterOverview(@RequestParam("sessionId") Long sessionId,
                                                              @RequestParam(value = "date", required = false) String date) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("sessionId", sessionId);
        result.put("date", date);
        result.put("dataMode", "FIXTURE_ADAPTER");
        Map<String, Object> metrics = new LinkedHashMap<>();
        metrics.put("gmv", 12850000);
        metrics.put("orders", 320);
        metrics.put("avgOrderValue", 40150);
        metrics.put("pv", 150000);
        metrics.put("uv", 32000);
        metrics.put("conversionRate", 2.1);
        result.put("metrics", metrics);
        result.put("funnel", Arrays.asList(
                funnel("enter", 32000, 100), funnel("click", 12160, 38),
                funnel("cart", 5760, 18), funnel("pay", 2560, 8)));
        result.put("diagnosis", Arrays.asList("手机核心卖点段贡献最高", "进入到点击环节流失最大", "建议强化可信参数与权益说明"));
        return new Result<>(result);
    }

    @RequestMapping(value = "/replays", method = RequestMethod.GET)
    public Result<List<Map<String, Object>>> listReplays() {
        return new Result<>(new ArrayList<>(replays.values()));
    }

    @RequestMapping(value = "/replays/{replayId}/clip", method = RequestMethod.POST)
    public Result<Map<String, Object>> startReplayClip(@PathVariable("replayId") String replayId) {
        Map<String, Object> replay = replays.get(replayId);
        if (replay == null) {
            return new Result<>(404, null, "回放不存在");
        }
        replay.put("clipStatus", "processing");
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("replayId", replayId);
        String taskId = createTask("JOYCLIP", payload);
        Map<String, Object> response = new LinkedHashMap<>(replay);
        response.put("taskId", taskId);
        response.put("algorithmAdapter", "PENDING_EXTERNAL_PROVIDER");
        return new Result<>(response);
    }

    @RequestMapping(value = "/replays/{replayId}", method = RequestMethod.DELETE)
    public Result<Boolean> deleteReplay(@PathVariable("replayId") String replayId) {
        return new Result<>(replays.remove(replayId) != null);
    }

    @RequestMapping(value = "/live/preparation", method = RequestMethod.POST)
    public Result<Map<String, Object>> saveLivePreparation(@RequestBody Map<String, Object> payload) {
        Long sessionId = numberAsLong(payload.get("sessionId"), 1L);
        Map<String, Object> saved = new LinkedHashMap<>(payload);
        saved.put("status", "READY");
        saved.put("savedAt", System.currentTimeMillis());
        saved.put("adapterMode", "FIXTURE_ADAPTER");
        livePreparations.put(sessionId, saved);
        return new Result<>(saved);
    }

    @RequestMapping(value = "/live/control", method = RequestMethod.POST)
    public Result<Map<String, Object>> controlLive(@RequestBody Map<String, Object> payload) {
        Long sessionId = numberAsLong(payload.get("sessionId"), 1L);
        Map<String, Object> state = livePreparations.computeIfAbsent(sessionId, ignored -> new LinkedHashMap<>());
        state.put("sessionId", sessionId);
        state.put("lastAction", payload.get("action"));
        state.put("updatedAt", System.currentTimeMillis());
        return new Result<>(state);
    }

    private String createTask(String type, Map<String, Object> payload) {
        String taskId = UUID.randomUUID().toString();
        Map<String, Object> task = new LinkedHashMap<>();
        task.put("taskId", taskId);
        task.put("type", type);
        task.put("status", "SUCCEEDED");
        task.put("createdAt", System.currentTimeMillis());
        task.put("source", "demo");
        task.put("input", payload);
        task.put("result", demoResult(type, payload));
        tasks.put(taskId, task);
        return taskId;
    }

    private Map<String, Object> demoResult(String type, Map<String, Object> payload) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "演示任务已完成；接入真实服务后由回调写入实际结果。");
        result.put("type", type);
        result.put("sessionId", payload.get("sessionId"));
        return result;
    }

    private void addReplay(String replayId, String title, String dateRange, String duration,
                           String views, String clickRate, Number gmv, String clipStatus) {
        Map<String, Object> replay = new LinkedHashMap<>();
        replay.put("replayId", replayId);
        replay.put("title", title);
        replay.put("dateRange", dateRange);
        replay.put("duration", duration);
        replay.put("views", views);
        replay.put("clickRate", clickRate);
        replay.put("gmv", gmv);
        replay.put("clipStatus", clipStatus);
        replay.put("dataMode", "FIXTURE_ADAPTER");
        replays.put(replayId, replay);
    }

    private Map<String, Object> funnel(String stage, Number uv, Number rate) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("stage", stage);
        item.put("uv", uv);
        item.put("rate", rate);
        return item;
    }

    private Long numberAsLong(Object value, Long defaultValue) {
        return value instanceof Number ? ((Number) value).longValue() : defaultValue;
    }
}
