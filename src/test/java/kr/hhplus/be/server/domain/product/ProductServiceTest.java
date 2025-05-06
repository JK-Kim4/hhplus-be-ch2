package kr.hhplus.be.server.domain.product;

import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    @Test
    void 상품을_생성할_수_있다() {
        // given
        ProductCommand.Create command = ProductCommand.Create.builder()
                .name("상품1")
                .price(BigDecimal.valueOf(10000))
                .stock(10)
                .build();

        // when
        ProductInfo.Create result = productService.create(command);

        // then
        verify(productRepository).save(any(Product.class));
        assertThat(result.getName()).isEqualTo("상품1");
        assertThat(result.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(10000));
        assertThat(result.getStock()).isEqualTo(10);
    }

    @Test
    void 재고를_증가시킬_수_있다() {
        // given
        Product product = Product.create("상품1", BigDecimal.valueOf(10000), 5);
        Long productId = 1L;

        when(productRepository.findByIdWithPessimisticLock(productId)).thenReturn(Optional.of(product));

        ProductCommand.IncreaseStock command = ProductCommand.IncreaseStock.builder()
                .productId(productId)
                .addStock(3)
                .build();

        // when
        ProductInfo.IncreaseStock result = productService.increaseStock(command);

        // then
        verify(productRepository).save(product);
        assertThat(result.getNowStock()).isEqualTo(8);
    }

    @Test
    void 재고를_차감할_수_있다() {
        // given
        Product product = Product.create("상품1", BigDecimal.valueOf(10000), 5);
        Long productId = 1L;

        when(productRepository.findByIdWithPessimisticLock(productId)).thenReturn(Optional.of(product));

        ProductCommand.DecreaseStock command =
                ProductCommand.DecreaseStock.builder()
                        .productId(productId)
                        .deductedStock(3)
                        .build();

        // when
        ProductInfo.DecreaseStock result = productService.decreaseStock(command);

        // then
        verify(productRepository).save(product);
        assertThat(result.getNowStock()).isEqualTo(2);
    }

    @Test
    void 재고차감시_보유재고가_부족할경우_예외(){
        //given
        Product product = Product.create("상품1", BigDecimal.valueOf(10000), 5);
        Long productId = 1L;

        when(productRepository.findByIdWithPessimisticLock(productId)).thenReturn(Optional.of(product));

        ProductCommand.DecreaseStock command =
                ProductCommand.DecreaseStock.builder()
                        .productId(productId)
                        .deductedStock(100)
                        .build();

        // expect
        assertThatThrownBy(() -> productService.decreaseStock(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("재고가 부족합니다.");

    }

    @Test
    void 재고차감시_상품이_없으면_예외() {
        // given
        when(productRepository.findByIdWithPessimisticLock(any())).thenReturn(Optional.empty());

        ProductCommand.DecreaseStock command =
                ProductCommand.DecreaseStock.builder()
                        .productId(999L)
                        .deductedStock(100)
                        .build();

        // expect
        assertThatThrownBy(() -> productService.decreaseStock(command))
                .isInstanceOf(NoResultException.class);
    }

    @Test
    void findByIdInWithPessimisticLock_정상조회() {
        List<Long> ids = List.of(1L, 2L);
        List<Product> products = List.of(
                Product.create("상품1", BigDecimal.valueOf(1000), 10),
                Product.create("상품2", BigDecimal.valueOf(2000), 20)
        );

        given(productRepository.findByIdInWithPessimisticLock(ids)).willReturn(products);

        ProductInfo.Products result = productService.findByIdInWithPessimisticLock(ids);

        assertThat(result).isNotNull();
    }
}
