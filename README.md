# JoyCue 智能直播辅助系统课题 Demo

JoyCue 是一个已经前后端联通、可完全在本机运行的直播辅助课题 Demo。它覆盖“播前商品准备与合规排雷 → 播中评论接入、规则分析、提词与告警 → 播后复盘”的演示链路。

当前版本已经移除对公司 JMF、UDCC、JSF 和远程数据库运行环境的依赖：后端使用 Spring Boot + MyBatis + 本地 H2 文件库，前端使用 React + Vite。本地化解决的是 Demo 的可启动、可联调和可演示；真实直播平台接入、生产算法和生产级安全能力仍需后续实现。

## 1. “播前排雷”是什么

“播前排雷”是直播开始前，对商品标题、卖点和主播话术做合规扫描，提前找出可能引发广告法、平台规则或客诉风险的表达。

本项目当前检测的本地词表包括：`最`、`第一`、`顶级`、`极品`、`绝无仅有`、`100%`、`国家级`、`世界级`。例如商品 `100018376` 的名称包含“世界级”，会得到命中结果和“替换为可验证的客观描述”的建议。

当前实现是字符串规则扫描，适合课题 Demo；它不是法律结论，也没有覆盖上下文、谐音、图片文字、承诺时效、价格欺诈等复杂风险。生产使用需要扩充规则库、审核工作流和 NLP/多模态模型。

## 2. 目录和文档

```text
课题demo/
├── UI2Code/                 React 前端
│   └── ARCHITECTURE.md      前端组件、函数和接口调用说明
├── server-demo/             Spring Boot 多模块后端
│   └── ARCHITECTURE.md      后端分层、类、方法和调用链说明
├── docs/                    补充设计资料
├── README.md                本文：项目、业务、数据、算法、启动说明
├── ARCHITECTURE.md          当前总体架构与运行边界
└── INTEGRATION_GUIDE.md     补充联调资料
```

以当前代码为准的三份架构文档、验收记录和两份需求文档：

- [总体架构文档](ARCHITECTURE.md)
- [后端架构文档](server-demo/ARCHITECTURE.md)
- [前端架构文档](UI2Code/ARCHITECTURE.md)
- [最终验收记录](FINAL_ACCEPTANCE.md)
- [PRD](融合版%20PRD：京东采销智能直播辅助系统.md)
- [TRD](融合版%20TRD：京东采销智能直播辅助系统_副本.md)

## 3. 整体架构

```text
浏览器 http://127.0.0.1:5173
  |
  | React 页面 -> UI2Code/api.js
  v
Vite 同源代理
  |
  v
Spring Boot :8080
  Controller -> Client Service -> App ServiceImpl -> DomainService
                                              |
                                              v
                                 Gateway -> MyBatis Mapper
                                              |
                                              v
                                  H2 ./data/joycue.mv.db

真实直播平台（待实现 Adapter）
  -> 将平台事件转成统一评论 JSON
  -> POST /live/comments/ingest
  -> 评论分析、回答建议、H2 持久化
  -> 前端每 2 秒读取 /danmaku/history
```

### 后端

- Java 8、Spring Boot 2.7.18、Maven 多模块。
- `web` 暴露 HTTP，`client` 定义契约，`app` 编排用例，`domain` 放业务规则，`infra-api` 定义 Gateway，`infra-impl` 用 MyBatis/H2 实现。
- 业务接口正常返回 `{code, data, msg}`；健康检查使用 Actuator 原生格式。

### 前端

- React 19、Vite 8、Tailwind CSS、Font Awesome 本地资源。
- `api.js` 是唯一后端访问层。
- 商品管理、直播前准备、直播中控、智能切片、剪辑工作台和数据中心均已有可操作的前端主流程。
- 数据和算法本体按课题范围暂不实现；后端提供 Fixture/Adapter 接口，后续替换接入实现即可。

## 4. 业务逻辑

### 4.1 播前

1. 在商品管理页手工录入 SKU，或加载本地联通示例。
2. 前端调用 `/sku/batch`，从 H2 查询商品。
3. 点击“播前排雷”，把商品名称送到 `/compliance/check`。
4. 后端用本地极限词规则返回命中项、通过状态和替换建议。
5. 点击生成脚本，后端按商品生成四段模板话术和脚本节点并写入 H2。

### 4.2 播中

1. 在直播控制台填写直播间 URL，点击“登记来源”。
2. 后端校验 URL 并在内存中记录 `READY` 状态，返回统一评论入口 `/live/comments/ingest`。
3. Demo 可点击“注入示例评论”；真实平台则需要单独 Adapter 订阅平台评论，再 POST 到统一入口。
4. 评论进入后端后，规则将“666”等短评论识别为 `WATER`，其他评论识别为 `QA`。
5. 对快充、屏幕/NFC、电池等关键词生成本地回答建议，并将原文、意图、答案和确认状态写入 `danmaku` 表。
6. 前端每 2 秒轮询评论历史，主播/运营可以确认或修改答案；指标和告警每 5 秒刷新。
7. 价格雷达展示本地竞品样例和话术；提词器展示脚本节点，但 ASR 跟随目前是 UI 模拟。

### 4.3 播后

1. 复盘页按 `sessionId` 请求 `/review/session`。
2. 后端从 H2 读取已有数据，不足部分用本地示例指标、摘要和时间线补全。
3. 前端可导出文本报告。
4. “知识回流”目前只返回成功确认，没有写入真实知识库或向量库。

## 5. 已经实现的功能

| 功能 | 状态 | 当前实现 |
| --- | --- | --- |
| 完全本地启动 | 已实现 | 前端、后端、图标、H2 均可本地运行，无公司运行时依赖 |
| 前后端健康联通 | 已实现 | 商品页显示“后端已联通 · H2 本地库” |
| 商品批量查询 | 已实现 | 手工 SKU 或本地样例，查询 H2 `sku` |
| 播前排雷 | 已实现（规则版） | 本地极限词扫描、命中和建议 |
| 直播脚本 | 已实现（模板版） | 生成 4 个脚本节点并持久化 |
| 直播链接入口 | 已实现（登记版） | 校验、识别平台名称、维护连接状态、返回 webhook |
| 标准评论接入 | 已实现 | `POST /live/comments/ingest` |
| 示例评论注入 | 已实现 | 前端按钮轮换注入 5 类本地评论 |
| 评论意图与回答 | 已实现（规则版） | `WATER/QA` 分类和关键词答案 |
| 评论历史与人工确认 | 已实现 | H2 持久化、前端轮询、确认/修改答案 |
| 活跃告警 | 已实现（本地数据） | 查询、轮询和忽略 H2 告警 |
| 竞品价格/话术 | 已实现（样例数据） | 返回固定本地平台价格和话术 |
| 实时指标 | 已实现（样例数据） | `/api/v1/live/metrics` 返回本地演示值 |
| 场次复盘 | 已实现（Demo） | 指标、摘要、时间线、导出文本 |
| H2 控制台 | 已实现 | `/h2-console` 查看本地数据 |
| 智能切片 | 已实现（交互/接口版） | 回放搜索、状态、切片任务、删除、剪辑工作台和素材清单导出；视频算法待接入 |
| 直播前准备 | 已实现（交互/接口版） | 商品/脚本、竖屏预览、场景、直播信息和开始直播 |
| 数据中心 | 已实现（Fixture版） | 指标、漏斗、商品、互动、主播、切片、对比、诊断和 CSV 导出 |

可手工测试的种子 SKU：

- `100018374`：5G 智能手机，正常商品。
- `100018375`：无线蓝牙耳机，正常商品。
- `100018376`：4K 智能电视，名称包含“世界级”，适合测试播前排雷。

## 6. 未实现和欠缺部分

### 直播平台与实时链路

- 仅粘贴一个直播 URL 不能直接抓到平台评论。抖音、快手、淘宝、京东直播等都需要官方开放平台、授权、SDK 或合规的数据通道。
- 尚未实现任何真实平台 Adapter、鉴权、签名校验、断线重连、限流和事件去重。
- 评论、告警和指标使用轮询，不是 WebSocket/SSE 实时推送。
- 直播来源连接状态存在内存中，后端重启会丢失。
- 直播前准备、回放和切片任务当前也是内存 Fixture；真实服务接入点已保留。

### 算法与智能能力

- 合规仅字符串包含匹配，没有上下文、正则、语义、多模态和规则版本管理。
- 评论只做 `WATER/QA` 简化分类；没有多意图、情绪、紧急度、置信度和聚类。
- 回答只覆盖少数关键词，不是真实 LLM、RAG 或商品知识库检索。
- 脚本是模板，不是模型生成；金句提取仍返回空列表。
- 价格没有真实抓取、同款匹配、时效校验和促销计算。
- ASR 没有采集音频，前端“ASR 在线”、匹配率和自动跟随都是展示数据。
- 复盘和知识回流没有真实聚合、向量化、审核入库。

### 数据与工程化

- Excel/CSV 上传只生成异步任务 ID，没有解析行、校验字段或写入 SKU。
- 异步任务保存在内存中，重启会丢失。
- H2 与当前 `VARCHAR` 金额字段适合演示，不适合生产；生产应使用 MySQL/PostgreSQL 和 decimal/分单位金额。
- 尚无用户登录、RBAC、租户、审计日志、敏感数据脱敏、接口幂等和生产监控。
- 前端 `sessionId=1`、`skuId=100018374` 仍为固定值，没有正式场次管理 UI。
- 没有完整 E2E 浏览器自动化测试和平台沙箱测试。

## 7. 数据库在哪里

### 配置与文件

- 数据源配置：`server-demo/server-demo-main/src/main/resources/application.properties`
- 建表 SQL：`server-demo/server-demo-main/src/main/resources/schema.sql`
- 种子数据：`server-demo/server-demo-main/src/main/resources/data.sql`
- JDBC URL：`jdbc:h2:file:./data/joycue;MODE=MySQL;DATABASE_TO_LOWER=TRUE`
- 从 `server-demo` 目录启动时，数据库文件：`server-demo/data/joycue.mv.db`

`./data` 是相对于后端进程的启动目录。如果从别的目录执行 JAR，数据库也会创建在那个目录下的 `data` 中。因此建议始终先 `cd server-demo` 再启动。

### H2 控制台

打开 `http://localhost:8080/h2-console`，填写：

```text
Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:file:./data/joycue
User Name: sa
Password: 留空
```

后端运行时数据库可能被进程锁定；需要直接调试文件时应先停止后端，或通过 H2 控制台连接当前实例。

### 当前表和所需数据

| 表 | 需要的数据 | 关键字段 |
| --- | --- | --- |
| `live_session` | 直播场次 | `id`、`title`、`session_code`、`operator`、`status`、计划/实际时间 |
| `sku` | 场次商品 | `session_id`、`sku_id`、`sku_name`、`main_image`、`category`、`brand`、`price`、`stock` |
| `alert` | 库存/价格/合规等告警 | `session_id`、`alert_type`、`level`、`sku_id`、`message`、`handled` |
| `danmaku` | 评论和答案 | `session_id`、`content`、`sender_nick`、`intent`、`answer`、确认/回复状态 |
| `compliance_result` | 排雷历史 | `session_id`、`sku_id`、`check_content`、`passed`、`hit_count` |
| `script` | 整体脚本 | `session_id`、`sku_id`、`title`、`content`、`ai_generated`、`status` |
| `script_node` | 分段提词节点 | `script_id`、`node_index`、`node_type`、`content`、`keywords`、`status` |
| `price_compare` | 价格对比记录 | `session_id`、`sku_id`、`our_price`、`competitor_data`、建议 |
| `jargon_translate` | 术语通俗解释 | `jargon`、`translation`、`category`、`aigc` |
| `script_generate` | 生成式脚本记录 | `session_id`、`content`、`aigc` |
| `review` | 场次复盘 | `session_id`、`summary`、`metrics_data`、`highlights`、`improvements` |
| `order`、`item` | 原课件兼容数据 | 仅旧示例接口使用 |

要让业务不依赖固定样例，至少需要准备：场次、场次 SKU、商品标题/参数/价格/库存、直播平台评论、告警事件。若接算法，还需要商品知识文档、合规规则版本、标准问答和人工反馈数据。

## 8. 当前接口数据格式

业务成功响应：

```json
{
  "code": 0,
  "data": {},
  "msg": ""
}
```

### 8.1 批量查 SKU

`POST /sku/batch`

```json
["100018374", "100018376"]
```

返回的单个商品核心字段：

```json
{
  "skuId": "100018374",
  "skuName": "京东自营 5G智能手机 骁龙8 Gen3 5000mAh",
  "mainImage": "",
  "price": "2999",
  "params": []
}
```

### 8.2 播前排雷

`POST /compliance/check`

```json
{
  "skuId": "100018376",
  "content": "世界级画质 4K超高清智能电视 65英寸"
}
```

```json
{
  "passed": false,
  "hits": [
    {
      "word": "世界级",
      "category": "EXTREME_WORD",
      "position": null
    }
  ],
  "suggestions": ["请将“世界级”替换为可验证的客观描述"]
}
```

### 8.3 生成脚本

`POST /script/generate`

```json
{
  "sessionId": 1,
  "skuId": "100018374",
  "templateType": "live-commerce",
  "skuParams": {}
}
```

返回 `id`、`sessionId`、`skuId`、`content`、`aiGenerated` 和 `nodes[]`；每个节点包含 `id`、`nodeIndex`、`content`、`keywords`、`status`。

### 8.4 登记直播来源

`POST /live/source/connect`

```json
{
  "sessionId": 1,
  "roomUrl": "https://live.example.com/room/course-demo",
  "platform": "AUTO"
}
```

返回示例：

```json
{
  "sessionId": 1,
  "roomUrl": "https://live.example.com/room/course-demo",
  "platform": "AUTO",
  "status": "READY",
  "connectionMode": "WEBHOOK_ADAPTER",
  "commentWebhook": "/live/comments/ingest",
  "message": "直播来源已登记；平台适配器应把评论推送到统一评论接口。"
}
```

### 8.5 标准直播评论入口

这是未来平台 Adapter 要保留的核心接口：`POST /live/comments/ingest`

```json
{
  "sessionId": 1,
  "userId": "平台用户ID或昵称",
  "text": "电池续航怎么样？",
  "timestamp": 1784000000000,
  "source": "DOUYIN_ADAPTER"
}
```

返回核心字段：

```json
{
  "id": 12,
  "sessionId": 1,
  "originalText": "电池续航怎么样？",
  "senderNick": "平台用户ID或昵称",
  "intent": "QA",
  "aiAnswer": "5000mAh 电池的实际续航因使用场景而异，建议避免绝对化承诺。",
  "aiGenerated": false,
  "humanConfirmed": false,
  "replied": false
}
```

当前后端没有使用 `timestamp` 和 `source` 持久化字段，Adapter 可以传入，但后续应在 `danmaku` 表补 `source`、`platform_comment_id`、`event_time`，用于去重和审计。

## 9. 需要哪些算法，接口在哪里

### 9.1 建议的算法清单

| 算法 | 输入 | 输出 | 当前替代 |
| --- | --- | --- | --- |
| 合规检测 | 文本、SKU、类目、规则版本 | 命中位置、风险级别、规则、建议 | 关键词包含扫描 |
| 评论意图分类 | 评论、会话上下文 | 意图、多标签、置信度 | `WATER/QA` 规则 |
| 情绪/紧急度 | 评论流 | 情绪、投诉风险、优先级 | 未实现 |
| 商品知识检索 | 问题、SKU、商品知识 | Top-K 证据、来源、分数 | 少数关键词硬编码 |
| 回答生成/RAG | 问题、证据、合规约束 | 建议答案、引用、置信度 | 本地模板答案 |
| 脚本生成 | 商品参数、受众、模板、合规规则 | 分段脚本、关键词、时长 | 固定 4 段模板 |
| ASR 与脚本对齐 | 音频流、脚本节点 | 转写、当前节点、匹配率 | 前端模拟 |
| 竞品同款匹配 | 标题、属性、图像、平台商品 | 对应商品和相似度 | 固定样例 |
| 价格策略 | 实时价、券、库存、竞品价 | 到手价、优势、话术 | 固定样例 |
| 复盘总结/金句 | 评论、指标、告警、转写 | 摘要、问题、金句、建议 | 本地默认数据 |

### 9.2 当前代码接口

- 合规入口：`POST /compliance/check`；Java 契约 `ComplianceService.checkCompliance`；规则在 `ComplianceDomainService.checkCompliance`。
- 意图入口：`GET /ai/classifyIntent?text=...` 或评论主链路；Java 契约 `AiAgentService.classifyIntent`；当前占位在 `AiAgentGatewayImpl.classifyIntent`。
- 评论回答：`POST /danmaku/generateAnswer`；Java 契约 `DanmakuService.generateAnswer`；当前规则在 `DanmakuDomainService.localAnswer`。
- 脚本生成：`POST /script/generate`；Java 契约 `ScriptService.generateScript`；当前模板在 `ScriptGatewayImpl.generate`。
- 术语翻译：`POST /ai/translateJargon`；Java 契约 `AiAgentService.translateJargon`。
- 金句：`GET /ai/goldenSentences?sessionId=1`；Java 契约 `AiAgentService.extractGoldenSentences`。
- 比价：`POST /price/compare`、`GET /price/competitors`；Java 契约 `PriceCompareService`。
- 复盘：`POST /review/session`；Java 契约 `ReviewService.getSessionReview`。

### 9.3 接算法的推荐方式

保留 Controller 和 `server-demo-client` 中的业务契约，在 `server-demo-infra-api` 增加算法 Gateway，例如 `ComplianceModelGateway`、`AnswerModelGateway`、`ScriptModelGateway`；在 `server-demo-infra-impl` 提供本地规则版和远程模型版实现。通过 Spring 配置选择实现，领域层不直接写 HTTP SDK。

真实算法服务建议统一返回：

```json
{
  "requestId": "可追踪ID",
  "modelVersion": "版本",
  "result": {},
  "confidence": 0.92,
  "evidence": [],
  "latencyMs": 86
}
```

同时为直播场景设置超时、熔断和本地降级；回答必须保留人工确认和引用证据，不应让模型直接无审核地对外发送。

## 10. 怎么启动

### 10.1 一键启动（推荐）

在仓库根目录执行：

```bash
./start.sh start
```

脚本会自动完成后端构建、后端健康检查、前端构建、前端启动并打开默认浏览器。其他命令：

```bash
./start.sh --no-open
./start.sh status
./start.sh restart
./start.sh stop
```

macOS 也可以双击根目录 `JoyCue一键启动.command`。运行日志和 PID 位于 `.run/`。

脚本会用 `lsof` 核对 `8080`、`5173` 的真实监听进程：PID 文件失效但端口仍由本项目监听时会自动恢复；端口被其他程序占用时会明确失败，避免把旧实例或无关服务误判为启动成功。

### 10.2 环境要求

- macOS 或 Linux。
- JDK 8。当前机器使用 `/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home`。
- Maven 3.x。
- Node.js `>=22.12.0 <27`，推荐使用 `UI2Code/.nvmrc` 中的版本。
- npm。
- `curl`、`lsof`（macOS 默认提供；Linux 请确认已安装）。

### 10.3 手动启动后端

新开一个终端：

```bash
cd server-demo
export JAVA_HOME=/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home
export PATH="$JAVA_HOME/bin:$PATH"
mvn test
mvn package -DskipTests
java -jar server-demo-main/target/server-demo-main-1.0.0.jar
```

验证：

```bash
curl http://127.0.0.1:8080/actuator/health
curl http://127.0.0.1:8080/order/hello
```

看到健康状态 `UP` 后再启动前端。

### 10.4 手动启动前端

再开一个终端：

```bash
cd UI2Code
npm install
npm run build
npm run preview -- --host 127.0.0.1 --port 5173
```

浏览器打开：`http://127.0.0.1:5173/`

本项目此前在开发服务器模式下出现过浏览器白屏，因此课题演示优先使用 `build + preview`。日常开发仍可使用：

```bash
npm run dev -- --host 127.0.0.1 --port 5173
```

如果后端不在 `8080`，修改 `UI2Code/.env`：

```dotenv
VITE_USE_MOCK=false
VITE_API_BASE_URL=
VITE_API_PROXY_TARGET=http://localhost:8080
```

修改 `.env` 后必须重启 Vite。

## 11. 建议的手工验收顺序

1. 商品管理页确认右上角显示“后端已联通 · H2 本地库”。
2. 点击“加载本地联通示例”，确认出现 3 个 SKU。
3. 对 `100018376` 执行播前排雷，确认命中“世界级”。
4. 生成脚本，确认出现 4 段话术。
5. 进入直播控制台，保留默认链接并点击“登记来源”。
6. 点击“注入示例评论”，确认右侧评论区出现评论和回答建议。
7. 确认或编辑回答，刷新后验证状态仍来自 H2。
8. 检查告警、价格雷达和指标展示。
9. 结束直播后进入智能切片，测试搜索、任务和剪辑工作台。
10. 进入数据中心，测试日期筛选、Excel 兼容导出和知识回流确认。
11. 打开 H2 控制台查看 `SKU`、`DANMAKU`、`SCRIPT`、`SCRIPT_NODE` 表。

## 12. 推荐的后续建设顺序

1. 先实现一个真实平台 Adapter，并给评论增加平台消息 ID、来源、事件时间和幂等约束。
2. 把 Excel/CSV 导入做实，补字段校验、错误明细和任务持久化。
3. 将前端固定 `sessionId/skuId` 改为可选择的真实场次上下文。
4. 接商品知识库与 RAG，要求答案带证据、置信度和人工确认。
5. 扩展合规规则、审核流和规则版本管理。
6. 接 WebSocket/SSE、ASR 音频流与脚本节点对齐。
7. 最后补鉴权、RBAC、审计、限流、监控及生产数据库迁移。
