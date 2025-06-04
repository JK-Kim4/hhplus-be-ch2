import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

export let errorCount = new Counter('errors');
const BASE_URL = 'http://localhost:8080';

// 테스트 조건 설정
const TARGET_COUPON_ID = 1;  // 실제 테스트할 쿠폰 ID
const SPIKE_STEADY_VUS_LOAD = 1000;

// VU 수만큼 정확히 1회씩 실행
export let options = {
    vus: Math.ceil(SPIKE_STEADY_VUS_LOAD * 1.2),         // 스파이크 테스트 VU 1000 + 20% 가중치
    iterations: Math.ceil(SPIKE_STEADY_VUS_LOAD * 1.2),  // VU당 1회 요청
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

    sleep(0.1);  // optional: 약간의 간격 주기
}
