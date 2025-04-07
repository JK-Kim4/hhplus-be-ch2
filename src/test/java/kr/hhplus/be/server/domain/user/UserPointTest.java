package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.interfaces.exception.InvalidAmountException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class UserPointTest {


    @Nested
    @DisplayName("충전 금액 정책 유효성 검증 테스트")
    class PolicyValidation {

        @Test
        void 포인트를_충전한다(){
            //given
            Integer chargePoint = 1000;
            UserPoint userPoint = UserPoint.createOrDefault(0);

            //when
            userPoint = userPoint.charge(chargePoint);

            //then
            Assertions.assertEquals(chargePoint, userPoint.point());
        }

        @Test
        void 최소_충전_금액_미만_충전_요청의경우_오류가_발생한다(){
            //given
            Integer chargePoint = 99;
            UserPoint userPoint = UserPoint.createOrDefault(0);

            //when

            //then
            InvalidAmountException exception = Assertions.assertThrows(InvalidAmountException.class,
                    () -> userPoint.charge(chargePoint));
            Assertions.assertEquals(exception.getMessage(), InvalidAmountException.INSUFFICIENT_CHARGE_AMOUNT);
        }

        @Test
        void 최대_보유_포인트를_초과하여_충전할경우_오류가_발생한다(){
            //given
            Integer defaultPoint = 99_000_000;
            UserPoint userPoint = UserPoint.createOrDefault(defaultPoint);
            Integer chargePoint = 1_000_001;

            //when

            //then
            InvalidAmountException invalidAmountException = Assertions.assertThrows(InvalidAmountException.class,
                    () -> userPoint.charge(chargePoint));
            Assertions.assertEquals(invalidAmountException.getMessage(), InvalidAmountException.MAXIMUM_BALANCE_EXCEEDED);
        }

    }

    @Nested
    @DisplayName("금액 차감 정책 유효성 검증 테스트")
    class UserPointUpdateTest{

        @Test
        void 포인트를_차감한다(){
            //given
            Integer defaultPoint = 5000;
            Integer deductPoint = 1000;
            UserPoint userPoint = UserPoint.createOrDefault(defaultPoint);

            //when
            userPoint = userPoint.deduct(deductPoint);

            //then
            Assertions.assertEquals(defaultPoint-deductPoint, userPoint.point());
        }
    }

    @Test
    void 보유잔고를_초과하는_결제요청의경우_오류가_발생한다(){
        //given
        Integer defaultPoint = 5000;
        UserPoint userPoint = UserPoint.createOrDefault(defaultPoint);
        Integer deductPoint = 10000;

        //when

        //then
        InvalidAmountException invalidAmountException = Assertions.assertThrows(InvalidAmountException.class,
                () -> userPoint.deduct(deductPoint));
        Assertions.assertEquals(invalidAmountException.getMessage(), InvalidAmountException.INSUFFICIENT_BALANCE);
    }

}
