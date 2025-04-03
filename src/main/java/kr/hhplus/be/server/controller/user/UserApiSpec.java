package kr.hhplus.be.server.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "User", description = "사용자 상세 정보 조회, 사용자 포인트 잔액 조회, 사용자 보유 쿠폰 조회")
public interface UserApiSpec {

    @Operation(summary = "사용자 상세 정보 조회",
            description = "사용자 고유 번호(userId)에 해당하는 사용자의 상세 정보를 조회합니다.")
    ResponseEntity<UserResponse> findById(Long userId);

    @Operation(summary = "사용자 포인트 잔액 조회",
            description = "사용자 고유 번호(userId)에 해당하는 사용자의 포인트 잔액을 조회합니다.")
    ResponseEntity<UserPointResponse> findUserPointById(Long userId);

    @Operation(summary = "사용자 보유 쿠폰 목록 조회",
            description = "사용자 고유 번호(userId)에 해당하는 사용자의 보유 쿠폰(사용, 미사용 일괄) 목록을 조회합니다.")
    ResponseEntity<UserCouponResponse> findUserCouponListByUserId(Long userId);

    @Operation(summary = "사용자 포인트 충전",
    description = "사용자 고유 번호(userId)에 해당하는 사용자의 포인트 잔고를 충전합니다.")
    ResponseEntity<UserPointResponse> chargePoint(Long userId, Integer chargePoint);
}
