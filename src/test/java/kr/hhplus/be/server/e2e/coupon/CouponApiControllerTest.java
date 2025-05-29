package kr.hhplus.be.server.e2e.coupon;

import kr.hhplus.be.server.common.keys.CacheKeys;
import kr.hhplus.be.server.domain.coupon.CouponEventInMemoryRepository;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import kr.hhplus.be.server.interfaces.coupon.api.CouponRequest;
import kr.hhplus.be.server.interfaces.coupon.api.CouponResponse;
import kr.hhplus.be.server.support.E2ETestSupport;
import kr.hhplus.be.server.support.domain.CouponDomainSupport;
import kr.hhplus.be.server.support.domain.UserDomainSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CouponApiControllerTest extends E2ETestSupport {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserCouponRepository userCouponRepository;

    @MockitoSpyBean
    CouponEventInMemoryRepository couponEventInMemoryRepository;

    Long userId;
    Long couponId;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    public void setUp() {
        userId = userRepository.save(UserDomainSupport.기본_사용자_생성()).getId();
        couponId = couponRepository.save(CouponDomainSupport.수량정보를_전달받아_유효_테스트쿠폰_생성(10)).getId();
        redissonClient.getBucket(CacheKeys.COUPON_ISSUABLE_FLAG.format(couponId)).set(CacheKeys.COUPON_ISSUABLE_FLAG.format(couponId));
        redissonClient.getScoredSortedSet(CacheKeys.COUPON_REQUEST_ISSUE.format(couponId)).entryRange(0, 10);
    }

    @Test
    void 쿠폰_발급_API_V2_요청시_메세지_발행_및_처리_확인(){
        //given
        CouponRequest.Issue issue = CouponRequest.Issue.of(couponId, userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CouponRequest.Issue> entity = new HttpEntity<>(issue, headers);

        // when
        ResponseEntity<CouponResponse.Void> response = restTemplate.postForEntity(
                "/api/v1/coupons/issueV2",
                entity,
                CouponResponse.Void.class
        );

        //then - 응답 검증
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("쿠폰 발급 요청 성공", response.getBody().getMessage());

        //then - 이벤트 검증
        Awaitility.await()
                .atMost(30, TimeUnit.SECONDS)
                .pollInterval(3, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    verify(couponEventInMemoryRepository, times(1))
                            .saveIdempotencyKey(any(String.class));
                });
    }

}
