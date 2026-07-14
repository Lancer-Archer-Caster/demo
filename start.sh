#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
BACKEND_DIR="$ROOT_DIR/server-demo"
FRONTEND_DIR="$ROOT_DIR/UI2Code"
RUN_DIR="$ROOT_DIR/.run"
BACKEND_PID_FILE="$RUN_DIR/backend.pid"
FRONTEND_PID_FILE="$RUN_DIR/frontend.pid"
BACKEND_LOG="$RUN_DIR/backend.log"
FRONTEND_LOG="$RUN_DIR/frontend.log"
BACKEND_URL="http://127.0.0.1:8080"
FRONTEND_URL="http://127.0.0.1:5173"
BACKEND_PORT="8080"
FRONTEND_PORT="5173"

mkdir -p "$RUN_DIR"

is_running() {
  local pid_file="$1"
  [[ -f "$pid_file" ]] || return 1
  local pid
  pid="$(cat "$pid_file")"
  kill -0 "$pid" 2>/dev/null || lsof -p "$pid" -Fn 2>/dev/null | head -n 1 | grep -q "^p$pid$"
}

listener_pid() {
  local port="$1"
  command -v lsof >/dev/null || return 1
  lsof -nP -tiTCP:"$port" -sTCP:LISTEN 2>/dev/null | head -n 1
}

process_cwd() {
  local pid="$1"
  command -v lsof >/dev/null || return 1
  lsof -a -p "$pid" -d cwd -Fn 2>/dev/null | sed -n 's/^n//p' | head -n 1
}

is_project_listener() {
  local pid="$1"
  local expected_dir="$2"
  local cwd
  cwd="$(process_cwd "$pid" || true)"
  [[ "$cwd" == "$expected_dir" || "$cwd" == "$expected_dir"/* ]]
}

reconcile_pid_file() {
  local pid_file="$1"
  local port="$2"
  local expected_dir="$3"
  local name="$4"

  if is_running "$pid_file"; then
    local recorded_pid current_listener
    recorded_pid="$(cat "$pid_file")"
    current_listener="$(listener_pid "$port" || true)"
    if [[ "$recorded_pid" == "$current_listener" ]] && is_project_listener "$recorded_pid" "$expected_dir"; then
      return 0
    fi
  fi
  rm -f "$pid_file"

  local pid
  pid="$(listener_pid "$port" || true)"
  if [[ -z "$pid" ]]; then
    return 0
  fi
  if ! is_project_listener "$pid" "$expected_dir"; then
    echo "[ERROR] 端口 $port 已被其他程序占用（PID $pid），无法启动${name}"
    return 1
  fi

  echo "$pid" >"$pid_file"
  echo "[INFO] 已识别现有${name}进程 PID $pid"
}

record_listener_pid() {
  local pid_file="$1"
  local port="$2"
  local expected_dir="$3"
  local name="$4"
  local pid
  pid="$(listener_pid "$port" || true)"
  if [[ -z "$pid" ]] || ! is_project_listener "$pid" "$expected_dir"; then
    echo "[ERROR] ${name}已响应，但未找到本项目的端口监听进程"
    return 1
  fi
  echo "$pid" >"$pid_file"
}

wait_for_url() {
  local url="$1"
  local name="$2"
  local log_file="$3"
  local port="$4"
  local expected_dir="$5"
  for _ in $(seq 1 60); do
    if curl -fsS "$url" >/dev/null 2>&1; then
      local pid
      pid="$(listener_pid "$port" || true)"
      if [[ -n "$pid" ]] && is_project_listener "$pid" "$expected_dir"; then
        echo "[OK] $name 已就绪：$url"
        return 0
      fi
      echo "[ERROR] $url 有响应，但端口 $port 不是由本项目${name}进程监听"
      return 1
    fi
    sleep 1
  done
  echo "[ERROR] $name 启动超时，最近日志："
  tail -n 40 "$log_file" || true
  return 1
}

stop_process() {
  local pid_file="$1"
  local name="$2"
  if ! is_running "$pid_file"; then
    rm -f "$pid_file"
    echo "[INFO] $name 未运行"
    return
  fi
  local pid
  pid="$(cat "$pid_file")"
  kill "$pid" 2>/dev/null || true
  for _ in $(seq 1 20); do
    if ! kill -0 "$pid" 2>/dev/null; then
      rm -f "$pid_file"
      echo "[OK] $name 已停止"
      return
    fi
    sleep 0.25
  done
  kill -9 "$pid" 2>/dev/null || true
  rm -f "$pid_file"
  echo "[OK] $name 已强制停止"
}

status() {
  reconcile_pid_file "$BACKEND_PID_FILE" "$BACKEND_PORT" "$BACKEND_DIR" "后端" || true
  reconcile_pid_file "$FRONTEND_PID_FILE" "$FRONTEND_PORT" "$FRONTEND_DIR" "前端" || true
  if is_running "$BACKEND_PID_FILE"; then echo "后端：运行中 PID $(cat "$BACKEND_PID_FILE")"; else echo "后端：未运行"; fi
  if is_running "$FRONTEND_PID_FILE"; then echo "前端：运行中 PID $(cat "$FRONTEND_PID_FILE")"; else echo "前端：未运行"; fi
  curl -fsS "$BACKEND_URL/actuator/health" 2>/dev/null || true
  echo
}

stop_all() {
  reconcile_pid_file "$FRONTEND_PID_FILE" "$FRONTEND_PORT" "$FRONTEND_DIR" "前端" || return 1
  reconcile_pid_file "$BACKEND_PID_FILE" "$BACKEND_PORT" "$BACKEND_DIR" "后端" || return 1
  stop_process "$FRONTEND_PID_FILE" "前端"
  stop_process "$BACKEND_PID_FILE" "后端"
}

start_all() {
  command -v curl >/dev/null || { echo "缺少 curl"; exit 1; }
  command -v mvn >/dev/null || { echo "缺少 Maven，请先安装 Maven 3.x"; exit 1; }
  command -v npm >/dev/null || { echo "缺少 npm，请安装 Node.js 22.12+"; exit 1; }

  if [[ -d "/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home" ]]; then
    export JAVA_HOME="/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home"
    export PATH="$JAVA_HOME/bin:$PATH"
  fi
  command -v java >/dev/null || { echo "缺少 Java，请安装 JDK 8"; exit 1; }
  command -v lsof >/dev/null || { echo "缺少 lsof，无法安全识别端口进程"; exit 1; }

  reconcile_pid_file "$BACKEND_PID_FILE" "$BACKEND_PORT" "$BACKEND_DIR" "后端"
  reconcile_pid_file "$FRONTEND_PID_FILE" "$FRONTEND_PORT" "$FRONTEND_DIR" "前端"

  if is_running "$BACKEND_PID_FILE" && is_running "$FRONTEND_PID_FILE"; then
    echo "JoyCue 已有进程运行。可执行：./start.sh restart"
    status
    return
  fi

  if is_running "$BACKEND_PID_FILE"; then
    echo "[1/4] 后端已运行，跳过构建"
  else
    echo "[1/4] 构建后端..."
    (cd "$BACKEND_DIR" && mvn package -q -DskipTests) >"$RUN_DIR/backend-build.log" 2>&1 || { tail -n 60 "$RUN_DIR/backend-build.log"; exit 1; }

    echo "[2/4] 启动后端..."
    (cd "$BACKEND_DIR" && nohup java -jar server-demo-main/target/server-demo-main-1.0.0.jar >"$BACKEND_LOG" 2>&1 & echo $! >"$BACKEND_PID_FILE")
    wait_for_url "$BACKEND_URL/actuator/health" "后端" "$BACKEND_LOG" "$BACKEND_PORT" "$BACKEND_DIR"
    record_listener_pid "$BACKEND_PID_FILE" "$BACKEND_PORT" "$BACKEND_DIR" "后端"
  fi

  if is_running "$FRONTEND_PID_FILE"; then
    echo "[3/4] 前端已运行，跳过构建"
  else
    echo "[3/4] 构建前端..."
    if [[ ! -d "$FRONTEND_DIR/node_modules" ]]; then
      (cd "$FRONTEND_DIR" && npm install) >"$RUN_DIR/frontend-install.log" 2>&1 || { tail -n 60 "$RUN_DIR/frontend-install.log"; stop_all; exit 1; }
    fi
    (cd "$FRONTEND_DIR" && npm run build) >"$RUN_DIR/frontend-build.log" 2>&1 || { tail -n 60 "$RUN_DIR/frontend-build.log"; stop_all; exit 1; }

    echo "[4/4] 启动前端..."
    (cd "$FRONTEND_DIR" && nohup npm run preview -- --host 127.0.0.1 --port 5173 >"$FRONTEND_LOG" 2>&1 & echo $! >"$FRONTEND_PID_FILE")
    wait_for_url "$FRONTEND_URL" "前端" "$FRONTEND_LOG" "$FRONTEND_PORT" "$FRONTEND_DIR"
    record_listener_pid "$FRONTEND_PID_FILE" "$FRONTEND_PORT" "$FRONTEND_DIR" "前端"
  fi

  echo
  echo "JoyCue 已一键启动"
  echo "前端：$FRONTEND_URL"
  echo "后端：$BACKEND_URL"
  echo "H2控制台：$BACKEND_URL/h2-console"
  echo "日志目录：$RUN_DIR"
  echo "停止命令：./start.sh stop"

  if [[ "${1:-}" != "--no-open" ]] && command -v open >/dev/null; then
    open "$FRONTEND_URL"
  fi
}

case "${1:-start}" in
  start) start_all "${2:-}" ;;
  --no-open) start_all --no-open ;;
  stop) stop_all ;;
  restart) stop_all; start_all "${2:-}" ;;
  status) status ;;
  *) echo "用法：./start.sh [start|stop|restart|status|--no-open]"; exit 1 ;;
esac
