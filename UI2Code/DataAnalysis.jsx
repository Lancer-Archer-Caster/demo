import React, { useState, useEffect } from 'react';
import { isMockMode, liveApi } from './api';

/**
 * =============================================
 * 数据接口定义 - DataAnalysis (复盘报告)
 * =============================================
 * 
 * 1. 复盘报告接口:
 *    GET /api/v1/report/session/{sessionId}
 *    Response: SessionReport
 * 
 * 2. 知识回流接口:
 *    POST /api/v1/knowledge/backflow
 *    Body: { sessionId, items: KnowledgeBackflowItem[] }
 * 
 * 3. 数据结构:
 *    SessionReport {
 *      sessionId, sessionName, date,
 *      metrics: { totalGMV, totalOrders, avgOrderValue, refundRate, ... },
 *      alerts: AlertSummary[],
 *      danmaku: DanmakuSummary,
 *      knowledgeItems: KnowledgeBackflowItem[],
 *      timeline: TimelineEvent[]
 *    }
 */

// ============ Mock 数据 ============

const MOCK_REPORT = {
  sessionId: 'live-20260713-001',
  sessionName: '5G智能手机专场',
  date: '2026-07-13',
  metrics: {
    totalGMV: 1200500,
    totalOrders: 3456,
    avgOrderValue: 34730,
    refundRate: 2.5,
    peakViewers: 12853,
    avgViewers: 8420,
    totalWatchTime: 10800,
    conversionRate: 4.2,
  },
  metricsTrend: {
    totalGMV: { value: 15.2, direction: 'up' },
    totalOrders: { value: 5.4, direction: 'up' },
    avgOrderValue: { value: 1.2, direction: 'down' },
    refundRate: { value: 0.3, direction: 'down' },
  },
  alertSummary: {
    total: 8,
    inventory: 3,
    trafficPeak: 2,
    priceChange: 3,
    handled: 7,
    missed: 1,
  },
  danmakuSummary: {
    total: 5230,
    filtered: 3812,
    aiAnswered: 856,
    broadcastAnswered: 124,
    aiInterceptionRate: 72.9,
  },
  knowledgeItems: [
    { id: 'k1', question: 'DDR5内存频率多少？', originalAnswer: null, finalAnswer: 'DDR5 4800Mbps起步，比DDR4快一倍', source: '主播口播', approved: false },
    { id: 'k2', question: '支持NFC吗？', originalAnswer: null, finalAnswer: '支持NFC，手机一碰支付', source: '主播口播', approved: false },
    { id: 'k3', question: '续航实测多久？', originalAnswer: '5000mAh电池日常约1.5天', finalAnswer: '5000mAh电池日常约1.5天，重度使用也能撑一天', source: 'AI回复+主播修改', approved: false },
  ],
  timeline: [
    { time: '14:00', event: '直播开始', type: 'start' },
    { time: '14:05', event: '库存告警：SKU 100018374 剩余12件', type: 'alert' },
    { time: '14:12', event: '流量达峰 12853人，触发逼单', type: 'peak' },
    { time: '14:18', event: 'AI弹幕回复 156 条', type: 'ai' },
    { time: '14:30', event: '价格优势触发：京东价低于竞品', type: 'price' },
    { time: '15:00', event: '直播结束', type: 'end' },
  ],
};

// ============ 数据钩子 ============

function useReportData(sessionId = 1) {
  const [report, setReport] = useState(null);
  const [loading, setLoading] = useState(true);
  const [source, setSource] = useState(isMockMode ? 'mock' : 'local-api');

  useEffect(() => {
    if (isMockMode) {
      const timer = setTimeout(() => {
        setReport(MOCK_REPORT);
        setLoading(false);
      }, 500);
      return () => clearTimeout(timer);
    }
    let cancelled = false;
    liveApi.getReview(sessionId)
      .then((remote) => {
        if (cancelled) return;
        setReport({
          ...MOCK_REPORT,
          sessionId: remote.sessionId || MOCK_REPORT.sessionId,
          sessionName: remote.sessionName || MOCK_REPORT.sessionName,
          date: String(remote.date || MOCK_REPORT.date).slice(0, 10),
          metrics: { ...MOCK_REPORT.metrics, ...(remote.metrics || {}) },
          knowledgeItems: MOCK_REPORT.knowledgeItems,
          timeline: Array.isArray(remote.timeline) && remote.timeline.length ? remote.timeline : MOCK_REPORT.timeline,
        });
        setSource('local-api');
      })
      .catch(() => {
        if (!cancelled) {
          setReport(MOCK_REPORT);
          setSource('fallback');
        }
      })
      .finally(() => { if (!cancelled) setLoading(false); });
    return () => { cancelled = true; };
  }, [sessionId]);

  return { report, loading, source };
}

// ============ 组件 ============

function DataAnalysis() {
  const { report, loading, source } = useReportData(1);
  const [activeTab, setActiveTab] = useState('overview'); // overview | alerts | danmaku | knowledge | timeline
  const [approvedIds, setApprovedIds] = useState(new Set());
  const [exporting, setExporting] = useState(false);
  const [synced, setSynced] = useState(false);
  const [selectedDate, setSelectedDate] = useState('2026-07-14');

  const formatGMV = (cents) => {
    const yuan = cents / 100;
    if (yuan >= 10000) return `¥${(yuan / 10000).toFixed(1)}万`;
    return `¥${yuan.toLocaleString()}`;
  };

  const handleApprove = (id) => {
    setApprovedIds(prev => new Set([...prev, id]));
  };

  const handleExport = async () => {
    setExporting(true);
    try {
      const content = isMockMode ? '指标,数值\nGMV,12005\n订单数,3456\n转化率,4.2%' : await liveApi.exportReview(1);
      const blob = new Blob(['\ufeff', content], { type: 'text/csv;charset=utf-8' });
      const url = URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `JoyCue数据中心-${selectedDate}.csv`;
      link.click();
      URL.revokeObjectURL(url);
    } catch (error) {
      window.alert(`导出失败：${error.message}`);
    } finally {
      setExporting(false);
    }
  };

  const handleSync = async () => {
    try {
      if (!isMockMode) await liveApi.backflowKnowledge(1);
      setSynced(true);
    } catch (error) {
      window.alert(`知识回流失败：${error.message}`);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-full">
        <i className="fas fa-spinner fa-spin text-purple-400 text-[24px] mr-[12px]"></i>
        <span className="text-gray-500">加载复盘数据...</span>
      </div>
    );
  }

  if (!report) return null;

  return (
    <div className="flex flex-col h-full w-full space-y-[16px]" data-ai-alt="复盘报告页">
      {/* 标题栏 */}
      <div className="bg-white/60 backdrop-blur-md border border-white/50 rounded-[16px] p-[20px] shadow-sm">
        <div className="flex items-center justify-between">
          <div>
            <h2 className="text-[20px] font-bold text-gray-800">直播数据中心</h2>
            <p className="text-[13px] text-gray-500 mt-[4px]">JoyCue 经营闭环 · 场次 {report.sessionId}</p>
          </div>
          <div className="flex items-center space-x-[12px]">
            <input type="date" value={selectedDate} onChange={(event) => setSelectedDate(event.target.value)} className="border border-gray-200 bg-white rounded-[8px] px-[10px] py-[7px] text-[12px]" />
            <span className="text-[11px] text-green-600 bg-green-50 px-[8px] py-[4px] rounded">{source === 'local-api' ? 'Fixture API 已接入' : source === 'fallback' ? '降级样例数据' : 'Mock数据'}</span>
            <button onClick={handleExport} disabled={exporting} className="bg-white border border-gray-300 text-gray-700 px-[16px] py-[8px] rounded-[8px] hover:bg-gray-50 text-[13px] shadow-sm">
              <i className="fas fa-file-excel mr-[6px]"></i>{exporting ? '导出中...' : '导出Excel'}
            </button>
            <button onClick={handleSync} disabled={synced} className="bg-gradient-to-r from-blue-500 to-purple-600 text-white px-[16px] py-[8px] rounded-[8px] hover:opacity-90 text-[13px] shadow-md disabled:opacity-60">
              <i className={`fas ${synced ? 'fa-check' : 'fa-sync'} mr-[6px]`}></i>{synced ? '已回流本地知识库' : '同步知识库'}
            </button>
          </div>
        </div>
      </div>

      {/* Tab导航 */}
      <div className="flex space-x-[8px]">
        {[
          { key: 'overview', label: '数据总览', icon: 'fa-chart-pie' },
          { key: 'alerts', label: '告警复盘', icon: 'fa-bell' },
          { key: 'danmaku', label: '弹幕分析', icon: 'fa-comments' },
          { key: 'knowledge', label: '知识回流', icon: 'fa-book' },
          { key: 'timeline', label: '时间线', icon: 'fa-stream' },
        ].map(tab => (
          <button
            key={tab.key}
            className={`px-[14px] py-[8px] rounded-[8px] text-[13px] font-medium transition-colors ${
              activeTab === tab.key
                ? 'bg-gradient-to-r from-blue-500 to-purple-600 text-white shadow-md'
                : 'bg-white/60 text-gray-600 hover:bg-white/80 border border-white/50'
            }`}
            onClick={() => setActiveTab(tab.key)}
          >
            <i className={`fas ${tab.icon} mr-[6px]`}></i>{tab.label}
          </button>
        ))}
      </div>

      {/* Tab内容 */}
      <div className="flex-1 bg-white/60 backdrop-blur-md border border-white/50 rounded-[16px] p-[24px] shadow-sm overflow-y-auto">
        {/* 数据总览 */}
        {activeTab === 'overview' && (
          <div className="space-y-[20px]">
            <div className="grid grid-cols-4 gap-[16px]">
              {[
                { label: '总GMV', val: formatGMV(report.metrics.totalGMV), trend: report.metricsTrend.totalGMV, icon: 'fa-yen-sign', color: 'blue' },
                { label: '订单数', val: report.metrics.totalOrders.toLocaleString(), trend: report.metricsTrend.totalOrders, icon: 'fa-shopping-cart', color: 'green' },
                { label: '客单价', val: formatGMV(report.metrics.avgOrderValue), trend: report.metricsTrend.avgOrderValue, icon: 'fa-receipt', color: 'purple' },
                { label: '退款率', val: `${report.metrics.refundRate}%`, trend: report.metricsTrend.refundRate, icon: 'fa-undo', color: 'red' },
              ].map((item, idx) => (
                <div key={idx} className="bg-white p-[20px] rounded-[12px] shadow-sm flex flex-col">
                  <div className="flex items-center justify-between mb-[8px]">
                    <span className="text-gray-500 text-[13px]">{item.label}</span>
                    <i className={`fas ${item.icon} text-${item.color}-400 text-[14px]`}></i>
                  </div>
                  <div className="text-[24px] font-bold">{item.val}</div>
                  <div className={`text-[12px] mt-[8px] ${item.trend.direction === 'up' ? 'text-green-500' : 'text-red-500'}`}>
                    {item.trend.direction === 'up' ? <i className="fas fa-arrow-up mr-[4px]"></i> : <i className="fas fa-arrow-down mr-[4px]"></i>}
                    {item.trend.value}% 同比上周
                  </div>
                </div>
              ))}
            </div>
            <div className="grid grid-cols-3 gap-[16px]">
              <div className="bg-white rounded-[12px] p-[16px] shadow-sm">
                <span className="text-gray-500 text-[13px]">峰值观众</span>
                <div className="text-[20px] font-bold mt-[4px]">{report.metrics.peakViewers.toLocaleString()}</div>
              </div>
              <div className="bg-white rounded-[12px] p-[16px] shadow-sm">
                <span className="text-gray-500 text-[13px]">平均观众</span>
                <div className="text-[20px] font-bold mt-[4px]">{report.metrics.avgViewers.toLocaleString()}</div>
              </div>
              <div className="bg-white rounded-[12px] p-[16px] shadow-sm">
                <span className="text-gray-500 text-[13px]">转化率</span>
                <div className="text-[20px] font-bold mt-[4px]">{report.metrics.conversionRate}%</div>
              </div>
            </div>
            <div className="grid grid-cols-3 gap-[16px]">
              <div className="bg-white rounded-[12px] p-[16px] shadow-sm">
                <div className="text-[14px] font-bold mb-[12px]">流量漏斗与趋势</div>
                <div className="space-y-[7px] text-[11px] text-white text-center">
                  {[['进入直播间', 100, 'bg-blue-200'], ['点击商品', 38, 'bg-blue-300'], ['加入购物车', 18, 'bg-blue-400'], ['支付下单', 8, 'bg-blue-500']].map(([label, value, color]) => <div key={label} className={`${color} mx-auto py-[5px] rounded`} style={{ width: `${45 + Number(value) / 2}%` }}>{label} {value}%</div>)}
                </div>
                <div className="text-[11px] text-red-500 mt-[10px]">最大流失：进入 → 点击，待数据 Adapter 校准行业均值</div>
              </div>
              <div className="bg-white rounded-[12px] p-[16px] shadow-sm">
                <div className="text-[14px] font-bold mb-[10px]">商品销售详情 TOP10</div>
                <div className="grid grid-cols-[1fr_70px_55px] text-[10px] text-gray-400 pb-[5px]"><span>商品</span><span>GMV</span><span>库存</span></div>
                {[['5G智能手机', '¥8.5w', '正常'], ['降噪耳机', '¥2.1w', '预警'], ['4K智能电视', '¥1.8w', '正常']].map((item) => <div key={item[0]} className="grid grid-cols-[1fr_70px_55px] text-[11px] border-t border-gray-100 py-[8px]"><span>{item[0]}</span><span>{item[1]}</span><span className={item[2] === '正常' ? 'text-green-500' : 'text-orange-500'}>{item[2]}</span></div>)}
              </div>
              <div className="bg-white rounded-[12px] p-[16px] shadow-sm">
                <div className="text-[14px] font-bold mb-[10px]">AI 经营诊断</div>
                <div className="space-y-[8px] text-[11px] text-gray-600">
                  <p className="bg-green-50 rounded p-[8px]"><strong className="text-green-700">卖得最好：</strong>手机核心卖点段贡献最高。</p>
                  <p className="bg-red-50 rounded p-[8px]"><strong className="text-red-700">流失最高：</strong>进入直播间后商品点击不足。</p>
                  <p className="bg-purple-50 rounded p-[8px]"><strong className="text-purple-700">建议动作：</strong>强化可信参数与权益说明。</p>
                  <p className="text-[10px] text-gray-400">诊断算法接口已预留，当前为 Fixture。</p>
                </div>
              </div>
            </div>
            <div className="grid grid-cols-2 gap-[16px]">
              <div className="bg-white rounded-[12px] p-[16px] shadow-sm">
                <div className="text-[14px] font-bold mb-[10px]">用户互动与主播表现</div>
                <div className="grid grid-cols-2 gap-[16px] text-[11px]">
                  <div><div className="text-gray-400 mb-[5px]">问题分类分布</div>{[['价格咨询',35],['规格参数',25],['优惠活动',18],['发货物流',15]].map(([label,value]) => <div key={label} className="flex items-center gap-[6px] mb-[5px]"><span className="w-[58px]">{label}</span><div className="h-[5px] bg-blue-400 rounded" style={{width:`${value * 2}px`}}></div><span>{value}%</span></div>)}</div>
                  <div><div className="text-gray-400 mb-[5px]">有效话术</div><div className="border-b py-[5px]">“限时权益” <span className="float-right text-green-500">+120人</span></div><div className="border-b py-[5px]">“真实体验” <span className="float-right text-green-500">+85人</span></div><div className="py-[5px]">“竞品对比” <span className="float-right text-red-500">-50人</span></div></div>
                </div>
              </div>
              <div className="bg-white rounded-[12px] p-[16px] shadow-sm">
                <div className="text-[14px] font-bold mb-[10px]">直播回放、智能切片与数据对比</div>
                <div className="grid grid-cols-2 gap-[14px] text-[11px]"><div className="space-y-[7px]"><div className="bg-gray-50 p-[7px] rounded">00:00-05:00 开场暖场</div><div className="bg-blue-50 text-blue-700 p-[7px] rounded">05:00-12:00 商品讲解 [切片2]</div><div className="bg-gray-50 p-[7px] rounded">12:00-18:00 高频问答</div></div><div className="rounded bg-gradient-to-br from-blue-50 to-purple-100 flex items-center justify-center text-gray-400"><i className="fas fa-chart-radar mr-[6px]"></i>本场 vs 品类均值接口</div></div>
              </div>
            </div>
          </div>
        )}

        {/* 告警复盘 */}
        {activeTab === 'alerts' && (
          <div className="space-y-[16px]">
            <div className="grid grid-cols-3 gap-[16px]">
              <div className="bg-red-50 p-[16px] rounded-[12px] border border-red-100">
                <div className="text-red-700 text-[14px] font-bold">库存告警</div>
                <div className="text-[24px] font-bold text-red-600 mt-[4px]">{report.alertSummary.inventory} 次</div>
              </div>
              <div className="bg-yellow-50 p-[16px] rounded-[12px] border border-yellow-100">
                <div className="text-yellow-700 text-[14px] font-bold">流量峰值</div>
                <div className="text-[24px] font-bold text-yellow-600 mt-[4px]">{report.alertSummary.trafficPeak} 次</div>
              </div>
              <div className="bg-blue-50 p-[16px] rounded-[12px] border border-blue-100">
                <div className="text-blue-700 text-[14px] font-bold">价格变动</div>
                <div className="text-[24px] font-bold text-blue-600 mt-[4px]">{report.alertSummary.priceChange} 次</div>
              </div>
            </div>
            <div className="bg-white rounded-[12px] p-[16px] shadow-sm">
              <div className="text-[14px] font-bold text-gray-800 mb-[8px]">告警处理率</div>
              <div className="flex items-center space-x-[12px]">
                <div className="flex-1 bg-gray-200 rounded-full h-[8px]">
                  <div className="bg-green-500 rounded-full h-[8px]" style={{ width: `${(report.alertSummary.handled / report.alertSummary.total * 100).toFixed(0)}%` }}></div>
                </div>
                <span className="text-[13px] text-gray-600">{(report.alertSummary.handled / report.alertSummary.total * 100).toFixed(0)}%</span>
              </div>
              <div className="text-[12px] text-gray-500 mt-[4px]">已处理 {report.alertSummary.handled} / {report.alertSummary.total}，遗漏 {report.alertSummary.missed} 次</div>
            </div>
          </div>
        )}

        {/* 弹幕分析 */}
        {activeTab === 'danmaku' && (
          <div className="space-y-[16px]">
            <div className="grid grid-cols-4 gap-[16px]">
              <div className="bg-white p-[16px] rounded-[12px] shadow-sm">
                <span className="text-gray-500 text-[13px]">弹幕总数</span>
                <div className="text-[20px] font-bold mt-[4px]">{report.danmakuSummary.total.toLocaleString()}</div>
              </div>
              <div className="bg-white p-[16px] rounded-[12px] shadow-sm">
                <span className="text-gray-500 text-[13px]">拦截水贴</span>
                <div className="text-[20px] font-bold mt-[4px] text-gray-600">{report.danmakuSummary.filtered.toLocaleString()}</div>
              </div>
              <div className="bg-white p-[16px] rounded-[12px] shadow-sm">
                <span className="text-gray-500 text-[13px]">AI秒回</span>
                <div className="text-[20px] font-bold mt-[4px] text-blue-600">{report.danmakuSummary.aiAnswered}</div>
              </div>
              <div className="bg-white p-[16px] rounded-[12px] shadow-sm">
                <span className="text-gray-500 text-[13px]">AI拦截率</span>
                <div className="text-[20px] font-bold mt-[4px] text-purple-600">{report.danmakuSummary.aiInterceptionRate}%</div>
              </div>
            </div>
            <div className="bg-white rounded-[12px] p-[16px] shadow-sm">
              <div className="text-[14px] font-bold text-gray-800 mb-[8px]">弹幕分层分布</div>
              <div className="space-y-[8px]">
                <div className="flex items-center space-x-[8px]">
                  <span className="text-[12px] text-gray-500 w-[80px]">高价值QA</span>
                  <div className="flex-1 bg-gray-100 rounded-full h-[6px]">
                    <div className="bg-blue-500 rounded-full h-[6px]" style={{ width: `${(report.danmakuSummary.aiAnswered / report.danmakuSummary.total * 100).toFixed(0)}%` }}></div>
                  </div>
                  <span className="text-[12px] text-gray-600">{report.danmakuSummary.aiAnswered}</span>
                </div>
                <div className="flex items-center space-x-[8px]">
                  <span className="text-[12px] text-gray-500 w-[80px]">水贴拦截</span>
                  <div className="flex-1 bg-gray-100 rounded-full h-[6px]">
                    <div className="bg-gray-400 rounded-full h-[6px]" style={{ width: `${(report.danmakuSummary.filtered / report.danmakuSummary.total * 100).toFixed(0)}%` }}></div>
                  </div>
                  <span className="text-[12px] text-gray-600">{report.danmakuSummary.filtered}</span>
                </div>
                <div className="flex items-center space-x-[8px]">
                  <span className="text-[12px] text-gray-500 w-[80px]">口播解答</span>
                  <div className="flex-1 bg-gray-100 rounded-full h-[6px]">
                    <div className="bg-yellow-500 rounded-full h-[6px]" style={{ width: `${(report.danmakuSummary.broadcastAnswered / report.danmakuSummary.total * 100).toFixed(0)}%` }}></div>
                  </div>
                  <span className="text-[12px] text-gray-600">{report.danmakuSummary.broadcastAnswered}</span>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* 知识回流 */}
        {activeTab === 'knowledge' && (
          <div className="space-y-[12px]">
            <div className="bg-purple-50 border border-purple-200 rounded-[8px] p-[12px] flex items-center mb-[12px]">
              <i className="fas fa-info-circle text-purple-500 mr-[8px]"></i>
              <span className="text-[13px] text-purple-700">本场主播修改过的优质回复和即兴金句，审核后可回流至商品知识库，供下场复用。</span>
            </div>
            {report.knowledgeItems.map(item => (
              <div key={item.id} className="bg-white rounded-[12px] p-[16px] shadow-sm border border-gray-100">
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <div className="text-[14px] font-bold text-gray-800 mb-[6px]">
                      <i className="fas fa-question-circle text-blue-400 mr-[6px]"></i>{item.question}
                    </div>
                    {item.originalAnswer && (
                      <div className="text-[12px] text-gray-400 mb-[4px] line-through">AI原答：{item.originalAnswer}</div>
                    )}
                    <div className="text-[13px] text-gray-600 bg-green-50 rounded-[6px] p-[8px]">
                      <i className="fas fa-check text-green-500 mr-[6px]"></i>最终答案：{item.finalAnswer}
                    </div>
                    <div className="text-[11px] text-gray-400 mt-[6px]">
                      <i className="fas fa-tag mr-[4px]"></i>来源：{item.source}
                    </div>
                  </div>
                  <button
                    className={`ml-[12px] px-[12px] py-[6px] rounded-[6px] text-[12px] font-medium transition-colors shrink-0 ${
                      approvedIds.has(item.id)
                        ? 'bg-green-100 text-green-600'
                        : 'bg-purple-600 text-white hover:bg-purple-500'
                    }`}
                    onClick={() => handleApprove(item.id)}
                    disabled={approvedIds.has(item.id)}
                  >
                    {approvedIds.has(item.id) ? <><i className="fas fa-check mr-[4px]"></i>已审核</> : <><i className="fas fa-plus mr-[4px]"></i>入库</>}
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}

        {/* 时间线 */}
        {activeTab === 'timeline' && (
          <div className="space-y-[0px]">
            {report.timeline.map((event, idx) => (
              <div key={idx} className="flex items-start">
                <div className="flex flex-col items-center mr-[16px]">
                  <div className={`w-[24px] h-[24px] rounded-full flex items-center justify-center text-[10px] text-white ${
                    event.type === 'start' ? 'bg-green-500' :
                    event.type === 'end' ? 'bg-gray-500' :
                    event.type === 'alert' ? 'bg-red-500' :
                    event.type === 'peak' ? 'bg-yellow-500' :
                    event.type === 'ai' ? 'bg-blue-500' :
                    'bg-purple-500'
                  }`}>
                    <i className={`fas ${
                      event.type === 'start' ? 'fa-play' :
                      event.type === 'end' ? 'fa-stop' :
                      event.type === 'alert' ? 'fa-exclamation' :
                      event.type === 'peak' ? 'fa-bolt' :
                      event.type === 'ai' ? 'fa-robot' :
                      'fa-tag'
                    }`}></i>
                  </div>
                  {idx < report.timeline.length - 1 && <div className="w-[2px] h-[32px] bg-gray-200"></div>}
                </div>
                <div className="pb-[16px]">
                  <span className="text-[12px] text-gray-400">{event.time}</span>
                  <div className="text-[14px] text-gray-700 mt-[2px]">{event.event}</div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default DataAnalysis;
