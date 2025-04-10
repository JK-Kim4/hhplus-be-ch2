package kr.hhplus.be.server.interfaces.api.payment;

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

    public PaymentApiController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    @PostMapping
    public ResponseEntity<PaymentResponse.Create> createPayment(
            @RequestBody PaymentRequest.Create request) {
        return ResponseEntity.ok(
                new PaymentResponse.Create(paymentService.save(request.toCommand())));
    }

    @Override
    @PostMapping("/process")
    public ResponseEntity<PaymentResponse.Process> processPayment(
            @RequestBody PaymentRequest.Process request) {
        return ResponseEntity.ok(
                new PaymentResponse.Process(paymentService.processPayment(request.toCommand())));
    }
}
