import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

// K6 테스트 스크립트 사용 변수
export let errorCount = new Counter('errors');  //발생 오류 카운트
const pageList = Array.from({ length: 30 }, (_, i) => i * 30);  //(offset) 조회 대상 페이지 상위 30페이지
const LIMIT = 30;   //(limit) 조회 대상 컨텐츠 수 (30건)
const BASE_URL = 'http://localhost:8080';   //BaseURL
const warmupIterations = pageList.length;   // 총 반복수를 warm-up + main test 분리


// scenario_01 Load test options
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
    errors:   ['rate<0.01'], // 오류률 1% 미만
  },
};


//상품 목록 조회 API Redis Cache Warm-up
export function setup() {
  console.log('Starting warm-up phase...');

  //조회 대상 페이지 list의 offset 만큼 조회 요청(cache set)
  for (let i = 0; i < warmupIterations; i++) {
    const offset = pageList[i];
    const url = `${BASE_URL}/api/v1/products?offset=${offset}&limit=${LIMIT}`;

    const res = http.get(url, { tags: { name: 'warmup_product_list' } });

    check(res, {
      '(Warm-up) status is 200': (r) => r.status === 200,
    });

    sleep(0.1);
  }

  console.log('Warm-up phase completed.');
}


//실제 테스트 함수
export default function () {
  //조회 대상 페이지 임의 선택 & 요청 URL
  const offset = pageList[Math.floor(Math.random() * pageList.length)];
  const url = `${BASE_URL}/api/v1/products?offset=${offset}&limit=${LIMIT}`;

  //상품 목록 조회 요청
  const res = http.get(url, { tags: { name: 'product_list' } });

  //결과 확인
  check(res, {
    'status is 200': (r) => r.status === 200,
  });

  sleep(0.5);
}