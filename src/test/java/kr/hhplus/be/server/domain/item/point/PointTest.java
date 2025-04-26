package kr.hhplus.be.server.domain.item.point;

import kr.hhplus.be.server.domain.FakeUser;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.point.Point;
import kr.hhplus.be.server.interfaces.common.exception.InvalidAmountException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PointTest {

    @Nested
    @DisplayName("포인트 생성 테스트")
    class create_point {

        private User user = new FakeUser(1L, "tester");

        @Test
        void 잔고가_0원인_기본포인트를_생성한다(){
            Point point = Point.createWithUser(user);

            assertEquals(0, point.getPointAmount());
        }

        @Test
        void 전달_사용자가_null인경우_IllegalArgumentException를_빈환한다(){
            //when
            IllegalArgumentException illegalArgumentException =
                    assertThrows(IllegalArgumentException.class,
                            () -> Point.createWithUser(null));

            //then
            assertEquals("사용자 정보가 존재하지 않습니다.", illegalArgumentException.getMessage());
        }
    }

    @Nested
    @DisplayName("포인트 충전 테스트")
    class point_charge_test {

        private Point point = Point.createWithUser(new FakeUser(1L, "tester"));

        @Test
        void 포인트를_충전한다(){
            //when
            point.charge(5_000);

            //then
            assertEquals(5_000, point.getPointAmount());
        }

        @ParameterizedTest
        @ValueSource(ints = {-100, -50, -10, 0, 1, 2, 10, 20, 30, 97, 98, 99})
        void 최소충전금액_100원_미만_충전시_InvalidAmountException를_반환한다(Integer amount){
            //when
            InvalidAmountException invalidAmountException =
                    assertThrows(InvalidAmountException.class, () -> point.charge(amount));

            //then
            assertEquals(InvalidAmountException.INSUFFICIENT_CHARGE_AMOUNT,
                    invalidAmountException.message);
        }

        @ParameterizedTest
        @ValueSource(ints = {100_000_001, 100_000_100, 100_000_300, 200_000_000, 300_000_000, 400_000_000})
        void 충전후_잔고가_최대보유잔고_100_000_000원_초과시_InvalidAmountException를_반환한다(Integer amount){
            //when
            InvalidAmountException invalidAmountException =
                    assertThrows(InvalidAmountException.class, () -> point.charge(amount));

            //then
            assertEquals(InvalidAmountException.MAXIMUM_BALANCE_EXCEEDED,
                    invalidAmountException.message);
        }

    }

    @Nested
    @DisplayName("포인트 차감 테스트")
    class point_deduct_test {

        private Point point = Point.createWithUser(new FakeUser(1L, "tester"));

        @Test
        void 포인트를_차감한다(){
            //given
            point.charge(5_000);

            //when
            point.deduct(5_000);

            //then
            assertEquals(0, point.getPointAmount());
        }

        @ParameterizedTest
        @ValueSource(ints = {5001, 5002, 5003, 5100, 5200, 5300, 10_000, 20_000, 30_000})
        void 잔액이_부족할경우_InvalidAmountException를_반환한다(Integer amount){
            //given
            point.charge(5_000);


            //when
            InvalidAmountException invalidAmountException =
                    assertThrows(InvalidAmountException.class, () -> point.deduct(amount));

            //then
            assertEquals(InvalidAmountException.INSUFFICIENT_BALANCE ,invalidAmountException.getMessage());

        }
    }






}
