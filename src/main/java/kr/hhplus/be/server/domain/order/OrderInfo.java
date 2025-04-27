package kr.hhplus.be.server.domain.order;

public class OrderInfo {

        public static class Create{

            private Long orderId;
            private Integer totalPrice;

            public static Create from(Order save) {
                return new Create(save.getId(), save.getTotalPrice());
            }

            public Create(Long orderId, Integer totalPrice) {
                this.orderId = orderId;
                this.totalPrice = totalPrice;
            }

            public Long getOrderId() {
                return orderId;
            }

            public Integer getTotalPrice() {
                return totalPrice;
            }
        }

}
