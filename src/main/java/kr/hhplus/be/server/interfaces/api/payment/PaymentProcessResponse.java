package kr.hhplus.be.server.interfaces.api.payment;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class PaymentProcessResponse {

    @Schema(name = "paymentId", description = "결제 고유 번호", example = "938")
    private Long paymentId;

    @Schema(name = "paymentResponseMessage", description = "결제 응답 메세지", example = "SUCCESS")
    private String paymentResponseMessage;

    @Schema(name = "paymentRequestDateTime", description = "결제 요청 시간", example = "2025-01-01H00:00:00")
    private LocalDateTime paymentRequestDateTime;

    @Schema(name = "paymentResponseDateTime", description = "결제 응답 시간", example = "2025-01-01H00:01:00")
    private LocalDateTime paymentResponseDateTime;

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentResponseMessage() {
        return paymentResponseMessage;
    }

    public void setPaymentResponseMessage(String paymentResponseMessage) {
        this.paymentResponseMessage = paymentResponseMessage;
    }

    public LocalDateTime getPaymentRequestDateTime() {
        return paymentRequestDateTime;
    }

    public void setPaymentRequestDateTime(LocalDateTime paymentRequestDateTime) {
        this.paymentRequestDateTime = paymentRequestDateTime;
    }

    public LocalDateTime getPaymentResponseDateTime() {
        return paymentResponseDateTime;
    }

    public void setPaymentResponseDateTime(LocalDateTime paymentResponseDateTime) {
        this.paymentResponseDateTime = paymentResponseDateTime;
    }
}
