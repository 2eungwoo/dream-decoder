import http from 'k6/http';
import { check, sleep } from 'k6';
import { randomUUID } from 'k6/crypto';

export const options = {
  vus: 2, // 10명의 가상 사용자가
  duration: '10s', // 30초 동안 테스트를 수행합니다.
};

const API_URL = 'http://localhost:8080/ai/chat?userId=3';
const AUTH_TOKEN = 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyNSIsImlhdCI6MTc2MjE1Nzk0OSwiZXhwIjoxNzYyMTYxNTQ5fQ.jztxcysHJAW0z-sDF6MM3cHxOcex3zyaRGe6ZpIw5h4';

export default function () {
  const headers = {
    'Content-Type': 'application/json',
    'Authorization': AUTH_TOKEN,
    'Idempotency-Key': `test-key-${__VU}-${__ITER}`, // Idempotency Key 랜덤생성
  };

  const payload = JSON.stringify({
    dreamContent: '하늘을 나는 용을 본 꿈',
    dreamEmotion: 'HAPPY',
    tags: '용,하늘,비행',
    style: 'DEFAULT'
  });

  const res = http.post(API_URL, payload, { headers: headers });

  check(res, {
    'is status 202 (Accepted)': (r) => r.status === 202,
  });

  sleep(1);
}
