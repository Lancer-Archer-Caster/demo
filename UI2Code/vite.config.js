import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  const proxyTarget = env.VITE_API_PROXY_TARGET;
  const backendPaths = [
    '/api', '/actuator', '/sku', '/session', '/compliance', '/script',
    '/ai', '/danmaku', '/price', '/alert', '/review', '/live', '/order', '/parrell',
  ];
  const proxy = proxyTarget
    ? Object.fromEntries(backendPaths.map((path) => [path, { target: proxyTarget, changeOrigin: true }]))
    : undefined;
  return {
    plugins: [react()],
    build: {
      // 课题演示机可能使用较旧的 Safari，构建为更保守的浏览器语法。
      target: ['es2018', 'safari13'],
    },
    server: {
      port: 5173,
      proxy,
    },
    preview: {
      port: 5173,
      proxy,
    },
  };
});
