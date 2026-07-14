import React, { useEffect, useState } from 'react';
import { isMockMode, liveApi } from './api';

const DEFAULT_ROOM_URL = 'https://live.example.com/room/local-demo';

function LiveSourceConnector({ sessionId = 1, onConnectionChange, onCommentInjected }) {
  const [roomUrl, setRoomUrl] = useState(DEFAULT_ROOM_URL);
  const [connection, setConnection] = useState({ status: isMockMode ? 'READY' : 'DISCONNECTED' });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isMockMode) return;
    const controller = new AbortController();
    liveApi.getLiveSourceStatus(sessionId, controller.signal)
      .then((value) => {
        setConnection(value);
        if (value.roomUrl) setRoomUrl(value.roomUrl);
        onConnectionChange?.(value);
      })
      .catch(() => {});
    return () => controller.abort();
  }, [sessionId]);

  const run = async (action) => {
    setLoading(true);
    try {
      const value = await action();
      if (value?.status) {
        setConnection(value);
        onConnectionChange?.(value);
      }
      return value;
    } catch (error) {
      window.alert(`直播来源操作失败：${error.message}`);
      return null;
    } finally {
      setLoading(false);
    }
  };

  const handleConnect = () => run(() => isMockMode
    ? Promise.resolve({ status: 'READY', roomUrl, platform: 'LOCAL', commentWebhook: '/live/comments/ingest' })
    : liveApi.connectLiveSource({ sessionId, roomUrl, platform: 'AUTO' }));

  const handleDisconnect = () => run(() => isMockMode
    ? Promise.resolve({ status: 'DISCONNECTED' })
    : liveApi.disconnectLiveSource(sessionId));

  const handleInject = async () => {
    const comment = await run(() => isMockMode
      ? Promise.resolve({ originalText: '这款手机支持快充吗？' })
      : liveApi.injectDemoComment(sessionId));
    if (comment) onCommentInjected?.(comment);
  };

  const ready = connection.status === 'READY';

  return (
    <div className="bg-gray-800/90 border border-gray-700 rounded-[10px] px-[14px] py-[10px] mb-[12px] shrink-0" data-ai-alt="直播来源接入区">
      <div className="flex items-center gap-[10px]">
        <div className="flex items-center shrink-0">
          <span className={`w-[8px] h-[8px] rounded-full mr-[6px] ${ready ? 'bg-green-400 animate-pulse' : 'bg-gray-500'}`}></span>
          <span className="text-[13px] font-bold text-gray-200">评论接入</span>
        </div>
        <input
          value={roomUrl}
          onChange={(event) => setRoomUrl(event.target.value)}
          placeholder="粘贴直播间链接（作为平台适配器配置）"
          className="flex-1 min-w-0 bg-gray-900 border border-gray-600 rounded-[6px] px-[10px] py-[6px] text-[12px] text-gray-200 focus:outline-none focus:border-purple-500"
          data-testid="live-room-url"
        />
        {ready ? (
          <button onClick={handleDisconnect} disabled={loading} className="px-[12px] py-[6px] rounded-[6px] bg-gray-600 text-white text-[12px]">断开</button>
        ) : (
          <button onClick={handleConnect} disabled={loading || !roomUrl.trim()} className="px-[12px] py-[6px] rounded-[6px] bg-purple-600 text-white text-[12px] disabled:opacity-50" data-testid="connect-live-source">登记来源</button>
        )}
        <button onClick={handleInject} disabled={loading || !ready} className="px-[12px] py-[6px] rounded-[6px] bg-blue-600 text-white text-[12px] disabled:opacity-40" data-testid="inject-demo-comment">
          注入示例评论
        </button>
      </div>
      <div className="flex items-center justify-between mt-[6px] text-[10px] text-gray-400">
        <span>{ready ? `来源已就绪 · ${connection.platform || 'CUSTOM'} · 页面每2秒读取新评论` : '先登记直播来源；真实平台通过适配器向统一接口推送评论'}</span>
        <code className="text-cyan-400">POST {connection.commentWebhook || '/live/comments/ingest'}</code>
      </div>
    </div>
  );
}

export default LiveSourceConnector;
