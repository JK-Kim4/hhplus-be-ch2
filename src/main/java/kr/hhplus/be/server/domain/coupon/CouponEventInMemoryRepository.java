package kr.hhplus.be.server.domain.coupon;


public interface CouponEventInMemoryRepository {

    void saveIdempotencyKey(String value);

    boolean hasIdempotencyKey(String value);

}
