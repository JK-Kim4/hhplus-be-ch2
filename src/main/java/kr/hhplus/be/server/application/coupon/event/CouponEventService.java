package kr.hhplus.be.server.application.coupon.event;

import kr.hhplus.be.server.common.event.CouponIssueRequestedEvent;
import kr.hhplus.be.server.common.keys.CacheKeys;
import kr.hhplus.be.server.common.keys.IdempotencyKeyGenerator;
import kr.hhplus.be.server.domain.coupon.CouponEventInMemoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CouponEventService {

    private final CouponEventInMemoryRepository couponEventRepository;

    public CouponEventService(CouponEventInMemoryRepository couponEventRepository) {
        this.couponEventRepository = couponEventRepository;
    }

    public void saveIssueRequestedIdempotencyKey(CouponIssueRequestedEvent event){
        String idempotencyKey = IdempotencyKeyGenerator.generateIdempotencyKey(event);
        couponEventRepository.saveIdempotencyKey(CacheKeys.IDEMPOTENCY_COUPON_ISSUE.format(idempotencyKey));
    }

    public boolean hasIssueRequestedIdempotencyKey(CouponIssueRequestedEvent event){
        String idempotencyKey = IdempotencyKeyGenerator.generateIdempotencyKey(event);
        return couponEventRepository.hasIdempotencyKey(CacheKeys.IDEMPOTENCY_COUPON_ISSUE.format(idempotencyKey));
    }
}
