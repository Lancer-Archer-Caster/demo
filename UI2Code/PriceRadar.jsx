import React, { useState, useEffect } from 'react';
import { isMockMode, liveApi } from './api';

/**
 * =============================================
 * 数据接口定义 - PriceRadar
 * =============================================
 * 
 * 1. WebSocket 推送接口（实时比价数据）:
 *    接口路径: ws://api.live.jd.com/ws/price-radar
 *    载荷格式: {
 *      type: 'price_update',
 *      data: PriceCompareItem
 *    }
 * 
 * 2. HTTP 接口（竞品参数对比）:
 *    GET /api/v1/price-radar/compare?skuId={skuId}
 *    Response: { ourProduct: ProductDetail, competitors: CompetitorDetail[] }
 * 
 * 3. AI 话术生成接口:
 *    POST /api/v1/ai/generate-talkpoint
 *    Body: { skuId, ourParams: Param[], competitorParams: Param[] }
 *    Response: { talkPoints: TalkPoint[] }
 * 
 * 4. 降级策略:
 *    - 比价接口超时 > 5s: 隐藏价格雷达模块
 *    - AI接口超时 > 30s: 仅展示参数对比，不展示话术
 */

// ============ 类型定义 ============

/**
 * @typedef {Object} PriceCompareItem
 * @property {string} skuId - 当前SKU ID
 * @property {string} productName - 商品名称
 * @property {number} ourPrice - 京东价（分）
 * @property {string} ourPriceDisplay - 京东价展示文本
 * @property {CompetitorPrice[]} competitors - 竞品价格列表
 * @property {'advantage'|'disadvantage'|'equal'} priceStatus - 价格状态
 */

/**
 * @typedef {Object} CompetitorPrice
 * @property {string} platform - 竞品平台名称
 * @property {number} price - 竞品价格（分）
 * @property {string} priceDisplay - 竞品价格展示文本
 * @property {string} platformIcon - 平台图标class
 */

/**
 * @typedef {Object} ProductDetail
 * @property {string} skuId
 * @property {string} name
 * @property {Param[]} params - 核心参数列表
 */

/**
 * @typedef {Object} Param
 * @property {string} key - 参数名
 * @property {string} value - 参数值
 * @property {'highlight'|'normal'|'weak'} advantage - 优势等级
 */

/**
 * @typedef {Object} TalkPoint
 * @property {string} id
 * @property {string} title - 话术标题
 * @property {string} content - 话术内容
 * @property {'price'|'param'|'service'} type - 话术类型
 * @property {boolean} isAIGenerated - 是否AI生成
 */

// ============ Mock 数据（可替换为真实接口） ============

const MOCK_PRICE_DATA = {
  skuId: '100018374',
  productName: '5G智能手机 骁龙8 Gen3',
  ourPrice: 299900,
  ourPriceDisplay: '¥2,999',
  competitors: [
    { platform: '某宝', price: 314900, priceDisplay: '¥3,149', platformIcon: 'fa-store' },
    { platform: '拼X', price: 29900, priceDisplay: '¥299', platformIcon: 'fa-shopping-bag', isAbnormal: true },
    { platform: '某东自营', price: 299900, priceDisplay: '¥2,999', platformIcon: 'fa-check-circle', isSelf: true },
  ],
  priceStatus: 'advantage',
};

const MOCK_PARAM_COMPARE = {
  ourProduct: {
    skuId: '100018374',
    name: '京东自营 5G智能手机',
    params: [
      { key: '处理器', value: '骁龙8 Gen3', advantage: 'highlight' },
      { key: '屏幕', value: '120Hz OLED', advantage: 'highlight' },
      { key: '电池', value: '5000mAh', advantage: 'highlight' },
      { key: '快充', value: '67W', advantage: 'normal' },
      { key: '售后', value: '180天只换不修', advantage: 'highlight' },
    ],
  },
  competitors: [
    {
      name: '友商A 同价位机型',
      params: [
        { key: '处理器', value: '骁龙8 Gen2', advantage: 'weak' },
        { key: '屏幕', value: '90Hz LCD', advantage: 'weak' },
        { key: '电池', value: '4500mAh', advantage: 'weak' },
        { key: '快充', value: '33W', advantage: 'weak' },
        { key: '售后', value: '1年保修', advantage: 'weak' },
      ],
    },
  ],
};

const MOCK_TALK_POINTS = [
  { id: 'tp1', title: '芯片降维打击', content: '咱们骁龙8 Gen3比友商8 Gen2领先整整一代，跑分高出30%！', type: 'param', isAIGenerated: true },
  { id: 'tp2', title: '屏幕碾压', content: '120Hz OLED对比90Hz LCD，刷新率高33%且色彩更艳，追剧打游戏体验天差地别！', type: 'param', isAIGenerated: true },
  { id: 'tp3', title: '服务保障', content: '180天只换不修，友商只给1年保修，出问题咱们直接换新机！', type: 'service', isAIGenerated: true },
];

// ============ 数据获取钩子（可替换为真实API调用） ============

/**
 * usePriceRadarData - 价格雷达数据获取钩子
 * 真实环境替换: 接入 WebSocket 推送 + HTTP API
 */
function usePriceRadarData(skuId) {
  const [priceData, setPriceData] = useState(null);
  const [paramCompare, setParamCompare] = useState(null);
  const [talkPoints, setTalkPoints] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (isMockMode) {
      const timer = setTimeout(() => {
        setPriceData(MOCK_PRICE_DATA);
        setParamCompare(MOCK_PARAM_COMPARE);
        setTalkPoints(MOCK_TALK_POINTS);
        setLoading(false);
      }, 600);
      return () => clearTimeout(timer);
    }
    let cancelled = false;
    const load = async () => {
      try {
        const [competitors, talkpoint] = await Promise.all([
          liveApi.getCompetitorPrices(skuId),
          liveApi.generatePriceTalkpoint(skuId),
        ]);
        const mapped = (competitors || []).map((item) => {
          const price = Number(item.price || 0) * 100;
          const isSelf = String(item.platform || '').includes('本店');
          return {
            platform: item.platform,
            price,
            priceDisplay: `¥${Number(item.price || 0).toLocaleString()}`,
            platformIcon: isSelf ? 'fa-check-circle' : 'fa-store',
            isSelf,
          };
        });
        const self = mapped.find((item) => item.isSelf) || mapped[0];
        const competitorValues = mapped.filter((item) => !item.isSelf).map((item) => item.price);
        if (!cancelled) {
          setPriceData({
            ...MOCK_PRICE_DATA,
            skuId,
            ourPrice: self?.price || MOCK_PRICE_DATA.ourPrice,
            ourPriceDisplay: self?.priceDisplay || MOCK_PRICE_DATA.ourPriceDisplay,
            competitors: mapped,
            priceStatus: competitorValues.length && self?.price <= Math.min(...competitorValues) ? 'advantage' : 'equal',
          });
          setParamCompare(MOCK_PARAM_COMPARE);
          setTalkPoints([{ id: 'local-talkpoint', title: '本地比价建议', content: talkpoint, type: 'price', isAIGenerated: false }]);
        }
      } catch (e) {
        if (!cancelled) {
          setError(e.message);
        }
      } finally {
        if (!cancelled) setLoading(false);
      }
    };
    load();
    return () => { cancelled = true; };
  }, [skuId]);

  // 真实环境: WebSocket 订阅实时价格更新
  // useEffect(() => {
  //   const ws = new WebSocket('ws://api.live.jd.com/ws/price-radar');
  //   ws.onmessage = (event) => {
  //     const msg = JSON.parse(event.data);
  //     if (msg.type === 'price_update') setPriceData(msg.data);
  //   };
  //   return () => ws.close();
  // }, []);

  return { priceData, paramCompare, talkPoints, loading, error };
}

// ============ 组件 ============

function PriceRadar({ skuId = '100018374' }) {
  const { priceData, paramCompare, talkPoints, loading, error } = usePriceRadarData(skuId);
  const [activeTab, setActiveTab] = useState('price'); // price | params | talk

  // 降级: 比价接口失效时隐藏模块
  if (error) {
    return (
      <div className="h-full bg-gray-900 rounded-[12px] p-[16px] flex flex-col items-center justify-center text-gray-500">
        <i className="fas fa-exclamation-circle text-[24px] mb-[8px]"></i>
        <span className="text-[12px]">比价数据暂不可用</span>
      </div>
    );
  }

  if (loading) {
    return (
      <div className="h-full bg-gray-900 rounded-[12px] p-[16px] flex flex-col items-center justify-center">
        <i className="fas fa-spinner fa-spin text-purple-400 text-[20px] mb-[8px]"></i>
        <span className="text-gray-500 text-[12px]">加载比价数据...</span>
      </div>
    );
  }

  return (
    <div className="h-full bg-gray-900 rounded-[12px] p-[16px] flex flex-col shadow-sm" data-ai-alt="全网比价雷达">
      {/* 标题 */}
      <h3 className="text-gray-400 text-[16px] font-bold mb-[12px] flex items-center shrink-0">
        <i className="fas fa-radar mr-[8px]"></i> 全网比价雷达
      </h3>

      {/* Tab切换 */}
      <div className="flex bg-gray-800 rounded-[6px] p-[2px] mb-[12px] shrink-0">
        {[
          { key: 'price', label: '比价', icon: 'fa-tag' },
          { key: 'params', label: '参数', icon: 'fa-list-alt' },
          { key: 'talk', label: '话术', icon: 'fa-bullhorn' },
        ].map(tab => (
          <button
            key={tab.key}
            className={`flex-1 text-[12px] py-[6px] rounded-[4px] transition-colors ${activeTab === tab.key ? 'bg-purple-600 text-white' : 'text-gray-400 hover:text-gray-200'}`}
            onClick={() => setActiveTab(tab.key)}
          >
            <i className={`fas ${tab.icon} mr-[4px]`}></i>{tab.label}
          </button>
        ))}
      </div>

      {/* 内容区 */}
      <div className="flex-1 overflow-y-auto space-y-[12px] pr-[4px]" data-ai-list="true">
        {/* 比价Tab */}
        {activeTab === 'price' && priceData && (
          <>
            {/* 价格优势 */}
            {priceData.priceStatus === 'advantage' && (
              <div className="bg-green-500/20 border border-green-500 rounded-[8px] p-[12px] animate-pulse" data-ai-changelog-id="live-price-advantage" data-ai-changelog-title="价格优势触发" data-ai-changelog-desc="京东价<竞品价时绿色大字闪烁提示">
                <div className="text-green-400 text-[18px] font-black mb-[6px]">全网最低！</div>
                <div className="text-green-300 text-[13px]">本店价 {priceData.ourPriceDisplay}，低于本地竞品样例价，请结合页面数据说明。</div>
              </div>
            )}
            {/* 价格劣势 */}
            {priceData.priceStatus === 'disadvantage' && (
              <div className="bg-yellow-500/10 border border-yellow-500/50 rounded-[8px] p-[12px]" data-ai-changelog-id="live-price-warning" data-ai-changelog-title="价格劣势预警" data-ai-changelog-desc="京东价>竞品价时黄色小字提示服务优势">
                <div className="text-yellow-500 text-[14px] font-bold mb-[4px]"><i className="fas fa-exclamation-circle mr-[4px]"></i>价格预警</div>
                <div className="text-yellow-400/80 text-[12px]">注意：竞品当前破价，请强调我们有 Plus 会员免邮+次日达服务保障。</div>
              </div>
            )}
            {/* 竞品价格列表 */}
            <div className="space-y-[6px]">
              {priceData.competitors.filter(c => !c.isSelf).map((comp, idx) => (
                <div key={idx} className={`bg-gray-800 rounded-[6px] p-[8px] flex items-center justify-between ${comp.isAbnormal ? 'border border-red-500/30' : ''}`}>
                  <div className="flex items-center">
                    <i className={`fas ${comp.platformIcon} text-gray-400 text-[12px] mr-[6px]`}></i>
                    <span className="text-gray-300 text-[12px]">{comp.platform}</span>
                    {comp.isAbnormal && <span className="text-[10px] text-red-400 ml-[4px]"><i className="fas fa-exclamation-triangle"></i> 异常价</span>}
                  </div>
                  <span className={`text-[13px] font-bold ${comp.isAbnormal ? 'text-red-400' : 'text-gray-200'}`}>{comp.priceDisplay}</span>
                </div>
              ))}
            </div>
            {/* 京东自营价 */}
            <div className="bg-blue-500/10 border border-blue-500/30 rounded-[6px] p-[8px] flex items-center justify-between">
              <div className="flex items-center">
                <i className="fas fa-check-circle text-blue-400 text-[12px] mr-[6px]"></i>
                <span className="text-blue-300 text-[12px]">本店自营</span>
              </div>
              <span className="text-blue-300 text-[13px] font-bold">{priceData.ourPriceDisplay}</span>
            </div>
          </>
        )}

        {/* 参数对比Tab */}
        {activeTab === 'params' && paramCompare && (
          <div className="space-y-[8px]">
            <div className="text-gray-400 text-[12px] mb-[4px]">本品 vs 竞品核心参数对比</div>
            {/* 参数对比表 */}
            <div className="bg-gray-800 rounded-[8px] overflow-hidden">
              <div className="grid grid-cols-3 text-[11px] text-gray-500 bg-gray-700 p-[6px]">
                <span>参数</span>
                <span className="text-blue-400">本品</span>
                <span className="text-gray-400">竞品</span>
              </div>
              {paramCompare.ourProduct.params.map((param, idx) => {
                const compParam = paramCompare.competitors[0]?.params[idx];
                return (
                  <div key={param.key} className="grid grid-cols-3 text-[12px] p-[6px] border-t border-gray-700/50">
                    <span className="text-gray-400">{param.key}</span>
                    <span className={`font-medium ${param.advantage === 'highlight' ? 'text-green-400' : param.advantage === 'weak' ? 'text-red-400' : 'text-gray-200'}`}>
                      {param.value}
                      {param.advantage === 'highlight' && <i className="fas fa-arrow-up text-[10px] ml-[2px]"></i>}
                    </span>
                    <span className={`font-medium ${compParam?.advantage === 'weak' ? 'text-red-400/60' : 'text-gray-500'}`}>
                      {compParam?.value || '-'}
                    </span>
                  </div>
                );
              })}
            </div>
          </div>
        )}

        {/* 降维打击话术Tab */}
        {activeTab === 'talk' && (
          <div className="space-y-[8px]">
            <div className="text-gray-400 text-[12px] mb-[4px] flex items-center justify-between">
              <span>AI降维打击话术</span>
              <span className="text-purple-400/60 text-[10px]"><i className="fas fa-robot mr-[2px]"></i>[由京东大模型生成]</span>
            </div>
            {talkPoints.map(tp => (
              <div key={tp.id} className={`rounded-[8px] p-[10px] border ${
                tp.type === 'price' ? 'bg-green-900/20 border-green-500/30' :
                tp.type === 'param' ? 'bg-blue-900/20 border-blue-500/30' :
                'bg-yellow-900/20 border-yellow-500/30'
              }`}>
                <div className="flex items-center mb-[4px]">
                  <i className={`fas ${
                    tp.type === 'price' ? 'fa-tag text-green-400' :
                    tp.type === 'param' ? 'fa-microchip text-blue-400' :
                    'fa-shield-alt text-yellow-400'
                  } text-[12px] mr-[6px]`}></i>
                  <span className="text-[13px] font-bold text-gray-200">{tp.title}</span>
                </div>
                <p className="text-[12px] text-gray-300 leading-relaxed pl-[18px]">{tp.content}</p>
              </div>
            ))}
            {talkPoints.length === 0 && (
              <div className="text-center text-gray-500 text-[12px] py-[20px]">暂无话术，请先选择SKU</div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}

export default PriceRadar;
