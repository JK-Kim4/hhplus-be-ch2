package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.application.order.OrderItemCriteria;
import kr.hhplus.be.server.application.order.OrderPaymentCriteria;
import kr.hhplus.be.server.domain.order.command.OrderCreateCommand;
import kr.hhplus.be.server.domain.order.command.OrderItemCreateCommand;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {

    static public class Create{

        public Create(){}

        @Schema(name = "userId", description = "주문 생성 사용자 고유 번호", example = "1")
        private Long userId;

        @Schema(name = "userCouponId", description = "(선택) 사용자 쿠폰 고유번호", example = "50")
        private Long userCouponId;

        @Schema(name = "orderItems", description = "주문 상품 목록")
        private List<OrderItemRequest> orderItems;

        public Create(Long userId, Long userCouponId, List<OrderItemRequest> orderItems) {
            this.userId = userId;
            this.userCouponId = userCouponId;
            this.orderItems = orderItems;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getUserCouponId() {
            return userCouponId;
        }

        public void setUserCouponId(Long userCouponId) {
            this.userCouponId = userCouponId;
        }

        public List<OrderItemRequest> getOrderItems() {
            return orderItems;
        }

        public void setOrderItems(List<OrderItemRequest> orderItems) {
            this.orderItems = orderItems;
        }

        public OrderCreateCommand toCommand() {
            List<OrderItemCreateCommand> orderItemListCommands = this.orderItems.stream().map(OrderItemRequest::toCommand)
                    .collect(Collectors.toList());
            return new OrderCreateCommand(this.userId, this.userCouponId, orderItemListCommands);
        }
    }

    public static class OrderPayment {

        @Schema(name = "userId", description = "주문 생성 사용자 고유 번호", example = "1")
        private Long userId;

        @Schema(name = "userCouponId", description = "(선택) 사용자 쿠폰 고유번호", example = "50")
        private Long userCouponId;

        @Schema(name = "orderItems", description = "주문 상품 목록")
        private List<OrderItemRequest> orderItems;

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
