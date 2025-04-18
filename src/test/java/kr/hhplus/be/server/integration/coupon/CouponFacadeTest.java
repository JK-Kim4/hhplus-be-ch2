package kr.hhplus.be.server.integration.coupon;

import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.FlatDiscountCoupon;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.user.userCoupon.UserCouponCriteria;
import kr.hhplus.be.server.domain.user.userCoupon.UserCouponInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
public class CouponFacadeTest {

    @Autowired
    CouponFacade couponFacade;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    UserRepository userRepository;

    Long couponId;
    Long userId;

    @BeforeEach
    void setUp() {
        Coupon coupon = new Coupon("test", CouponType.FLAT, 10,
                LocalDate.of(2025, 12, 31), LocalDate.of(2025,1,1));
        new FlatDiscountCoupon(coupon, 50_000);
        couponRepository.save(coupon);
        couponId = coupon.getId();

        User user = User.createWithName("Tester");
        userRepository.save(user);
        userId = user.getId();
    }


    @Test
    void 사용자쿠폰을_발급한다(){
        //given
        UserCouponCriteria.Issue criteria = new UserCouponCriteria.Issue(couponId, userId);

        //when
        UserCouponInfo.Issue issue = couponFacade.issue(criteria);
        Coupon coupon = couponRepository.findById(couponId).get();

        //then
        assertNotNull(issue);
        assertEquals(couponId, issue.getCouponId());
        assertEquals(userId, issue.getUserId());
        assertEquals(9, coupon.getQuantity());
    }
}
