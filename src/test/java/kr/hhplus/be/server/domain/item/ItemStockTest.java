package kr.hhplus.be.server.domain.item;

import kr.hhplus.be.server.interfaces.exception.InvalidStockException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ItemStockTest {

    @Nested
    @DisplayName("상품 재고 생성 테스트")
    class createItemStock {

        @Test
        void 상품재고가_0인_기본_상품가격을_생성한다(){
            //given
            Integer defaultStock = 0;

            //when
            ItemStock itemStock = ItemStock.createOrDefault(null);

            //then
            assertEquals(defaultStock, itemStock.stock());
        }

        @Test
        void 초기상품재고를_전달받아_상품가격을_생성한다(){
            //given
            Integer initStock = 10;

            //when
            ItemStock itemStock = ItemStock.createOrDefault(initStock);

            //then
            assertEquals(initStock, itemStock.stock());
        }

        @Test
        void 초기상품재고가_0개미만일경우_오류를반환한다(){
            //given
            Integer insufficientStockQuantity = -10;

            //when
            InvalidStockException exception = assertThrows(InvalidStockException.class,
                    () -> ItemStock.createOrDefault(insufficientStockQuantity));

            //then
            assertEquals(InvalidStockException.INSUFFICIENT_MINIMUM_STOCK_QUANTITY, exception.getMessage());
        }

        @Test
        void 초기상품재고가_최대보유재고수를_초과할경우_오류를반환한다(){
            //given
            Integer overMaximumStockQuantity = 100_001;

            //when
            InvalidStockException exception = assertThrows(InvalidStockException.class,
                    () -> ItemStock.createOrDefault(overMaximumStockQuantity));

            //then
            assertEquals(InvalidStockException.OVER_MAXIMUM_STOCK_QUANTITY, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("상품 재고 변경 유효성 검증 테스트")
    class ItemStockValidation {

        private ItemStock itemStock;

        @BeforeEach
        void init() {
            itemStock = ItemStock.createOrDefault(10);
        }

        @Test
        void 상품재고를_추가한다(){
            //given
            Integer increaseQuantity = 100;
            Integer currentStockQuantity = itemStock.stock();

            //when
            itemStock = itemStock.increase(increaseQuantity);

            //then
            assertEquals(currentStockQuantity+increaseQuantity, itemStock.stock());
        }

        @Test
        void 재고추가수량이_1개미만일경우_오류를반환한다(){
            //given
            Integer zeroQuantity = 0;

            //when
            InvalidStockException invalidStockException = assertThrows(InvalidStockException.class,
                    () -> itemStock.increase(zeroQuantity));

            //then
            assertEquals(InvalidStockException.INSUFFICIENT_MINIMUM_STOCK_QUANTITY, invalidStockException.getMessage());
        }

        @Test
        void 재고보유량은_최대보유가능수량_100_000개를_초과할수없다(){
            //given
            Integer overMaximumQuantity = 200_000;

            //when
            InvalidStockException invalidStockException = assertThrows(InvalidStockException.class,
                    () -> itemStock.increase(overMaximumQuantity));

            //then
            assertEquals(InvalidStockException.OVER_MAXIMUM_STOCK_QUANTITY, invalidStockException.getMessage());
        }

        @Test
        void 상품재고수량은_차감한다(){
            //given
            Integer deductQuantity = 5;
            Integer currentStockQuantity = itemStock.stock();

            //when
            itemStock = itemStock.decrease(deductQuantity);

            //then
            assertEquals(currentStockQuantity-deductQuantity, itemStock.stock());
        }

        @Test
        void 보유재고룰_초과하는_차감요청시_오류를_반환한다(){
            //given
            Integer overCurrentStockDeductQuantity = itemStock.stock() + 5;

            //when
            InvalidStockException invalidStockException = assertThrows(InvalidStockException.class,
                    () -> itemStock.decrease(overCurrentStockDeductQuantity));

            //then
            assertEquals(InvalidStockException.INSUFFICIENT_STOCK_QUANTITY, invalidStockException.getMessage());
        }
    }
}
