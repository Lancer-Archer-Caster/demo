import React, { useEffect, useState } from 'react';
import { isMockMode, liveApi } from './api';

// 模拟弹幕数据
const MOCK_DANMAKU = [
  {
    id: 1,
    user: '数码控',
    avatar: 'D',
    content: '这个屏幕刷新率是多少的？玩游戏卡不卡？',
    intent: 'QA',
    aiAnswer: '120Hz高刷OLED屏，支持自适应刷新，配合骁龙芯片原神满帧不卡。',
    hasKnowledgeMatch: true,
    timestamp: '14:32:05',
  },
  {
    id: 2,
    user: '省钱小能手',
    avatar: 'S',
    content: '保价双十一吗？现在买亏不亏？',
    intent: 'QA',
    aiAnswer: '承诺全程保价双十一，若降价支持一键退差价，此时购买还多送延保。',
    hasKnowledgeMatch: true,
    timestamp: '14:32:18',
  },
  {
    id: 3,
    user: '花开富贵',
    avatar: 'H',
    content: '有红色的吗？',
    intent: 'QA',
    aiAnswer: '当前SKU包含「晨曦红」，请引导用户选择对应颜色规格。',
    hasKnowledgeMatch: true,
    timestamp: '14:32:30',
  },
  {
    id: 4,
    user: '技术宅老王',
    avatar: 'W',
    content: 'DDR5内存频率多少？支持NFC吗？',
    intent: 'QA',
    aiAnswer: null,
    hasKnowledgeMatch: false,
    timestamp: '14:33:01',
  },
  {
    id: 5,
    user: '路人甲',
    avatar: 'L',
    content: '666',
    intent: 'water',
    aiAnswer: null,
    hasKnowledgeMatch: false,
    timestamp: '14:33:10',
  },
  {
    id: 6,
    user: '吃瓜群众',
    avatar: 'C',
    content: '来了来了',
    intent: 'water',
    aiAnswer: null,
    hasKnowledgeMatch: false,
    timestamp: '14:33:15',
  },
  {
    id: 7,
    user: '极客玩家',
    avatar: 'J',
    content: '电池续航实测多久？充电速度怎么样？',
    intent: 'QA',
    aiAnswer: '5000mAh电池日常使用约1.5天，67W闪充30分钟充至70%，告别续航焦虑。',
    hasKnowledgeMatch: true,
    timestamp: '14:33:22',
  },
];

function toUiDanmaku(item) {
  const user = item.senderNick || `观众${item.id}`;
  const createdAt = String(item.createdAt || '');
  const time = createdAt.includes(' ') ? createdAt.split(' ')[1]?.slice(0, 8) : new Date().toLocaleTimeString('zh-CN', { hour12: false });
  return {
    id: item.id,
    user,
    avatar: user.slice(0, 1).toUpperCase(),
    content: item.originalText || '',
    intent: String(item.intent || '').toUpperCase() === 'WATER' ? 'water' : 'QA',
    aiAnswer: item.aiAnswer || null,
    hasKnowledgeMatch: Boolean(item.aiAnswer),
    timestamp: time,
  };
}

function GeekBulletScreen({ sessionId = 1, refreshToken = 0, sourceReady = false }) {
  const [danmakuList, setDanmakuList] = useState(isMockMode ? MOCK_DANMAKU : []);
  const [loadError, setLoadError] = useState('');
  const [filterMode, setFilterMode] = useState('valuable'); // valuable | all | water
  const [editingId, setEditingId] = useState(null);
  const [editText, setEditText] = useState('');
  const [sentIds, setSentIds] = useState(new Set());
  const [broadcastIds, setBroadcastIds] = useState(new Set());

  useEffect(() => {
    if (isMockMode) return;
    let cancelled = false;
    const poll = async () => {
      try {
        const history = await liveApi.getDanmakuHistory(sessionId);
        if (!cancelled) {
          setDanmakuList((history || []).map(toUiDanmaku));
          setLoadError('');
        }
      } catch (error) {
        if (!cancelled) setLoadError(error.message);
      }
    };
    poll();
    const interval = setInterval(poll, 2000);
    return () => { cancelled = true; clearInterval(interval); };
  }, [sessionId, refreshToken, sourceReady]);

  // 过滤弹幕
  const filteredList = danmakuList.filter(item => {
    if (filterMode === 'valuable') return item.intent === 'QA';
    if (filterMode === 'water') return item.intent === 'water';
    return true;
  });

  // 确认发送
  const handleConfirmSend = async (id) => {
    const item = danmakuList.find((value) => value.id === id);
    if (!isMockMode) {
      try {
        await liveApi.confirmDanmakuAnswer(id, item?.aiAnswer || '');
      } catch (error) {
        window.alert(`确认回复失败：${error.message}`);
        return;
      }
    }
    setSentIds(prev => new Set([...prev, id]));
  };

  // 修改后发送
  const handleEditSend = async (id) => {
    if (!isMockMode) {
      try {
        await liveApi.modifyDanmakuAnswer(id, editText);
      } catch (error) {
        window.alert(`修改回复失败：${error.message}`);
        return;
      }
    }
    setDanmakuList(prev => prev.map(item =>
      item.id === id ? { ...item, aiAnswer: editText } : item
    ));
    setSentIds(prev => new Set([...prev, id]));
    setEditingId(null);
    setEditText('');
  };

  // 标记为口播解答
  const handleMarkBroadcast = (id) => {
    setBroadcastIds(prev => new Set([...prev, id]));
  };

  // 开始编辑AI回复
  const handleStartEdit = (item) => {
    setEditingId(item.id);
    setEditText(item.aiAnswer || '');
  };

  // 丢弃水贴
  const handleDiscardWater = (id) => {
    setDanmakuList(prev => prev.filter(item => item.id !== id));
  };

  return (
    <div className="h-full bg-gray-800 rounded-[12px] p-[16px] flex flex-col shadow-sm border border-gray-700" data-ai-alt="极客弹幕区">
      {/* 标题栏 */}
      <div className="flex items-center justify-between mb-[12px] shrink-0">
        <h3 className="text-blue-400 text-[16px] font-bold flex items-center">
          <i className="fas fa-comments mr-[8px]"></i> 评论分析
          <span className={`ml-[6px] text-[9px] px-[5px] py-[1px] rounded ${sourceReady ? 'bg-green-500/20 text-green-400' : 'bg-gray-700 text-gray-500'}`}>
            {sourceReady ? '本地评论流' : '等待来源'}
          </span>
        </h3>
        <div className="flex items-center space-x-[4px]">
          <span className="text-[11px] text-gray-500 mr-[4px]">过滤:</span>
          <button
            className={`text-[11px] px-[6px] py-[2px] rounded-[4px] transition-colors ${filterMode === 'valuable' ? 'bg-blue-600 text-white' : 'bg-gray-700 text-gray-400 hover:text-gray-200'}`}
            onClick={() => setFilterMode('valuable')}
          >
            高价值
          </button>
          <button
            className={`text-[11px] px-[6px] py-[2px] rounded-[4px] transition-colors ${filterMode === 'all' ? 'bg-blue-600 text-white' : 'bg-gray-700 text-gray-400 hover:text-gray-200'}`}
            onClick={() => setFilterMode('all')}
          >
            全部
          </button>
          <button
            className={`text-[11px] px-[6px] py-[2px] rounded-[4px] transition-colors ${filterMode === 'water' ? 'bg-blue-600 text-white' : 'bg-gray-700 text-gray-400 hover:text-gray-200'}`}
            onClick={() => setFilterMode('water')}
          >
            水贴
          </button>
        </div>
      </div>

      {/* 统计栏 */}
      <div className="flex items-center space-x-[12px] mb-[12px] text-[11px] shrink-0">
        <span className="text-green-400"><i className="fas fa-filter mr-[4px]"></i>已拦截水贴 {danmakuList.filter(d => d.intent === 'water').length} 条</span>
        <span className="text-blue-400"><i className="fas fa-robot mr-[4px]"></i>AI秒回 {danmakuList.filter(d => d.hasKnowledgeMatch).length} 条</span>
        <span className="text-yellow-400"><i className="fas fa-microphone mr-[4px]"></i>待口播 {danmakuList.filter(d => d.intent === 'QA' && !d.hasKnowledgeMatch && !broadcastIds.has(d.id)).length} 条</span>
      </div>

      {/* 弹幕列表 */}
      <div className="flex-1 overflow-y-auto space-y-[8px] pr-[4px]" data-ai-list="true">
        {loadError && <div className="text-red-400 text-[11px]">评论读取失败：{loadError}</div>}
        {filteredList.map((item) => (
          <div key={item.id} className={`rounded-[8px] p-[10px] shadow-sm ${
            item.intent === 'water'
              ? 'bg-gray-700/50 border-l-4 border-gray-500'
              : item.hasKnowledgeMatch
                ? 'bg-gray-700 border-l-4 border-blue-500'
                : 'bg-gray-700 border-l-4 border-yellow-500'
          }`}>
            {/* 用户弹幕 */}
            <div className="flex items-start justify-between mb-[6px]">
              <div className="flex items-center">
                <span className="w-[20px] h-[20px] rounded-full bg-gradient-to-r from-blue-400 to-purple-400 text-white text-[10px] flex items-center justify-center mr-[6px] shrink-0">
                  {item.avatar}
                </span>
                <span className="text-gray-400 text-[12px] mr-[6px]">@{item.user}</span>
                <span className="text-gray-200 text-[13px] font-medium">{item.content}</span>
              </div>
              <span className="text-gray-500 text-[10px] shrink-0 ml-[8px]">{item.timestamp}</span>
            </div>

            {/* 水贴标记 */}
            {item.intent === 'water' && (
              <div className="flex items-center justify-between">
                <span className="text-[11px] text-gray-500"><i className="fas fa-filter mr-[4px]"></i>已识别为水贴，自动拦截</span>
                <button
                  className="text-[11px] text-gray-500 hover:text-red-400 transition-colors"
                  onClick={() => handleDiscardWater(item.id)}
                >
                  <i className="fas fa-trash-alt mr-[4px]"></i>丢弃
                </button>
              </div>
            )}

            {/* AI回答 - 有知识库匹配 */}
            {item.intent === 'QA' && item.hasKnowledgeMatch && !sentIds.has(item.id) && (
              <div className="mt-[6px]" data-ai-changelog-id="live-danmaku-rag" data-ai-changelog-title="高价值弹幕与知识库秒回" data-ai-changelog-desc="丢弃无意义弹幕，过滤高价值问题并显示蓝色AI标准答案">
                <div className="bg-gray-800/50 rounded-[6px] p-[8px] text-blue-400 text-[12px] flex items-start">
                  <i className="fas fa-robot mt-[2px] mr-[6px] shrink-0"></i>
                  {editingId === item.id ? (
                    <textarea
                      className="flex-1 bg-gray-900 text-blue-300 text-[12px] rounded-[4px] p-[6px] border border-blue-500/30 focus:outline-none focus:border-blue-400 resize-none"
                      value={editText}
                      onChange={e => setEditText(e.target.value)}
                      rows={2}
                    />
                  ) : (
                    <span>{item.aiAnswer}</span>
                  )}
                </div>
                <div className="flex items-center justify-between mt-[6px]">
                  <span className="text-[10px] text-purple-400/60"><i className="fas fa-database mr-[2px]"></i>[由本地知识规则生成]</span>
                  <div className="flex space-x-[6px]">
                    {editingId === item.id ? (
                      <>
                        <button
                          className="text-[11px] bg-gray-600 text-gray-300 px-[8px] py-[3px] rounded-[4px] hover:bg-gray-500 transition-colors"
                          onClick={() => { setEditingId(null); setEditText(''); }}
                        >
                          取消
                        </button>
                        <button
                          className="text-[11px] bg-blue-600 text-white px-[8px] py-[3px] rounded-[4px] hover:bg-blue-500 transition-colors"
                          onClick={() => handleEditSend(item.id)}
                        >
                          <i className="fas fa-paper-plane mr-[4px]"></i>修改发送
                        </button>
                      </>
                    ) : (
                      <>
                        <button
                          className="text-[11px] bg-blue-600 text-white px-[8px] py-[3px] rounded-[4px] hover:bg-blue-500 transition-colors"
                          onClick={() => handleConfirmSend(item.id)}
                        >
                          <i className="fas fa-check mr-[4px]"></i>确认发送
                        </button>
                        <button
                          className="text-[11px] bg-gray-600 text-gray-300 px-[8px] py-[3px] rounded-[4px] hover:bg-gray-500 transition-colors"
                          onClick={() => handleStartEdit(item)}
                        >
                          <i className="fas fa-edit mr-[4px]"></i>修改
                        </button>
                        <button
                          className="text-[11px] bg-yellow-600/80 text-white px-[8px] py-[3px] rounded-[4px] hover:bg-yellow-500 transition-colors"
                          onClick={() => handleMarkBroadcast(item.id)}
                        >
                          <i className="fas fa-microphone mr-[4px]"></i>口播
                        </button>
                      </>
                    )}
                  </div>
                </div>
              </div>
            )}

            {/* 已发送状态 */}
            {item.intent === 'QA' && item.hasKnowledgeMatch && sentIds.has(item.id) && (
              <div className="mt-[6px] flex items-center text-[11px] text-green-400">
                <i className="fas fa-check-circle mr-[4px]"></i>已发送回复
              </div>
            )}

            {/* 无知识库匹配 - 需口播 */}
            {item.intent === 'QA' && !item.hasKnowledgeMatch && !broadcastIds.has(item.id) && (
              <div className="mt-[6px]">
                <div className="bg-yellow-900/30 border border-yellow-600/30 rounded-[6px] p-[8px] text-yellow-400 text-[12px] flex items-start">
                  <i className="fas fa-exclamation-triangle mt-[2px] mr-[6px] shrink-0"></i>
                  <span>知识库暂无匹配答案，建议主播口播解答</span>
                </div>
                <div className="flex justify-end mt-[6px]">
                  <button
                    className="text-[11px] bg-yellow-600/80 text-white px-[8px] py-[3px] rounded-[4px] hover:bg-yellow-500 transition-colors"
                    onClick={() => handleMarkBroadcast(item.id)}
                  >
                    <i className="fas fa-microphone mr-[4px]"></i>标记为口播解答
                  </button>
                </div>
              </div>
            )}

            {/* 已标记口播 */}
            {item.intent === 'QA' && !item.hasKnowledgeMatch && broadcastIds.has(item.id) && (
              <div className="mt-[6px] flex items-center text-[11px] text-yellow-400">
                <i className="fas fa-microphone mr-[4px]"></i>已标记口播解答
              </div>
            )}
          </div>
        ))}

        {filteredList.length === 0 && (
          <div className="flex flex-col items-center justify-center py-[40px] text-gray-500">
            <i className="fas fa-comments text-[32px] mb-[12px] opacity-50"></i>
            <span className="text-[13px]">
              {filterMode === 'valuable' ? '暂无高价值弹幕' : filterMode === 'water' ? '暂无水贴' : '暂无弹幕'}
            </span>
          </div>
        )}
      </div>
    </div>
  );
}

export default GeekBulletScreen;
