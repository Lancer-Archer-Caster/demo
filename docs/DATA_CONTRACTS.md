# 数据接入格式

## 通用约定

- 编码：UTF-8。
- ID：字符串，避免 JavaScript 大整数精度问题。
- 金额：`priceFen`、`amountFen`，类型为整数，单位为分。
- 时间：ISO 8601，例如 `2026-07-14T14:32:05+08:00`。
- 比率：使用百分数值，例如 `4.2` 表示 `4.2%`。
- 实时事件必须包含 `eventId`，用于幂等去重。
- HTTP 返回统一为 `{ "code": 0, "data": ..., "msg": "" }`。

## 1. 直播场次

```json
{
  "sessionId": "live-20260714-001",
  "sessionCode": "20260714-phone",
  "title": "5G 智能手机专场",
  "anchorName": "主播A",
  "operator": "采销A",
  "status": "LIVE",
  "plannedStartTime": "2026-07-14T14:00:00+08:00",
  "plannedEndTime": "2026-07-14T16:00:00+08:00"
}
```

必填：`sessionId`、`title`、`status`。状态建议固定为 `DRAFT/READY/LIVE/FINISHED/CANCELLED`。

## 2. 商品与 SKU

批量文件推荐 CSV；Excel 也必须使用相同表头：

```csv
skuId,skuName,brand,category,priceFen,stock,mainImage,paramsJson,updatedAt
100018374,5G智能手机,示例品牌,手机,299900,120,https://example/image.jpg,"{""处理器"":""骁龙8 Gen3"",""电池"":""5000mAh""}",2026-07-14T13:50:00+08:00
```

必填：`skuId`、`skuName`、`priceFen`、`stock`。`paramsJson` 是扁平 JSON 对象，值尽量保持原始单位。

JSON 接口格式：

```json
{
  "skuId": "100018374",
  "skuName": "5G智能手机",
  "brand": "示例品牌",
  "category": "手机",
  "priceFen": 299900,
  "stock": 120,
  "mainImage": "https://example/image.jpg",
  "params": { "处理器": "骁龙8 Gen3", "屏幕": "120Hz OLED", "电池": "5000mAh" }
}
```

## 3. 竞品价格快照

```json
{
  "eventId": "price-00001",
  "sessionId": "live-20260714-001",
  "skuId": "100018374",
  "platform": "competitor-a",
  "competitorProductId": "A99881",
  "productName": "同款5G智能手机",
  "priceFen": 314900,
  "promotionText": "满3000减100",
  "productUrl": "https://example/product/A99881",
  "capturedAt": "2026-07-14T14:30:00+08:00",
  "matchConfidence": 0.96
}
```

必填：`eventId`、`skuId`、`platform`、`priceFen`、`capturedAt`。`matchConfidence` 用于过滤错配商品和异常低价。

## 4. 弹幕事件

```json
{
  "eventId": "dm-10001",
  "sessionId": "live-20260714-001",
  "userId": "u-8821",
  "userNick": "数码控",
  "text": "这个屏幕刷新率是多少？",
  "sentAt": "2026-07-14T14:32:05+08:00"
}
```

必填：`eventId`、`sessionId`、`text`、`sentAt`。用户 ID/昵称进入模型前应脱敏。

## 5. ASR 片段

```json
{
  "eventId": "asr-3201",
  "sessionId": "live-20260714-001",
  "segmentId": "seg-3201",
  "text": "这款手机采用骁龙八第三代处理器",
  "startMs": 1920000,
  "endMs": 1924200,
  "confidence": 0.93,
  "isFinal": true,
  "createdAt": "2026-07-14T14:32:04+08:00"
}
```

只有 `isFinal=true` 的片段进入知识回流；临时片段可用于提词器低延迟跟随。

## 6. 直播指标与订单/库存事件

指标快照：

```json
{
  "eventId": "metric-140032",
  "sessionId": "live-20260714-001",
  "timestamp": "2026-07-14T14:00:32+08:00",
  "onlineUsers": 12853,
  "orderRate": 23.0,
  "totalRevenueFen": 3456000,
  "viewersTrend": "RISING"
}
```

订单事件：

```json
{
  "eventId": "order-evt-1",
  "sessionId": "live-20260714-001",
  "orderId": "order-90001",
  "skuId": "100018374",
  "amountFen": 299900,
  "status": "PAID",
  "occurredAt": "2026-07-14T14:01:02+08:00"
}
```

库存事件：

```json
{
  "eventId": "stock-evt-1",
  "sessionId": "live-20260714-001",
  "skuId": "100018374",
  "availableStock": 12,
  "initialStock": 120,
  "occurredAt": "2026-07-14T14:05:00+08:00"
}
```

## 7. 合规规则库

```json
{
  "ruleId": "rule-extreme-001",
  "pattern": "绝无仅有",
  "matchType": "KEYWORD",
  "category": "EXTREME_WORD",
  "severity": "HIGH",
  "suggestion": "改为：具有明显优势",
  "enabled": true,
  "effectiveFrom": "2026-01-01T00:00:00+08:00"
}
```

`matchType` 建议支持 `KEYWORD/REGEX/SEMANTIC`；规则必须可版本化和回溯。

## 8. RAG 知识库文档

```json
{
  "docId": "kb-100018374-nfc",
  "skuId": "100018374",
  "title": "NFC 功能说明",
  "content": "该型号支持 NFC，可用于交通卡、门禁卡和支付。",
  "source": "商品参数中心",
  "sourceUrl": "jd://product/100018374",
  "version": "20260714-1",
  "updatedAt": "2026-07-14T13:00:00+08:00"
}
```

必须保留 `source`、`version`、`updatedAt`，以便回答展示来源并淘汰过期知识。

## 最小数据集

完成一场可验证 demo，至少准备：1 个直播场次、3～5 个 SKU、每个 SKU 10 个以上参数、2 个竞品平台价格、50 条合规规则、每个 SKU 10 条知识、100 条弹幕样本、完整 ASR 样本、5 分钟粒度或更细的指标/订单/库存事件。
