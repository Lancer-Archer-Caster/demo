import React, { useMemo, useState } from 'react';
import { isMockMode, liveApi } from './api';
import LiveMetricsBanner from './LiveMetricsBanner';
import PriceRadar from './PriceRadar';
import Teleprompter from './Teleprompter';
import GeekBulletScreen from './GeekBulletScreen';
import LiveSourceConnector from './LiveSourceConnector';

const FALLBACK_PRODUCTS = [
  { id: '100018374', name: '年度旗舰智能手机', params: '骁龙8 Gen3 · 120Hz OLED · 5000mAh', price: 2999 },
  { id: '100018375', name: '专业级降噪蓝牙耳机', params: '40dB深度降噪 · 30h续航', price: 399 },
  { id: '100018376', name: '超轻薄便携笔记本', params: '2.5K屏 · 长续航', price: 4999 },
];

const SCENES = [
  { id: 'warm', label: '温馨客厅', tone: 'from-orange-200 to-rose-300' },
  { id: 'clean', label: '极简纯白', tone: 'from-gray-100 to-white' },
  { id: 'studio', label: '专业影棚', tone: 'from-blue-900 to-slate-950' },
];

function readImportedCard() {
  try {
    return JSON.parse(sessionStorage.getItem('joycue.live.script') || 'null');
  } catch {
    return null;
  }
}

function LivePreparation({ imported, onStart }) {
  const [sceneId, setSceneId] = useState('warm');
  const [roomName, setRoomName] = useState('JoyCue 可信采销直播间');
  const [liveTime, setLiveTime] = useState('2026-07-14T20:00');
  const [description, setDescription] = useState('专业采销现场讲解，商品参数和价格请以页面为准。');
  const products = imported?.skus?.length ? imported.skus : FALLBACK_PRODUCTS;
  const fullScript = imported?.content || '暂无导入脚本。可返回商品管理，通过 JoyCard 生成、编辑并导入直播台。';

  const start = async () => {
    const payload = { sessionId: 1, roomName, liveTime, description, sceneId, productIds: products.map((item) => item.id), fullScript };
    try {
      if (!isMockMode) await liveApi.saveLivePreparation(payload);
      onStart(payload);
    } catch (error) {
      window.alert(`直播准备保存失败：${error.message}`);
    }
  };

  return (
    <div className="grid grid-cols-[280px_minmax(320px,1fr)_300px] gap-[14px] flex-1 min-h-0">
      <div className="flex flex-col gap-[12px] min-h-0">
        <section className="bg-white rounded-[12px] p-[14px] flex-1 min-h-0 overflow-y-auto">
          <div className="flex items-center justify-between mb-[10px]"><h3 className="font-bold text-[14px]">商品导入</h3><span className="text-[10px] text-green-600 bg-green-50 px-[6px] py-[3px] rounded">JoyCard 已同步</span></div>
          <div className="space-y-[8px]">{products.map((product) => <div key={product.id} className="border border-gray-100 rounded-[9px] p-[9px] flex gap-[9px]"><div className="w-[42px] h-[42px] rounded-[7px] bg-gradient-to-br from-blue-100 to-purple-200 flex items-center justify-center text-purple-500"><i className="fas fa-box"></i></div><div className="min-w-0"><div className="text-[12px] font-bold truncate">{product.name}</div><div className="text-[10px] text-gray-400">SKU: {product.id}</div><div className="text-[10px] text-gray-500 truncate mt-[2px]">{product.params}</div></div></div>)}</div>
        </section>
        <section className="bg-white rounded-[12px] p-[14px] h-[220px] flex flex-col">
          <div className="flex justify-between items-center mb-[8px]"><h3 className="font-bold text-[14px]">整体脚本预览</h3><span className="text-[10px] text-purple-600">Fixture / Adapter Ready</span></div>
          <div className="flex-1 overflow-y-auto whitespace-pre-wrap bg-gray-50 p-[10px] rounded-[8px] text-[11px] leading-relaxed text-gray-600">{fullScript}</div>
        </section>
      </div>

      <section className="bg-gray-950 rounded-[14px] relative overflow-hidden flex items-center justify-center">
        <div className={`absolute inset-0 bg-gradient-to-br ${SCENES.find((item) => item.id === sceneId)?.tone} opacity-50`}></div>
        <div className="relative aspect-[9/16] h-[88%] rounded-[22px] border-[8px] border-gray-900 bg-gradient-to-b from-indigo-950 to-purple-900 shadow-2xl flex flex-col items-center justify-center text-white">
          <i className="fas fa-video text-[36px] text-white/50 mb-[12px]"></i>
          <div className="font-bold">竖屏实时预览</div><div className="text-[11px] text-white/50 mt-[4px]">本地 Fixture · 摄像头待接入</div>
        </div>
        <span className="absolute top-[12px] left-[14px] bg-black/40 text-white text-[10px] px-[8px] py-[4px] rounded"><i className="fas fa-eye mr-[5px]"></i>预览中</span>
      </section>

      <div className="flex flex-col gap-[12px] min-h-0">
        <section className="bg-white rounded-[12px] p-[14px]">
          <h3 className="font-bold text-[14px] mb-[10px]">场景编辑</h3>
          <div className="grid grid-cols-2 gap-[8px]">{SCENES.map((scene) => <button key={scene.id} onClick={() => setSceneId(scene.id)} className={`relative h-[70px] rounded-[8px] bg-gradient-to-br ${scene.tone} border-2 ${sceneId === scene.id ? 'border-purple-500' : 'border-transparent'}`}><span className="absolute inset-x-0 bottom-0 bg-black/35 text-white text-[10px] py-[3px]">{scene.label}</span>{sceneId === scene.id && <i className="fas fa-check-circle absolute right-[5px] top-[5px] text-purple-600 bg-white rounded-full"></i>}</button>)}</div>
        </section>
        <section className="bg-white rounded-[12px] p-[14px] flex-1 overflow-y-auto">
          <h3 className="font-bold text-[14px] mb-[10px]">直播信息设置</h3>
          <label className="block text-[11px] text-gray-500 mb-[4px]">选择摄像头</label><select className="w-full border border-gray-200 rounded-[7px] p-[8px] text-[11px] mb-[9px]"><option>高清主摄 (1080P) · Adapter待接入</option></select>
          <label className="block text-[11px] text-gray-500 mb-[4px]">直播间名称</label><input value={roomName} onChange={(event) => setRoomName(event.target.value)} className="w-full border border-gray-200 rounded-[7px] p-[8px] text-[11px] mb-[9px]" />
          <label className="block text-[11px] text-gray-500 mb-[4px]">直播时间</label><input type="datetime-local" value={liveTime} onChange={(event) => setLiveTime(event.target.value)} className="w-full border border-gray-200 rounded-[7px] p-[8px] text-[11px] mb-[9px]" />
          <label className="block text-[11px] text-gray-500 mb-[4px]">直播简介</label><textarea value={description} onChange={(event) => setDescription(event.target.value)} className="w-full border border-gray-200 rounded-[7px] p-[8px] text-[11px] h-[64px] resize-none" />
          <div className="flex gap-[8px] mt-[12px]"><button className="flex-1 border border-gray-200 rounded-[7px] py-[8px] text-[11px]">保存草稿</button><button onClick={start} className="flex-1 bg-purple-600 text-white rounded-[7px] py-[8px] text-[11px] font-bold"><i className="fas fa-play mr-[5px]"></i>开始直播</button></div>
        </section>
      </div>
    </div>
  );
}

function LiveConsole() {
  const imported = useMemo(readImportedCard, []);
  const [activeTab, setActiveTab] = useState('prep');
  const [connection, setConnection] = useState({ status: 'DISCONNECTED' });
  const [commentRefresh, setCommentRefresh] = useState(0);
  const [liveStatus, setLiveStatus] = useState('live');
  const [casting, setCasting] = useState(false);
  const [currentProductIndex, setCurrentProductIndex] = useState(0);
  const products = imported?.skus?.length ? imported.skus : FALLBACK_PRODUCTS;

  const control = async (action) => {
    if (action === 'pause') setLiveStatus('paused');
    if (action === 'resume') setLiveStatus('live');
    if (action === 'end') { setLiveStatus('ended'); setActiveTab('prep'); }
    if (!isMockMode) liveApi.controlLive({ sessionId: 1, action }).catch(() => {});
  };

  return (
    <div className="flex flex-col h-full w-full min-h-0" data-ai-alt="直播控制台布局">
      <div className="flex justify-between items-center mb-[12px] shrink-0">
        <div className="flex gap-[18px] text-[13px] font-bold">
          <button onClick={() => setActiveTab('prep')} className={`pb-[7px] border-b-2 ${activeTab === 'prep' ? 'text-purple-600 border-purple-600' : 'text-gray-500 border-transparent'}`}>直播前准备</button>
          <button onClick={() => setActiveTab('console')} className={`pb-[7px] border-b-2 ${activeTab === 'console' ? 'text-purple-600 border-purple-600' : 'text-gray-500 border-transparent'}`}>直播中控台</button>
        </div>
        {activeTab === 'console' && <div className="flex gap-[8px]"><button onClick={() => setCasting((value) => !value)} className={`px-[12px] py-[7px] rounded-[7px] text-[11px] ${casting ? 'bg-purple-100 text-purple-700' : 'bg-white text-gray-600'}`}><i className="fas fa-display mr-[5px]"></i>{casting ? '结束投屏' : '提词器投屏'}</button><button onClick={() => control(liveStatus === 'paused' ? 'resume' : 'pause')} className="px-[12px] py-[7px] rounded-[7px] bg-yellow-500 text-white text-[11px]"><i className={`fas ${liveStatus === 'paused' ? 'fa-play' : 'fa-pause'} mr-[5px]`}></i>{liveStatus === 'paused' ? '恢复直播' : '暂停直播'}</button><button onClick={() => control('end')} className="px-[12px] py-[7px] rounded-[7px] bg-red-500 text-white text-[11px]"><i className="fas fa-power-off mr-[5px]"></i>结束直播</button></div>}
      </div>

      {activeTab === 'prep' ? <LivePreparation imported={imported} onStart={() => { setLiveStatus('live'); setActiveTab('console'); }} /> : (
        <div className="flex flex-col flex-1 min-h-0 bg-gray-900 rounded-[16px] p-[12px]">
          <LiveSourceConnector sessionId={1} onConnectionChange={setConnection} onCommentInjected={() => setCommentRefresh((value) => value + 1)} />
          <LiveMetricsBanner sessionId={1} />
          {casting && <div className="text-center text-[10px] text-purple-300 bg-purple-900/30 rounded py-[3px] mb-[6px]">提词器投屏中</div>}
          {liveStatus === 'paused' && <div className="absolute z-20 inset-0 pointer-events-none flex items-center justify-center"><span className="bg-black/70 text-white px-[30px] py-[14px] rounded-full text-[18px]">直播已暂停</span></div>}
          <div className="flex flex-1 gap-[12px] min-h-0">
            <div className="w-[24%] min-w-[220px] bg-gray-950 rounded-[12px] p-[12px] flex flex-col">
              <h3 className="text-gray-300 font-bold text-[13px] mb-[9px]"><i className="fas fa-box-open mr-[6px]"></i>直播商品列表</h3>
              <div className="space-y-[7px] flex-1 overflow-y-auto">{products.map((product, index) => <button key={product.id} onClick={() => setCurrentProductIndex(index)} className={`w-full text-left p-[9px] rounded-[8px] border ${index === currentProductIndex ? 'bg-purple-900/40 border-purple-500' : 'bg-gray-800 border-gray-700'}`}><div className="text-[11px] text-gray-200 font-bold truncate">{product.name}</div><div className="text-[9px] text-gray-500 mt-[3px]">SKU {product.id}</div>{index === currentProductIndex && <span className="inline-block mt-[5px] text-[9px] text-purple-300">讲解中</span>}</button>)}</div>
              <div className="flex gap-[6px] mt-[8px]"><button disabled={currentProductIndex === 0} onClick={() => setCurrentProductIndex((value) => value - 1)} className="flex-1 py-[7px] rounded bg-gray-700 text-gray-300 text-[10px] disabled:opacity-30">上一商品</button><button disabled={currentProductIndex === products.length - 1} onClick={() => setCurrentProductIndex((value) => value + 1)} className="flex-1 py-[7px] rounded bg-purple-600 text-white text-[10px] disabled:opacity-30">下一商品</button></div>
            </div>
            <div className="w-[51%] min-w-0 flex flex-col"><div className="flex-1 min-h-0"><Teleprompter /></div><div className="h-[220px] mt-[10px]"><PriceRadar skuId={products[currentProductIndex]?.id || '100018374'} /></div></div>
            <div className="w-[25%] min-w-[230px] flex flex-col min-h-0"><div className="h-[150px] bg-gradient-to-br from-indigo-950 to-purple-900 rounded-[12px] mb-[10px] flex items-center justify-center relative"><i className="fas fa-video text-white/30 text-[34px]"></i><span className="absolute left-[8px] top-[8px] bg-red-500 text-white text-[9px] px-[6px] py-[2px] rounded">{liveStatus === 'live' ? 'LIVE' : liveStatus.toUpperCase()}</span></div><div className="flex-1 min-h-0"><GeekBulletScreen sessionId={1} refreshToken={commentRefresh} sourceReady={connection.status === 'READY'} /></div></div>
          </div>
        </div>
      )}
    </div>
  );
}

export default LiveConsole;
