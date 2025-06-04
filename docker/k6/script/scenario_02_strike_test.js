import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

export let errorCount = new Counter('errors');
const BASE_URL = 'http://localhost:8080';

// 테스트 조건 설정
const TARGET_COUPON_ID = 1;  // 실제 테스트할 쿠폰 ID
const SPIKE_STEADY_VUS_LOAD = 3500;

// VU 수만큼 정확히 1회씩 실행
export let options = {
  vus: SPIKE_STEADY_VUS_LOAD,
  iterations: SPIKE_STEADY_VUS_LOAD,
  thresholds: {
    http_req_duration: ['p(95)<1000'], //1초 이내 응답
    http_req_failed:   ['rate<0.01'], // 오류률 1% 미만
  }
};

export default function () {
  const userId = __VU;  // 각 VU당 유저 고유 ID 부여

  const requestBody = {
    couponId: TARGET_COUPON_ID,
    userId: userId
  };

  const res = http.post(
      `${BASE_URL}/api/v1/coupons/issueV2`,
      JSON.stringify(requestBody),
      { headers: { 'Content-Type': 'application/json' }, tags: { name: 'coupon_issue' } }
  );

  const success = check(res, { 'coupon issue 200': (r) => r.status === 200 });
  if (!success) {
    errorCount.add(1);
  }

  sleep(0.5);
}
