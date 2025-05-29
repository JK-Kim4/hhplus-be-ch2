package kr.hhplus.be.server.support;

import kr.hhplus.be.server.domain.balance.BalanceRepository;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class TestRepositorySupport {

    @Autowired protected UserRepository userRepository;
    @Autowired protected BalanceRepository balanceRepository;
    @Autowired protected CouponRepository couponRepository;
    @Autowired protected RedissonClient redissonClient;
}
