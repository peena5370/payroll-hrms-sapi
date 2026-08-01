package com.company.payroll.logging.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import java.util.Optional;
import java.util.UUID;

@Component
public class AsyncLoggingInterceptor implements AsyncHandlerInterceptor {

    private static final String HEADER_TRACE_ID = "x-trace-id";
    private static final String HEADER_CONTEXT_ID = "x-context-id";

    public static final String MDC_TRACE_ID = "traceId";
    public static final String MDC_CONTEXT_ID = "contextId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String traceId = Optional.ofNullable(request.getHeader(HEADER_TRACE_ID))
                .filter(id -> !id.isBlank())
                .orElseGet(() -> UUID.randomUUID().toString());

        String contextId = Optional.ofNullable(request.getHeader(HEADER_CONTEXT_ID))
                .filter(id -> !id.isBlank())
                .orElse("DEFAULT-CONTEXT");

        MDC.put(MDC_TRACE_ID, traceId);
        MDC.put(MDC_CONTEXT_ID, contextId);

        return true;
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Called when standard Servlet thread releases the request to an async thread.
        // MDC is NOT cleared here because the request is still active asynchronously.
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable Exception ex) {
        // Always clean up MDC at the absolute end of the request execution
        MDC.clear();
    }
}