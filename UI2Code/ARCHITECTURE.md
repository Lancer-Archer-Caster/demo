# JoyCue 前端架构文档

> 位置：`UI2Code`。本文以 WIP v3 对齐后的当前源码为准，列出页面、组件、函数、状态和 API 调用点。

## 1. 技术与数据流

- React 19.2.7、Vite 8.1.4、Tailwind CSS 3.4.17。
- Font Awesome 从本地依赖加载，构建目标为 ES2018 / Safari 13。
- 不使用路由和全局状态库；`App` 管理页面，组件使用 Hooks。
- 所有请求必须经过 `api.js`。

```text
UI event -> component handle* -> liveApi -> request/upload
 -> Vite proxy -> Spring Boot -> Result.data -> component state
```

## 2. 文件与装配

| 文件 | 组件/函数 | 调用位置 |
| --- | --- | --- |
| `main.jsx` | React `createRoot`、全局样式/图标 | `index.html` 入口 |
| `index.jsx` | `App`、`NAV_ITEMS` | 装配全部页面 |
| `api.js` | `query`、`request`、`upload`、`liveApi`、`toUiSku` | 所有业务组件 |
| `SkuManage.jsx` | `SkuManage` | 商品管理 / JoyCard |
| `LiveConsole.jsx` | `LivePreparation`、`LiveConsole` | 直播前准备与直播中控 |
| `LiveSourceConnector.jsx` | `LiveSourceConnector` | 直播来源配置 |
| `LiveMetricsBanner.jsx` | `useLiveAlerts`、`useLiveMetrics`、`LiveMetricsBanner` | JoyGuard 指标/告警 |
| `PriceRadar.jsx` | `usePriceRadarData`、`PriceRadar` | JoyGuard 比价/参数/话术 |
| `Teleprompter.jsx` | `Teleprompter` | 动态提词 |
| `GeekBulletScreen.jsx` | `toUiDanmaku`、`GeekBulletScreen` | JoyRadar 评论 |
| `DataAnalysis.jsx` | `useReportData`、`DataAnalysis` | 数据中心 |
| `SmartClip.jsx` | `SmartClip` | JoyClip 回放列表 |
| `ClipWorkbench.jsx` | `ClipWorkbench` | 智能剪辑工作台 |
| `HomeDashboard.jsx` | `HomeDashboard` | 早期保留组件，当前未渲染 |

## 3. App 与全局导航

`App` 状态：

- `currentPage`：`skuManage/liveConsole/videoClip/clipWorkbench/dataCenter`。
- `isSidebarCollapsed`：导航收起状态。
- `activeReplayId`：进入剪辑工作台的回放 ID。

`App` 的三个 `useEffect`：

1. 从根节点 `data-page-key` 同步页面。
2. 暴露 `window.__setCurrentPage(pageKey)` 给验收脚本。
3. `MutationObserver` 监听页面 key 外部变化。

页面装配：

```text
skuManage    -> SkuManage(onImportToLive)
liveConsole  -> LiveConsole
videoClip    -> SmartClip(onOpenWorkbench)
clipWorkbench-> ClipWorkbench(replayId,onBack)
dataCenter   -> DataAnalysis
```

未知页面显示占位，不白屏。正式产品可把 `currentPage` 替换为 React Router。

## 4. API 层

### 4.1 基础方法

| 方法 | 作用 |
| --- | --- |
| `query(params)` | 过滤空值并生成查询串 |
| `request(path, options)` | JSON fetch、HTTP/业务码检查、`data` 解包 |
| `upload(path, file, fields)` | `FormData` 上传 |
| `toUiSku(sku)` | 后端 SKU DTO 转页面模型 |
| `isMockMode` | 根据 `VITE_USE_MOCK` 切换 Fixture 或后端 |

### 4.2 `liveApi` 与调用点

| 方法 | HTTP | 调用点 |
| --- | --- | --- |
| `health` | `/actuator/health` | `SkuManage` 挂载 |
| `batchSkus` | `POST /sku/batch` | `handleAddSku`、`handleLoadLocalDemo` |
| `checkCompliance` | `POST /compliance/check` | `handleComplianceCheck` |
| `generateScript` | `POST /script/generate` | `handleGenerateScript` |
| `importSkuFile` | `POST /api/v1/sku/import` | `handleFileChange` |
| `saveLivePreparation` | `POST /api/v1/live/preparation` | `LivePreparation.start` |
| `controlLive` | `POST /api/v1/live/control` | `LiveConsole.control` |
| `connectLiveSource/getStatus/disconnect` | `/live/source/*` | `LiveSourceConnector` |
| `injectDemoComment` | `POST /live/comments/demo` | `handleInject` |
| `ingestLiveComment` | `POST /live/comments/ingest` | 外部 Adapter/测试使用 |
| `getDanmakuHistory` | `GET /danmaku/history` | JoyRadar 每 2 秒轮询 |
| `confirm/modifyDanmakuAnswer` | `/danmaku/confirmAnswer`、`modifyAnswer` | 评论卡操作 |
| `getActiveAlerts/dismissAlert` | `/alert/active`、`dismiss` | JoyGuard 每 5 秒轮询 |
| `getMetrics` | `/api/v1/live/metrics` | 指标每 5 秒轮询 |
| `getCompetitorPrices/generatePriceTalkpoint` | `/price/*` | `usePriceRadarData` |
| `getReview/backflow/exportReview` | `/review/*` | `DataAnalysis` |
| `getDataCenterOverview` | `/api/v1/data-center/overview` | 已定义，真实聚合接入点 |
| `listReplays` | `GET /api/v1/replays` | `SmartClip` 挂载 |
| `startReplayClip` | `POST /api/v1/replays/{id}/clip` | `SmartClip.startClip` |
| `deleteReplay` | `DELETE /api/v1/replays/{id}` | `SmartClip.removeReplay` |
| `submitScriptTask/getTask` | `/api/v1/tasks/*` | 长任务扩展接口 |

## 5. JoyCard / 商品管理

`SkuManage` 状态：`skuList`、`skuInput`、`isThinking`、`thinkingStep`、`complianceResults`、`generatedScript`、`activeTab`、`backendStatus`。

| 函数 | 作用 | 下游 |
| --- | --- | --- |
| `parseSkuInput` | 多行文本解析 | `handleAddSku` |
| `handleAddSku` | 查询并追加商品 | `batchSkus` |
| `handleRemoveSku` | 页面移除商品 | React state |
| `handleLoadLocalDemo` | 加载三个种子 SKU | `batchSkus` |
| `handleExcelImport` | 打开文件选择 | DOM ref |
| `handleFileChange` | 创建导入任务 | `importSkuFile` |
| `handleComplianceCheck` | 播前排雷 | `checkCompliance` |
| `handleGenerateScript` | 思考状态 + 脚本生成 | `generateScript` |
| `updateScriptSection` | 更新卡片文本/选中 | `generatedScript.sections` |
| `exportWord` | 选中卡片生成 Word 兼容 Blob | 浏览器下载 |
| `importToLive` | 写入直播上下文并跳页 | `sessionStorage`、`onImportToLive` |

`finalScript` 由所有 `selected !== false` 卡片按顺序实时拼接。跨页键为 `joycue.live.script`。

## 6. 直播准备与中控

### 6.1 `LivePreparation`

状态：`sceneId`、`roomName`、`liveTime`、`description`。商品和脚本优先读取 JoyCard 导入上下文，否则使用 Fixture。

`start()` 组装 `{sessionId, roomName, liveTime, description, sceneId, productIds, fullScript}`，调用 `saveLivePreparation`，成功后由父组件切到中控 Tab。

### 6.2 `LiveConsole`

状态：

- `activeTab`：`prep/console`。
- `connection`：直播来源状态。
- `commentRefresh`：评论注入刷新令牌。
- `liveStatus`：`live/paused/ended`。
- `casting`：提词器投屏状态。
- `currentProductIndex`：当前讲解商品。

`control(action)` 更新本地 UI 并调用 `controlLive`。上一/下一商品在首尾禁用。

### 6.3 子组件

- `LiveSourceConnector.run` 统一 loading/错误/状态；`handleConnect/Disconnect/Inject` 调用来源 API。
- `useLiveAlerts` 和 `useLiveMetrics` 每 5 秒刷新。
- `usePriceRadarData` 并行读取竞品价和话术，失败隐藏结论。
- `Teleprompter.handleNodeClick` 手动推进节点；`handleJargonClick` 打开解释。
- JoyRadar 每 2 秒读取历史，`handleConfirmSend/handleEditSend` 写回后端；口播标记和隐藏水贴当前只改 UI。

## 7. 数据中心

`useReportData` 调 `/review/session`，失败使用 `MOCK_REPORT`。`DataAnalysis` 维护日期、标签页、知识审核、导出和同步状态。

主要函数：

- `formatGMV`：分转展示金额。
- `handleApprove`：本地审核知识项。
- `handleExport`：调用报告接口并下载 Excel 兼容 CSV。
- `handleSync`：调用知识回流确认接口。

数据总览已包含核心指标、流量漏斗、TOP 商品、经营诊断、用户互动、主播表现、回放切片和数据对比 Fixture。真实数据由 `getDataCenterOverview` 接入。

## 8. JoyClip

### 8.1 `SmartClip`

状态：`replays`、`query`。派生值：`visible` 和各状态 `counts`。

| 函数 | 作用 |
| --- | --- |
| 挂载 `useEffect` | 调 `listReplays`，失败保留 Fixture |
| `startClip` | 先乐观置 `processing`，再调 `startReplayClip` |
| `removeReplay` | 确认后移除并调 `deleteReplay` |
| `onOpenWorkbench` | 将 replayId 交给 `App` 并进入工作台 |

### 8.2 `ClipWorkbench`

状态：`messages`、`input`、`activeTab`、`videos`、`images`。

- `send`：空输入不发送；Enter 发送，Shift+Enter 换行。
- 素材删除只影响当前列表。
- `exportAll` 下载包含 replayId、视频和图片清单的 JSON。
- 中间 9:16 预览是 Fixture；真实媒体播放和生成由视频 Adapter 接入。

## 9. 环境与构建

`.env`：

```dotenv
VITE_USE_MOCK=false
VITE_API_BASE_URL=
VITE_API_PROXY_TARGET=http://localhost:8080
```

`false` 表示调用本地后端；`true` 表示纯前端 Fixture。Vite 代理路径集中在 `vite.config.js`。

```bash
npm run build
npm run preview -- --host 127.0.0.1 --port 5173
```

根目录推荐 `./start.sh start` 一键启动。

## 10. 接入边界

- 商品、指标、回放和算法结果必须在 `api.js` 映射，组件不得感知供应商 SDK。
- ASR、RAG、诊断和切片尚未实现，UI 必须显示 Fixture/待接入状态。
- 当前轮询可替换为统一事件 Hook；页面状态模型保持不变。
- 正式产品需补 Router、错误边界、Toast、鉴权、权限和响应式移动端策略。

