import React, { useEffect, useMemo, useState } from 'react';
import { isMockMode, liveApi } from './api';

const REPLAY_FIXTURES = [
  { replayId: 'replay-001', title: '双十一特惠家电数码大促专场', dateRange: '2026-07-12 19:00 - 23:00', duration: '04:00:00', views: '12.5w', clickRate: '15.3%', gmv: 345000, clipStatus: 'ready', tone: 'from-slate-800 to-blue-900' },
  { replayId: 'replay-002', title: '秋季美妆护肤新品发布会', dateRange: '2026-07-10 20:00 - 22:00', duration: '02:00:00', views: '8.2w', clickRate: '12.1%', gmv: 128500, clipStatus: 'done', tone: 'from-rose-100 to-orange-100' },
  { replayId: 'replay-003', title: '夏末秋初男女装清仓捡漏', dateRange: '2026-07-08 18:00 - 22:00', duration: '04:00:00', views: '6.7w', clickRate: '9.8%', gmv: 98600, clipStatus: 'failed', tone: 'from-amber-900 to-stone-700' },
  { replayId: 'replay-004', title: '电竞外设全网底价狂欢', dateRange: '2026-07-06 18:00 - 24:00', duration: '06:00:00', views: '10.1w', clickRate: '14.6%', gmv: 216800, clipStatus: 'processing', tone: 'from-indigo-950 to-fuchsia-900' },
];

const statusMeta = {
  ready: { label: '待剪辑', className: 'bg-blue-100 text-blue-700' },
  processing: { label: '生成中', className: 'bg-purple-100 text-purple-700' },
  done: { label: '已生成', className: 'bg-green-100 text-green-700' },
  failed: { label: '剪辑失败', className: 'bg-red-100 text-red-700' },
};

function SmartClip({ onOpenWorkbench }) {
  const [replays, setReplays] = useState(REPLAY_FIXTURES);
  const [query, setQuery] = useState('');

  useEffect(() => {
    if (isMockMode) return;
    liveApi.listReplays().then((items) => {
      if (Array.isArray(items) && items.length) setReplays(items.map((item, index) => ({ ...REPLAY_FIXTURES[index % REPLAY_FIXTURES.length], ...item })));
    }).catch(() => {});
  }, []);

  const visible = useMemo(() => replays.filter((item) => item.title.toLowerCase().includes(query.trim().toLowerCase())), [replays, query]);
  const counts = useMemo(() => replays.reduce((result, item) => ({ ...result, [item.clipStatus]: (result[item.clipStatus] || 0) + 1 }), {}), [replays]);

  const startClip = async (replayId) => {
    setReplays((items) => items.map((item) => item.replayId === replayId ? { ...item, clipStatus: 'processing' } : item));
    if (!isMockMode) liveApi.startReplayClip(replayId).catch(() => {});
  };

  const removeReplay = async (replayId) => {
    if (!window.confirm('确认从演示列表删除这场回放吗？')) return;
    setReplays((items) => items.filter((item) => item.replayId !== replayId));
    if (!isMockMode) liveApi.deleteReplay(replayId).catch(() => {});
  };

  return (
    <div className="h-full overflow-y-auto" data-page-key="videoClip">
      <div className="mb-[20px]">
        <h2 className="text-[22px] font-bold text-gray-900">视频回放列表</h2>
        <p className="text-[13px] text-gray-500 mt-[4px]">JoyClip 将回放按 SKU 讲解区间送入剪辑队列；当前由 Fixture API 演示任务状态。</p>
      </div>

      <div className="flex items-center gap-[12px] mb-[18px]">
        <div className="flex gap-[8px] text-[12px]">
          <span className="bg-white px-[10px] py-[7px] rounded-[8px]">共 {replays.length} 场</span>
          <span className="bg-purple-50 text-purple-700 px-[10px] py-[7px] rounded-[8px]">生成中 {counts.processing || 0}</span>
          <span className="bg-green-50 text-green-700 px-[10px] py-[7px] rounded-[8px]">已生成 {counts.done || 0}</span>
          <span className="bg-red-50 text-red-700 px-[10px] py-[7px] rounded-[8px]">失败 {counts.failed || 0}</span>
        </div>
        <div className="relative w-[300px]">
          <i className="fas fa-search absolute left-[12px] top-[10px] text-gray-400 text-[12px]"></i>
          <input value={query} onChange={(event) => setQuery(event.target.value)} placeholder="搜索直播标题..." className="w-full bg-white border border-gray-200 rounded-[8px] pl-[34px] pr-[12px] py-[8px] text-[13px] outline-none focus:border-purple-400" />
        </div>
      </div>

      {visible.length === 0 ? (
        <div className="bg-white/70 rounded-[16px] py-[70px] text-center text-gray-400">
          <i className="fas fa-film text-[36px] mb-[12px]"></i>
          <div>未找到匹配的直播回放</div>
          <button onClick={() => setQuery('')} className="mt-[12px] text-purple-600 text-[13px]">清空搜索条件</button>
        </div>
      ) : (
        <div className="grid grid-cols-2 xl:grid-cols-3 gap-[18px]">
          {visible.map((replay) => (
            <div key={replay.replayId} className="group bg-white rounded-[14px] overflow-hidden shadow-sm border border-white hover:shadow-lg transition-all">
              <div className={`h-[150px] bg-gradient-to-br ${replay.tone} relative flex items-center justify-center overflow-hidden`}>
                <i className="fas fa-video text-white/30 text-[48px]"></i>
                <span className={`absolute left-[10px] top-[10px] px-[8px] py-[3px] rounded-full text-[11px] ${statusMeta[replay.clipStatus]?.className}`}>{statusMeta[replay.clipStatus]?.label}</span>
                <span className="absolute right-[8px] bottom-[8px] bg-black/60 text-white text-[10px] px-[6px] py-[2px] rounded">{replay.duration}</span>
                <div className="absolute inset-0 bg-black/65 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center gap-[8px]">
                  <button disabled={replay.clipStatus === 'processing'} onClick={() => startClip(replay.replayId)} className="px-[12px] py-[7px] bg-purple-600 text-white rounded-[7px] text-[12px] disabled:opacity-50"><i className="fas fa-magic mr-[5px]"></i>{replay.clipStatus === 'processing' ? '生成中' : '智能切片'}</button>
                  <button onClick={() => onOpenWorkbench?.(replay.replayId)} className="px-[12px] py-[7px] bg-blue-600 text-white rounded-[7px] text-[12px]"><i className="fas fa-cut mr-[5px]"></i>去剪辑</button>
                  <button onClick={() => removeReplay(replay.replayId)} className="w-[32px] h-[32px] bg-red-500 text-white rounded-[7px]"><i className="fas fa-trash"></i></button>
                </div>
              </div>
              <div className="p-[14px]">
                <div className="font-bold text-[14px] text-gray-800 truncate">{replay.title}</div>
                <div className="text-[11px] text-gray-400 mt-[5px]"><i className="far fa-clock mr-[5px]"></i>{replay.dateRange}</div>
                <div className="grid grid-cols-3 gap-[8px] mt-[14px] text-[11px] text-gray-500">
                  <div>场关数据<strong className="block text-[14px] text-gray-800 mt-[2px]">{replay.views}</strong></div>
                  <div>曝光-观看率<strong className="block text-[14px] text-gray-800 mt-[2px]">{replay.clickRate}</strong></div>
                  <div>直播场GMV<strong className="block text-[14px] text-gray-800 mt-[2px]">¥{Number(replay.gmv).toLocaleString()}</strong></div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default SmartClip;
