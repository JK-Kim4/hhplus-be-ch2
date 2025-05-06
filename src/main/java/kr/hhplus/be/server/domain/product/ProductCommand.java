package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.order.OrderInfo;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductCommand {

    @Getter
    public static class Create {
        String name;
        BigDecimal price;
        Integer stock;

        @Builder
        private Create(String name, BigDecimal price, Integer stock) {
            this.name = name;
            this.price = price;
            this.stock = stock;
        }

    }

    @Getter
    public static class IncreaseStock {

        Long productId;
        Integer additionStock;

        @Builder
        private IncreaseStock(Long productId, Integer addStock) {
            this.productId = productId;
            this.additionStock = addStock;
        }
    }

    @Getter
    public static class DecreaseStock {

        Long productId;
        Integer deductedStock;

        public static DecreaseStock of(Long productId, Integer deductedStock){
            return DecreaseStock.builder().productId(productId).deductedStock(deductedStock).build();
        }

        @Builder
        private DecreaseStock(Long productId, Integer deductedStock) {
            this.productId = productId;
            this.deductedStock = deductedStock;
        }

    }

    @Getter
    public static class OrderItem {
        Long productId;

        public static OrderItem of(Long productId){
            return OrderItem.builder().productId(productId).build();
        }

        @Builder
        private OrderItem(Long productId) {
            this.productId = productId;
        }
    }


    @Getter
    public static class DeductStock {
        Map<Long, Integer> quantityMap;

        public static DeductStock from(OrderInfo.OrderItems orderItems){
            return DeductStock.builder().orderItems(orderItems).build();
        }

        @Builder
        private DeductStock(OrderInfo.OrderItems orderItems) {
            this.quantityMap = orderItems.getOrderItems().stream()
                    .collect(Collectors.toMap(
                            kr.hhplus.be.server.domain.order.OrderInfo.OrderItem::getProductId,
                            kr.hhplus.be.server.domain.order.OrderInfo.OrderItem::getQuantity));
        }
    }
}
