package kr.hhplus.be.server.interfaces.api.product;

import kr.hhplus.be.server.domain.product.ProductInfo;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

public class ProductResponse {

    @Getter
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
    public static class Ranks {
    }
}
