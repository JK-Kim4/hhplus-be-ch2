package kr.hhplus.be.server.interfaces.api.payment;

import kr.hhplus.be.server.application.orderPayment.OrderFacade;
import kr.hhplus.be.server.application.orderPayment.result.PaymentResult;
import kr.hhplus.be.server.domain.payment.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentApiController implements PaymentApiSpec {

    private final PaymentService paymentService;
    private final OrderFacade orderFacade;

    public PaymentApiController(
            PaymentService paymentService,
            OrderFacade orderFacade) {
        this.paymentService = paymentService;
        this.orderFacade = orderFacade;
    }

    @Override
    @PostMapping
    public ResponseEntity<PaymentResponse.Create> createPayment(
            @RequestBody PaymentRequest.Create request) {
        PaymentResult.Create result
                = orderFacade.createPayment(request.toCriteria());
        return ResponseEntity.ok(PaymentResponse.Create.from(result));
    }

    @Override
    @PostMapping("/process")
    public ResponseEntity<PaymentResponse.Process> processPayment(
            @RequestBody PaymentRequest.Process request) {
        PaymentResult.Process result
                = orderFacade.processPayment(request.toCriteria());
        return ResponseEntity.ok(PaymentResponse.Process.from(result));
    }
}
