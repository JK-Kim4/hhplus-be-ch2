package kr.hhplus.be.server.domain.product;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

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

        public static Create from(kr.hhplus.be.server.domain.product.Product product) {
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

        public static IncreaseStock from(kr.hhplus.be.server.domain.product.Product product) {
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

        public static DecreaseStock from(kr.hhplus.be.server.domain.product.Product product) {
            return DecreaseStock.builder().productId(product.getId()).nowStock(product.getQuantity()).build();
        }
    }

    @Getter
    public static class OrderItem {
        Long productId;
        BigDecimal price;
        Integer quantity;


        public static OrderItem from(kr.hhplus.be.server.domain.product.Product product) {
            return OrderItem.builder()
                        .productId(product.getId())
                        .price(product.getAmount())
                        .quantity(product.getQuantity())
                    .build();
        }

        @Builder
        private OrderItem(Long productId, BigDecimal price, Integer quantity) {
            this.productId = productId;
            this.price = price;
            this.quantity = quantity;
        }
    }

    @Getter
    public static class Products{
        List<ProductInfo.Product> products;

        public static Products fromList(List<kr.hhplus.be.server.domain.product.Product> productList) {
            return Products.builder().products(productList.stream().map(ProductInfo.Product::from).toList()).build();
        }

        @Builder
        private Products(List<ProductInfo.Product> products) {
            this.products = products;
        }

    }

    @Getter
    public static class Product {
        Long productId;
        BigDecimal price;
        Integer quantity;

        public static Product from(kr.hhplus.be.server.domain.product.Product product) {
            return Product.builder()
                    .productId(product.getId())
                    .price(product.getAmount())
                    .quantity(product.getQuantity())
                    .build();
        }

        @Builder
        private Product(Long productId, BigDecimal price, Integer quantity) {
            this.productId = productId;
            this.price = price;
            this.quantity = quantity;
        }
    }
}
