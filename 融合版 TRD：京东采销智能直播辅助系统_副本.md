# JoyCue 技术需求与设计文档（TRD）

> 版本：v2.0 - WIP 对齐版  
> 更新日期：2026-07-14  
> 对应 PRD：`融合版 PRD：京东采销智能直播辅助系统.md` v3.0  
> 工程原则：前后端骨架先可运行，数据和算法通过 Adapter 后接

## 1. 技术目标

1. 在没有公司中间件、生产数据和算法服务时，使用本地 H2 + Fixture Adapter 完成演示。
2. 数据/算法接入后不修改 React 页面主流程和 Controller 对外契约，只替换基础设施 Adapter。
3. JoyCard、JoyGuard、JoyRadar、JoyClip 与数据中心形成可追踪的端到端调用链。
4. 长任务使用统一 Task 状态，实时能力可从轮询平滑升级到 WebSocket/SSE。
5. 明确区分 Fixture、规则、模型和人工结果，避免把演示数据当生产事实。

## 2. 当前技术栈

| 层 | 当前选型 | 生产替换边界 |
| --- | --- | --- |
| 前端 | React 19、Vite 8、Tailwind CSS | 页面与 `api.js` 契约保持不变 |
| 后端 | Java 8、Spring Boot 2.7.18、Maven 多模块 | 可升级 JDK/Spring，但 API 兼容 |
| 数据 | H2 File + MyBatis | 由 GatewayImpl 替换为 MySQL/PostgreSQL/数据服务 |
| 实时 | 前端 2s/5s 轮询 | WebSocket/SSE Adapter |
| 任务 | 内存 ConcurrentMap Fixture | MQ + 任务表 + 回调 |
| 算法 | 本地规则/模板/Fixture | 模型/RAG/ASR/视频算法 Adapter |

当前代码不依赖 JMF、UDCC、JSF、JMQ、JIMDB、DongDAL、言犀或公司运行时。

## 3. 总体架构

```text
Browser
  -> React Component
  -> UI2Code/api.js
  -> Vite Proxy
  -> Controller (web)
  -> Service contract (client)
  -> ServiceImpl (app)
  -> DomainService (domain)
  -> Gateway SPI (infra-api)
  -> Fixture/H2 Adapter (infra-impl)

未来：
  Gateway SPI -> JD Data / Transaction / Inventory / Replay Adapter
  Algorithm SPI -> Compliance / LLM / RAG / ASR / Clip Adapter
```

后端详细类和方法见 `server-demo/ARCHITECTURE.md`，前端组件调用见 `UI2Code/ARCHITECTURE.md`。

## 4. 模块设计

### 4.1 前端

| 模块 | 组件 | 关键状态/函数 |
| --- | --- | --- |
| 全局壳 | `index.jsx/App` | `currentPage`、`isSidebarCollapsed`、`activeReplayId` |
| JoyCard | `SkuManage` | SKU、排雷、生成、卡片选中/编辑、`finalScript`、导出、导入直播台 |
| 直播准备/中控 | `LiveConsole` | `activeTab`、`liveStatus`、`casting`、`currentProductIndex` |
| 来源 | `LiveSourceConnector` | URL 登记、断开、评论 Fixture 注入 |
| JoyGuard | `LiveMetricsBanner`、`PriceRadar` | 指标/告警轮询、比价与话术降级 |
| JoyRadar | `GeekBulletScreen` | 评论轮询、确认/修改/口播/水贴处理 |
| 提词 | `Teleprompter` | 节点状态、手动推进、术语浮层 |
| 数据中心 | `DataAnalysis` | 日期、指标、漏斗、TOP 商品、互动、诊断、导出 |
| JoyClip | `SmartClip` | 搜索、状态聚合、切片任务、删除、进入工作台 |
| 剪辑工作台 | `ClipWorkbench` | 对话、预览、视频/图片素材、导出清单 |

所有网络请求必须经过 `api.js` 的 `request/upload/liveApi`。

### 4.2 后端分层

| Maven 模块 | 职责 |
| --- | --- |
| `server-demo-web` | HTTP、统一结果、Controller、Fixture 任务门面 |
| `server-demo-client` | Service、Param、DTO 稳定契约 |
| `server-demo-app` | 用例编排、BO/DTO 转换 |
| `server-demo-domain` | 合规、评论、脚本、告警等领域规则 |
| `server-demo-infra-api` | Gateway/DO，生产接入的稳定 SPI |
| `server-demo-infra-impl` | H2/MyBatis 和本地 Fixture 实现 |
| `server-demo-main` | 启动、配置、SQL 初始化 |

## 5. 前端状态与跨页数据

### 5.1 导航

```js
currentPage: 'skuManage' | 'liveConsole' | 'videoClip' | 'clipWorkbench' | 'dataCenter'
isSidebarCollapsed: boolean
```

`window.__setCurrentPage(pageKey)` 保留给验收脚本；正式应用可替换为 React Router。

### 5.2 JoyCard 导入直播台

当前使用 `sessionStorage['joycue.live.script']`：

```json
{
  "title": "SKU 100018374 直播脚本",
  "content": "最终拼接文本",
  "sections": [{ "id": 1, "title": "开场", "content": "...", "selected": true }],
  "skus": [{ "id": "100018374", "name": "...", "price": 2999 }]
}
```

生产可改为后端 `scriptId/sessionId`，但页面模型保持一致。

## 6. HTTP API

统一响应：

```json
{ "code": 0, "data": {}, "msg": "" }
```

### 6.1 已有业务接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/sku/batch` | 批量商品查询 |
| POST | `/compliance/check` | JoyCard 播前排雷 |
| POST | `/script/generate` | 模板/算法脚本生成 |
| GET | `/script/nodes` | 提词节点 |
| POST | `/live/source/connect` | 登记直播来源 |
| POST | `/live/comments/ingest` | 平台统一评论入口 |
| GET | `/danmaku/history` | 评论历史 |
| GET | `/alert/active` | JoyGuard 活跃告警 |
| GET | `/price/competitors` | 竞品价格 Adapter |
| GET | `/api/v1/live/metrics` | 直播指标 Adapter |
| POST | `/review/session` | 复盘聚合 |

### 6.2 WIP 新增骨架接口

| 方法 | 路径 | 当前实现 | 未来 Adapter |
| --- | --- | --- | --- |
| GET | `/api/v1/data-center/overview` | Fixture 指标、漏斗、诊断 | 交易/流量/库存/售后/诊断聚合 |
| GET | `/api/v1/replays` | 内存 Fixture 回放 | 视频/回放服务 |
| POST | `/api/v1/replays/{id}/clip` | 创建 `JOYCLIP` 任务并置 processing | 视频切片算法 |
| DELETE | `/api/v1/replays/{id}` | 从 Fixture 列表移除 | 回放/任务/对象存储删除策略 |
| POST | `/api/v1/live/preparation` | 内存保存准备信息 | 场次与草稿存储 |
| POST | `/api/v1/live/control` | 内存保存控制动作 | 直播控制 Adapter |

### 6.3 统一长任务

```json
{
  "taskId": "uuid",
  "type": "SCRIPT_GENERATION | SKU_IMPORT | JOYCLIP",
  "status": "PENDING | RUNNING | SUCCEEDED | FAILED",
  "createdAt": 1784000000000,
  "input": {},
  "result": {},
  "error": null
}
```

当前 Fixture 立即完成普通任务；JoyClip 前端先显示 `processing`。生产实现需要任务表、超时、重试、幂等键和回调。

## 7. 数据 Adapter 设计

建议在 `server-demo-infra-api` 新增以下 SPI；本课题只保留设计和现有 HTTP 契约，不实现公司 SDK：

```java
interface ProductDataGateway { ProductSnapshot get(String skuId); }
interface LiveEventGateway { List<LiveCommentEvent> pullComments(String roomId, String cursor); }
interface TradeMetricsGateway { DataCenterSnapshot aggregate(Long sessionId, String date); }
interface ReplayGateway { List<Replay> list(Long sessionId); }
interface DocumentGateway { ExportTask export(ExportRequest request); }
```

每个数据快照至少包含：

- `source`：来源系统。
- `eventTime`：业务事件时间。
- `fetchedAt`：读取时间。
- `version`：快照版本。
- `traceId`：链路追踪 ID。
- `available` / `degraded`：可用与降级状态。

## 8. 算法 Adapter 设计

```java
interface ComplianceAlgorithmGateway { ComplianceResult check(ComplianceInput input); }
interface ScriptAlgorithmGateway { TaskRef generate(ScriptInput input); }
interface CommentAlgorithmGateway { CommentInsight analyze(CommentInput input); }
interface AnswerAlgorithmGateway { GroundedAnswer answer(AnswerInput input); }
interface AsrAlignmentGateway { AlignmentResult align(AsrFrame frame, List<ScriptNode> nodes); }
interface DiagnosisAlgorithmGateway { DiagnosisResult diagnose(DataCenterSnapshot snapshot); }
interface ClipAlgorithmGateway { TaskRef clip(ClipRequest request); }
```

统一算法响应：

```json
{
  "requestId": "req-...",
  "provider": "fixture|jd-model|third-party",
  "modelVersion": "v1",
  "status": "SUCCEEDED|DEGRADED|FAILED",
  "confidence": 0.92,
  "result": {},
  "evidence": [{ "source": "sku", "ref": "100018374", "text": "..." }],
  "latencyMs": 86
}
```

领域层依赖 Gateway 接口，不能直接依赖模型 SDK。生产 Bean 可用 Profile/配置选择；失败时回落本地规则或人工模式。

## 9. 直播评论接入

`POST /live/source/connect` 只登记 URL，不抓取平台数据。平台 Adapter 负责授权、订阅、去重和转换，最终调用：

```http
POST /live/comments/ingest
Content-Type: application/json
```

```json
{
  "sessionId": 1,
  "userId": "viewer-1",
  "text": "电池续航怎么样？",
  "timestamp": 1784000000000,
  "source": "DOUYIN_ADAPTER"
}
```

生产表需增加 `platform_comment_id`、`source`、`event_time`、`trace_id`，并用平台 ID + 来源建立幂等约束。

## 10. 数据模型

当前 H2 表：`live_session`、`sku`、`alert`、`danmaku`、`compliance_result`、`script`、`script_node`、`price_compare`、`jargon_translate`、`script_generate`、`review`，另有旧示例 `order/item`。

WIP 后续建议新增：

| 表 | 关键字段 |
| --- | --- |
| `live_preparation` | `session_id, room_name, scene_id, camera_id, live_time, description, status` |
| `replay` | `replay_id, session_id, title, duration, cover_url, source_url, clip_status` |
| `clip_task` | `task_id, replay_id, provider, status, progress, error, created_at` |
| `clip_material` | `material_id, task_id, type, url, duration, sort_order` |
| `metric_snapshot` | `session_id, metric_name, metric_value, event_time, source` |
| `interaction_event` | `session_id, sku_id, event_type, user_hash, event_time, experiment_group` |
| `adapter_audit` | `trace_id, adapter_type, provider, status, latency_ms, request_digest` |

## 11. 实时升级方案

Demo 使用轮询：评论 2 秒，指标/告警 5 秒。生产升级统一事件信封：

```json
{
  "type": "alert|asr_node|danmaku|metric|clip_progress|heartbeat",
  "sessionId": "1",
  "ts": 1784000000000,
  "seq": 12034,
  "payload": {}
}
```

需支持心跳、断线重连、`seq` 断点补偿、房间隔离、反压和慢消费者降级。WebSocket 失败时继续使用 HTTP 拉取。

## 12. 降级与安全

| 依赖异常 | 降级行为 |
| --- | --- |
| 商品数据不可用 | 显示上次可信快照、时间和不可用标记，不生成事实性承诺 |
| ASR 不可用 | 提词器切手动模式 |
| 评论算法不可用 | 展示原评论，关闭答案预填 |
| RAG 无证据 | 返回“需主播结合商品详情回答” |
| 比价不可用 | 隐藏优势结论，仅展示本店事实 |
| 诊断不可用 | 展示基础指标，不展示 AI 诊断 |
| 视频算法不可用 | 任务标记失败，可重试或进入手工工作台 |

生产必须补：OAuth/SSO、RBAC、接口签名、幂等、限流、审计、PII 脱敏、日志脱敏、HTTPS、密钥托管和数据保留策略。

## 13. 一键启动设计

根目录 `start.sh` 是唯一编排入口：

```bash
./start.sh start
./start.sh --no-open
./start.sh status
./start.sh restart
./start.sh stop
```

它依次完成：后端构建 → 后端启动/健康检查 → 前端构建 → Vite Preview 启动/健康检查 → 打开默认浏览器。PID 和日志写入根目录 `.run/`。

macOS 可双击 `JoyCue一键启动.command`。

## 14. 测试与验收

### 自动验证

- `mvn test`：后端模块和 Spring 上下文。
- `npm run build`：React/Vite 构建与语法检查。
- `./start.sh --no-open`：一键启动。
- 健康检查、SKU、合规、脚本、评论、数据中心、回放与切片任务接口冒烟测试。

### 手工主流程

1. 收折/展开导航。
2. 加载 SKU，执行排雷，生成并编辑话术卡。
3. 取消某卡，确认最终脚本实时变化；导出 Word。
4. 导入直播台，完成直播准备并开始直播。
5. 登记评论来源、注入评论、确认/修改答案。
6. 暂停/恢复、投屏、切换商品、结束直播。
7. 搜索回放、发起切片、进入工作台、管理素材。
8. 数据中心按日期查看并导出 Excel 兼容文件。

