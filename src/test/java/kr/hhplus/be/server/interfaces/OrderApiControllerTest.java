package kr.hhplus.be.server.interfaces;

import kr.hhplus.be.server.interfaces.api.order.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class OrderApiControllerTest {

    @Autowired
    OrderApiController orderApiController;

    OrderResponse.Create orderCreateResponse;
    OrderRequest.Create orderCreateRequest;

    @BeforeEach
    void setUp() {
        orderCreateResponse = new OrderResponse.Create();
        orderCreateRequest = new OrderRequest.Create();
    }

    @DisplayName("주문 생성 성공 (HTTP STATUS 200)")
    @Test
    void test(){
        ResponseEntity<OrderResponse.Create> order = orderApiController.createOrder(orderCreateRequest);
        Assertions.assertEquals(HttpStatus.OK, order.getStatusCode());
    }


}
