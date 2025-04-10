package kr.hhplus.be.server.domain.userCoupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.user.User;

import java.time.LocalDateTime;

public class UserCoupon {

    private Long id;
    private User user;
    private Coupon coupon;
    private LocalDateTime issueDateTime;
    private boolean active;

    public UserCoupon(User user, Coupon coupon) {
        this.user = user;
        this.coupon = coupon;
        this.issueDateTime = LocalDateTime.now();
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getIssueDateTime() {
        return issueDateTime;
    }

    public void updateActive(boolean active) {
        this.active = active;
    }

    public Integer discount(Integer price) {
        return coupon.discount(price);
    }
}
