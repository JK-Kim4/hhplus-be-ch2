package kr.hhplus.be.server.interfaces.api.order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderApiController implements OrderApiSpec{

    @Override
    @PostMapping
    public ResponseEntity<OrderResponse.Create> createOrder(
            @RequestBody OrderRequest.Create request) {
        return ResponseEntity.ok(new OrderResponse.Create());
    }
}
