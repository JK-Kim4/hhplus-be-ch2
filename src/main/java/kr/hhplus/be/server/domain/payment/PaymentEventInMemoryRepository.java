package kr.hhplus.be.server.domain.payment;

public interface PaymentEventInMemoryRepository {

    void saveIdempotencyKey(String value);

    boolean hasIdempotencyKey(String value);
}
