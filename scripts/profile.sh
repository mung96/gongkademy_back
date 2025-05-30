#!/usr/bin/env bash

# bash는 return value가 안되니 *제일 마지막줄에 echo로 해서 결과 출력*후, 클라이언트에서 값을 사용한다

# 쉬고 있는 profile 찾기: real1이 사용중이면 real2가 쉬고 있고, 반대면 real1이 쉬고 있음

exec >> /home/ubuntu/deploy.log 2>&1
function find_idle_profile()
{
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile) # 1

    if [ ${RESPONSE_CODE} -ge 400 ] # 400 보다 크면 (즉, 40x/50x 에러 모두 포함)
    then
        CURRENT_PROFILE=prod2
    else
        CURRENT_PROFILE=$(curl -s http://localhost/profile)
    fi

    if [ ${CURRENT_PROFILE} == prod1 ]
    then
      IDLE_PROFILE=prod2 # 2
    else
      IDLE_PROFILE=prod1
    fi

    echo "${IDLE_PROFILE}" # 3
}

# 쉬고 있는 profile의 port 찾기
function find_idle_port()
{
    IDLE_PROFILE=$(find_idle_profile)

    if [ ${IDLE_PROFILE} == prod1 ]
    then
      echo "8081"
    else
      echo "8082"
    fi
}

# 활성화 profile의 port 찾기
function find_active_port()
{
    IDLE_PROFILE=$(find_idle_profile)

  if [ ${IDLE_PROFILE} == prod2 ]
  then
    echo "8081"
  else
    echo "8082"
  fi
}

