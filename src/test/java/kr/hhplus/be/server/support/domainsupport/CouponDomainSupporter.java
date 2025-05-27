package kr.hhplus.be.server.support.domainsupport;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.DiscountPolicy;
import kr.hhplus.be.server.domain.coupon.UserCoupon;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CouponDomainSupporter {

    public static Coupon 수량정보를_전달받아_유효_테스트쿠폰_생성(Integer quantity){
        return Coupon.create("test coupon", quantity, DiscountPolicy.FLAT, BigDecimal.valueOf(10000), LocalDate.now().plusDays(7));
    }

    public static Coupon 기간만료_쿠폰_생성(){
        return Coupon.create("expried coupon", 1, DiscountPolicy.FLAT, BigDecimal.valueOf(10000), LocalDate.now().minusDays(7));
    }

    public static UserCoupon 쿠폰정보를_전달받아_사용자쿠폰_생성(Coupon coupon){
        return UserCoupon.issue(coupon, 1L);

    }
}
