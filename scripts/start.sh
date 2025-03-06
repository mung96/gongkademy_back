#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ubuntu/gongkademy_back
PROJECT_NAME=gongkademy
JAR_NAME=project.jar

# 현재 실행 중인 포트 확인
CURRENT_PROFILE=$(find_idle_profile)
if [ "$CURRENT_PROFILE" == "prod1" ]; then
  IDLE_PORT=8082
  IDLE_PROFILE=prod2
else
  IDLE_PORT=8081
  IDLE_PROFILE=prod1
fi

echo "> 배포할 프로파일: $IDLE_PROFILE (포트: $IDLE_PORT)"

# JAR 파일 실행
echo "> JAR 실행 권한 추가"
chmod +x $REPOSITORY/current/$JAR_NAME

echo "> 새 애플리케이션 실행 (포트: $IDLE_PORT)"
nohup java -jar \
    -Dserver.port=$IDLE_PORT \
    -Dspring.profiles.active=$IDLE_PROFILE \
    $REPOSITORY/current/$JAR_NAME > $REPOSITORY/current/output-$IDLE_PROFILE.log 2>&1 &

echo "> 새 애플리케이션이 실행되었습니다. (포트: $IDLE_PORT)"
