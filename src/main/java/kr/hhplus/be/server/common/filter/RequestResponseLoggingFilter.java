package kr.hhplus.be.server.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Collectors;
@Slf4j
@Component
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {


        ContentCachingRequestWrapper wrappedRequest  =
                new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse =
                new ContentCachingResponseWrapper(response);

        long start = System.currentTimeMillis();
        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - start;

            String reqBody = new String(
                    wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8);

            log.info("[REQ] {} {} | headers={} | body={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    headersToString(wrappedRequest),
                    truncate(reqBody));

            String resBody = new String(
                    wrappedResponse.getContentAsByteArray(), StandardCharsets.UTF_8);

            log.info("[RES] {} {} | status={} | took {} ms | body={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration,
                    truncate(resBody));

            wrappedResponse.copyBodyToResponse();
        }
    }

    /* 로그 과다 방지용 헬퍼 */
    private static String truncate(String body) {
        int limit = 1_000;   // 1 KB 초과 시 잘라냄
        return body.length() > limit
                ? body.substring(0, limit) + "...(truncated)"
                : body;
    }

    /* Request header to string*/
    private static String headersToString(HttpServletRequest req) {
        return Collections.list(req.getHeaderNames())          // Enumeration → List
                .stream()
                .flatMap(name ->
                        Collections.list(req.getHeaders(name)) // 같은 이름의 다중 값 처리
                                .stream()
                                .map(value -> name + ": " + value))
                .collect(Collectors.joining(", "));
    }

}
