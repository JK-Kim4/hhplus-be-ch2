# e-Commerce 시스템 동시성 이슈 대응 및 대안 제시

> **작성일**: 2024-04-22  
> **작성자**: 김종완
> **Status**: ✅ Accepted

---

## 📚 개요

본 문서는 e-Commerce 시스템 구축 과정에서 발생 가능한 동시성 이슈를 예측하고,  
이를 방지하기 위한 대응 방안과 향후 개선 방향을 정리한 기술 보고서입니다.

---

## 🧠 동시성 문제 개요

- **동시성 문제란?**  
  공유 자원에 대해 여러 트랜잭션이 동시에 접근하여 데이터 정합성에 문제가 생기는 현상.

- **주요 문제 유형**
    - Dirty Read (더티 리드)
    - Non-Repeatable Read (반복 불가능 읽기)
    - Phantom Read (팬텀 리드)
    - Lost Update (업데이트 손실)

---

## 🔥 예상 주요 이슈 및 대응 전략

### 1. 사용자 포인트 잔액 충전/사용 충돌

| 항목 | 내용 |
|:---|:---|
| 문제 상황 | 포인트 충전과 사용 요청이 동시에 발생할 경우 잔액 불일치 |
| 발생 이슈 | Lost Update |
| 대응 방안 | Optimistic Lock (`@Version`) 적용 + 재시도 로직 구현 |

---

### 2. 상품 재고 차감 동시성 문제

| 항목 | 내용 |
|:---|:---|
| 문제 상황 | 상품 주문 요청이 동시에 발생해 재고 차감 실패 가능성 |
| 발생 이슈 | Lost Update, 데이터 불일치 |
| 대응 방안 | Pessimistic Lock (`@Lock(PESSIMISTIC_WRITE)`) 적용 |

---

### 3. 선착순 쿠폰 발급 동시성 문제

| 항목 | 내용                                                     |
|:---|:-------------------------------------------------------|
| 문제 상황 | 쿠폰 수량 초과 발급 및 순서 불일치 발생 가능성                            |
| 발생 이슈 | Lost Update + 순서 불일치                                   |
| 대응 방안 | - Pessimistic Lock을 통한 수량 차감 <br/>- InMemoryQueue를 통한 순차 처리 |
  | 주의사항 | 다중 서버 환경에서는 Redis Queue 등 글로벌 Queue 도입 필요              |



---

## ⚙️ 기술 적용 요약

| 기술 요소 | 적용 방법 |
|:---|:---|
| Optimistic Lock | `@Version` 필드 적용 |
| Pessimistic Lock | `@Lock(LockModeType.PESSIMISTIC_WRITE)` 적용 |
| 테스트 지원 | `ConcurrentTestExecutor` 유틸 작성 |
| 순차 처리 | `InMemoryCouponIssueQueue` + `CouponIssueWorker` 구조 구현 |

---

## ⚡ InMemoryQueue 한계 및 향후 개선 방안

| 한계 | 대안 |
|:---|:---|
| 서버 단일성 한계 | Redis 기반 Queue로 확장 고려 |
| 서버 장애 시 요청 손실 가능성 | Redis/Kafka로 데이터 영속성 확보 |
| 메모리 한계 (OOM 위험) | Queue 사이즈 제한 및 초과 요청 정책 필요 |
| 다중 서버 확장성 문제 | 외부 메시지 브로커 도입 검토 (Kafka, AWS SQS 등) |

---

## 📈 최종 정리

- **사용자 포인트 잔액** → Optimistic Lock + 재시도 로직
- **상품 재고 차감** → Pessimistic Lock
- **선착순 쿠폰 발급** → Pessimistic Lock + InMemoryQueue 순차 처리
- **향후 준비** → InMemoryQueue → Redis Queue 전환

---

## 🛠 향후 Action Plan

- Redis 기반 Coupon Issue Queue 리팩토링
- Dead Letter Queue 설계 및 구축
- 동시성 제어 기능에 대한 통합 테스트 강화
- 시스템 확장 대비 Kafka/SQS 도입 검토

---

## ✨ 참고

> 이 문서는 트래픽 증가, 시스템 구조 변경에 따라 업데이트될 수 있습니다.

---

## 동시성 이슈 테스트 보조 클래스
```java
public class ConcurrentTestExecutor {

    public static void execute(int numberOfThreads, int counter, List<Runnable> tasks) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(counter);

        for(int i = 0; i < counter; i ++){
            for (Runnable task : tasks) {
                executorService.submit(() -> {
                    try {
                        task.run();
                    } finally {
                        latch.countDown();
                    }
                });
            }
        }
        latch.await();
        executorService.shutdown();
    }
}
```
