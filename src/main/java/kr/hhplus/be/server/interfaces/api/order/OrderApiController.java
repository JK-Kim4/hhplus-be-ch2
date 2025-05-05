package kr.hhplus.be.server.interfaces.api.order;

import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderApiController implements OrderApiSpec{

    private final OrderFacade orderFacade;

    public OrderApiController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @Override
    @PostMapping
    public ResponseEntity<OrderResponse.Order> order(
            @RequestBody OrderRequest.Order request) {

        OrderResult.Order result = orderFacade.order(request.toCriteria());
        return ResponseEntity.ok(OrderResponse.Order.from(result));
    }
}
