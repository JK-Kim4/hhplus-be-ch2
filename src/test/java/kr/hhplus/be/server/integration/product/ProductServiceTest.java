package kr.hhplus.be.server.integration.product;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.support.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class ProductServiceTest {

    @MockitoSpyBean
    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    DatabaseCleanup databaseCleanup;

    @BeforeEach
    void setup() {
        databaseCleanup.truncate();
        redisTemplate.execute((RedisConnection connection) -> {
            connection.flushAll();  // 전체 Redis DB 초기화 (모든 key 삭제)
            return null;
        });

    }

    @Test
    void 상품_목록_조회_테스트_offset_10_limit_5_테스트() {
        샘플_상품데이터_30건_등록();
        ProductInfo.Products products = productService.findAll(10, 5);

        assertEquals(5, products.getProducts().size());
        assertEquals("상품20", products.getProducts().get(0).getProductName());
        assertEquals("상품16", products.getProducts().get(4).getProductName());
    }

    private void 샘플_상품데이터_30건_등록(){
        for (int i = 1; i <= 30; i++) {
            Product product = Product.create("상품" + i, BigDecimal.valueOf(1000 * i), i);
            productRepository.save(product);
        }
        productRepository.flush();
    }

}
