package kr.hhplus.be.server.domain.item;

import kr.hhplus.be.server.interfaces.exception.InvalidPriceException;
import kr.hhplus.be.server.interfaces.exception.InvalidStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    @Nested
    @DisplayName("상품 객체 생성 테스트")
    class create_item_test {

        private String name = "test item";
        private Integer price = 9000;
        private Integer stock = 500;

        @Test
        void 상품이름과_가격정보를_전달받아_상품객체를_생성한다(){
            //when
            Item item = Item.createWithPrice(name, price);

            //then
            assertAll("전달받은 판매가격과 0개의 재고량을 가진다.",
                    () -> assertEquals(price, item.price()),
                    () -> assertEquals(Stock.MINIMUM_STOCK_QUANTITY, item.stock())
            );
        }

        @Test
        void 상품이름_가격_재고정보를_전달받아_상품객체를_생성한다(){
            //when
            Item item = Item.createWithPriceAndStock(name, price, stock);

            //then
            assertAll("전달받은 판매가격과 재고량을 가진다",
                    () -> assertEquals(price, item.price()),
                    () -> assertEquals(stock, item.stock())
            ); ;

        }
    }


    @Nested
    @DisplayName("상품 가격 변경 유효성 검증 테스트")
    class update_item_price_validation_test {

        private String name = "test item";
        private Integer price = 9000;
        private Integer stock = 500;
        private Item item;

        @BeforeEach
        void init() {
            item = Item.createWithPriceAndStock(name, price, stock);
        }

        @ParameterizedTest
        @ValueSource(ints = {12000, 9000, 200, 110, 90000, 99_000_000})
        void 상품가격을_변경한다(Integer price){
            //when
            item.updatePrice(price);

            //then
            assertEquals(price, item.price());
        }

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, -99,-50, -20,-10, -1, 0, 1, 2, 7, 8, 9, 10, 20, 30, 80, 90, 99})
        void 유효하지않은_가격변경요청시_오류를반환한다_최소가격(Integer price){
            //when
            InvalidPriceException invalidPriceException = assertThrows(InvalidPriceException.class,
                    () -> item.updatePrice(price));

            //then
            assertEquals(InvalidPriceException.INSUFFICIENT_MINIMUM_PRICE, invalidPriceException.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {100_000_001, 100_000_100, 111_000_000, 200_000_001, 300_000_001, Integer.MAX_VALUE})
        void 유효하지않은_가격변경요청시_오류를반환한다_최대가격(Integer price){
            //when
            InvalidPriceException invalidPriceException = assertThrows(InvalidPriceException.class,
                    () -> item.updatePrice(price));

            //then
            assertEquals(InvalidPriceException.OVER_MAXIMUM_PRICE, invalidPriceException.getMessage());
        }

    }

    @Nested
    @DisplayName("상품 재고 변경 유효성 검증 테스트")
    class update_item_stock_validation_test {

        private String name = "test item";
        private Integer price = 9000;
        private Integer stock = 500;
        private Item item;

        @BeforeEach
        void init() {
            item = Item.createWithPriceAndStock(name, price, stock);
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 10, 50, 100, 200, 400, 500})
        void 상품재고를_차감한다(Integer amount){
            //when
            item.decreaseStock(amount);

            //then
            assertEquals(stock-amount, item.stock());
        }

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, -10, -1, 0})
        void 유효하지_않은_재고차감요청시_오류를_반환한다(Integer amount){
            //when
            InvalidStockException invalidStockException = assertThrows(InvalidStockException.class,
                    () -> item.decreaseStock(amount));

            //then
            assertEquals(InvalidStockException.INVALID_DECREASE_QUANTITY, invalidStockException.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 10, 20, 30, 100, 200, 300, 500, 99_500})
        void 잔여재고를_추가한다(Integer amount){
            //when
            item.increaseStock(amount);

            //then
            assertEquals(stock+amount, item.stock());
        }

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, -200,-10, 0})
        void 유효하지않은_재고충전요청시_오류를_반환한다_0이하(Integer amount){
            //when
            InvalidStockException invalidStockException = assertThrows(InvalidStockException.class,
                    () -> item.increaseStock(amount));

            //then
            assertEquals(InvalidStockException.INVALID_INCREASE_QUANTITY, invalidStockException.getMessage());
        }

        @ParameterizedTest
        @ValueSource(ints = {99_501, 100_000, 200_000})
        void 유효하지않은_재고충전요청시_오류를_반환한다_최대재고초과(Integer amount){
            //when
            InvalidStockException invalidStockException = assertThrows(InvalidStockException.class,
                    () -> item.increaseStock(amount));

            //then
            assertEquals(InvalidStockException.OVER_MAXIMUM_STOCK_QUANTITY, invalidStockException.getMessage());
        }

        @DisplayName("상품의 재고가 충분한지 검증한다.")
        @ParameterizedTest
        @ValueSource(ints = {1, 5,10,20,500})
        void has_enough_stock_test_true(Integer quantity){
            assertTrue(item.hasEnoughStock(quantity));
        }

        @DisplayName("상품의 재고가 충분한지 검증한다.")
        @ParameterizedTest
        @ValueSource(ints = {501, 600, 700, 999999})
        void has_enough_stock_test_false(Integer quantity){
            assertThrows(InvalidStockException.class, ()
                    -> item.hasEnoughStock(quantity));
        }

        @DisplayName("상품의 재고가 충분한지 검증한다.")
        @ParameterizedTest
        @ValueSource(ints = {100_000,  200_000, 999_999})
        void has_enough_stock_test_exception(Integer quantity){
            assertThrows(InvalidStockException.class,
                    () -> item.hasEnoughStock(quantity));
        }
    }

}
