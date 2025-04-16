package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.userCoupon.UserCoupon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CouponTemplateTest {

    @Test
    @DisplayName("쿠폰 템플릿을 생성한다.")
    void create_coupon_template() {
        CouponTemplate couponTemplate = CouponTemplate.builder()
                .build();

        assertNotNull(couponTemplate);
    }

    @Test
    @DisplayName("쿠폰 템플릿을 파라미터로하는 생성자")
    void coupon_template_create_coupon(){
        //given
        String couponName = "FAKE COUPON";
        CouponTemplate couponTemplate = CouponTemplate.builder()
                .name(couponName)
                .build();

        //when
        Coupon coupon = new FakeCoupon(couponTemplate);

        //then
        assertEquals(couponName, coupon.getName());
    }


    class FakeCoupon extends Coupon{

        FakeCoupon(CouponTemplate couponTemplate) {
            super(couponTemplate);
        }

        @Override
        boolean validate(LocalDateTime targetDateTime) {
            return false;
        }

        @Override
        public UserCoupon issue(User user) {
            return null;
        }

        @Override
        public Integer discount(Integer integer) {
            return 0;
        }
    }
}
