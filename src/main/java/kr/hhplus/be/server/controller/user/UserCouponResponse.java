package kr.hhplus.be.server.controller.user;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserCouponResponse {

    @Schema(name = "userId", description = "사용자 고유번호", example = "1")
    private Long userId;

    @Schema(name = "userCouponId", description = "사용자 쿠폰 고유번호", example = "9")
    private Long userCouponId;

    @Schema(name = "isUsed", description = "쿠폰 사용 여부", example = "false")
    private Boolean isUsed;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserCouponId() {
        return userCouponId;
    }

    public void setUserCouponId(Long userCouponId) {
        this.userCouponId = userCouponId;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }
}
