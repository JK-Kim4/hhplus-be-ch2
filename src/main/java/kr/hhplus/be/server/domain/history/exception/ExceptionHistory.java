package kr.hhplus.be.server.domain.history.exception;

import lombok.*;

import java.time.LocalDateTime;

//TODO History Type
@EqualsAndHashCode
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExceptionHistory {

    private String method;
    private String serializedParameter;
    private LocalDateTime requestDateTime;
    private boolean retryable;

    public static ExceptionHistory of(String method, String serializedParameter, LocalDateTime requestDateTime, boolean retryable) {
        return new ExceptionHistory(method, serializedParameter, requestDateTime, retryable);
    }

    @Builder
    private ExceptionHistory(String method, String serializedParameter, LocalDateTime requestDateTime, boolean retryable) {
        this.method = method;
        this.serializedParameter = serializedParameter;
        this.requestDateTime = requestDateTime;
        this.retryable = retryable;
    }
}
