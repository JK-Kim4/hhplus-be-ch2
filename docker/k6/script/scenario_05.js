import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

export let errorCount = new Counter('errors');
const BASE_URL = 'http://localhost:8080';

// 테스트 조건 설정
const TARGET_COUPON_ID = 1;  // 실제 테스트할 쿠폰 ID

export let options = {
    vus: 6000,        // 테스트할 유저 수 (예: 1000명)
    iterations: 6000, // VU당 1회 요청 (중복 없이)
    thresholds: {
        http_req_duration: ['p(95)<1500'],
        errors: ['count<10']  // 약간 여유 있게 설정 (중복 실패 가능성 고려)
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
