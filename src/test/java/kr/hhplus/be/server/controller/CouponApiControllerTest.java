package kr.hhplus.be.server.controller;

import kr.hhplus.be.server.interfaces.api.coupon.CouponApiController;
import kr.hhplus.be.server.interfaces.api.coupon.CouponCreateRequest;
import kr.hhplus.be.server.interfaces.api.coupon.CouponCreateResponse;
import kr.hhplus.be.server.interfaces.api.coupon.CouponIssueResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class CouponApiControllerTest {

    @Autowired
    CouponApiController couponApiController;

    private CouponCreateRequest request;
    private CouponCreateResponse response;
    private CouponIssueResponse issueResponse;

    @BeforeEach
    void setUp() {
        request = new CouponCreateRequest();
        response = new CouponCreateResponse();
    }


    @DisplayName("쿠폰 생성 요청 성공 (HTTP STATUS 200)")
    @Test
    void test(){
        ResponseEntity<CouponCreateResponse> coupon = couponApiController.createCoupon(request);
        Assertions.assertEquals(HttpStatus.OK, coupon.getStatusCode());
    }

    @DisplayName("쿠폰 발급 요청 성공 (HTTP STATUS 200)")
    @Test
    void test2(){
        ResponseEntity<CouponIssueResponse> couponIssueResponseResponseEntity = couponApiController.issueCoupon(1L, 2L);
        Assertions.assertEquals(HttpStatus.OK, couponIssueResponseResponseEntity.getStatusCode());
    }
}
