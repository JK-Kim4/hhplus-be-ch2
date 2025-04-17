package kr.hhplus.be.server.integration.coupon;

import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.domain.couponv2.CouponType;
import kr.hhplus.be.server.domain.couponv2.CouponV2;
import kr.hhplus.be.server.domain.couponv2.CouponV2Repository;
import kr.hhplus.be.server.domain.couponv2.FlatDiscountCouponV2;
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
    CouponV2Repository couponV2Repository;

    @Autowired
    UserRepository userRepository;

    Long couponId;
    Long userId;

    @BeforeEach
    void setUp() {
        CouponV2 coupon = new CouponV2("test", CouponType.FLAT, 10,
                LocalDate.of(2025, 12, 31), LocalDate.of(2025,1,1));
        new FlatDiscountCouponV2(coupon, 50_000);
        couponV2Repository.save(coupon);
        couponId = coupon.getId();

        User user = new User("Tester");
        userRepository.save(user);
        userId = user.getId();
    }


    @Test
    void 사용자쿠폰을_발급한다(){
        //given
        UserCouponCriteria.Issue criteria = new UserCouponCriteria.Issue(couponId, userId);

        //when
        UserCouponInfo.Issue issue = couponFacade.issue(criteria);
        CouponV2 couponV2 = couponV2Repository.findById(couponId).get();

        //then
        assertNotNull(issue);
        assertEquals(couponId, issue.getCouponId());
        assertEquals(userId, issue.getUserId());
        assertEquals(9, couponV2.getQuantity());
    }
}
