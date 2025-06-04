import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

export let errorCount = new Counter('errors');
const BASE_URL = 'http://localhost:8080';

// 테스트 조건 설정
const TARGET_COUPON_ID = 1;  // 실제 테스트할 쿠폰 ID
const SPIKE_STEADY_VUS_LOAD = 1000;

export let options = {
    scenarios: {
        spike_once: {
            executor: 'per-vu-iterations', // VU당 지정 횟수만 실행
            vus: VUS,                      // 1 000 VU 동시에 생성
            iterations: 1,                 // VU당 1회 실행
            maxDuration: '1m',             // 모든 VU가 1분 안에 끝나야 함
            startTime: '0s',               // 0초에 즉시 시작(동시성 보장)
        },
    },
    thresholds: {
        http_req_duration: ['p(95)<1000'], // 95% ≤ 1 000 ms
        http_req_failed:   ['rate<0.01'],  // 실패율 < 1 %
    },
    discardResponseBodies: true,           // 메모리 절약 (선택)
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

    sleep(3);          // 3초 동안 VU 유지 → 집계 구간에 1,000 VU가 동시에 존재
}
