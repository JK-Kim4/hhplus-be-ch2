import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

export let errorCount = new Counter('errors');
const BASE_URL = 'http://localhost:8080';

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
    const userId = __VU;

    const payload = {
        userId: userId,
        userCouponId: null,
        items: [
            {
                productId: 500,
                price: 10000,
                quantity: 5
            }
        ]
    };

    // 1단계: 주문 생성 요청
    const res1 = http.post(
        `${BASE_URL}/api/v1/orders`,
        JSON.stringify(payload),
        { headers: { 'Content-Type': 'application/json' }, tags: { name: 'order_create' } }
    );

    const orderCreated = check(res1, { 'order create 200': (r) => r.status === 200 });
    if (!orderCreated) {
        errorCount.add(1);
        return;
    }

    const resBody = JSON.parse(res1.body);
    const orderId = resBody.orderId;

    if (!orderId) {
        console.error('No orderId returned!');
        errorCount.add(1);
        return;
    }

    sleep(0.2);

    const paymentPayload = {
        orderId: orderId,
        userId: userId
    };

    const res2 = http.post(
        `${BASE_URL}/api/v1/payments`,
        JSON.stringify(paymentPayload),
        { headers: { 'Content-Type': 'application/json' }, tags: { name: 'payment_request' } }
    );

    const paymentSuccess = check(res2, { 'payment 200': (r) => r.status === 200 });
    if (!paymentSuccess) {
        errorCount.add(1);
    }

    sleep(0.5);
}
