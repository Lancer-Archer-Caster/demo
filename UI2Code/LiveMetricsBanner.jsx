import React, { useState, useEffect, useCallback } from 'react';
import { isMockMode, liveApi } from './api';

/**
 * =============================================
 * 数据接口定义 - LiveMetricsBanner
 * =============================================
 * 
 * 1. WebSocket 推送接口（实时告警）:
 *    接口路径: ws://api.live.jd.com/ws/alerts
 *    载荷格式: AlertMessage
 *    {
 *      type: 'alert',
 *      subType: 'inventory' | 'traffic_peak' | 'price_change',
 *      skuId: string,
 *      text: string,
 *      severity: 'critical' | 'warning' | 'info',
 *      timestamp: number
 *    }
 * 
 * 2. HTTP 接口（实时指标）:
 *    GET /api/v1/live/metrics?sessionId={sessionId}
 *    Response: LiveMetrics
 *    {
 *      onlineUsers: number,
 *      orderRate: number,        // 订单/分钟
 *      totalRevenue: number,     // 累计GMV（分）
 *      viewersTrend: 'rising' | 'stable' | 'declining'
 *    }
 * 
 * 3. 降级策略:
 *    - WebSocket 断连 > 5s: 显示"告警服务重连中"
 *    - 指标接口超时: 显示最近一次缓存数据
 */

// ============ 类型定义 ============

/**
 * @typedef {Object} AlertMessage
 * @property {'inventory'|'traffic_peak'|'price_change'} subType - 告警子类型
 * @property {string} skuId - 关联SKU
 * @property {string} text - 告警文本
 * @property {'critical'|'warning'|'info'} severity - 严重程度
 * @property {number} timestamp - 时间戳
 * @property {number} [remaining] - 库存剩余数（仅inventory类型）
 */

/**
 * @typedef {Object} LiveMetrics
 * @property {number} onlineUsers - 在线人数
 * @property {number} orderRate - 订单速率（单/分钟）
 * @property {number} totalRevenue - 累计GMV（分）
 * @property {'rising'|'stable'|'declining'} viewersTrend - 观众趋势
 */

// ============ Mock 数据 ============

const MOCK_ALERTS = [
  {
    id: 'alert-1',
    subType: 'inventory',
    skuId: '100018374',
    text: '警报：库存仅剩12件，准备切品！',
    severity: 'critical',
    timestamp: Date.now(),
    remaining: 12,
  },
  {
    id: 'alert-2',
    subType: 'traffic_peak',
    skuId: '',
    text: '流量达峰，请立即倒数3-2-1上链接促单！',
    severity: 'warning',
    timestamp: Date.now(),
  },
];

const MOCK_METRICS = {
  onlineUsers: 12853,
  orderRate: 23,
  totalRevenue: 3456000,
  viewersTrend: 'rising',
};

// ============ 数据获取钩子 ============

/**
 * useLiveAlerts - 实时告警数据钩子
 * 真实环境替换: WebSocket 订阅
 */
function useLiveAlerts(sessionId) {
  const [alerts, setAlerts] = useState([]);
  const [dismissedIds, setDismissedIds] = useState(new Set());
  const [wsStatus, setWsStatus] = useState(isMockMode ? 'connected' : 'reconnecting');

  useEffect(() => {
    if (isMockMode) {
      const timer = setTimeout(() => setAlerts(MOCK_ALERTS), 500);
      return () => clearTimeout(timer);
    }
    let cancelled = false;
    const poll = async () => {
      try {
        const data = await liveApi.getActiveAlerts(sessionId);
        if (!cancelled) {
          setAlerts((data || []).map((item) => ({
            id: item.id,
            subType: String(item.alertType || 'info').toLowerCase(),
            skuId: item.skuId || '',
            text: item.content || '',
            severity: String(item.level || '').toUpperCase() === 'CRITICAL' ? 'critical' : String(item.level || '').toUpperCase() === 'WARN' ? 'warning' : 'info',
            timestamp: item.createdAt,
          })));
          setWsStatus('connected');
        }
      } catch (_) {
        if (!cancelled) setWsStatus('reconnecting');
      }
    };
    poll();
    const interval = setInterval(poll, 5000);
    return () => { cancelled = true; clearInterval(interval); };
  }, [sessionId]);

  const dismissAlert = useCallback((id) => {
    setDismissedIds(prev => new Set([...prev, id]));
    if (!isMockMode) liveApi.dismissAlert(id).catch(() => {});
  }, []);

  const activeAlerts = alerts.filter(a => !dismissedIds.has(a.id));

  return { alerts: activeAlerts, dismissAlert, wsStatus };
}

/**
 * useLiveMetrics - 实时指标数据钩子
 * 真实环境替换: 轮询 HTTP API
 */
function useLiveMetrics(sessionId) {
  const [metrics, setMetrics] = useState(null);

  useEffect(() => {
    if (isMockMode) {
      const timer = setTimeout(() => setMetrics(MOCK_METRICS), 300);
      return () => clearTimeout(timer);
    }
    let cancelled = false;
    const poll = async () => {
      try {
        const data = await liveApi.getMetrics(sessionId);
        if (!cancelled) setMetrics(data);
      } catch (_) { /* 保留上一次本地指标 */ }
    };
    poll();
    const interval = setInterval(poll, 5000);
    return () => { cancelled = true; clearInterval(interval); };
  }, [sessionId]);

  return { metrics };
}

// ============ 组件 ============

function LiveMetricsBanner({ sessionId = 1 }) {
  const { alerts, dismissAlert, wsStatus } = useLiveAlerts(sessionId);
  const { metrics } = useLiveMetrics(sessionId);

  // 格式化数字
  const formatNum = (num) => {
    if (num >= 10000) return `${(num / 10000).toFixed(1)}万`;
    return num.toLocaleString();
  };

  const formatRevenue = (cents) => {
    const yuan = cents / 100;
    if (yuan >= 10000) return `¥${(yuan / 10000).toFixed(1)}万`;
    return `¥${yuan.toLocaleString()}`;
  };

  return (
    <div className="w-full flex flex-col space-y-[6px] shrink-0 mb-[12px]" data-ai-alt="生命体征监控区域">
      {/* 实时指标栏 */}
      {metrics && (
        <div className="flex items-center space-x-[16px] bg-gray-800/80 rounded-[8px] px-[16px] py-[6px]">
          <div className="flex items-center space-x-[4px]">
            <span className="w-[6px] h-[6px] rounded-full bg-green-400 animate-pulse"></span>
            <span className="text-[12px] text-gray-400">在线</span>
            <span className="text-[14px] text-white font-bold">{formatNum(metrics.onlineUsers)}</span>
            {metrics.viewersTrend === 'rising' && <i className="fas fa-arrow-up text-[10px] text-green-400"></i>}
            {metrics.viewersTrend === 'declining' && <i className="fas fa-arrow-down text-[10px] text-red-400"></i>}
          </div>
          <div className="w-[1px] h-[12px] bg-gray-600"></div>
          <div className="flex items-center space-x-[4px]">
            <i className="fas fa-shopping-cart text-[10px] text-blue-400"></i>
            <span className="text-[12px] text-gray-400">订单</span>
            <span className="text-[14px] text-white font-bold">{metrics.orderRate}<span className="text-[10px] text-gray-500">/min</span></span>
          </div>
          <div className="w-[1px] h-[12px] bg-gray-600"></div>
          <div className="flex items-center space-x-[4px]">
            <i className="fas fa-yen-sign text-[10px] text-yellow-400"></i>
            <span className="text-[12px] text-gray-400">GMV</span>
            <span className="text-[14px] text-white font-bold">{formatRevenue(metrics.totalRevenue)}</span>
          </div>
          <div className="flex-1"></div>
          <div className="flex items-center space-x-[4px]">
            <span className={`w-[6px] h-[6px] rounded-full ${wsStatus === 'connected' ? 'bg-green-400' : wsStatus === 'reconnecting' ? 'bg-yellow-400 animate-pulse' : 'bg-red-400'}`}></span>
            <span className="text-[11px] text-gray-500">{isMockMode ? '演示数据' : wsStatus === 'connected' ? '本地轮询已连接' : wsStatus === 'reconnecting' ? '重连中...' : '已断开'}</span>
          </div>
        </div>
      )}

      {/* 告警横幅 */}
      {alerts.map((alert) => (
        <div
          key={alert.id}
          className={`flex items-center px-[16px] py-[8px] rounded-[8px] shadow-sm ${
            alert.severity === 'critical'
              ? 'bg-red-500 text-white'
              : alert.severity === 'warning'
                ? 'bg-yellow-500/90 text-white'
                : 'bg-blue-500/80 text-white'
          }`}
          data-ai-changelog-id={alert.subType === 'inventory' ? 'live-alert-inventory' : 'live-alert-push'}
          data-ai-changelog-title={alert.subType === 'inventory' ? '库存熔断报警' : '逼单时机提示'}
          data-ai-changelog-desc={alert.subType === 'inventory' ? '库存<10%时跑马灯提示准备切品' : '在线人数达峰值且订单流速持平时提示促单'}
        >
          <i className={`fas ${
            alert.severity === 'critical' ? 'fa-exclamation-triangle' : 'fa-bolt'
          } mr-[12px] ${alert.severity === 'critical' ? 'animate-pulse' : ''}`}></i>
          {alert.severity === 'critical' ? (
            <div className="whitespace-nowrap animate-[marquee_10s_linear_infinite] text-[14px] font-bold flex-1">
              {alert.text}
            </div>
          ) : (
            <span className="text-[14px] font-bold flex-1">{alert.text}</span>
          )}
          <button
            className="ml-[12px] text-white/70 hover:text-white transition-colors text-[12px]"
            onClick={() => dismissAlert(alert.id)}
            title="关闭告警"
          >
            <i className="fas fa-times"></i>
          </button>
        </div>
      ))}

      {/* 无告警状态 */}
      {alerts.length === 0 && (
        <div className="bg-green-900/30 border border-green-500/30 text-green-400 px-[16px] py-[6px] rounded-[8px] flex items-center text-[13px]">
          <i className="fas fa-check-circle mr-[8px]"></i>
          <span>系统运行正常，暂无告警</span>
        </div>
      )}
    </div>
  );
}

export default LiveMetricsBanner;
