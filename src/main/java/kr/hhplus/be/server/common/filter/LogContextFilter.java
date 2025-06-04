package kr.hhplus.be.server.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.hhplus.be.server.common.log.LogContext;
import kr.hhplus.be.server.common.log.LogContextHolder;
import kr.hhplus.be.server.common.utility.MDCUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Order(1)
@Component
public class LogContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            LogContext context = new LogContext();
            context.setTraceId(UUID.randomUUID().toString());
            LogContextHolder.setContext(context);

            MDCUtil.put(context);

            filterChain.doFilter(request, response);
        } finally {
            LogContextHolder.clear();
            MDCUtil.clear();
        }
    }
}
