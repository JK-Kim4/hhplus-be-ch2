package kr.hhplus.be.server.controller;

import kr.hhplus.be.server.interfaces.api.payment.PaymentApiController;
import kr.hhplus.be.server.interfaces.api.payment.PaymentRequest;
import kr.hhplus.be.server.interfaces.api.payment.PaymentResponse;
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

    PaymentRequest paymentRequest;

    @BeforeEach
    void setUp() {
        paymentRequest = new PaymentRequest();
    }

    @DisplayName("결제 처리 성공 (HTTP STATUS 200)")
    @Test
    void test(){
        ResponseEntity<PaymentResponse> paymentProcessResponseResponseEntity = paymentApiController.processPayment(paymentRequest);
        Assertions.assertEquals(HttpStatus.OK, paymentProcessResponseResponseEntity.getStatusCode());
    }
}
