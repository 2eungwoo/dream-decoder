import http from 'k6/http';
import { check, sleep } from 'k6';
import { randomUUID } from 'k6/crypto';

export const options = {
  vus: 10, // 10명의 가상 사용자가
  duration: '30s', // 30초 동안 테스트를 수행합니다.
};

const API_URL = 'http://localhost:8080/ai/chat?userId=1';

export default function () {
  const headers = {
    'Content-Type': 'application/json',
    'Idempotency-Key': randomUUID(), // Idempotency Key 랜덤생성
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
