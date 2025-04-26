package kr.hhplus.be.server.interfaces.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;

public class ErrorResponse {

    @Schema(name = "code", description = "에러 코드", example = "001")
    private String code;
    @Schema(name = "message", description = "오류 메세지", example = "NPE")
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
