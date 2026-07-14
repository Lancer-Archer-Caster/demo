import React, { useEffect, useState, useRef } from 'react';
import { isMockMode, liveApi, toUiSku } from './api';

// 模拟合规词库
const COMPLIANCE_WORDS = ['最', '第一', '顶级', '极品', '绝无仅有', '空前', '史无前例', '万能', '100%', '国家级', '世界级'];

// 模拟SKU数据
const MOCK_SKU_DATA = [
  { id: '100018374', name: '京东自营 5G智能手机 骁龙8 Gen3 5000mAh', price: 2999, image: '', params: '120Hz OLED | 67W闪充 | 5000mAh', status: 'normal' },
  { id: '100018375', name: '京东自营 无线蓝牙耳机 降噪Pro', price: 399, image: '', params: 'ANC主动降噪 | 30h续航 | 蓝牙5.3', status: 'normal' },
  { id: '100018376', name: '京东自营 4K超高清智能电视 65英寸', price: 3299, image: '', params: '4K HDR | MEMC | 2GB+32GB', status: 'warning' },
];

const LOCAL_DEMO_SKU_IDS = ['100018374', '100018375', '100018376'];

// 模拟AI脚本生成结果
const MOCK_SCRIPT = {
  title: '5G智能手机专场直播脚本',
  thinking: [
    { step: '解析SKU文本内容并进行数据清洗', done: true },
    { step: '提取核心卖点属性（参数、优势、适用人群）', done: true },
    { step: '交叉比对合规词库，检测极限词与敏感表述', done: true },
    { step: '匹配最佳直播话术模板并开始生成', done: true },
  ],
  sections: [
    {
      id: 1,
      title: '开场暖场（0-3分钟）',
      content: '家人们好！今天给大家带来的是京东自营5G智能手机，骁龙8 Gen3旗舰芯片，性能炸裂！先别急着下单，听我把亮点说完，保证你们觉得值！',
      duration: '3min',
      keyPoints: ['品牌背书', '旗舰芯片', '悬念铺垫'],
    },
    {
      id: 2,
      title: '核心卖点讲解（3-10分钟）',
      content: '这款手机三大杀手锏：第一，120Hz高刷OLED屏幕，刷视频丝滑到飞起；第二，5000mAh大电池配67W闪充，告别电量焦虑；第三，骁龙8 Gen3，原神满帧不卡！游戏党、追剧党，这波必须冲！',
      duration: '7min',
      keyPoints: ['屏幕参数', '续航优势', '性能表现'],
    },
    {
      id: 3,
      title: '竞品对比与逼单（10-15分钟）',
      content: '同配置友商卖3499，咱们今天京东价2999，还送原装耳机！Plus会员再享免邮+次日达，这个价格全网找不到第二家！倒计时3-2-1，上链接！',
      duration: '5min',
      keyPoints: ['价格对比', '服务优势', '限时逼单'],
    },
    {
      id: 4,
      title: '售后保障与收尾（15-18分钟）',
      content: '180天只换不修，Plus会员专属客服，售后完全不用担心！没抢到的家人关注主播，下场还有更多好货！',
      duration: '3min',
      keyPoints: ['售后保障', '关注引导'],
    },
  ],
};

function SkuManage({ onImportToLive }) {
  const [skuList, setSkuList] = useState([]);
  const [skuInput, setSkuInput] = useState('');
  const [isThinking, setIsThinking] = useState(false);
  const [thinkingStep, setThinkingStep] = useState(0);
  const [complianceResults, setComplianceResults] = useState([]);
  const [generatedScript, setGeneratedScript] = useState(null);
  const [activeTab, setActiveTab] = useState('sku'); // sku | compliance | script
  const [backendStatus, setBackendStatus] = useState(isMockMode ? 'mock' : 'checking');
  const fileInputRef = useRef(null);

  const finalScript = generatedScript?.sections
    ?.filter((section) => section.selected !== false)
    .map((section) => section.content)
    .join('\n\n') || '';

  useEffect(() => {
    if (isMockMode) return;
    const controller = new AbortController();
    liveApi.health(controller.signal)
      .then((health) => setBackendStatus(health.status === 'UP' ? 'up' : 'down'))
      .catch(() => setBackendStatus('down'));
    return () => controller.abort();
  }, []);

  // 解析SKU输入
  const parseSkuInput = (text) => {
    const lines = text.split('\n').filter(l => l.trim());
    return lines.map(line => {
      const parts = line.split(/[\s,，]+/).filter(Boolean);
      return { id: parts[0] || '', name: parts.slice(1).join(' ') || `SKU ${parts[0]}`, status: 'normal' };
    });
  };

  // 添加SKU
  const handleAddSku = async () => {
    if (!skuInput.trim()) return;
    const parsedSkus = parseSkuInput(skuInput);
    try {
      const newSkus = isMockMode
        ? parsedSkus
        : (await liveApi.batchSkus(parsedSkus.map((sku) => sku.id))).map(toUiSku);
      setSkuList(prev => [...prev, ...newSkus]);
    } catch (error) {
      window.alert(`商品查询失败：${error.message}`);
      return;
    }
    setSkuInput('');
  };

  // 删除SKU
  const handleRemoveSku = (id) => {
    setSkuList(prev => prev.filter(s => s.id !== id));
  };

  const handleLoadLocalDemo = async () => {
    try {
      const demoSkus = isMockMode
        ? MOCK_SKU_DATA
        : (await liveApi.batchSkus(LOCAL_DEMO_SKU_IDS)).map(toUiSku);
      setSkuList(demoSkus);
      setActiveTab('sku');
    } catch (error) {
      window.alert(`联通示例加载失败：${error.message}`);
    }
  };

  // 演示模式直接填充样例；真实模式交给后端异步导入，便于支持 CSV/XLSX 和大文件。
  const handleExcelImport = () => {
    fileInputRef.current?.click();
  };

  const handleFileChange = async (e) => {
    const [file] = e.target.files || [];
    if (!file) return;
    if (isMockMode) {
      setSkuList(prev => [...prev, ...MOCK_SKU_DATA]);
    } else {
      try {
        const task = await liveApi.importSkuFile(file, 1);
        window.alert(`导入任务已创建：${task.taskId}。接入真实数据源后可轮询任务状态。`);
      } catch (error) {
        window.alert(`文件导入失败：${error.message}`);
      }
    }
    e.target.value = '';
  };

  // 合规检测
  const handleComplianceCheck = async () => {
    if (skuList.length === 0) return;
    let results;
    try {
      results = await Promise.all(skuList.map(async (sku) => {
        if (isMockMode) {
          const found = COMPLIANCE_WORDS.filter(w => sku.name.includes(w));
          return { ...sku, violations: found, isCompliant: found.length === 0 };
        }
        const result = await liveApi.checkCompliance({ skuId: sku.id, content: sku.name });
        return {
          ...sku,
          violations: (result.hits || []).map((hit) => hit.word || hit.content || hit),
          isCompliant: Boolean(result.passed),
          suggestions: result.suggestions || [],
        };
      }));
    } catch (error) {
      window.alert(`合规检测失败：${error.message}`);
      return;
    }
    setComplianceResults(results);
    setActiveTab('compliance');
  };

  // AI脚本生成（模拟异步思考过程）
  const handleGenerateScript = async () => {
    if (skuList.length === 0) return;
    setIsThinking(true);
    setThinkingStep(0);
    setGeneratedScript(null);
    setActiveTab('script');

    const steps = MOCK_SCRIPT.thinking;
    let step = 0;
    const timer = setInterval(() => {
      step++;
      setThinkingStep(step);
      if (step >= steps.length) {
        clearInterval(timer);
        setTimeout(async () => {
          try {
            if (isMockMode) {
              setGeneratedScript(MOCK_SCRIPT);
            } else {
              const script = await liveApi.generateScript({
                sessionId: 1,
                skuId: skuList[0].id,
                templateType: 'live-commerce',
              });
              setGeneratedScript({
                title: `SKU ${script.skuId || skuList[0].id} 直播脚本`,
                thinking: steps,
                sections: (script.nodes || []).map((node, index) => ({
                  id: node.id || index,
                  title: node.title || `话术节点 ${index + 1}`,
                  content: node.content || '',
                  duration: '',
                  keyPoints: String(node.keywords || '').split(',').filter(Boolean),
                  selected: true,
                })),
              });
            }
          } catch (error) {
            window.alert(`话术生成失败：${error.message}`);
          } finally {
            setIsThinking(false);
          }
        }, 500);
      }
    }, 800);
  };

  const updateScriptSection = (id, patch) => {
    setGeneratedScript((script) => ({
      ...script,
      sections: script.sections.map((section) => section.id === id ? { ...section, ...patch } : section),
    }));
  };

  const exportWord = () => {
    if (!finalScript.trim()) {
      window.alert('请至少选择一条话术');
      return;
    }
    const html = `<html><meta charset="utf-8"><body><h1>${generatedScript.title}</h1>${generatedScript.sections.filter((item) => item.selected !== false).map((item) => `<h2>${item.title}</h2><p>${item.content}</p>`).join('')}</body></html>`;
    const url = URL.createObjectURL(new Blob([html], { type: 'application/msword;charset=utf-8' }));
    const link = document.createElement('a');
    link.href = url;
    link.download = 'JoyCue直播手卡.doc';
    link.click();
    URL.revokeObjectURL(url);
  };

  const importToLive = () => {
    if (!finalScript.trim()) {
      window.alert('暂无可导入脚本');
      return;
    }
    sessionStorage.setItem('joycue.live.script', JSON.stringify({
      title: generatedScript.title,
      content: finalScript,
      sections: generatedScript.sections.filter((section) => section.selected !== false),
      skus: skuList,
    }));
    onImportToLive?.();
  };

  return (
    <div className="flex flex-col h-full w-full space-y-[16px]" data-ai-alt="商品管理页面" data-page-key="skuManage">
      {/* 上半部分：操作区 */}
      <div className="bg-white/60 backdrop-blur-md border border-white/50 rounded-[16px] p-[24px] shadow-sm" data-ai-alt="操作区">
        <div className="flex items-center justify-between mb-[16px]">
          <h3 className="text-[18px] font-bold text-gray-800">商品SKU录入</h3>
          <div
            data-testid="backend-status"
            className={`flex items-center px-[10px] py-[5px] rounded-full text-[12px] font-medium ${
              backendStatus === 'up'
                ? 'bg-green-100 text-green-700'
                : backendStatus === 'checking'
                  ? 'bg-blue-100 text-blue-700'
                  : backendStatus === 'mock'
                    ? 'bg-gray-100 text-gray-600'
                    : 'bg-red-100 text-red-700'
            }`}
          >
            <span className={`w-[7px] h-[7px] rounded-full mr-[6px] ${
              backendStatus === 'up' ? 'bg-green-500' : backendStatus === 'checking' ? 'bg-blue-500 animate-pulse' : backendStatus === 'mock' ? 'bg-gray-400' : 'bg-red-500'
            }`}></span>
            {backendStatus === 'up' && '后端已联通 · H2 本地库'}
            {backendStatus === 'checking' && '正在检查后端...'}
            {backendStatus === 'mock' && '当前为 Mock 模式'}
            {backendStatus === 'down' && '后端未连接'}
          </div>
        </div>
        <div className="flex flex-col space-y-[12px]">
          <textarea
            placeholder="请输入或粘贴SKU文本，每行一个SKU（如：100018374 5G智能手机）"
            className="w-full h-[100px] resize-none border border-gray-300 rounded-[8px] px-[16px] py-[12px] text-[14px] bg-white/80 focus:outline-none focus:ring-2 focus:ring-purple-400 transition-all"
            value={skuInput}
            onChange={e => setSkuInput(e.target.value)}
            data-ai-alt="SKU输入框"
            data-ai-changelog-id="sku-input"
            data-ai-changelog-title="SKU输入框"
            data-ai-changelog-desc="支持粘贴多行SKU文字"
          />
          <div className="flex justify-between items-center">
            <div className="text-[12px] text-gray-400">
              {skuList.length > 0 && `已添加 ${skuList.length} 个SKU`}
            </div>
            <div className="flex space-x-[12px]">
              <button
                className="bg-green-50 border border-green-300 text-green-700 px-[16px] py-[8px] rounded-[8px] hover:bg-green-100 transition-colors text-[14px] font-medium shadow-sm flex items-center"
                onClick={handleLoadLocalDemo}
                data-testid="load-local-demo"
              >
                <i className="fas fa-database mr-[8px]"></i>加载联通示例
              </button>
              <button
                className="bg-white border border-gray-300 text-gray-700 px-[16px] py-[8px] rounded-[8px] hover:bg-gray-50 transition-colors text-[14px] font-medium shadow-sm flex items-center"
                onClick={handleExcelImport}
                data-ai-alt="Excel导入按钮"
                data-ai-changelog-id="sku-import-btn"
                data-ai-changelog-title="Excel导入按钮"
                data-ai-changelog-desc="通过Excel导入商品信息"
              >
                <i className="fas fa-file-excel text-green-600 mr-[8px]"></i>Excel导入
              </button>
              <input
                ref={fileInputRef}
                type="file"
                accept=".xlsx,.xls,.csv"
                className="hidden"
                onChange={handleFileChange}
              />
              <button
                className="bg-white border border-purple-400 text-purple-600 px-[16px] py-[8px] rounded-[8px] hover:bg-purple-50 transition-colors text-[14px] font-medium shadow-sm flex items-center"
                onClick={handleAddSku}
                disabled={!skuInput.trim()}
              >
                <i className="fas fa-plus mr-[8px]"></i>添加SKU
              </button>
              <button
                className="bg-white border border-yellow-500 text-yellow-700 px-[16px] py-[8px] rounded-[8px] hover:bg-yellow-50 transition-colors text-[14px] font-medium shadow-sm flex items-center"
                onClick={handleComplianceCheck}
                disabled={skuList.length === 0}
              >
                <i className="fas fa-shield-alt mr-[8px]"></i>播前排雷
              </button>
              <button
                className="bg-gradient-to-r from-blue-500 to-purple-600 text-white px-[20px] py-[8px] rounded-[8px] hover:opacity-90 transition-opacity text-[14px] font-medium shadow-md flex items-center"
                onClick={handleGenerateScript}
                disabled={skuList.length === 0}
                data-ai-alt="生成脚本按钮"
                data-ai-changelog-id="sku-generate-btn"
                data-ai-changelog-title="生成脚本按钮"
                data-ai-changelog-desc="根据SKU生成脚本"
              >
                <i className="fas fa-magic mr-[8px]"></i>AI生成脚本
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* 下半部分：Tab内容区 */}
      <div className="flex-1 bg-white/60 backdrop-blur-md border border-white/50 rounded-[16px] p-[24px] shadow-sm flex flex-col min-h-0" data-ai-alt="内容展示区">
        {/* Tab导航 */}
        <div className="flex space-x-[16px] mb-[16px] border-b border-gray-200 pb-[12px]">
          <button
            className={`text-[14px] font-medium pb-[8px] border-b-2 transition-colors ${activeTab === 'sku' ? 'border-purple-600 text-purple-600' : 'border-transparent text-gray-500 hover:text-gray-700'}`}
            onClick={() => setActiveTab('sku')}
          >
            <i className="fas fa-box mr-[6px]"></i>SKU列表 ({skuList.length})
          </button>
          <button
            className={`text-[14px] font-medium pb-[8px] border-b-2 transition-colors ${activeTab === 'compliance' ? 'border-purple-600 text-purple-600' : 'border-transparent text-gray-500 hover:text-gray-700'}`}
            onClick={() => setActiveTab('compliance')}
          >
            <i className="fas fa-shield-alt mr-[6px]"></i>合规检测 {complianceResults.length > 0 && `(${complianceResults.filter(r => !r.isCompliant).length}项风险)`}
          </button>
          <button
            className={`text-[14px] font-medium pb-[8px] border-b-2 transition-colors ${activeTab === 'script' ? 'border-purple-600 text-purple-600' : 'border-transparent text-gray-500 hover:text-gray-700'}`}
            onClick={() => setActiveTab('script')}
          >
            <i className="fas fa-scroll mr-[6px]"></i>直播脚本
          </button>
        </div>

        {/* Tab内容 */}
        <div className="flex-1 overflow-y-auto">
          {/* SKU列表 */}
          {activeTab === 'sku' && (
            <div className="space-y-[8px]">
              {skuList.length === 0 ? (
                <div className="flex flex-col items-center justify-center py-[60px] text-gray-400">
                  <i className="fas fa-inbox text-[48px] mb-[16px] opacity-50"></i>
                  <span className="text-[14px]">暂无SKU，请通过输入框或Excel导入添加</span>
                </div>
              ) : (
                <div className="grid grid-cols-1 gap-[8px]">
                  {skuList.map((sku, idx) => (
                    <div key={sku.id + idx} className="flex items-center bg-white/80 rounded-[12px] p-[12px] border border-gray-100 hover:shadow-sm transition-shadow">
                      <div className="w-[40px] h-[40px] rounded-[8px] bg-gradient-to-br from-blue-100 to-purple-100 flex items-center justify-center text-purple-500 shrink-0 mr-[12px]">
                        <i className="fas fa-mobile-alt text-[16px]"></i>
                      </div>
                      <div className="flex-1 min-w-0">
                        <div className="text-[14px] font-bold text-gray-800 truncate">{sku.name}</div>
                        <div className="text-[12px] text-gray-500">SKU: {sku.id} {sku.price && `| ¥${sku.price}`}</div>
                      </div>
                      {sku.status === 'warning' && (
                        <span className="text-[12px] bg-yellow-100 text-yellow-700 px-[8px] py-[2px] rounded-[4px] mr-[8px]">需排雷</span>
                      )}
                      <button
                        className="text-gray-400 hover:text-red-500 transition-colors p-[4px]"
                        onClick={() => handleRemoveSku(sku.id)}
                        title="移除"
                      >
                        <i className="fas fa-times"></i>
                      </button>
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}

          {/* 合规检测结果 */}
          {activeTab === 'compliance' && (
            <div className="space-y-[12px]">
              {complianceResults.length === 0 ? (
                <div className="flex flex-col items-center justify-center py-[60px] text-gray-400">
                  <i className="fas fa-shield-alt text-[48px] mb-[16px] opacity-50"></i>
                  <span className="text-[14px]">请先添加SKU并点击"播前排雷"进行合规检测</span>
                </div>
              ) : (
                <>
                  <div className="bg-green-50 border border-green-200 rounded-[8px] p-[12px] flex items-center">
                    <i className="fas fa-check-circle text-green-500 mr-[8px]"></i>
                    <span className="text-[14px] text-green-700">
                      检测完成：{complianceResults.filter(r => r.isCompliant).length} 项通过，{complianceResults.filter(r => !r.isCompliant).length} 项存在风险
                    </span>
                  </div>
                  {complianceResults.map((result, idx) => (
                    <div key={idx} className={`rounded-[8px] p-[12px] border ${result.isCompliant ? 'bg-green-50/50 border-green-100' : 'bg-red-50/50 border-red-200'}`}>
                      <div className="flex items-center justify-between">
                        <div className="flex items-center">
                          <i className={`fas ${result.isCompliant ? 'fa-check-circle text-green-500' : 'fa-exclamation-triangle text-red-500'} mr-[8px]`}></i>
                          <span className="text-[14px] font-bold text-gray-800">{result.name}</span>
                        </div>
                        {result.isCompliant ? (
                          <span className="text-[12px] bg-green-100 text-green-700 px-[8px] py-[2px] rounded-[4px]">合规</span>
                        ) : (
                          <span className="text-[12px] bg-red-100 text-red-700 px-[8px] py-[2px] rounded-[4px]">风险</span>
                        )}
                      </div>
                      {!result.isCompliant && (
                        <div className="mt-[8px] pl-[24px]">
                          <span className="text-[12px] text-red-600">检测到极限词：{result.violations.map(w => `"${w}"`).join('、')}</span>
                          <div className="text-[12px] text-gray-500 mt-[4px]">建议：替换为合规表述，如"领先"替代"第一"，"优质"替代"顶级"</div>
                        </div>
                      )}
                    </div>
                  ))}
                </>
              )}
            </div>
          )}

          {/* AI脚本生成 */}
          {activeTab === 'script' && (
            <div data-ai-alt="脚本生成区" data-ai-changelog-id="sku-thinking-process" data-ai-changelog-title="思考过程展示" data-ai-changelog-desc="点击生成脚本后展示AI思考分析的过程">
              {isThinking ? (
                <div className="flex flex-col items-center justify-center py-[40px] space-y-[16px] w-full max-w-[600px] mx-auto">
                  <div className="flex items-center space-x-[12px] text-purple-600 font-bold text-[18px]">
                    <i className="fas fa-spinner fa-spin"></i>
                    <span>AI正在深入分析商品特征...</span>
                  </div>
                  <div className="bg-white/80 rounded-[8px] p-[24px] w-full border border-purple-200 shadow-sm space-y-[12px]">
                    {MOCK_SCRIPT.thinking.map((step, idx) => (
                      <div key={idx} className={`flex items-center text-[14px] ${idx < thinkingStep ? 'text-gray-600' : 'text-gray-400'}`}>
                        {idx < thinkingStep ? (
                          <i className="fas fa-check-circle text-green-500 mr-[8px]"></i>
                        ) : idx === thinkingStep ? (
                          <i className="fas fa-circle-notch fa-spin text-purple-500 mr-[8px]"></i>
                        ) : (
                          <i className="fas fa-circle text-gray-300 mr-[8px] text-[8px]"></i>
                        )}
                        <span className={idx === thinkingStep ? 'animate-pulse' : ''}>{step.step}</span>
                      </div>
                    ))}
                  </div>
                </div>
              ) : generatedScript ? (
                <div className="space-y-[16px]">
                  <div className="flex items-center justify-between">
                    <div>
                      <h4 className="text-[16px] font-bold text-gray-800">JoyCard · {generatedScript.title}</h4>
                      <p className="text-[11px] text-gray-400 mt-[3px]">勾选和编辑话术卡片，右侧最终脚本会实时更新。</p>
                    </div>
                    <div className="flex items-center gap-[8px]">
                      <span className="text-[12px] text-purple-500 bg-purple-50 px-[8px] py-[5px] rounded-[4px]"><i className="fas fa-robot mr-[4px]"></i>[本地 Fixture 生成]</span>
                      <button onClick={exportWord} className="px-[12px] py-[7px] rounded-[7px] border border-gray-200 text-gray-600 text-[12px]"><i className="fas fa-file-word text-blue-500 mr-[5px]"></i>导出Word</button>
                      <button onClick={importToLive} className="px-[12px] py-[7px] rounded-[7px] bg-purple-600 text-white text-[12px]"><i className="fas fa-satellite-dish mr-[5px]"></i>导入直播台</button>
                    </div>
                  </div>
                  <div className="grid grid-cols-[minmax(0,1.6fr)_minmax(300px,1fr)] gap-[16px]">
                    <div className="grid grid-cols-2 gap-[10px]">
                      {generatedScript.sections.map((section) => (
                        <div key={section.id} className={`bg-white/80 rounded-[12px] p-[13px] border shadow-sm ${section.selected === false ? 'border-gray-100 opacity-55' : 'border-purple-100'}`}>
                          <div className="flex items-center justify-between mb-[8px]">
                            <label className="flex items-center min-w-0 cursor-pointer">
                              <input type="checkbox" checked={section.selected !== false} onChange={(event) => updateScriptSection(section.id, { selected: event.target.checked })} className="accent-purple-600 mr-[8px]" />
                              <span className="text-[13px] font-bold text-gray-800 truncate">{section.title}</span>
                            </label>
                            <span className="text-[10px] text-gray-400">#{section.id}</span>
                          </div>
                          <textarea value={section.content} onChange={(event) => updateScriptSection(section.id, { content: event.target.value })} className="w-full h-[96px] resize-none bg-gray-50 border border-gray-100 rounded-[8px] p-[9px] text-[12px] leading-relaxed outline-none focus:border-purple-300" />
                          <div className="flex flex-wrap gap-[4px] mt-[7px]">
                            {section.keyPoints.map((point, i) => <span key={i} className="text-[10px] bg-purple-50 text-purple-600 px-[6px] py-[2px] rounded">#{point}</span>)}
                          </div>
                        </div>
                      ))}
                    </div>
                    <div className="bg-white rounded-[12px] border border-gray-100 p-[16px] flex flex-col min-h-[320px]">
                      <div className="font-bold text-[14px] text-gray-800 mb-[10px]">最终脚本预览</div>
                      <div className="flex-1 whitespace-pre-wrap text-[13px] leading-[1.8] text-gray-600 overflow-y-auto bg-gray-50 rounded-[8px] p-[12px]">{finalScript || '请至少勾选一条话术'}</div>
                      <div className="text-[10px] text-gray-400 mt-[8px]">导入直播台后将映射为动态提词器节点。</div>
                    </div>
                  </div>
                </div>
              ) : (
                <div className="flex flex-col items-center justify-center py-[60px] text-gray-400">
                  <i className="fas fa-scroll text-[48px] mb-[16px] opacity-50"></i>
                  <span className="text-[14px]">请先添加SKU并点击"AI生成脚本"</span>
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default SkuManage;
