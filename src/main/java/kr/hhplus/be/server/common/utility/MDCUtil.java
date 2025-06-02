package kr.hhplus.be.server.common.utility;

import kr.hhplus.be.server.common.log.LogContext;
import org.slf4j.MDC;

public class MDCUtil {

    public static void put(LogContext context) {
        if (context == null) return;
        MDC.put("traceId", context.getTraceId());
        MDC.put("userId", context.getUserId());
        MDC.put("requestId", context.getRequestId());
    }

    public static void clear() {
        MDC.clear();
    }

}
