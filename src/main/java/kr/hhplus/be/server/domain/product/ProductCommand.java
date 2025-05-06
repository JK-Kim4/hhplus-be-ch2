package kr.hhplus.be.server.domain.product;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

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
}
