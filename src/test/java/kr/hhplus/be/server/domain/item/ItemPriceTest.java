package kr.hhplus.be.server.domain.item;

import kr.hhplus.be.server.interfaces.exception.InvalidPriceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ItemPriceTest {

    @Nested
    @DisplayName("상품 가격 생성 테스트")
    class createItemPrice {

        @Test
        void 판매가격이_100원인_기본_상품가격을_생성한다(){
            //given
            Integer defaultPrice = 100;

            //when
            ItemPrice itemPrice = ItemPrice.createOrDefault(null);

            //then
            assertEquals(defaultPrice, itemPrice.price());
        }

        @Test
        void 초기_상품가격을_파라미터로_상품가격을_생성한다(){
            //given
            Integer initPrice = 5000;

            //when
            ItemPrice itemPrice = ItemPrice.createOrDefault(initPrice);

            //then
            assertEquals(initPrice, itemPrice.price());
        }

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, -99, -50, -40, -30, -10, -9, -8, -5, -1, 0, 1, 2, 3, 4, 10, 20, 30, 99})
        void 초기_상품가격이_최소_판매가격_미만일_경우_오류를_반환한다(){
            //given
            Integer insufficientMinimumPrice = 99;

            //when
            InvalidPriceException exception = assertThrows(InvalidPriceException.class,
                    () -> ItemPrice.createOrDefault(insufficientMinimumPrice));

            //then
            assertEquals(exception.getMessage(), InvalidPriceException.INSUFFICIENT_MINIMUM_PRICE);
        }

        @ParameterizedTest
        @ValueSource(ints = {100_000_001, 200_000_000, 999_999_999, Integer.MAX_VALUE})
        void 초기_상품가격이_최대_판매가격을_초과할경우_오류를_반환한다(){
            //given
            Integer insufficientMinimumPrice = 100_000_001;

            //when
            InvalidPriceException exception = assertThrows(InvalidPriceException.class,
                    () -> ItemPrice.createOrDefault(insufficientMinimumPrice));

            //then
            assertEquals(exception.getMessage(), InvalidPriceException.OVER_MAXIMUM_PRICE);
        }

        @Test
        void 상품가격_최대_최소값에_해당하는_상품가격을_생성한다() {
            ItemPrice minPrice = ItemPrice.createOrDefault(ItemPrice.MINIMUM_ITEM_PRICE);
            ItemPrice macPrice = ItemPrice.createOrDefault(ItemPrice.MAXIMUM_ITEM_PRICE);

            assertEquals(ItemPrice.MINIMUM_ITEM_PRICE, minPrice.price());
            assertEquals(ItemPrice.MAXIMUM_ITEM_PRICE, macPrice.price());
        }
    }


    @Nested
    @DisplayName("상품 가격 정책 유효성 검증 테스트")
    class ItemPriceValidation{

        private ItemPrice itemPrice;

        @BeforeEach
        void init(){
            itemPrice = ItemPrice.createOrDefault(5000);
        }

        @Test
        void 판매_가격을_변경한다(){
            //given
            Integer updatePrice = 50000;

            //when
            itemPrice = itemPrice.update(updatePrice);

            //then
            assertEquals(updatePrice, itemPrice.price());
        }

        @ParameterizedTest
        @ValueSource(ints = {Integer.MIN_VALUE, -99, -50, -40, -30, -10, -9, -8, -5, -1, 0, 1, 2, 3, 4, 10, 20, 30, 99})
        void 변경되는_판매_가격이_최소_판매_금액_미만일_경우_오류를_반환한다(){
            //given
            Integer insufficientMinimumPrice = 10;

            //when
            InvalidPriceException invalidPriceException = assertThrows(InvalidPriceException.class,
                    () -> ItemPrice.update(insufficientMinimumPrice));

            //then
            assertEquals(invalidPriceException.getMessage(), InvalidPriceException.INSUFFICIENT_MINIMUM_PRICE);
        }


        @ParameterizedTest
        @ValueSource(ints = {100_000_001, 200_000_000, 999_999_999, Integer.MAX_VALUE})
        void 변경되는_판매_가격이_최대_판매_금액_초과할_경우_오류를_반환한다(){
            //given
            Integer overMaximumPrice = 100_000_001;

            //when
            InvalidPriceException invalidPriceException = assertThrows(InvalidPriceException.class,
                    () -> ItemPrice.update(overMaximumPrice));

            //then
            assertEquals(invalidPriceException.getMessage(), InvalidPriceException.OVER_MAXIMUM_PRICE);
        }


    }

}
