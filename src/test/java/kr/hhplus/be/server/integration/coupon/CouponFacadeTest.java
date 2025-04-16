package kr.hhplus.be.server.integration.coupon;

import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.domain.coupon.*;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.user.userCoupon.UserCouponCriteria;
import kr.hhplus.be.server.domain.user.userCoupon.UserCouponInfo;
import kr.hhplus.be.server.domain.user.userCoupon.UserCouponRepository;
import kr.hhplus.be.server.domain.user.userCoupon.UserCoupons;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
public class CouponFacadeTest {

    @Autowired
    CouponFacade couponFacade;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserCouponRepository userCouponRepository;

    Coupon coupon;
    User user;

    @BeforeEach
    void setUp() {
        coupon = new FlatDiscountCoupon(CouponTemplate
                .builder()
                .expireDateTime(LocalDateTime.now().plusDays(10))
                .remainingQuantity(100)
                .name("test")
                .couponType(CouponType.FLAT)
                .build());
        user = new User("tester");
        couponRepository.save(coupon);
        userRepository.save(user);

    }

    @Test
    @Transactional
    void 사용가능한_쿠폰을_발급한다(){
        //given
        UserCouponCriteria.Issue criteria = new UserCouponCriteria.Issue(coupon.getId(), user.getId());

        //when
        UserCouponInfo.Issue issue = couponFacade.issue(criteria);
        userCouponRepository.findById(issue.getUserCouponId()).get();
        User user = userRepository.findById(issue.getUserId()).get();
        UserCoupons userCoupons = user.getUserCoupons();

        //then
        assertTrue(userCoupons.isAlreadyIssuedCoupon(coupon));
    }
}