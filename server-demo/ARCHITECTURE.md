# JoyCue 后端架构说明

> 本文以当前 `server-demo` 源码为准，说明模块职责、主要类和方法、调用位置、数据库及扩展点。项目介绍、业务流程和启动方式见仓库根目录 [README.md](../README.md)。

## 1. 技术与运行架构

- Java 8、Spring Boot 2.7.18、Maven 多模块工程。
- MyBatis 负责数据访问，H2 File 模式负责本地持久化。
- HTTP 接口统一由 `server-demo-web` 暴露，正常业务响应格式为 `{ "code": 0, "data": ..., "msg": "" }`。
- 目前不依赖公司 JMF、UDCC、JSF 或远程数据库；旧示例接口保留了名称，但已改为本地实现。
- 真实直播平台评论不由本服务直接抓取。平台适配器应将评论转换为统一模型，再调用 `POST /live/comments/ingest`。

主链路如下：

```text
React 页面
  -> Vite 代理
  -> Controller（web）
  -> Service 接口（client）
  -> ServiceImpl（app）
  -> DomainService（domain）
  -> Gateway 接口（infra-api）
  -> GatewayImpl + Mapper（infra-impl）
  -> H2 文件数据库
```

DTO 返回时沿相反方向转换：`DO -> BO -> DTO -> Result -> JSON`。直播来源登记是一个例外：`LiveSourceServiceImpl` 在内存中维护连接状态，再调用 `DanmakuService` 将评论送入标准弹幕链路。

## 2. Maven 模块

| 模块 | 职责 | 主要内容 |
| --- | --- | --- |
| `server-demo-main` | 启动与运行配置 | Spring Boot 启动类、`application.properties`、`schema.sql`、`data.sql` |
| `server-demo-web` | HTTP 适配层 | Controller、统一 `Result`、异步任务门面、异常转换 |
| `server-demo-client` | 稳定服务契约 | `*Service` 接口、Param、DTO；Web 层只依赖这些契约 |
| `server-demo-app` | 应用编排层 | `*ServiceImpl`、BO/DTO 转换；组织一次用例 |
| `server-demo-domain` | 领域规则层 | `*DomainService`、BO、DO/BO 转换，本地规则算法 |
| `server-demo-infra-api` | 基础设施抽象 | `*Gateway` 接口和 DO，隔离数据库或外部系统 |
| `server-demo-infra-impl` | 基础设施实现 | `*GatewayImpl`、MyBatis Mapper、本地线程池和配置 |
| `server-demo-common` | 公共能力 | 枚举、常量、异常、工具类 |
| `server-demo-test` | 测试聚合 | 多模块测试依赖和测试入口 |

依赖方向原则是：`web -> client <- app -> domain -> infra-api <- infra-impl`，`main` 负责把所有模块装配到同一个 Spring 容器。

## 3. Web 层：接口和调用位置

| Controller | HTTP 接口 | 调用的 Client Service |
| --- | --- | --- |
| `SkuController` | `POST /sku/detail`、`POST /sku/batch`、`POST /sku/compare` | `SkuService.querySkuDetail`、`batchQuerySkus`、`compareSkuParams` |
| `LiveSessionController` | `POST /session/create`、`GET /session/get`、`POST /session/updateStatus`、`POST /session/list`、`POST /session/importSkus` | `LiveSessionService` 同名方法 |
| `ComplianceController` | `POST /compliance/check`、`GET /compliance/words` | `ComplianceService.checkCompliance`、`getComplianceWords` |
| `ScriptController` | `POST /script/generate`、`GET /script/get`、`POST /script/updateNodeStatus`、`GET /script/nodes` | `ScriptService` 同名方法 |
| `AiAgentController` | `POST /ai/translateJargon`、`POST /ai/generateScriptContent`、`GET /ai/classifyIntent`、`GET /ai/goldenSentences` | `AiAgentService` 同名方法 |
| `DanmakuController` | `POST /danmaku/process`、`POST /danmaku/generateAnswer`、`POST /danmaku/confirmAnswer`、`POST /danmaku/modifyAnswer`、`GET /danmaku/history` | `DanmakuService` 同名方法 |
| `LiveSourceController` | `POST /live/source/connect`、`GET /live/source/status`、`POST /live/source/disconnect`、`POST /live/comments/ingest`、`POST /live/comments/demo` | `LiveSourceService.connect/getStatus/disconnect/ingestComment/ingestDemoComment` |
| `PriceCompareController` | `POST /price/compare`、`GET /price/competitors`、`POST /price/talkpoint` | `PriceCompareService.comparePrice/getCompetitorPrices/generateTalkpoint` |
| `AlertController` | `POST /alert/subscribe`、`GET /alert/active`、`POST /alert/dismiss`、`GET /alert/history` | `AlertService` 同名方法 |
| `ReviewController` | `POST /review/session`、`POST /review/backflow`、`GET /review/export` | `ReviewService.getSessionReview/backflowKnowledge/exportReport` |
| `IntegrationTaskController` | 任务、导入、直播指标、数据中心、直播准备/控制、回放与切片接口 | 内存 Fixture/Adapter 门面；生产接入迁移到独立分层 |
| `OrderController` | `/order/hello`、`/getfromrpc`、`/getfromdb`、`/contextinfo`、`/duccget`、`/testCluster*` | 旧课件兼容接口，调用本地 `OrderService` 或本地配置 |
| `ParrellController` | `GET /parrell/get` | `ParrellService` 本地线程池演示 |

`/actuator/health` 由 Spring Boot Actuator 提供，不经过业务 Controller。

### 3.1 WIP v3 新增的 Fixture/Adapter 方法

这些方法当前集中在 `IntegrationTaskController`，目的是先固定前后端契约并保证本地可运行。它们不代表真实数据或算法已经接入。

| 方法 | HTTP | 前端调用 | 当前实现 / 未来实现 |
| --- | --- | --- | --- |
| `getDataCenterOverview(sessionId,date)` | `GET /api/v1/data-center/overview` | `liveApi.getDataCenterOverview` | Fixture 指标/漏斗/诊断；未来调用交易、流量、库存、售后和诊断 Adapter |
| `listReplays()` | `GET /api/v1/replays` | `SmartClip` 挂载 | 返回内存回放；未来调用 ReplayGateway |
| `startReplayClip(replayId)` | `POST /api/v1/replays/{id}/clip` | `SmartClip.startClip` | 置 `processing` 并创建 `JOYCLIP` 任务；未来调用 ClipAlgorithmGateway |
| `deleteReplay(replayId)` | `DELETE /api/v1/replays/{id}` | `SmartClip.removeReplay` | 删除 Fixture；未来按回放/任务/素材策略删除 |
| `saveLivePreparation(payload)` | `POST /api/v1/live/preparation` | `LivePreparation.start` | 内存保存直播准备；未来持久化场次草稿 |
| `controlLive(payload)` | `POST /api/v1/live/control` | `LiveConsole.control` | 内存记录控制动作；未来调用直播控制 Adapter |

内部辅助方法：

- `createTask(type,payload)`：建立统一任务对象。
- `demoResult(type,payload)`：Fixture 任务结果。
- `addReplay(...)`：初始化回放 Fixture。
- `funnel(stage,uv,rate)`：构造漏斗项。
- `numberAsLong(value,defaultValue)`：宽松读取 JSON 数字。

内存集合：`tasks`、`replays`、`livePreparations`。后端重启后任务和准备信息会丢失，回放恢复构造函数中的默认值。

## 4. Client 与 App 层方法

每个 Client 接口都有同名 `*ServiceImpl`，实现位于 `server-demo-app/.../api/impl`。实现类完成参数拆解、调用领域服务、BO 转 DTO；除直播来源外不直接访问 Mapper。

| 接口 / 实现类 | 已定义的公开方法 | 下游调用 |
| --- | --- | --- |
| `SkuService` / `SkuServiceImpl` | `querySkuDetail`、`batchQuerySkus`、`compareSkuParams` | `SkuDomainService` |
| `LiveSessionService` / `LiveSessionServiceImpl` | `createSession`、`getSession`、`updateStatus`、`listSessions`、`importSkus` | `LiveSessionDomainService` |
| `ComplianceService` / `ComplianceServiceImpl` | `checkCompliance`、`getComplianceWords` | `ComplianceDomainService` |
| `ScriptService` / `ScriptServiceImpl` | `generateScript`、`getScript`、`updateNodeStatus`、`getScriptNodes` | `ScriptDomainService` |
| `AiAgentService` / `AiAgentServiceImpl` | `translateJargon`、`generateScriptContent`、`classifyIntent`、`extractGoldenSentences` | `AiAgentDomainService` |
| `DanmakuService` / `DanmakuServiceImpl` | `processDanmaku`、`generateAnswer`、`confirmAnswer`、`modifyAnswer`、`getDanmakuHistory` | `DanmakuDomainService` |
| `LiveSourceService` / `LiveSourceServiceImpl` | `connect`、`getStatus`、`disconnect`、`ingestComment`、`ingestDemoComment` | 内存 `ConcurrentHashMap`；评论再调用 `DanmakuService.processDanmaku` |
| `PriceCompareService` / `PriceCompareServiceImpl` | `comparePrice`、`getCompetitorPrices`、`generateTalkpoint` | `PriceCompareDomainService` |
| `AlertService` / `AlertServiceImpl` | `subscribeAlerts`、`getActiveAlerts`、`dismissAlert`、`getAlertHistory` | `AlertDomainService` |
| `ReviewService` / `ReviewServiceImpl` | `getSessionReview`、`backflowKnowledge`、`exportReport` | `ReviewDomainService` |
| `OrderService` / `OrderServiceImpl` | `getFromRpc`、`getFromDb`、`testCluster`、`testCluster1` | `OrderDomainService` 或本地回退 |
| `ParrellService` / `ParrellServiceImpl` | `getFromThreadPool`、`getFromScheduledThreadPool`、`getFromThreadPoolTask` | 本地线程池 Bean |
| `ItemService` / `ItemServiceImpl` | `sayHello` | `ItemDomainService`，仅旧示例使用 |

转换类位于每个业务包的 `converter` 目录，例如 `SkuBOConverter`、`DanmakuBOConverter`、`ScriptBOConverter`。它们被相应 `ServiceImpl` 调用，不包含业务规则。

## 5. Domain 层方法与规则

| 领域服务 | 方法 | 当前逻辑 |
| --- | --- | --- |
| `SkuDomainService` | `querySkuDetail`、`batchQuerySkus`、`compareSkuParams` | 查询 H2；参数对比目前只比较参数项数量，属于简化实现 |
| `LiveSessionDomainService` | `createSession`、`getSession`、`updateStatus`、`listSessions`、`importSkus` | 场次增查改；导入方法委托给 Gateway |
| `ComplianceDomainService` | `checkCompliance(skuId)`、`checkCompliance(skuId, content)`、`getComplianceResult`、`getComplianceWords` | 对文本遍历本地极限词词表，产生命中词与替换建议 |
| `ScriptDomainService` | `generateScript`、`getScript`、`updateNodeStatus`、`getScriptNodes` | 调用 Gateway 生成并持久化 4 段模板脚本，维护节点状态 |
| `AiAgentDomainService` | `translateJargon`、`generateScriptContent`、`classifyIntent`、`extractGoldenSentences` | 术语/脚本查库；意图与金句仍是占位实现 |
| `DanmakuDomainService` | `processDanmaku` 两个重载、`generateAnswer`、`confirmAnswer`、`modifyAnswer`、`getDanmakuHistory` | 短文本/“666”等归为 `WATER`；其余归为 `QA`，按快充、屏幕、NFC、电池关键词生成本地回答并写库 |
| `PriceCompareDomainService` | `comparePrice`、`getPriceCompare`、`getCompetitorPrices`、`generateTalkpoint` | 返回本地示例竞品价格与本地话术；真实采价算法未实现 |
| `AlertDomainService` | `subscribeAlerts`、`getActiveAlerts`、`dismissAlert`、`getAlertHistory` | 查询及更新 H2 告警 |
| `ReviewDomainService` | `getSessionReview`、`backflowKnowledge`、`exportReport` | 查库，数据不足时补本地演示指标、时间线和文本 |
| `OrderDomainService` | `getFromDb`、`getFromRpc` | 旧课件示例；RPC 名称保留但已使用本地 Gateway |
| `ItemDomainService` | `doSomething` | 旧课件示例，查询本地 `item` |

领域转换类如 `SkuDOConverter`、`DanmakuDOConverter` 将基础设施 DO 转成领域 BO。

## 6. Gateway、实现类、Mapper 与表

| Gateway / 实现类 | 主要方法 | Mapper | H2 表或数据源 |
| --- | --- | --- | --- |
| `SkuGateway` / `SkuGatewayImpl` | `insert`、`findById`、`findBySessionId`、`update`、`getBySkuId`、`listBySkuIds` | `SkuMapper` | `sku` |
| `LiveSessionGateway` / `LiveSessionGatewayImpl` | `insert`、`findById`、`findByStatus`、`update`、`listByParam`、`importSkus` | `LiveSessionMapper`、`SkuMapper` | `live_session`、`sku` |
| `ComplianceGateway` / `ComplianceGatewayImpl` | `findBySessionIdAndSkuId`、`findById`、`insert`、`findBySkuId`、`getComplianceWords` | `ComplianceResultMapper` | `compliance_result`；词表当前在实现类中 |
| `ScriptGateway` / `ScriptGatewayImpl` | `insert`、`findById`、`findBySessionId`、`update`、`generate`、`updateNodeStatus`、`listNodesByScriptId` | `ScriptMapper` | `script`、`script_node` |
| `AiAgentGateway` / `AiAgentGatewayImpl` | `findJargonByJargon`、`insertJargon`、`findScriptBySessionId`、`insertScript`、`classifyIntent`、`extractGoldenSentences` | `JargonTranslateMapper`、`ScriptGenerateMapper` | `jargon_translate`、`script_generate`；后两方法为占位 |
| `DanmakuGateway` / `DanmakuGatewayImpl` | `insert`、`findById`、`findBySessionId`、`update`、`generateAnswer` | `DanmakuMapper` | `danmaku` |
| `PriceCompareGateway` / `PriceCompareGatewayImpl` | `findBySessionIdAndSkuId`、`findById`、`insert`、`findBySkuId`、`getCompetitorPrices`、`generateTalkpoint` | `PriceCompareMapper` | `price_compare` + 本地示例 |
| `AlertGateway` / `AlertGatewayImpl` | `insert`、`findById`、`findBySessionId`、`update`、`subscribeAlerts`、`getActiveAlerts`、`dismissAlert`、`getAlertHistory` | `AlertMapper` | `alert` |
| `ReviewGateway` / `ReviewGatewayImpl` | `insert`、`findById`、`findBySessionId`、`findAll`、`backflowKnowledge`、`exportReport` | `ReviewMapper` | `review` + 本地确认/导出文本 |
| `OrderGateway` / `OrderGatewayImpl` | `getByIdFromDb`、`getByIdFromRpc` | `OrderMapper` | `order`；RPC 方法为本地兼容 |
| `ItemGateway` / `ItemGatewayImpl` | `getById` | `ItemMapper` | `item` |

Mapper 使用注解 SQL。主要方法是 `insert`、`getById`、按 `sessionId`/`skuId` 查询、`update`；`ScriptMapper` 额外提供 `insertNode`、`getNodesByScriptId`、`updateNodeStatus`。

## 7. 端到端调用示例

### 7.1 商品批量查询

```text
SkuManage.handleAddSku
 -> liveApi.batchSkus
 -> POST /sku/batch
 -> SkuController.batchQuerySkus
 -> SkuServiceImpl.batchQuerySkus
 -> SkuDomainService.batchQuerySkus
 -> SkuGatewayImpl.listBySkuIds
 -> SkuMapper.listBySkuIds
 -> H2.sku
```

### 7.2 播前排雷

```text
SkuManage.handleComplianceCheck
 -> liveApi.checkCompliance
 -> POST /compliance/check
 -> ComplianceController.checkCompliance
 -> ComplianceServiceImpl.checkCompliance
 -> ComplianceDomainService.checkCompliance
 -> ComplianceGatewayImpl.getComplianceWords
 -> 返回 hits、passed、suggestions
```

### 7.3 直播评论

```text
平台适配器 / 前端示例按钮
 -> POST /live/comments/ingest 或 /live/comments/demo
 -> LiveSourceController
 -> LiveSourceServiceImpl.ingestComment
 -> DanmakuServiceImpl.processDanmaku
 -> DanmakuDomainService.processDanmaku
 -> DanmakuGatewayImpl.insert
 -> DanmakuMapper.insert
 -> H2.danmaku
 -> 前端 GeekBulletScreen 每 2 秒请求 /danmaku/history
```

## 8. 数据库与配置

- 配置：`server-demo-main/src/main/resources/application.properties`
- 建表：`server-demo-main/src/main/resources/schema.sql`
- 种子数据：`server-demo-main/src/main/resources/data.sql`
- JDBC：`jdbc:h2:file:./data/joycue;MODE=MySQL;DATABASE_TO_LOWER=TRUE`
- 从 `server-demo` 目录启动时，实际文件为 `server-demo/data/joycue.mv.db`。
- H2 控制台：`http://localhost:8080/h2-console`
- 用户名：`sa`；密码为空。JDBC URL 必须与启动进程的工作目录对应。

当前表：`order`、`item`、`live_session`、`sku`、`alert`、`danmaku`、`compliance_result`、`script`、`script_node`、`price_compare`、`jargon_translate`、`script_generate`、`review`。

注意：直播来源连接保存在 `LiveSourceServiceImpl.connections` 内存 Map，不在数据库中，后端重启后会变回 `DISCONNECTED`；已经接入的评论保存在 `danmaku` 表中。

## 9. 算法替换位置

| 能力 | 当前实现位置 | 接入真实算法的建议位置 |
| --- | --- | --- |
| 合规/NLP | `ComplianceDomainService.checkCompliance` | 保留 `ComplianceService` 契约，实现新的规则库或模型 Gateway |
| 评论意图分类 | `DanmakuDomainService.isWaterComment`、`AiAgentGatewayImpl.classifyIntent` | 新建 `IntentModelGateway`，返回统一意图和置信度 |
| 评论答案/RAG | `DanmakuDomainService.localAnswer`、`DanmakuGatewayImpl.generateAnswer` | 新建 `AnswerModelGateway`，输入评论、SKU、知识片段，输出答案与引用 |
| 脚本生成 | `ScriptGatewayImpl.generate`、`AiAgentDomainService.generateScriptContent` | 新建 `ScriptModelGateway`，仍由 `ScriptService.generateScript` 对外 |
| 术语翻译 | `AiAgentDomainService.translateJargon` | 在 `AiAgentGateway` 后接词库或模型 |
| 竞品匹配/采价 | `PriceCompareDomainService.getCompetitorPrices` | 新建平台价格 Adapter + 商品匹配算法 Gateway |
| ASR 节点跟随 | 后端尚无音频接口 | 新建 `/asr/stream` 或 WebSocket，并将匹配结果映射到 `ScriptService.updateNodeStatus` |
| 复盘总结/金句 | `ReviewDomainService`、`AiAgentGatewayImpl.extractGoldenSentences` | 新建 Review/LLM Gateway，结果写入 `review` |

算法实现应放在新的基础设施实现包中，领域层只依赖 Gateway 接口。这样本地规则、公司算法服务或第三方模型可以替换，而 Controller 和前端契约不变。

### 9.1 建议新增的数据/算法 SPI

本课题暂不实现数据源和算法本体。生产接入时，在 `server-demo-infra-api` 定义接口，在 `server-demo-infra-impl` 实现公司/第三方 Adapter：

```text
ProductDataGateway         LiveEventGateway
TradeMetricsGateway        ReplayGateway
DocumentGateway            ComplianceAlgorithmGateway
ScriptAlgorithmGateway     CommentAlgorithmGateway
AnswerAlgorithmGateway     AsrAlignmentGateway
DiagnosisAlgorithmGateway  ClipAlgorithmGateway
```

数据响应必须包含 `source/eventTime/fetchedAt/version/traceId/available`；算法响应必须包含 `requestId/provider/modelVersion/status/confidence/evidence/latencyMs`。领域层不得直接引用供应商 SDK。

## 10. 启动编排

后端通常由根目录 `start.sh` 构建和启动。脚本使用 `server-demo` 作为工作目录，确保 H2 文件稳定落在 `server-demo/data/joycue.mv.db`，并将日志/PID 写入根目录 `.run/`。

```bash
../start.sh start
../start.sh status
../start.sh stop
```

## 11. 当前边界与已知欠缺

- `connect` 只登记直播链接，未直接连接抖音、快手、淘宝或京东直播评论流。
- 评论分析、合规和话术属于规则/模板 Demo，不是训练模型或真实 LLM/RAG。
- Excel/CSV 上传只创建内存任务，尚未解析文件并写入 `sku`。
- `/api/v1/live/metrics`、部分价格和复盘数据是固定演示数据。
- `Teleprompter` 所示 ASR 目前在前端模拟，后端没有音频流处理。
- 异步任务和直播来源状态是内存数据，重启会丢失。
- 尚无登录鉴权、权限、限流、审计、平台签名校验、幂等和生产监控。
- H2 适合课题 Demo；生产需迁移到 MySQL/PostgreSQL，并补数据库迁移脚本。
