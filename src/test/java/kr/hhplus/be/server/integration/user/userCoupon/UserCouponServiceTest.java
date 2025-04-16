package kr.hhplus.be.server.integration.user.userCoupon;

import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.domain.coupon.*;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.user.userCoupon.UserCoupon;
import kr.hhplus.be.server.domain.user.userCoupon.UserCouponCriteria;
import kr.hhplus.be.server.domain.user.userCoupon.UserCouponInfo;
import kr.hhplus.be.server.domain.user.userCoupon.UserCouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class UserCouponServiceTest {

    @Autowired
    UserCouponService userCouponService;

    @Autowired
    CouponFacade couponFacade;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    UserRepository userRepository;


    Coupon coupon;
    User user;
    Long userCouponId;

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
        UserCouponCriteria.Issue criteria = new UserCouponCriteria.Issue(coupon.getId(), user.getId());
        UserCouponInfo.Issue issue = couponFacade.issue(criteria);
        userCouponId = issue.getUserCouponId();
    }

    @Test
    @Transactional
    void 발급된_쿠폰을_조회한다(){
        //when
        UserCoupon userCoupon = userCouponService.findUserCouponById(userCouponId);

        //then
        assertEquals(user, userCoupon.getUser());
        assertEquals(coupon, userCoupon.getCoupon());
    }


}
