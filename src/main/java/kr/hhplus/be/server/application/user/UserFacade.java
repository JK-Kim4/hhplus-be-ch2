package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.domain.balance.BalanceInfo;
import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.coupon.CouponInfo;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.user.UserCommand;
import kr.hhplus.be.server.domain.user.UserInfo;
import kr.hhplus.be.server.domain.user.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class UserFacade {

    private final UserService userService;
    private final BalanceService balanceService;
    private final CouponService couponService;

    public UserFacade(
            UserService userService,
            BalanceService balanceService,
            CouponService couponService){
        this.userService = userService;
        this.balanceService = balanceService;
        this.couponService = couponService;
    }

    public UserResult.Create create(UserCriteria.Create criteria){
        userService.findByName(criteria.getUsername()).isPresentThenThrow();

        UserInfo.Create user =
                userService.create(UserCommand.Create.builder().name(criteria.getUsername()).build());

        BalanceInfo.Create balance =
                balanceService.create(criteria.toBalanceCreateCommand(user.getUserId()));

        return UserResult.Create.of(
                user.getUserId(),
                user.getName(),
                balance.getPoint()
        );
    }

    public UserResult.Coupons findCouponsByUserId(Long userId) {
        CouponInfo.Coupons coupons = couponService.findCouponsByUserId(userId);

        return UserResult.Coupons.from(coupons.getCoupons());
    }
}
