import React, { useState, useEffect } from 'react';
import HomeDashboard from './HomeDashboard';
import LiveConsole from './LiveConsole';
import SkuManage from './SkuManage';
import DataAnalysis from './DataAnalysis';
import SmartClip from './SmartClip';
import ClipWorkbench from './ClipWorkbench';

const NAV_ITEMS = [
  { key: 'skuManage', label: '商品管理', icon: 'fa-box' },
  { key: 'liveConsole', label: '直播控制台', icon: 'fa-video' },
  { key: 'videoClip', label: '智能切片', icon: 'fa-cut' },
  { key: 'dataCenter', label: '数据中心', icon: 'fa-chart-pie' }
];

function App() {
  const [currentPage, setCurrentPage] = useState(window.__INITIAL_PAGE_KEY__ || 'skuManage');
  const [isSidebarCollapsed, setIsSidebarCollapsed] = useState(false);
  const [activeReplayId, setActiveReplayId] = useState(null);

  useEffect(() => {
    const handlePageChange = () => {
      const pageKey = document.querySelector('[data-page-key]')?.getAttribute('data-page-key');
      if (pageKey && pageKey !== currentPage) {
        setCurrentPage(pageKey);
      }
    };
    handlePageChange();
  }, [currentPage]);

  useEffect(() => {
    window.__setCurrentPage = (pageKey) => {
      if (pageKey) setCurrentPage(pageKey);
    };
    return () => { delete window.__setCurrentPage; };
  }, []);

  useEffect(() => {
    const rootEl = document.querySelector('[data-page-key]');
    if (!rootEl) return;
    const observer = new MutationObserver(() => {
      const newKey = rootEl.getAttribute('data-page-key');
      if (newKey) setCurrentPage(newKey);
    });
    observer.observe(rootEl, { attributes: true, attributeFilter: ['data-page-key'] });
    return () => observer.disconnect();
  }, []);

  return (
    <div className="flex w-full h-screen bg-[#f7f8fc] font-sans text-gray-800 relative overflow-hidden" data-page-key={currentPage} data-ai-alt="系统主容器" data-ai-changelog-id="page-index" data-ai-changelog-title="主页界面更新" data-ai-changelog-desc="更新为蓝紫SaaS风格主页">
      {/* 柔和流体渐变气泡悬浮背景 */}
      <div className="absolute top-[-100px] left-[-100px] w-[500px] h-[500px] bg-purple-200/40 rounded-full blur-[80px] pointer-events-none"></div>
      <div className="absolute bottom-[-100px] right-[-100px] w-[600px] h-[600px] bg-blue-200/40 rounded-full blur-[100px] pointer-events-none"></div>

      {/* 侧边栏 */}
      <div className={`${isSidebarCollapsed ? 'w-0 border-r-0' : 'w-[240px]'} overflow-hidden bg-gradient-to-b from-[#f3f1fa] to-[#e6e9f8] border-r border-white/50 backdrop-blur-sm flex flex-col h-full shrink-0 z-10 transition-all duration-300`} data-ai-alt="左侧导航栏">
        <div className="flex items-center h-[72px] px-[24px]" data-ai-alt="系统Logo">
          <span className="text-[28px] font-black bg-clip-text text-transparent bg-gradient-to-r from-purple-600 to-blue-500 whitespace-nowrap">JoyCue</span>
        </div>
        <div className="flex-1 py-[24px] space-y-[8px] px-[16px]" data-ai-list="true" data-ai-alt="导航菜单列表">
          {NAV_ITEMS.map((item) => (
            <button
              key={item.key}
              data-action={`go-${item.key}`}
              className={`w-full flex items-center px-[16px] py-[12px] rounded-[12px] transition-all ${currentPage === item.key ? 'bg-white shadow-sm text-purple-600' : 'text-gray-600 hover:bg-white/50'}`}
              onClick={() => setCurrentPage(item.key)}
              data-ai-alt={`导航项-${item.label}`}
            >
              <i className={`fas ${item.icon} w-[24px] text-center mr-[12px] flex items-center justify-center text-[16px]`} data-ai-alt={`图标-${item.label}`}></i>
              <span className="text-[14px] font-medium">{item.label}</span>
            </button>
          ))}
        </div>
      </div>

      {/* 主内容区 */}
      <div className="flex-1 flex flex-col h-full z-10 relative" data-ai-alt="主内容区域">
        {/* 顶部栏 */}
        <div className="h-[72px] flex items-center justify-between px-[32px] shrink-0" data-ai-alt="顶部操作栏">
          <div className="flex items-center gap-[14px]">
            <button onClick={() => setIsSidebarCollapsed((value) => !value)} className="w-[36px] h-[36px] rounded-[10px] bg-white/60 text-gray-500 hover:text-purple-600 hover:bg-white transition-colors" title={isSidebarCollapsed ? '展开导航' : '收起导航'}>
              <i className="fas fa-bars"></i>
            </button>
            <div className="text-[20px] font-bold text-gray-800" data-ai-alt="当前页面标题">
              {currentPage === 'clipWorkbench' ? '智能剪辑工作台' : (NAV_ITEMS.find(i => i.key === currentPage)?.label || '加载中...')}
            </div>
          </div>
          <div className="flex items-center space-x-[20px]" data-ai-alt="顶部右侧工具">
            <div className="w-[36px] h-[36px] rounded-full bg-white/60 backdrop-blur-sm border border-white/50 flex items-center justify-center text-purple-500 cursor-pointer hover:bg-white">
               <i className="fas fa-bell"></i>
            </div>
            <div className="w-[40px] h-[40px] rounded-[12px] bg-gradient-to-tr from-blue-300 to-purple-300 flex items-center justify-center text-white font-bold cursor-pointer shadow-sm" data-ai-alt="用户头像">
              U
            </div>
          </div>
        </div>

        {/* 页面内容占位 */}
        <div className="flex-1 p-[32px] overflow-y-auto" data-ai-alt="页面具体内容区">
          {currentPage === 'skuManage' && <SkuManage onImportToLive={() => setCurrentPage('liveConsole')} />}
          {currentPage === 'liveConsole' && <LiveConsole />}
          {currentPage === 'videoClip' && <SmartClip onOpenWorkbench={(replayId) => { setActiveReplayId(replayId); setCurrentPage('clipWorkbench'); }} />}
          {currentPage === 'clipWorkbench' && <ClipWorkbench replayId={activeReplayId} onBack={() => setCurrentPage('videoClip')} />}
          {currentPage === 'dataCenter' && <DataAnalysis />}
          {!['skuManage', 'liveConsole', 'videoClip', 'clipWorkbench', 'dataCenter'].includes(currentPage) && (
            <div className="w-full h-full bg-white/40 backdrop-blur-md rounded-[20px] border border-white/50 shadow-sm flex items-center justify-center" data-ai-alt="开发中占位">
               <div className="text-center">
                 <div className="w-[64px] h-[64px] rounded-[16px] bg-white/60 mx-auto flex items-center justify-center mb-[16px]">
                    <i className="fas fa-tools text-[28px] text-purple-300"></i>
                 </div>
                 <p className="text-gray-600 font-medium">{NAV_ITEMS.find(i => i.key === currentPage)?.label} 模块开发中...</p>
               </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default App;
