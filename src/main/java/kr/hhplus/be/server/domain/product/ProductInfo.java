package kr.hhplus.be.server.domain.product;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

public class ProductInfo {

    @Getter
    public static class Create {
        Long productId;
        String name;
        BigDecimal price;
        Integer stock;

        @Builder
        private Create(Long productId, String name, BigDecimal price, Integer stock) {
            this.productId = productId;
            this.name = name;
            this.price = price;
            this.stock = stock;
        }

        public static Create from(Product product) {
            return Create.builder()
                    .productId(product.getId())
                    .name(product.getName())
                    .price(product.getAmount())
                    .stock(product.getQuantity())
                    .build();
        }
    }

    @Getter
    public static class IncreaseStock {

        Long productId;
        Integer nowStock;

        @Builder
        private IncreaseStock(Long productId, Integer nowStock) {
            this.productId = productId;
            this.nowStock = nowStock;
        }

        public static IncreaseStock from(Product product) {
            return IncreaseStock.builder().productId(product.getId()).nowStock(product.getQuantity()).build();
        }
    }

    @Getter
    public static class DecreaseStock {

        Long productId;
        Integer nowStock;

        @Builder
        private DecreaseStock(Long productId, Integer nowStock) {
            this.productId = productId;
            this.nowStock = nowStock;
        }

        public static DecreaseStock from(Product product) {
            return DecreaseStock.builder().productId(product.getId()).nowStock(product.getQuantity()).build();
        }
    }
}
