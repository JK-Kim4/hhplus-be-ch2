package kr.hhplus.be.server.controller;

import kr.hhplus.be.server.interfaces.api.coupon.*;
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

    private CouponRequest.Create request;

    @BeforeEach
    void setUp() {
        request = new CouponRequest.Create();
    }


    @DisplayName("쿠폰 생성 요청 성공 (HTTP STATUS 200)")
    @Test
    void test(){
        ResponseEntity<CouponResponse.Create> coupon = couponApiController.createCoupon(request);
        Assertions.assertEquals(HttpStatus.OK, coupon.getStatusCode());
    }

    @DisplayName("쿠폰 발급 요청 성공 (HTTP STATUS 200)")
    @Test
    void test2(){
        ResponseEntity<CouponResponse.Issue> couponIssueResponseResponseEntity = couponApiController.issueCoupon(1L, 2L);
        Assertions.assertEquals(HttpStatus.OK, couponIssueResponseResponseEntity.getStatusCode());
    }
}
