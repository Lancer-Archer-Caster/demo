import React from 'react';

const CARDS = [
  { title: '实时记录', icon: 'fa-clock', desc: '实时转写与重点标记' },
  { title: '阅读助手', icon: 'fa-book-open', desc: '智能提炼长文核心' },
  { title: 'PPT 创作', icon: 'fa-file-powerpoint', desc: '一键生成汇报演示' },
  { title: '音视频速读', icon: 'fa-forward', desc: '音视频内容摘要提取' },
  { title: '论文速读', icon: 'fa-graduation-cap', desc: '学术文献核心解析' },
  { title: '链接速读', icon: 'fa-link', desc: '网页链接一键摘要' }
];

function HomeDashboard() {
  return (
    <div className="flex flex-col h-full w-full space-y-[24px]" data-ai-alt="主页内容容器">
      <div className="grid grid-cols-3 gap-[24px] shrink-0">
        {CARDS.map((card, idx) => (
          <div 
            key={idx} 
            className="bg-white/60 backdrop-blur-md border border-white/50 rounded-[16px] p-[24px] shadow-sm hover:shadow-md transition-all cursor-pointer flex flex-col"
            data-ai-alt={`功能卡片-${card.title}`}
          >
            <div className="w-[48px] h-[48px] rounded-[12px] bg-gradient-to-br from-blue-100 to-purple-100 flex items-center justify-center text-purple-500 mb-[16px]">
              <i className={`fas ${card.icon} text-[20px]`}></i>
            </div>
            <h3 className="text-[18px] font-bold text-gray-800 mb-[8px]">{card.title}</h3>
            <p className="text-[14px] text-gray-500">{card.desc}</p>
          </div>
        ))}
      </div>

      <div className="flex-1 bg-white/60 backdrop-blur-md border border-white/50 rounded-[16px] p-[24px] shadow-sm flex flex-col min-h-0" data-ai-alt="用户使用案例区域">
        <h3 className="text-[18px] font-bold text-gray-800 mb-[16px]">用户使用案例</h3>
        <div className="flex-1 overflow-y-auto space-y-[16px]" data-ai-list="true">
          {[1, 2, 3].map((item) => (
            <div key={item} className="flex items-start space-x-[16px] p-[16px] bg-white/40 rounded-[12px]">
              <div className="w-[40px] h-[40px] rounded-full bg-gradient-to-r from-blue-200 to-purple-200 shrink-0"></div>
              <div>
                <div className="text-[14px] font-bold text-gray-800">某头部带货主播团队</div>
                <div className="text-[13px] text-gray-500 mt-[4px]">通过智能直播辅助系统，在单场直播中提升了30%的用户互动响应率，并利用智能切片功能快速产出了50+短视频素材。</div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default HomeDashboard;
