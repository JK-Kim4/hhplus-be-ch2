package kr.hhplus.be.server.interfaces;

import kr.hhplus.be.server.interfaces.api.user.UserApiController;
import kr.hhplus.be.server.interfaces.api.user.UserResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class UserApiControllerTest {

    @Autowired
    UserApiController userApiController;

    @BeforeEach
    void setUp() {}

    @DisplayName("사용자 조회 성공(HTTP STATUS 200)")
    @Test
    void test() {
        ResponseEntity<UserResponse.Detail> byId = userApiController.findById(1L);
        Assertions.assertEquals(HttpStatus.OK, byId.getStatusCode());
    }

    @DisplayName("사용자 포인트 조회 성공(HTTP STATUS 200)")
    @Test
    void test2(){
        ResponseEntity<UserResponse.Point> userPointById = userApiController.findUserPointById(1L);
        Assertions.assertEquals(HttpStatus.OK, userPointById.getStatusCode());
    }

    @DisplayName("사용자 쿠폰 조회 성공(HTTp STATUS 200)")
    @Test
    void test3(){
        ResponseEntity<UserResponse.Coupon> userCouponListByUserId = userApiController.findUserCouponListByUserId(1L);
        Assertions.assertEquals(HttpStatus.OK, userCouponListByUserId.getStatusCode());
    }

    @DisplayName("사용자 포인트 충전 성공(HTTP STATUS 200)")
    @Test
    void test4(){
        ResponseEntity<UserResponse.Point> userPointResponseResponseEntity = userApiController.chargePoint(1L, 2000);
        Assertions.assertEquals(HttpStatus.OK, userPointResponseResponseEntity.getStatusCode());
    }

}
