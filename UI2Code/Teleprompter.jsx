import React, { useState } from 'react';

// 模拟黑话翻译词库
const JARGON_DB = {
  '144Hz刷新率': '相当于每秒翻144页的连环画，打游戏绝不卡顿，画面丝滑到飞起！',
  '120Hz': '每秒刷新120次，比普通手机快一倍，刷视频、玩游戏都更流畅！',
  'OLED': '每个像素自己发光，黑色更纯、色彩更艳，就像电影院级的视觉体验！',
  '骁龙8 Gen3': '高通最新旗舰芯片，就像给手机装了个V8发动机，性能拉满！',
  '5000mAh': '5000毫安时大电池，正常用一天半不用充电，出门再也不用带充电宝！',
  '67W闪充': '67瓦超级快充，喝杯咖啡的功夫就能充到50%，告别电量焦虑！',
  'HDR1000': '亮度高达1000尼特，阳光下也能看清屏幕，就像自带了一盏小太阳！',
  'MEMC': '运动补偿技术，看球赛、看电影不拖影，画面流畅得像在现场！',
  '4K': '4倍于1080P的超高清分辨率，每个细节都纤毫毕现！',
  'ANC主动降噪': '戴上就像进入静音舱，地铁、飞机上也能享受纯净音乐！',
  '蓝牙5.3': '最新蓝牙技术，连接更稳、延迟更低，打游戏不卡音！',
  'DDR5': '第五代内存，速度比上一代快一倍，开App秒开不等待！',
  'NFC': '手机一碰就支付、一碰就开门禁，出门不用带卡包！',
};

// 模拟脚本节点数据
const SCRIPT_NODES = [
  {
    id: 1,
    title: '品牌背景与核心卖点介绍',
    content: '京东自营5G智能手机，骁龙8 Gen3旗舰芯片，5000mAh大电池，120Hz OLED屏幕',
    status: 'past',
    matchScore: 98,
    jargonTerms: ['骁龙8 Gen3', '5000mAh', '120Hz'],
  },
  {
    id: 2,
    title: '屏幕与续航深度讲解',
    content: '独家定制120Hz高刷OLED屏幕，配合67W闪充与5000mAh大电池，告别电量焦虑！144Hz刷新率让游戏体验更上一层楼',
    status: 'current',
    matchScore: 92,
    jargonTerms: ['120Hz', 'OLED', '67W闪充', '5000mAh', '144Hz刷新率'],
  },
  {
    id: 3,
    title: '竞品对比与价格优势',
    content: '同配置友商卖3499，咱们京东价2999，Plus会员再享免邮+次日达',
    status: 'future',
    matchScore: 0,
    jargonTerms: [],
  },
  {
    id: 4,
    title: '售后保障与逼单收尾',
    content: '180天只换不修，限时赠送原装耳机，倒计时3-2-1上链接！',
    status: 'future',
    matchScore: 0,
    jargonTerms: [],
  },
];

function Teleprompter() {
  const [nodes, setNodes] = useState(SCRIPT_NODES);
  const [expandedPast, setExpandedPast] = useState(null);
  const [jargonPopup, setJargonPopup] = useState(null);
  const [asrMode, setAsrMode] = useState('auto'); // auto | manual
  const [manualScrollTop, setManualScrollTop] = useState(0);

  // 切换节点状态（模拟ASR推进）
  const handleNodeClick = (nodeId) => {
    setNodes(prev => prev.map(n => {
      if (n.id === nodeId) return { ...n, status: 'current', matchScore: 95 };
      if (n.status === 'current') return { ...n, status: 'past', matchScore: 100 };
      if (n.id < nodeId && n.status !== 'past') return { ...n, status: 'past', matchScore: 100 };
      return n;
    }));
  };

  // 黑话翻译
  const handleJargonClick = (term, e) => {
    e.stopPropagation();
    if (JARGON_DB[term]) {
      setJargonPopup({ term, explanation: JARGON_DB[term], x: e.clientX, y: e.clientY });
    }
  };

  // 渲染带黑话高亮的文本
  const renderContentWithJargon = (content, jargonTerms) => {
    if (!jargonTerms || jargonTerms.length === 0) return content;
    
    let result = [content];
    jargonTerms.forEach(term => {
      const newResult = [];
      result.forEach(part => {
        if (typeof part !== 'string') {
          newResult.push(part);
          return;
        }
        const parts = part.split(term);
        parts.forEach((p, i) => {
          if (i > 0) {
            newResult.push(
              <span
                key={`${term}-${i}`}
                className="text-cyan-300 underline decoration-dashed cursor-pointer hover:text-cyan-200 transition-colors relative"
                onClick={(e) => handleJargonClick(term, e)}
                title="点击查看通俗解释"
              >
                {term}
                <i className="fas fa-language ml-[2px] text-[10px] text-cyan-400"></i>
              </span>
            );
          }
          if (p) newResult.push(p);
        });
      });
      result = newResult;
    });
    return result;
  };

  return (
    <div className="h-full bg-gray-800 rounded-[12px] p-[24px] flex flex-col shadow-sm border border-gray-700" data-ai-alt="动态提词器">
      {/* 标题栏 */}
      <div className="flex items-center justify-between mb-[16px] shrink-0">
        <h3 className="text-purple-400 text-[16px] font-bold flex items-center">
          <i className="fas fa-microphone-alt mr-[8px]"></i> 动态提词器 (ASR跟随)
        </h3>
        <div className="flex items-center space-x-[8px]">
          {/* ASR模式切换 */}
          <div className="flex bg-gray-700 rounded-[6px] p-[2px]">
            <button
              className={`text-[12px] px-[8px] py-[4px] rounded-[4px] transition-colors ${asrMode === 'auto' ? 'bg-purple-600 text-white' : 'text-gray-400 hover:text-gray-200'}`}
              onClick={() => setAsrMode('auto')}
            >
              <i className="fas fa-magic mr-[4px]"></i>自动
            </button>
            <button
              className={`text-[12px] px-[8px] py-[4px] rounded-[4px] transition-colors ${asrMode === 'manual' ? 'bg-purple-600 text-white' : 'text-gray-400 hover:text-gray-200'}`}
              onClick={() => setAsrMode('manual')}
            >
              <i className="fas fa-hand-paper mr-[4px]"></i>手动
            </button>
          </div>
          {/* ASR连接状态 */}
          <div className="flex items-center text-[12px] text-green-400">
            <span className="w-[6px] h-[6px] rounded-full bg-green-400 mr-[4px] animate-pulse"></span>
            ASR在线
          </div>
        </div>
      </div>

      {/* 黑话翻译提示 */}
      <div className="bg-cyan-900/30 border border-cyan-700/50 rounded-[6px] px-[12px] py-[6px] mb-[12px] text-[12px] text-cyan-300 flex items-center shrink-0">
        <i className="fas fa-lightbulb mr-[6px] text-cyan-400"></i>
        <span>点击<span className="underline decoration-dashed text-cyan-200">蓝色下划线术语</span>可查看黑话通俗翻译，由AI生成</span>
      </div>

      {/* 提词器内容 */}
      <div className="flex-1 overflow-y-auto space-y-[16px] pr-[8px]" data-ai-list="true">
        {nodes.map((node) => (
          <div key={node.id} onClick={() => asrMode === 'manual' && handleNodeClick(node.id)}>
            {/* 已讲节点 - 折叠 */}
            {node.status === 'past' && (
              <div
                className="bg-gray-700 rounded-[8px] p-[10px] cursor-pointer hover:bg-gray-600 transition-colors"
                data-ai-changelog-id="live-prompt-past"
                data-ai-changelog-title="已讲节点折叠"
                data-ai-changelog-desc="已讲节点自动折叠为单行摘要可展开"
              >
                <div
                  className="text-gray-400 text-[13px] flex items-center justify-between"
                  onClick={(e) => { e.stopPropagation(); setExpandedPast(expandedPast === node.id ? null : node.id); }}
                >
                  <span>
                    <i className="fas fa-check-circle text-green-500 mr-[8px]"></i>
                    [已讲] {node.title}
                  </span>
                  <i className={`fas fa-chevron-${expandedPast === node.id ? 'up' : 'down'} text-[10px]`}></i>
                </div>
                {expandedPast === node.id && (
                  <div className="mt-[8px] pl-[24px] text-gray-400 text-[12px] leading-relaxed">
                    {renderContentWithJargon(node.content, node.jargonTerms)}
                  </div>
                )}
              </div>
            )}

            {/* 当前节点 - 高亮放大 */}
            {node.status === 'current' && (
              <div
                className="bg-gradient-to-r from-blue-600 to-purple-700 rounded-[12px] p-[24px] shadow-md transform scale-105 transition-transform"
                data-ai-changelog-id="live-prompt-current"
                data-ai-changelog-title="当前讲节点高亮"
                data-ai-changelog-desc="当前节点白色高亮并放大显示"
              >
                <div className="flex items-center justify-between mb-[8px]">
                  <div className="text-white/80 text-[13px]">
                    <i className="fas fa-broadcast-tower mr-[6px] text-green-300 animate-pulse"></i>
                    当前讲解模块
                  </div>
                  <div className="flex items-center space-x-[8px]">
                    <span className="text-[12px] bg-white/20 text-white/80 px-[6px] py-[2px] rounded-[4px]">
                      ASR匹配 {node.matchScore}%
                    </span>
                  </div>
                </div>
                <div className="text-white text-[24px] font-black leading-tight mb-[8px]">
                  {renderContentWithJargon(node.content, node.jargonTerms)}
                </div>
                <div className="flex items-center space-x-[8px] mt-[8px]">
                  {node.jargonTerms.slice(0, 3).map(term => (
                    <span
                      key={term}
                      className="text-[11px] bg-white/20 text-white/70 px-[6px] py-[2px] rounded-[4px] cursor-pointer hover:bg-white/30"
                      onClick={(e) => handleJargonClick(term, e)}
                    >
                      <i className="fas fa-language mr-[2px]"></i>{term}
                    </span>
                  ))}
                </div>
              </div>
            )}

            {/* 未讲节点 - 置灰缩小 */}
            {node.status === 'future' && (
              <div
                className={`opacity-50 transform scale-95 transition-all ${asrMode === 'manual' ? 'cursor-pointer hover:opacity-70' : ''}`}
                data-ai-changelog-id="live-prompt-future"
                data-ai-changelog-title="未讲节点缩小"
                data-ai-changelog-desc="未讲节点灰色文字轻微缩小"
              >
                <div className="text-gray-500 text-[12px] mb-[4px]">下一模块</div>
                <div className="text-gray-400 text-[16px] font-bold">
                  {node.content}
                </div>
              </div>
            )}
          </div>
        ))}
      </div>

      {/* 黑话翻译弹窗 */}
      {jargonPopup && (
        <div
          className="fixed z-50 bg-gray-900 border border-cyan-500/50 rounded-[12px] p-[16px] shadow-xl max-w-[320px]"
          style={{
            left: Math.min(jargonPopup.x, window.innerWidth - 340),
            top: Math.min(jargonPopup.y - 10, window.innerHeight - 200),
          }}
        >
          <div className="flex items-center justify-between mb-[8px]">
            <span className="text-cyan-400 text-[14px] font-bold">
              <i className="fas fa-language mr-[6px]"></i>黑话翻译
            </span>
            <button
              className="text-gray-500 hover:text-gray-300 text-[12px]"
              onClick={() => setJargonPopup(null)}
            >
              <i className="fas fa-times"></i>
            </button>
          </div>
          <div className="text-white text-[12px] mb-[8px]">
            <span className="text-gray-400">专业术语：</span>
            <span className="text-cyan-300 font-bold">{jargonPopup.term}</span>
          </div>
          <div className="text-gray-200 text-[14px] leading-relaxed bg-gray-800 rounded-[8px] p-[12px]">
            {jargonPopup.explanation}
          </div>
          <div className="mt-[8px] text-[11px] text-purple-400/70 flex items-center">
            <i className="fas fa-robot mr-[4px]"></i>[由京东大模型生成]
          </div>
        </div>
      )}

      {/* 点击遮罩关闭弹窗 */}
      {jargonPopup && (
        <div className="fixed inset-0 z-40" onClick={() => setJargonPopup(null)} />
      )}
    </div>
  );
}

export default Teleprompter;