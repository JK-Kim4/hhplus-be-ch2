package kr.hhplus.be.server.controller.user;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserResponse {

    @Schema(name = "userId", description = "사용자 고유번호", example = "1")
    private Long userId;

    @Schema(name = "name", description = "사용자 이름", example = "홍길동")
    private String name;

    @Schema(name = "point", description = "포인트 잔액", example = "15000")
    private Integer point;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}
