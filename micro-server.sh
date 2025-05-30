#!/bin/bash

#包名
APP_NAME="micro-user-1.0.0-SNAPSHOT.jar"
#包路径
APP_PATH="$(pwd)/${APP_NAME}"

#进程PID
pid=0

# 帮助
Help() {
    # 输出信息
    echo "[启动=start|停止=stop|重启=restart|查看状态=status]"
    echo "请类似这样执行 ./*.sh start"
    exit 1
}

# 检查pid
checkPID() {
   # 查询pid
   pid=$(pgrep -f "$APP_NAME")
}


# 查询状态
status() {
   # 检查pid
   checkPID
   # 判断是否存在
   if [ -z "$pid" ]; then
      # 不存在
      echo "目前不存在该进程."
   else
      echo "pid=${pid}"
   fi
}

# 停止
stop(){
    # 检查pid
    checkPID
    # 判断是否存在pid
    if [ -n "$pid" ]; then
        # 立刻杀死线程
        kill -9 $pid
        # 再次检查确保进程终止
        if pgrep -f "$APP_NAME" > /dev/null; then
            # 停止失败
            echo "警告：进程 ${pid} 停止失败！"
        else
            # 停止成功
            echo "${pid} 进程已成功终止."
        fi
    else
        # 不存在任何进程
        echo "${APP_NAME} 没有启动."
    fi
}

# 启动
start(){
    # 检查pid
    checkPID
    # pid存在,则不启动,直接输出
    if [ -n "$pid" ]; then
        # 输出启动信息
        echo "${APP_NAME} 已经启动,PID:${pid}"
    else

        # 加载全局环境变量(否则通过ssh执行脚本,无法找到java命令)
        # 如果通过ssh执行该脚本,需要执行,如果不通过ssh,可以省略
        source /etc/profile

        # 执行java -jar 正常启动服务
        nohup java -jar $APP_NAME > /dev/null 2>&1 &

        # 执行java -jar 正常启动服务,并强行指定配置文件
        # nohup java -Xms256m -Xmx512m -jar $APP_NAME --spring.config.location=file:./application.properties > /dev/null 2>&1 &

        # 检查pid
        checkPID
        # 输出
        echo "${APP_NAME} 启动成功,PID:${pid}"
    fi
}

# 重启
restart(){
    stop
    start
}

# 命令表
case "$1" in
    "start")
        start
        ;;
    "stop")
        stop
        ;;
    "restart")
        restart
        ;;

    "status")
        status
        ;;
    *)
    Help
    ;;
esac