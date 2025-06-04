package kr.hhplus.be.server.common.log;

public class LogContext {

    private String traceId;
    private String userId;
    private String requestId;

    // 생성자, getter, setter
    public LogContext() {}

    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    @Override
    public String toString() {
        return String.format("[traceId=%s userId=%s requestId=%s]", traceId, userId, requestId);
    }
}
