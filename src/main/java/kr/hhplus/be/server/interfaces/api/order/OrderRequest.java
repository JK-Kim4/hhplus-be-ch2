package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.hhplus.be.server.application.order.criteria.OrderItemCriteria;
import kr.hhplus.be.server.application.order.criteria.OrderPaymentCriteria;
import kr.hhplus.be.server.domain.order.command.OrderCommand;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderRequest {

    static public class Create{

        public Create(){}

        @Schema(name = "userId", description = "주문 생성 사용자 고유 번호", example = "1")
        @NotNull @Positive
        private Long userId;

        @Schema(name = "userCouponId", description = "(선택) 사용자 쿠폰 고유번호", example = "50")
        @Positive
        private Long userCouponId;

        @Schema(name = "orderItems", description = "주문 상품 목록")
        private List<OrderItemRequest> orderItems = new ArrayList<>();

        public Create(Long userId, Long userCouponId, List<OrderItemRequest> orderItems) {
            this.userId = userId;
            this.userCouponId = userCouponId;
            this.orderItems = orderItems;
        }


        public OrderCommand.Create toCommand(){
            List<OrderCommand.OrderItemCreate> orderItemList =
                    this.orderItems.stream()
                        .map(OrderItemRequest::toCommand)
                        .collect(Collectors.toList());

            return OrderCommand.Create.of(this.userId, this.userCouponId, orderItemList);
        }

        public OrderPaymentCriteria toCriteria(){
            List<OrderItemCriteria> orderItemList = orderItems.stream()
                    .map(oi ->
                            OrderItemCriteria.of(oi.getItemId(), oi.getPrice(), oi.getQuantity()))
                    .collect(Collectors.toList());

            return new OrderPaymentCriteria(this.userId, this.userCouponId, orderItemList);
        }
    }

    @Getter
    public static class OrderPayment {

        @Schema(name = "userId", description = "주문 생성 사용자 고유 번호", example = "1")
        private Long userId;

        @Schema(name = "userCouponId", description = "(선택) 사용자 쿠폰 고유번호", example = "50")
        private Long userCouponId;

        @Schema(name = "orderItems", description = "주문 상품 목록")
        private List<OrderItemRequest> orderItems;

        public OrderPayment(){}

        public OrderPayment(Long userId, Long userCouponId, List<OrderItemRequest> orderItems) {
            this.userId = userId;
            this.userCouponId = userCouponId;
            this.orderItems = orderItems;
        }

        public OrderPaymentCriteria toCriteria() {
            List<OrderItemCriteria> orderItemCriterias = this.orderItems.stream().map(OrderItemRequest::toCriteria)
                    .collect(Collectors.toList());
            return new OrderPaymentCriteria(this.userId, this.userCouponId, orderItemCriterias);
        }

        public Long getUserId() {
            return userId;
        }

        public Long getUserCouponId() {
            return userCouponId;
        }

        public List<OrderItemRequest> getOrderItems() {
            return orderItems;
        }
    }
}
