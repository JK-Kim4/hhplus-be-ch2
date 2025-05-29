package kr.hhplus.be.server.interfaces.payment.api;

import kr.hhplus.be.server.application.payment.PaymentFacade;
import kr.hhplus.be.server.application.payment.PaymentResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentApiController implements PaymentApiSpec {

    private final PaymentFacade paymentFacade;

    public PaymentApiController(PaymentFacade paymentFacade) {
        this.paymentFacade = paymentFacade;
    }

    @Override
    @PostMapping
    public ResponseEntity<PaymentResponse.Pay> pay(
            @RequestBody PaymentRequest.Pay request) {

        PaymentResult.Pay pay = paymentFacade.pay(request.toCriteria());
        return ResponseEntity.ok(PaymentResponse.Pay.from(pay));
    }
}
