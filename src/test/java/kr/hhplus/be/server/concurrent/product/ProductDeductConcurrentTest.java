package kr.hhplus.be.server.concurrent.product;

import kr.hhplus.be.server.concurrent.support.ConcurrentTestExecutor;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductCommand;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class ProductDeductConcurrentTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    Product testProduct;

    @BeforeEach
    void setup(){
        testProduct = Product.create("prodcut1", BigDecimal.valueOf(10_000), 5);
        productRepository.save(testProduct);
        productRepository.flush();
    }

    @Test
    void 상품재고가_5개인_상품에_대하여_재고차감요청_10개_동시성_테스트() throws InterruptedException {

        List<Runnable> tasks = Arrays.asList(
                () -> productService.decreaseStock(ProductCommand.DecreaseStock.of(testProduct.getId(), 1)),
                () -> productService.decreaseStock(ProductCommand.DecreaseStock.of(testProduct.getId(), 1)),
                () -> productService.decreaseStock(ProductCommand.DecreaseStock.of(testProduct.getId(), 1)),
                () -> productService.decreaseStock(ProductCommand.DecreaseStock.of(testProduct.getId(), 1)),
                () -> productService.decreaseStock(ProductCommand.DecreaseStock.of(testProduct.getId(), 1)),
                () -> productService.decreaseStock(ProductCommand.DecreaseStock.of(testProduct.getId(), 1)),
                () -> productService.decreaseStock(ProductCommand.DecreaseStock.of(testProduct.getId(), 1)),
                () -> productService.decreaseStock(ProductCommand.DecreaseStock.of(testProduct.getId(), 1)),
                () -> productService.decreaseStock(ProductCommand.DecreaseStock.of(testProduct.getId(), 1)),
                () -> productService.decreaseStock(ProductCommand.DecreaseStock.of(testProduct.getId(), 1))
        );


        List<Throwable> execute = ConcurrentTestExecutor.execute(5, tasks);

        productRepository.flush();
        Product product = productRepository.findById(testProduct.getId()).get();

        assertEquals(0, product.getQuantity());
        assertEquals(5, execute.size());



    }




}
