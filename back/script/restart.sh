#!/bin/bash

JAR_NAME=back-0.0.1-SNAPSHOT.jar
APP_DIR=/home/ubuntu/app

cd $APP_DIR || { echo "Failed to cd to $APP_DIR"; exit 1; }

PID=$(pgrep -f $JAR_NAME)
if [ -n "$PID" ]; then
  echo "Stopping existing app (PID: $PID)..."
  kill -9 $PID
  sleep 2
fi

echo "Starting $JAR_NAME..."
nohup java -jar $JAR_NAME > app.log 2>&1 &
echo "App started with PID $!"