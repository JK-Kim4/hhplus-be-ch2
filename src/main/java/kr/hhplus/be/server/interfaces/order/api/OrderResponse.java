package kr.hhplus.be.server.interfaces.order.api;

import kr.hhplus.be.server.application.order.OrderResult;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderResponse {

    @Getter
    public static class Order {

        Long orderId;
        BigDecimal totalPrice;
        BigDecimal finalPrice;
        LocalDateTime orderedAt;

        public static Order from(OrderResult.Create result){
            return Order.builder()
                    .orderId(result.getOrderId())
                    .totalPrice(result.getTotalPrice())
                    .finalPrice(result.getFinalPrice())
                    .orderedAt(result.getOrderedAt())
                    .build();
        }


        @Builder
        private Order(Long orderId, BigDecimal totalPrice, BigDecimal finalPrice, LocalDateTime orderedAt) {
            this.orderId = orderId;
            this.totalPrice = totalPrice;
            this.finalPrice = finalPrice;
            this.orderedAt = orderedAt;
        }
    }
}
