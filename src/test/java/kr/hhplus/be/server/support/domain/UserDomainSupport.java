package kr.hhplus.be.server.support.domain;

import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.Point;
import kr.hhplus.be.server.domain.user.User;

import java.math.BigDecimal;

public class UserDomainSupport {

    public static User 기본_사용자_생성() {
        User user = User.createWithName("사용자");
        return user;
    }

    public static Balance 사용자_잔고_생성(Long userId, BigDecimal balanceAmount) {
        return Balance.create(userId, Point.of(balanceAmount));
    }

}
