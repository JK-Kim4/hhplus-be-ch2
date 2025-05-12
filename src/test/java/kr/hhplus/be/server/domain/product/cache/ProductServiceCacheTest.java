package kr.hhplus.be.server.domain.product.cache;

import kr.hhplus.be.server.domain.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ProductServiceCacheTest {

    @MockitoSpyBean
    private ProductService productService;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void clearCache() {
        cacheManager.getCache("products:list").clear();
    }

    @Test
    void 캐시는_두번째_호출부터_메서드를_실행하지_않는다() {
        // given
        int offset = 0, limit = 10;

        // when
        productService.findAll(offset, limit); // 캐시 미적중 → 메서드 실행
        productService.findAll(offset, limit); // 캐시 적중 → 메서드 실행 X
        productService.findAll(offset, limit); // 캐시 적중 → 메서드 실행 X
        productService.findAll(offset, limit); // 캐시 적중 → 메서드 실행 X
        productService.findAll(offset, limit); // 캐시 적중 → 메서드 실행 X

        // then
        verify(productService, times(1)).findAll(offset, limit);
    }
}
