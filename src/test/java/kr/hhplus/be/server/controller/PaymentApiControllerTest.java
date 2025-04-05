package kr.hhplus.be.server.controller;

import kr.hhplus.be.server.controller.payment.PaymentApiController;
import kr.hhplus.be.server.controller.payment.PaymentProcessRequest;
import kr.hhplus.be.server.controller.payment.PaymentProcessResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class PaymentApiControllerTest {

    @Autowired
    PaymentApiController paymentApiController;

    PaymentProcessRequest paymentProcessRequest;

    @BeforeEach
    void setUp() {
        paymentProcessRequest = new PaymentProcessRequest();
    }

    @DisplayName("결제 처리 성공 (HTTP STATUS 200)")
    @Test
    void test(){
        ResponseEntity<PaymentProcessResponse> paymentProcessResponseResponseEntity = paymentApiController.processPayment(paymentProcessRequest);
        Assertions.assertEquals(HttpStatus.OK, paymentProcessResponseResponseEntity.getStatusCode());
    }
}
