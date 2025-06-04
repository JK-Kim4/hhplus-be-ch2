import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

// K6 테스트 스크립트 사용 변수
export let errorCount = new Counter('errors');  //발생 오류 카운트
const pageList = Array.from({ length: 30 }, (_, i) => i * 30);  //(offset) 조회 대상 페이지 상위 30페이지
const LIMIT = 30;   //(limit) 조회 대상 컨텐츠 수 (30건)
const BASE_URL = 'http://localhost:8080';   //BaseURL
const warmupIterations = pageList.length;   // 총 반복수를 warm-up + main test 분리


// Stress Test 계산용 상수
const MAX_VUS_STRESS = 800;

export let options = {
  stages: [
    // 선형 램프-업: 0 → 948 VU (총 10분)
    { duration: '3m', target: Math.ceil(MAX_VUS_STRESS * 0.1)  },
    { duration: '1m', target: Math.ceil(MAX_VUS_STRESS * 0.2)  },
    { duration: '1m', target: Math.ceil(MAX_VUS_STRESS * 0.3)  },
    { duration: '3m', target: Math.ceil(MAX_VUS_STRESS * 0.3)  },
    { duration: '1m', target: Math.ceil(MAX_VUS_STRESS * 0.4)  },
    { duration: '1m', target: Math.ceil(MAX_VUS_STRESS * 0.5)  },
    { duration: '3m', target: Math.ceil(MAX_VUS_STRESS * 0.5)  },
    { duration: '1m', target: Math.ceil(MAX_VUS_STRESS * 0.6)  },
    { duration: '1m', target: Math.ceil(MAX_VUS_STRESS * 0.7)  },
    { duration: '3m', target: Math.ceil(MAX_VUS_STRESS * 0.7)  },
    { duration: '1m', target: Math.ceil(MAX_VUS_STRESS * 0.8)  },
    { duration: '1m', target: Math.ceil(MAX_VUS_STRESS * 0.9)  },
    { duration: '1m', target: MAX_VUS_STRESS },

    // 최대 부하 유지 분
    { duration: '3m', target: MAX_VUS_STRESS },

    // 추가 스파이크: +30 %
    { duration: '1m', target: Math.ceil(MAX_VUS_STRESS * 1.3) },   // 1 233 VU
    { duration: '2m', target: Math.ceil(MAX_VUS_STRESS * 1.3) },

    // 램프-다운
    { duration: '1m', target: 0 },
  ],

  thresholds: {
    http_req_duration: ['p(95)<500'], // Stress 상황이므로 500 ms로 완화
    http_req_failed:   ['rate<0.01'], // 오류률 1% 미만
  },

  systemTags: ['status', 'http_req_duration'],
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