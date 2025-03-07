#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

echo "> Health Check Start!"

sleep 10

for RETRY_COUNT in {1..10}
do
  RESPONSE=$(curl -s http://localhost/profile)
  UP_COUNT=$(echo ${RESPONSE} | grep 'prod' | wc -l)
  IDLE_PORT=$(find_idle_port)
  IDLE_PROFILE=$(find_idle_profile)


  if [ ${UP_COUNT} -ge 1 ]
  then
      echo "> Health check 성공"

      break
  else
      echo "> Health check의 응답을 알 수 없거나 혹은 실행 상태가 아닙니다."
      echo "> IDLE_PORT: ${IDLE_PORT}"
      echo "> IDLE_PROFILE: ${IDLE_PROFILE}"
      echo "> Health check: ${RESPONSE}"
  fi

  if [ ${RETRY_COUNT} -eq 10 ]
  then
    echo "> Health check 실패. "
    echo "> 엔진엑스에 연결하지 않고 배포를 종료합니다."
    exit 1
  fi

  echo "> Health check 연결 실패. 재시도..."
  sleep 10
done
