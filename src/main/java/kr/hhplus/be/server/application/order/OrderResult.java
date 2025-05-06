package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.OrderInfo;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderResult {

    @Getter
    public static class Create {

        Long orderId;
        BigDecimal totalPrice;
        BigDecimal finalPrice;
        LocalDateTime orderedAt;

        public static Create from(OrderInfo.Create order) {
            return Create.builder()
                    .orderId(order.getOrderId())
                    .totalPrice(order.getTotalPrice())
                    .finalPrice(order.getFinalPrice())
                    .orderedAt(order.getOrderedAt())
                    .build();
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
