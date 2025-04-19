package kr.hhplus.be.server.domain.item;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {


    @Test
    void 잔여_재고가_부족할경우_hasEnoughStock는_false를_리턴(){
        //given
        Item item = createItemFixture();

        //when//then
        assertFalse(item.hasEnoughStock(999));
    }

    @Test
    void 가격정보가_일치하지않을경우_isSamePrice는_false를_리턴(){
        //given
        Item item = createItemFixture();

        //when//then
        assertFalse(item.isSamePrice(9_000));
    }

    @Nested
    class 재고_변경{

        @Test
        void 추가수량을_전달받아_재고를_추가한다(){
            //given
            Item item = createItemFixture();
            Integer existingStock = item.stock();
            Integer plus = 10;

            //when
            item.increaseStock(plus);

            //then
            assertEquals(existingStock+plus, item.stock());
        }

        @Test
        void 추가수량이_0이하일경우_재고_추가시_IllegalArgumentException(){
            //given
            Item item = createItemFixture();

            //when//then
            assertThrows(IllegalArgumentException.class, ()
                    -> item.decreaseStock(0));
        }

        @Test
        void 추가수량과_현재재고수량의_합이_보유가능수량_100_000개를_초과할경우_IllegalArgumentException(){
            //given
            Item item = createItemFixture();

            //when//then
            assertThrows(IllegalArgumentException.class, ()
                    -> item.increaseStock(100_001));
        }

        @Test
        void 차감수량을_전달받아_재고를_차감한다(){
            //given
            Item item = createItemFixture();
            Integer existingStock = item.stock();
            Integer minus = 10;

            //when
            item.decreaseStock(minus);

            //then
            assertEquals(existingStock-minus, item.stock());
        }

        @Test
        void 재고가_부족할경우_재고차감_요청시_IllegalArgumentException(){
            //given
            Item item = createItemFixture();
            Integer existingStock = item.stock();

            //when
            assertThrows(IllegalArgumentException.class, ()
                    -> item.decreaseStock(existingStock + 1));
        }
    }

    @Nested
    class 가격_변경{

        @Test
        void 가격정보를_전달받아_상품의_가격을_변경한다(){
            //given
            Item item = createItemFixture();

            //when
            item.updatePrice(1_000);

            //then
            assertEquals(1_000, item.price());
        }

        @Test
        void 전달받은_가격정보가_최대판매금액_100_000_000원을_초과할경우_IllegalArgumentException(){
            //given
            Item item = createItemFixture();
            Integer overPoint = 100_000_001;

            //when//then
            assertThrows(IllegalArgumentException.class, ()
                    -> item.updatePrice(overPoint));
        }

        @Test
        void 전달받은_가격정보가_최소판매금액_100원_미만일경우_IllegalArgumentException(){
            //given
            Item item = createItemFixture();
            Integer underMinimum = 99;

            //when//then
            assertThrows(IllegalArgumentException.class, ()
                    -> item.updatePrice(underMinimum));
        }
    }

    private Item createItemFixture(){
        return Item.createWithNameAndPriceAndStock(
                "test item",
                5_000,
                10);
    }

}
