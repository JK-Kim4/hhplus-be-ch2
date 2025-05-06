package kr.hhplus.be.server.domain.order;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderInfo {

    @Getter
    public static class Create {
        Long orderId;
        BigDecimal totalPrice;
        BigDecimal finalPrice;
        LocalDateTime orderedAt;

        public static Create from(Order order) {
            return new Create(order.getId(), order.getTotalAmount(), order.getFinalAmount(), order.getOrderedAt());
        }

        @Builder
        private Create(Long orderId, BigDecimal totalPrice, BigDecimal finalPrice, LocalDateTime orderedAt) {
            this.orderId = orderId;
            this.totalPrice = totalPrice;
            this.finalPrice = finalPrice;
            this.orderedAt = orderedAt;
        }

    }
}
