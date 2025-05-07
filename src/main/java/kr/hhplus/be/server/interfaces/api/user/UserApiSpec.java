package kr.hhplus.be.server.interfaces.api.user;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "User", description = "사용자 정보 관리")
public interface UserApiSpec {

    @Operation(summary = "신규 사용자 생성",
            description = "사용자 이름을 전달받아 신규 사용자를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 생성 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.Create.class)))
    })
    ResponseEntity<UserResponse.Create> create(UserRequest.Create request);
}
