import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

// 에러 카운터
export let errorCount = new Counter('errors');
const pageList = Array.from({ length: 30 }, (_, i) => i * 30);
const BASE_URL = 'http://localhost:8080';
const LIMIT = 30;

// 총 반복수를 warm-up + main test 분리
const warmupIterations = pageList.length;

export let options = {
    stages: [
        { duration: '30s', target: 50 },
        { duration: '30s', target: 100 },
        { duration: '30s', target: 150 },
        { duration: '2m', target: 150 },
        { duration: '30s', target: 0 },
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'],
        errors: ['count<100'],
    }
};

export function setup() {
    console.log('Starting warm-up phase...');

    // warm-up 단계: offset 순차 호출
    for (let i = 0; i < warmupIterations; i++) {
        const offset = pageList[i];
        const url = `${BASE_URL}/api/v1/products?offset=${offset}&limit=${LIMIT}`;

        const res = http.get(url, { tags: { name: 'warmup_product_list' } });

        check(res, {
            'status is 200': (r) => r.status === 200,
        });

        sleep(0.1);
    }

    console.log('Warm-up phase completed.');
}

export default function () {
    const offset = pageList[Math.floor(Math.random() * pageList.length)];
    const url = `${BASE_URL}/api/v1/products?offset=${offset}&limit=${LIMIT}`;

    const res = http.get(url, { tags: { name: 'product_list' } });

    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    sleep(0.5);
}