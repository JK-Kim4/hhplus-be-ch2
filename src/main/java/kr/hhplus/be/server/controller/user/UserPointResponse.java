package kr.hhplus.be.server.controller.user;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserPointResponse {

    @Schema(name = "userId", description = "사용자 고유번호", example = "10")
    private Long userId;

    @Schema(name = "point", description = "현재 포인트 잔액", example = "20000")
    private Integer point;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}
