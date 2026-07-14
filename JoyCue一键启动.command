#!/usr/bin/env bash
cd "$(dirname "$0")"
./start.sh start
echo
read -r -p "JoyCue 已启动。按回车关闭此窗口..." _
