package kr.hhplus.be.server.domain.coupon;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class CouponTemplate {

    private Long id;
    private String name;
    private CouponType couponType;
    private AtomicInteger remainingQuantity;
    private LocalDateTime expireDateTime;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CouponType getCouponType() {
        return couponType;
    }

    public AtomicInteger getRemainingQuantity() {
        return remainingQuantity;
    }

    public LocalDateTime getExpireDateTime() {
        return expireDateTime;
    }

    public static CouponTemplateBuilder builder() {
        return new CouponTemplateBuilder();
    }

    // 정적 내부 빌더 클래스
    public static class CouponTemplateBuilder {
        private Long id;
        private String name;
        private CouponType couponType;
        private AtomicInteger remainingQuantity;
        private LocalDateTime expireDateTime;

        public CouponTemplateBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CouponTemplateBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CouponTemplateBuilder couponType(CouponType couponType) {
            this.couponType = couponType;
            return this;
        }

        public CouponTemplateBuilder remainingQuantity(AtomicInteger remainingQuantity) {
            this.remainingQuantity = remainingQuantity;
            return this;
        }

        public CouponTemplateBuilder expireDateTime(LocalDateTime expireDateTime) {
            this.expireDateTime = expireDateTime;
            return this;
        }

        public CouponTemplate build() {
            CouponTemplate template = new CouponTemplate();
            template.id = this.id;
            template.name = this.name;
            template.couponType = this.couponType;
            template.remainingQuantity = this.remainingQuantity;
            template.expireDateTime = this.expireDateTime;
            return template;
        }
    }

}
