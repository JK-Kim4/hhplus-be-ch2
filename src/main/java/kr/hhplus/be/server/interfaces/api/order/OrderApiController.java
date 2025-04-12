package kr.hhplus.be.server.interfaces.api.order;

import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.domain.order.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderApiController implements OrderApiSpec{

    private final OrderFacade orderFacade;
    private final OrderService orderService;

    public OrderApiController(
            OrderFacade orderFacade,
            OrderService orderService) {
        this.orderFacade = orderFacade;
        this.orderService = orderService;
    }

    @Override
    @PostMapping
    public ResponseEntity<OrderResponse.Create> createOrder(
            @RequestBody OrderRequest.Create request) {
        return ResponseEntity.ok(
                new OrderResponse.Create(orderService.createOrder(request.toCommand())));
    }

    @Override
    public ResponseEntity<OrderResponse.OrderPayment> orderPayment(
            @RequestBody OrderRequest.OrderPayment request) {

        return ResponseEntity.ok(
                new OrderResponse.OrderPayment(orderFacade.orderPayment(request.toCriteria())));
    }
}
