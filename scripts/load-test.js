import http from 'k6/http';
import { check, sleep } from 'k6';
import { randomUUID } from 'k6/crypto';

export const options = {
  vus: 20,
  duration: '30s',
};

const API_URL = 'http://localhost:8080/ai/chat?userId=1';
const AUTH_TOKEN = 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTc2MjE1OTMzNSwiZXhwIjoxNzYyMTYyOTM1fQ.mJ84EklqfE9LrG0RzVyoYpUTPw4AUCk1hRX_QyN-BII';

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
