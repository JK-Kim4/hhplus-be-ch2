package kr.hhplus.be.server.controller.order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderApiController implements OrderApiSpec{

    @Override
    @PostMapping
    public ResponseEntity<OrderCreateResponse> createOrder(OrderCreateRequest request) {
        return null;
    }
}
