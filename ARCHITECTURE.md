# JoyCue 总体架构文档

> 位置：课题 Demo 根目录。  
> 基线：2026-07-14 WIP v3 对齐版。  
> 范围：前后端架构、运行时、接入边界和核心调用链。前端细节见 [UI2Code/ARCHITECTURE.md](UI2Code/ARCHITECTURE.md)，后端细节见 [server-demo/ARCHITECTURE.md](server-demo/ARCHITECTURE.md)。

## 1. 架构目标

- JoyCard、JoyGuard、JoyRadar、JoyClip 与数据中心主流程可在本地完整演示。
- 数据和算法本体暂不实现，只保留稳定 Adapter/API；当前使用 H2、规则和 Fixture。
- 后续接入真实商品、直播、交易、算法和视频服务时，不改页面主流程。
- 不依赖 JMF、UDCC、JSF 或公司运行时。

## 2. 运行时

```text
Browser :5173
  -> React Components
  -> UI2Code/api.js
  -> Vite Proxy
  -> Spring Boot :8080
  -> Controller / Client Service / App / Domain
  -> Gateway SPI
  -> H2 + Fixture Adapter

Future adapters:
  Product / Price / Inventory / Trade / Replay / Document
  Compliance / Script / Comment / RAG / ASR / Diagnosis / Clip
```

普通业务主链路：

```text
Component.handle*
 -> liveApi.*
 -> Controller
 -> Service interface
 -> ServiceImpl
 -> DomainService
 -> Gateway
 -> GatewayImpl / Mapper
 -> H2
```

WIP 新增的准备、数据中心和切片任务当前由 `IntegrationTaskController` 提供 Fixture 接口；生产接入时应迁移到独立 Client/App/Domain/Gateway 分层。

## 3. 产品模块映射

| 产品能力 | 前端 | 后端/API | 当前数据源 |
| --- | --- | --- | --- |
| JoyCard | `SkuManage` | `/sku/*`、`/compliance/*`、`/script/*` | H2 + 本地规则/模板 |
| 直播准备 | `LiveConsole/LivePreparation` | `/api/v1/live/preparation` | 内存 Fixture |
| JoyGuard | `LiveMetricsBanner`、`PriceRadar` | `/alert/*`、`/price/*`、`/api/v1/live/metrics` | H2 + Fixture |
| JoyRadar | `LiveSourceConnector`、`GeekBulletScreen` | `/live/*`、`/danmaku/*` | 统一评论接口 + H2 |
| 动态提词 | `Teleprompter` | `/script/nodes`、`/script/updateNodeStatus` | 前端 Fixture / H2 节点契约 |
| 数据中心 | `DataAnalysis` | `/review/*`、`/api/v1/data-center/overview` | Review + Fixture Adapter |
| JoyClip | `SmartClip`、`ClipWorkbench` | `/api/v1/replays*`、`/api/v1/tasks/*` | 内存 Fixture |

## 4. 关键端到端流程

### 4.1 JoyCard 到直播台

```text
SKU 录入
 -> /sku/batch
 -> 播前排雷 /compliance/check
 -> 生成脚本 /script/generate
 -> 前端勾选/编辑卡片
 -> finalScript
 -> sessionStorage['joycue.live.script']
 -> LiveConsole 直播前准备
 -> /api/v1/live/preparation
 -> 直播中控台
```

### 4.2 评论接入

```text
平台 Adapter
 -> POST /live/comments/ingest
 -> LiveSourceServiceImpl
 -> DanmakuServiceImpl
 -> DanmakuDomainService
 -> DanmakuGateway / Mapper
 -> H2.danmaku
 -> 前端每 2 秒 GET /danmaku/history
```

登记直播 URL 只产生连接配置，不直接抓取评论。

### 4.3 JoyClip

```text
SmartClip 列表
 -> GET /api/v1/replays
 -> POST /api/v1/replays/{id}/clip
 -> JOYCLIP taskId + processing
 -> Future ClipAlgorithmGateway
 -> clip_progress event / task polling
 -> ClipWorkbench 素材管理
```

### 4.4 数据中心

```text
DataAnalysis
 -> /review/session + /api/v1/data-center/overview
 -> Future TradeMetricsGateway / DiagnosisAlgorithmGateway
 -> 核心指标、漏斗、商品、互动、主播、切片、对比、诊断
 -> DocumentGateway 导出 PDF/Excel
```

## 5. 数据层

- JDBC：`jdbc:h2:file:./data/joycue;MODE=MySQL;DATABASE_TO_LOWER=TRUE`
- 从 `server-demo` 启动时文件：`server-demo/data/joycue.mv.db`
- 建表：`server-demo-main/src/main/resources/schema.sql`
- 种子：`server-demo-main/src/main/resources/data.sql`
- H2 控制台：`http://127.0.0.1:8080/h2-console`

H2 已持久化商品、场次、告警、评论、合规、脚本、比价和复盘。直播来源、任务、准备信息和回放 Fixture 目前在内存中，重启会恢复默认值。

## 6. 接入层原则

数据 Adapter 必须提供来源、业务时间、读取时间、版本、可用状态和 traceId。算法 Adapter 必须提供 requestId、provider、模型版本、状态、置信度、证据和耗时。

推荐 SPI：

```text
ProductDataGateway      TradeMetricsGateway
LiveEventGateway        ReplayGateway
DocumentGateway         ComplianceAlgorithmGateway
ScriptAlgorithmGateway  CommentAlgorithmGateway
AnswerAlgorithmGateway  AsrAlignmentGateway
DiagnosisAlgorithmGateway  ClipAlgorithmGateway
```

领域层只依赖 SPI，具体公司 SDK 或第三方 HTTP 客户端放在基础设施实现层。

## 7. 实时与降级

Demo：评论 2 秒轮询，指标/告警 5 秒轮询。生产可升级统一 WebSocket/SSE 事件：`alert`、`asr_node`、`danmaku`、`metric`、`clip_progress`、`heartbeat`。

降级规则：数据不可用显示上次快照和时间；ASR 不可用切手动提词；回答无证据交给主播；比价不可用隐藏结论；诊断不可用只展示基础指标；切片失败可重试或进入人工工作台。

## 8. 启动与进程

根目录 `start.sh` 负责构建、启动、健康检查、日志和 PID。它会核对 `8080`、`5173` 的真实监听进程：同项目的失效 PID 文件可自动恢复，其他程序占用端口时会中止并提示，避免重复实例和 H2 文件锁冲突：

```bash
./start.sh start
./start.sh --no-open
./start.sh status
./start.sh restart
./start.sh stop
```

macOS 可双击 `JoyCue一键启动.command`。运行文件写入 `.run/`，数据库写入 `server-demo/data/`。

## 9. 当前边界

- 已完成前后端骨架与本地可运行主流程。
- 真实平台、生产数据、ASR、RAG、诊断和切片算法未接入。
- Word 为浏览器生成的兼容文档；数据中心为 Excel 兼容 CSV，生产 PDF/Excel 服务待接入。
- 正式生产仍需路由、鉴权、RBAC、审计、幂等、限流、监控、任务持久化和生产数据库。
