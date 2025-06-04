import http from 'k6/http';
import { check, sleep } from 'k6';
import { SharedArray } from 'k6/data';
import { Counter } from 'k6/metrics';

// 주문 데이터 로드 (파일명 주의: order_create_request.json)
const orderPayloads = new SharedArray('Order Payloads', () => {
    return JSON.parse(open('./dummy_request/order_create_request.json'));
});

const BASE_URL = 'http://localhost:8080';  // ✅ 실제 서버 주소로 교체 필요
const PRODUCT_API = '/api/v1/products';
const ORDER_API = '/api/v1/orders';

const LIMIT = 30;
const MAX_PAGE = 30;  // offset: 0 ~ 29 까지만 사용
const totalOrders = orderPayloads.length;

const errors = new Counter('errors');  // custom metric 등록
const orderIndexCounter = new Counter('order_index_usage');

// 주문 데이터 순서대로 소진 (Thread-safe X — 단일 VU 기준에선 충분)
function getNextOrder() {
    const index = (__VU - 1) * 10000 + __ITER; // VU별 고유 인덱스 확보
    if (index >= totalOrders) {
        return null;
    }
    orderIndexCounter.add(1);
    return orderPayloads[index];
}

const STEADY_VUS_LOAD = 300;

export let options = {
    stages: [
        { duration: '20s', target: Math.ceil(STEADY_VUS_LOAD * 0.25) },
        { duration: '20s', target: Math.ceil(STEADY_VUS_LOAD * 0.50) },
        { duration: '20s', target: Math.ceil(STEADY_VUS_LOAD * 0.75) },
        { duration: '15s', target: STEADY_VUS_LOAD },
        { duration: '3m', target: STEADY_VUS_LOAD },
        { duration: '1m',  target: 0 },
    ],
    thresholds: {
        http_req_duration: ['p(95)<250'], // p95 250 ms
        http_req_failed:   ['rate<0.01'], // 오류률 1% 미만
    },
};

export default function () {
    // 상품 목록 조회
    const offset = Math.floor(Math.random() * MAX_PAGE) * LIMIT;
    const res = http.get(`${BASE_URL}${PRODUCT_API}?offset=${offset}&limit=${LIMIT}`);
    const isProductSuccess = check(res, { 'product list 200': (r) => r.status === 200 });

    if (!isProductSuccess) {
        errors.add(1);
    }

    // 구매 전환률 평군 2%
    if (Math.random() < 0.04) {
        const order = getNextOrder();

        if (order != null) {
            const resOrder = http.post(`${BASE_URL}${ORDER_API}`, JSON.stringify(order), {
                headers: { 'Content-Type': 'application/json' }
            });
            const isOrderSuccess = check(resOrder, { 'order create 200': (r) => r.status === 200 });

            if (!isOrderSuccess) {
                errors.add(1);
            }
        }
    }

    sleep(1);  // think time (사용자 대기시간 시뮬레이션)
}
