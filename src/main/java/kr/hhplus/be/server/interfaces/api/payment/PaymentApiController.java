package kr.hhplus.be.server.interfaces.api.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentApiController implements PaymentApiSpec {

    @Override
    @PostMapping("/process")
    public ResponseEntity<PaymentProcessResponse> processPayment(
            @RequestBody PaymentProcessRequest paymentProcessRequest) {
        return ResponseEntity.ok(new PaymentProcessResponse());
    }
}
