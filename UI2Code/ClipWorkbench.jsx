import React, { useState } from 'react';

const initialVideos = [
  { id: 'v1', title: '核心卖点 15 秒', duration: '00:15' },
  { id: 'v2', title: '价格权益 12 秒', duration: '00:12' },
  { id: 'v3', title: '高频问答 18 秒', duration: '00:18' },
];

function ClipWorkbench({ replayId, onBack }) {
  const [messages, setMessages] = useState([{ id: 1, role: 'ai', content: '我是 JoyClip 助手。请描述剪辑风格、目标时长或要突出的话术。' }]);
  const [input, setInput] = useState('');
  const [activeTab, setActiveTab] = useState('video');
  const [videos, setVideos] = useState(initialVideos);
  const [images, setImages] = useState([{ id: 'i1', title: '商品卖点封面' }, { id: 'i2', title: '价格权益海报' }, { id: 'i3', title: '高频问答卡片' }]);

  const send = () => {
    if (!input.trim()) return;
    setMessages((items) => [...items, { id: Date.now(), role: 'user', content: input.trim() }, { id: Date.now() + 1, role: 'ai', content: '已收到需求。真实视频算法接入后将在此更新任务进度；当前展示可交互的素材工作台。' }]);
    setInput('');
  };

  const exportAll = () => {
    const manifest = JSON.stringify({ replayId, videos, images }, null, 2);
    const url = URL.createObjectURL(new Blob([manifest], { type: 'application/json' }));
    const link = document.createElement('a');
    link.href = url;
    link.download = `joyclip-${replayId || 'demo'}-manifest.json`;
    link.click();
    URL.revokeObjectURL(url);
  };

  return (
    <div className="h-full flex flex-col" data-page-key="clipWorkbench">
      <div className="flex justify-between items-center mb-[16px]">
        <button onClick={onBack} className="text-[13px] text-gray-500 hover:text-purple-600"><i className="fas fa-chevron-left mr-[7px]"></i>视频回放列表 / 智能剪辑工作台</button>
        <button onClick={exportAll} className="bg-purple-600 text-white px-[15px] py-[8px] rounded-[8px] text-[13px]"><i className="fas fa-box-open mr-[6px]"></i>全部导出</button>
      </div>
      <div className="flex-1 min-h-0 grid grid-cols-[300px_1fr_340px] gap-[14px]">
        <section className="bg-white/80 rounded-[14px] p-[16px] flex flex-col min-h-0">
          <h3 className="font-bold text-gray-800"><i className="fas fa-wand-magic-sparkles text-purple-500 mr-[7px]"></i>AI剪辑助手</h3>
          <div className="flex-1 overflow-y-auto space-y-[10px] py-[14px]">
            {messages.map((message) => <div key={message.id} className={`text-[12px] leading-relaxed rounded-[10px] p-[10px] ${message.role === 'user' ? 'ml-[30px] bg-purple-600 text-white' : 'mr-[20px] bg-purple-50 text-purple-900'}`}>{message.content}</div>)}
          </div>
          <textarea value={input} onChange={(event) => setInput(event.target.value)} onKeyDown={(event) => { if (event.key === 'Enter' && !event.shiftKey) { event.preventDefault(); send(); } }} placeholder="如：生成30秒竖屏种草片，突出续航和快充" className="h-[78px] resize-none border border-gray-200 rounded-[9px] p-[10px] text-[12px] outline-none focus:border-purple-400" />
          <button onClick={send} className="mt-[8px] bg-purple-600 text-white py-[7px] rounded-[8px] text-[12px]">发送需求</button>
        </section>

        <section className="bg-gray-900 rounded-[14px] flex items-center justify-center relative overflow-hidden">
          <div className="aspect-[9/16] h-[90%] max-h-[620px] rounded-[18px] bg-gradient-to-br from-indigo-950 via-purple-900 to-blue-800 border-[6px] border-gray-800 shadow-2xl flex items-center justify-center relative">
            <button className="w-[64px] h-[64px] rounded-full bg-white/20 text-white text-[22px] backdrop-blur"><i className="fas fa-play ml-[4px]"></i></button>
            <div className="absolute left-[14px] right-[14px] bottom-[16px]">
              <div className="text-white text-[16px] font-bold mb-[10px]">5000mAh 大电池 · 67W 快充</div>
              <div className="h-[3px] bg-white/20 rounded"><div className="h-full bg-purple-400 rounded w-[42%]"></div></div>
            </div>
          </div>
          <span className="absolute top-[12px] left-[14px] text-[11px] text-gray-400">回放：{replayId || 'demo'}</span>
        </section>

        <section className="bg-white/80 rounded-[14px] p-[14px] flex flex-col min-h-0">
          <div className="flex bg-gray-100 p-[3px] rounded-[8px] mb-[12px]">
            <button onClick={() => setActiveTab('video')} className={`flex-1 py-[7px] text-[12px] rounded-[6px] ${activeTab === 'video' ? 'bg-white text-purple-600 shadow-sm' : 'text-gray-500'}`}>生成片段</button>
            <button onClick={() => setActiveTab('image')} className={`flex-1 py-[7px] text-[12px] rounded-[6px] ${activeTab === 'image' ? 'bg-white text-purple-600 shadow-sm' : 'text-gray-500'}`}>生成图片</button>
          </div>
          <div className="flex-1 overflow-y-auto">
            {activeTab === 'video' ? videos.map((item, index) => (
              <div key={item.id} className="group flex items-center gap-[10px] p-[9px] mb-[8px] rounded-[9px] border border-gray-100 bg-white">
                <i className="fas fa-grip-vertical text-gray-300"></i>
                <div className="w-[78px] h-[52px] rounded bg-gradient-to-br from-indigo-800 to-purple-600 flex items-center justify-center text-white/60"><i className="fas fa-play"></i></div>
                <div className="flex-1"><div className="text-[12px] font-bold">{item.title}</div><div className="text-[10px] text-gray-400 mt-[4px]">{item.duration}</div></div>
                <button onClick={() => setVideos((items) => items.filter((video) => video.id !== item.id))} className="text-gray-300 hover:text-red-500"><i className="fas fa-trash"></i></button>
              </div>
            )) : <div className="grid grid-cols-2 gap-[8px]">{images.map((item) => (
              <div key={item.id} className="group relative aspect-square rounded-[9px] bg-gradient-to-br from-blue-100 to-purple-200 flex items-center justify-center text-purple-500">
                <i className="fas fa-image text-[28px]"></i>
                <div className="absolute inset-x-0 bottom-0 p-[7px] text-[10px] bg-white/80 text-gray-700">{item.title}</div>
                <button onClick={() => setImages((items) => items.filter((image) => image.id !== item.id))} className="absolute right-[6px] top-[6px] opacity-0 group-hover:opacity-100 w-[25px] h-[25px] rounded bg-red-500 text-white"><i className="fas fa-trash text-[10px]"></i></button>
              </div>
            ))}</div>}
          </div>
        </section>
      </div>
    </div>
  );
}

export default ClipWorkbench;
