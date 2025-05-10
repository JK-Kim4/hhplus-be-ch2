package kr.hhplus.be.server.interfaces.api.product;

import kr.hhplus.be.server.domain.product.ProductInfo;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ProductResponse {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Products {
        List<Product> products;

        public static Products from(ProductInfo.Products products) {
            return Products.builder()
                        .products(products.getProducts().stream().map(Product::from).toList())
                    .build();
        }

        @Builder
        private Products(List<Product> products) {
            this.products = products;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product {
        Long productId;
        BigDecimal price;
        Integer quantity;

        public static Product from(ProductInfo.Product product) {
            return Product.builder()
                    .productId(product.getProductId())
                    .price(product.getPrice())
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


    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Ranks implements Serializable{
        List<ProductResponse.Rank> ranks;

        public static Ranks from(ProductInfo.Ranks rankWithLimit) {
            return Ranks.builder()
                    .ranks(rankWithLimit.getRanks().stream().map(ProductResponse.Rank::from).toList())
                    .build();
        }

        @Builder
        private Ranks(List<ProductResponse.Rank> ranks) {
            this.ranks = ranks;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Rank implements Serializable {
        Long productId;
        String productName;
        BigDecimal price;
        Integer quantity;
        Integer rank;
        Long salesAmount;
        LocalDate salesDate;

        public static Rank from(ProductInfo.Rank rank){
            return Rank.builder()
                        .productId(rank.getProductId())
                        .productName(rank.getProductName())
                        .price(rank.getPrice())
                        .quantity(rank.getQuantity())
                        .rank(rank.getRank())
                        .salesAmount(rank.getSalesAmount())
                        .salesDate(rank.getSalesDate())
                    .build();
        }

        @Builder
        private Rank(Long productId, String productName, BigDecimal price, Integer quantity, Integer rank, Long salesAmount, LocalDate salesDate) {
            this.productId = productId;
            this.productName = productName;
            this.price = price;
            this.quantity = quantity;
            this.rank = rank;
            this.salesAmount = salesAmount;
            this.salesDate = salesDate;
        }
    }
}
