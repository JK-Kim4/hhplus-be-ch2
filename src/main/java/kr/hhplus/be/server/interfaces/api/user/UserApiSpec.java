package kr.hhplus.be.server.interfaces.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "User", description = "사용자 상세 정보 조회, 사용자 포인트 잔액 조회, 사용자 보유 쿠폰 조회")
public interface UserApiSpec {

    @Operation(summary = "사용자 포인트 잔액 조회",
            description = "사용자 고유 번호(userId)에 해당하는 사용자의 포인트 잔액을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 포인트 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.Point.class)))
    })
    ResponseEntity<UserResponse.Point> findUserPointById(Long userId);

    @Operation(summary = "사용자 포인트 충전",
    description = "사용자 고유 번호(userId)에 해당하는 사용자의 포인트 잔고를 충전합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 포인트 충전 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    })
    ResponseEntity<Void> chargePoint(Long userId, UserRequest.Charge point);
}
