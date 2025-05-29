package kr.hhplus.be.server.domain.payment.event;

import kr.hhplus.be.server.common.keys.IdempotencyKeyGenerator;
import kr.hhplus.be.server.domain.payment.PaymentEventInMemoryRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentEventService {

    private final PaymentEventInMemoryRepository paymentEventInMemoryRepository;

    public PaymentEventService(PaymentEventInMemoryRepository paymentEventInMemoryRepository) {
        this.paymentEventInMemoryRepository = paymentEventInMemoryRepository;
    }

    public void saveIdempotencyKey(PaymentCompletedEvent event) {
        String idempotencyKey = IdempotencyKeyGenerator.generateIdempotencyKey(event);
        paymentEventInMemoryRepository.saveIdempotencyKey(idempotencyKey);
    }

    public boolean hasIdempotencyKey(PaymentCompletedEvent event) {
        String idempotencyKey = IdempotencyKeyGenerator.generateIdempotencyKey(event);
        return paymentEventInMemoryRepository.hasIdempotencyKey(idempotencyKey);
    }

}
