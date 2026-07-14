/**
 * The only place UI code talks to the backend.  Keep the page models here so
 * a later data source / model service only changes this adapter, not the UI.
 */
const config = import.meta.env;
const baseUrl = (config.VITE_API_BASE_URL || '').replace(/\/$/, '');

export const isMockMode = String(config.VITE_USE_MOCK ?? 'true') !== 'false';

function query(params) {
  const search = new URLSearchParams();
  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null) search.set(key, String(value));
  });
  return search.toString();
}

async function request(path, { method = 'GET', body, signal } = {}) {
  const response = await fetch(`${baseUrl}${path}`, {
    method,
    signal,
    headers: body ? { 'Content-Type': 'application/json' } : undefined,
    body: body ? JSON.stringify(body) : undefined,
  });
  if (!response.ok) throw new Error(`请求失败（${response.status}）`);
  const payload = await response.json();
  if (payload?.code !== undefined && payload.code !== 0) {
    throw new Error(payload.msg || '服务返回异常');
  }
  return payload?.data ?? payload;
}

async function upload(path, file, fields = {}, signal) {
  const body = new FormData();
  body.append('file', file);
  Object.entries(fields).forEach(([key, value]) => {
    if (value !== undefined && value !== null) body.append(key, String(value));
  });
  const response = await fetch(`${baseUrl}${path}`, { method: 'POST', body, signal });
  if (!response.ok) throw new Error(`上传失败（${response.status}）`);
  const payload = await response.json();
  if (payload?.code !== undefined && payload.code !== 0) throw new Error(payload.msg || '服务返回异常');
  return payload?.data ?? payload;
}

export const liveApi = {
  // 本地联通检查
  health: (signal) => request('/actuator/health', { signal }),

  // 场次与商品数据
  createSession: (payload, signal) => request('/session/create', { method: 'POST', body: payload, signal }),
  getSession: (sessionId, signal) => request(`/session/get?${query({ sessionId })}`, { signal }),
  listSessions: (payload = {}, signal) => request('/session/list', { method: 'POST', body: payload, signal }),
  batchSkus: (skuIds, signal) => request('/sku/batch', { method: 'POST', body: skuIds, signal }),
  checkCompliance: (payload, signal) => request('/compliance/check', { method: 'POST', body: payload, signal }),

  // 脚本、ASR 跟随与术语翻译
  generateScript: (payload, signal) => request('/script/generate', { method: 'POST', body: payload, signal }),
  getScriptNodes: (scriptId, signal) => request(`/script/nodes?${query({ scriptId })}`, { signal }),
  updateScriptNode: (nodeId, status, signal) => request(`/script/updateNodeStatus?${query({ nodeId, status })}`, { method: 'POST', signal }),
  translateJargon: (payload, signal) => request('/ai/translateJargon', { method: 'POST', body: payload, signal }),

  // 弹幕问答
  getDanmakuHistory: (sessionId, signal) => request(`/danmaku/history?${query({ sessionId })}`, { signal }),
  processDanmaku: (payload, signal) => request('/danmaku/process', { method: 'POST', body: payload, signal }),
  generateDanmakuAnswer: (danmakuId, signal) => request(`/danmaku/generateAnswer?${query({ danmakuId })}`, { method: 'POST', signal }),
  confirmDanmakuAnswer: (danmakuId, answer, signal) => request(`/danmaku/confirmAnswer?${query({ danmakuId, answer })}`, { method: 'POST', signal }),
  modifyDanmakuAnswer: (danmakuId, answer, signal) => request(`/danmaku/modifyAnswer?${query({ danmakuId, answer })}`, { method: 'POST', signal }),

  // 直播来源与统一评论入口。真实平台适配器最终也调用 ingestLiveComment。
  connectLiveSource: (payload, signal) => request('/live/source/connect', { method: 'POST', body: payload, signal }),
  getLiveSourceStatus: (sessionId, signal) => request(`/live/source/status?${query({ sessionId })}`, { signal }),
  disconnectLiveSource: (sessionId, signal) => request(`/live/source/disconnect?${query({ sessionId })}`, { method: 'POST', signal }),
  ingestLiveComment: (payload, signal) => request('/live/comments/ingest', { method: 'POST', body: payload, signal }),
  injectDemoComment: (sessionId, signal) => request(`/live/comments/demo?${query({ sessionId })}`, { method: 'POST', signal }),

  // 比价、告警与指标
  comparePrice: (payload, signal) => request('/price/compare', { method: 'POST', body: payload, signal }),
  getCompetitorPrices: (skuId, signal) => request(`/price/competitors?${query({ skuId })}`, { signal }),
  generatePriceTalkpoint: (skuId, signal) => request(`/price/talkpoint?${query({ skuId })}`, { method: 'POST', signal }),
  getActiveAlerts: (sessionId, signal) => request(`/alert/active?${query({ sessionId })}`, { signal }),
  dismissAlert: (alertId, signal) => request(`/alert/dismiss?${query({ alertId })}`, { method: 'POST', signal }),

  // 复盘与异步任务
  getReview: (sessionId, signal) => request('/review/session', { method: 'POST', body: { sessionId }, signal }),
  backflowKnowledge: (sessionId, signal) => request(`/review/backflow?${query({ sessionId })}`, { method: 'POST', signal }),
  exportReview: (sessionId, signal) => request(`/review/export?${query({ sessionId })}`, { signal }),
  getMetrics: (sessionId, signal) => request(`/api/v1/live/metrics?${query({ sessionId })}`, { signal }),
  submitScriptTask: (payload, signal) => request('/api/v1/tasks/script', { method: 'POST', body: payload, signal }),
  getTask: (taskId, signal) => request(`/api/v1/tasks/${encodeURIComponent(taskId)}`, { signal }),
  importSkuFile: (file, sessionId, signal) => upload('/api/v1/sku/import', file, { sessionId }, signal),

  // WIP 产品骨架：数据/算法由 Adapter 接入，当前后端返回 Fixture。
  getDataCenterOverview: (sessionId, date, signal) => request(`/api/v1/data-center/overview?${query({ sessionId, date })}`, { signal }),
  listReplays: (signal) => request('/api/v1/replays', { signal }),
  startReplayClip: (replayId, signal) => request(`/api/v1/replays/${encodeURIComponent(replayId)}/clip`, { method: 'POST', signal }),
  deleteReplay: (replayId, signal) => request(`/api/v1/replays/${encodeURIComponent(replayId)}`, { method: 'DELETE', signal }),
  saveLivePreparation: (payload, signal) => request('/api/v1/live/preparation', { method: 'POST', body: payload, signal }),
  controlLive: (payload, signal) => request('/api/v1/live/control', { method: 'POST', body: payload, signal }),
};

export function toUiSku(sku) {
  return {
    id: String(sku.skuId ?? sku.id ?? ''),
    name: sku.skuName ?? sku.name ?? '',
    price: Number(sku.price ?? 0),
    image: sku.mainImage ?? sku.image ?? '',
    params: Array.isArray(sku.params)
      ? sku.params.map((item) => `${item.name ?? item.key ?? ''} ${item.value ?? ''}`.trim()).join(' | ')
      : (sku.params ?? ''),
    status: sku.status ?? 'normal',
  };
}
