# JoyCue 数据与算法接入约定

> 当前状态：前端 Mock 演示和本地 Spring Boot/H2 后端均已验证；基础表与种子数据会自动初始化。继续联调主要需要补充业务数据、算法实现和直播页面 API 绑定。完整字段见 `docs/DATA_CONTRACTS.md`，算法清单见 `docs/ALGORITHM_REQUIREMENTS.md`。

## 运行模式

- `UI2Code/.env` 中 `VITE_USE_MOCK=true`：页面使用内置演示数据，可脱离服务端展示。
- `VITE_USE_MOCK=false`：页面只经由 `UI2Code/api.js` 访问后端。禁止在页面组件中直接 `fetch`。

演示环境推荐在项目根目录运行 `./start.sh start`；脚本会完成后端构建、健康检查、前端构建和预览服务启动。仅做前端开发时，再使用 `cd UI2Code && npm install && npm run dev`。

## 稳定边界

| 领域 | 页面调用 | 当前后端接口 | 后续替换点 |
| --- | --- | --- | --- |
| 商品 | `liveApi.batchSkus`、`importSkuFile` | `/sku/batch`、`/api/v1/sku/import` | 商品中心/文件解析适配器 |
| 合规 | `liveApi.checkCompliance` | `/compliance/check` | 词库、规则引擎或审核模型 |
| 话术 | `liveApi.generateScript`、`submitScriptTask` | `/script/generate`、`/api/v1/tasks/script` | LLM/RAG 编排服务 |
| 实时指标 | `liveApi.getMetrics` | `/api/v1/live/metrics` | 实时数仓、流服务、WebSocket/SSE |
| 复盘 | `liveApi.getReview` | `/review/session` | 数仓聚合及知识库回流 |
| 数据中心 | `liveApi.getDataCenterOverview` | `/api/v1/data-center/overview` | 交易、流量、库存、售后与诊断 Adapter |
| 回放切片 | `liveApi.listReplays`、`startReplayClip` | `/api/v1/replays*` | 回放库与切片算法 Adapter |
| 直播准备 | `liveApi.saveLivePreparation`、`controlLive` | `/api/v1/live/preparation`、`/api/v1/live/control` | 场次持久化与直播平台 Adapter |

## 异步任务契约

模型推理、Excel 导入、报告导出这类操作统一使用：

1. 调用当前已提供的 `POST /api/v1/tasks/script`、`POST /api/v1/sku/import` 或对应业务提交接口，返回 `taskId`。
2. 前端轮询 `GET /api/v1/tasks/{taskId}`，或订阅同一任务的 SSE/WebSocket 事件。
3. 状态仅使用 `PENDING`、`RUNNING`、`SUCCEEDED`、`FAILED`、`CANCELLED`。
4. 失败写入可展示的 `message` 和可追踪的 `traceId`，不要把模型异常直接暴露给页面。

当前 `IntegrationTaskController` 以 `source: demo` 和立即完成的任务验证契约。真实接入时，保留 URL、字段和状态值，将创建任务的实现换为消息队列或 HTTP 算法客户端；回调再更新任务状态与 `result`。

## 数据与模型的责任边界

- `web`：参数校验、协议转换、返回任务 ID；不写模型提示词和数据源访问。
- `app/domain`：编排业务流程、保存任务状态、控制幂等。
- `infra`：分别实现 `ProductDataGateway`、`ComplianceGateway`、`AlgorithmGateway`、`MetricsGateway`；每个真实供应方一个 adapter。
- 真实凭证仅放在环境变量或配置中心，不能放进前端 `.env` 或源码。

## 建议的接入顺序

1. 商品查询/导入和场次数据；
2. 合规规则与词库；
3. 话术生成和弹幕 RAG；
4. 指标、告警、ASR 的实时流；
5. 复盘计算与知识回流。

## 上线切换检查

1. 按需修改 `application.properties` 或通过环境变量覆盖本地配置；
2. 在 H2 中补充演示所需的商品、场次和直播数据；
3. 在 `infra-impl` 实现真实数据与算法 adapter；
4. 将前端实时页面的数据 Hook 接到 `liveApi`，保留 Mock 作为降级；
5. 设置 `VITE_USE_MOCK=false` 和实际 API 地址；
6. 执行 Maven 测试、前端构建和一场端到端回放；
7. 对模型超时、空数据、断流、重复事件和错误价格做降级演练。
