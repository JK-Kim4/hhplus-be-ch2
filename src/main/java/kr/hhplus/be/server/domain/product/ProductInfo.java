package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.application.salesStat.SalesStatProcessor;
import kr.hhplus.be.server.domain.salesStat.SalesStat;
import kr.hhplus.be.server.domain.salesStat.TypedScore;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Getter @Setter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Products implements Serializable {
        List<ProductInfo.Product> products;

        public static Products fromList(List<kr.hhplus.be.server.domain.product.Product> productList) {
            return Products.builder().products(productList.stream().map(ProductInfo.Product::from).toList()).build();
        }

        @Builder
        private Products(List<ProductInfo.Product> products) {
            this.products = products;
        }

        @Override
        public String toString() {
            return "Products{" +
                    "products=" + products +
                    '}';
        }
    }

    @Getter @Setter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Product implements Serializable{
        Long productId;
        String productName;
        BigDecimal price;
        Integer quantity;

        public static Product from(kr.hhplus.be.server.domain.product.Product product) {
            return Product.builder()
                        .productId(product.getId())
                        .productName(product.getName())
                        .price(product.getAmount())
                        .quantity(product.getQuantity())
                    .build();
        }

        @Builder
        private Product(Long productId, String productName, BigDecimal price, Integer quantity) {
            this.productId = productId;
            this.productName = productName;
            this.price = price;
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return "Product{" +
                    "productId=" + productId +
                    ", productName='" + productName + '\'' +
                    ", price=" + price +
                    ", quantity=" + quantity +
                    '}';
        }
    }

    @Getter @Setter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Ranks implements Serializable{
        List<Rank> ranks;


        public static Ranks of(List<SalesStat> salesStats, List<kr.hhplus.be.server.domain.product.Product> products) {
            Map<Long, kr.hhplus.be.server.domain.product.Product> productMap = products.stream()
                    .collect(java.util.stream.Collectors.toMap(
                            kr.hhplus.be.server.domain.product.Product::getId,
                            product -> product));

            return Ranks.builder().ranks(
                    salesStats.stream().map((ss) ->
                            Rank.of(
                                    ss.getProductId(),
                                    productMap.get(ss.getProductId()).getName(),
                                    productMap.get(ss.getProductId()).getAmount(),
                                    productMap.get(ss.getProductId()).getQuantity(),
                                    salesStats.indexOf(ss) + 1,
                                    ss.getSalesAmount(),
                                    ss.getSalesDate()
                            )
                    ).toList()).build();
        }

        public static Ranks of(List<TypedScore> typedScores, List<kr.hhplus.be.server.domain.product.Product> products, LocalDate nowDate) {
            Map<Long, kr.hhplus.be.server.domain.product.Product> productMap = products.stream()
                    .collect(java.util.stream.Collectors.toMap(
                            kr.hhplus.be.server.domain.product.Product::getId,
                            product -> product));

            List<Rank> ranks = new ArrayList<>();
            int rank = 1;

            for (TypedScore report : typedScores) {

                if (report.member().equals(SalesStatProcessor.SALES_REPORT_IGNORE_VALUE)) {
                    continue;
                }

                Long productId = Long.parseLong(report.member());
                kr.hhplus.be.server.domain.product.Product product = productMap.get(productId);

                if (product == null) {
                    continue; // 매핑되지 않은 상품은 스킵
                }

                ranks.add(Rank.of(
                        productId,
                        product.getName(),
                        product.getAmount(),
                        product.getQuantity(),
                        rank++,
                        (long) report.score(),
                        nowDate
                ));
            }

            return Ranks.builder().ranks(ranks).build();
        }

        @Builder
        private Ranks(List<Rank> ranks) {
            this.ranks = ranks;
        }
    }

    @Getter @Setter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Rank implements Serializable{
        Long productId;
        String productName;
        BigDecimal price;
        Integer quantity;
        Integer rank;
        Long salesAmount;
        LocalDate salesDate;

        public static Rank of(Long productId, String productName, BigDecimal price, Integer quantity, Integer rank, Long salesAmount, LocalDate salesDate) {
            return Rank.builder()
                        .productId(productId)
                        .productName(productName)
                        .price(price)
                        .quantity(quantity)
                        .rank(rank)
                        .salesAmount(salesAmount)
                        .salesDate(salesDate)
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
