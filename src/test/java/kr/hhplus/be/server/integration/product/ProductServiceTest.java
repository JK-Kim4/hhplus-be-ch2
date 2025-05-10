package kr.hhplus.be.server.integration.product;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.support.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    DatabaseCleanup databaseCleanup;

    @BeforeEach
    void setup() {
        databaseCleanup.truncate();
        for (int i = 1; i <= 30; i++) {
            Product product = Product.create("상품" + i, BigDecimal.valueOf(1000 * i), i);
            productRepository.save(product);
        }
        productRepository.flush();
    }

    @Test
    void 상품_목록_조회_테스트_offset_10_limit_5_테스트() {
        ProductInfo.Products products = productService.findAll(10, 5);

        assertEquals(5, products.getProducts().size());
        assertEquals("상품20", products.getProducts().get(0).getProductName());
        assertEquals("상품16", products.getProducts().get(4).getProductName());
    }

}
