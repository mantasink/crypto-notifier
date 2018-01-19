#!/bin/bash
HOME_DIR=~/crypto-notifier
PIDFILE="/tmp/crypto-notifier-pid.txt"

PID="$(cat $PIDFILE)"
echo "kill -9 $PID"
kill -9 $PID

cd ~
APP_STARTUP_CMD="java -DconfigFile=$HOME_DIR/config.properties -jar $HOME_DIR/crypto-notifier-1.0.jar"
nohup $APP_STARTUP_CMD 0<&- &>$HOME_DIR/app.log 2>&1 &

echo $! >$PIDFILE
echo "Restarted PID=$!"
exit
