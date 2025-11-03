package com.company.payroll.security.filter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import jakarta.servlet.http.HttpFilter;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServletSecurityFilter extends HttpFilter {
    private static final String CLASS_NAME = "[ServletSecurityFilter]";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
        log.info("{} initialized at={} ", CLASS_NAME, ZonedDateTime.now());
        log.info("{} filter name={} ", CLASS_NAME, filterConfig.getFilterName());
        log.info("{} servlet context={}", CLASS_NAME, filterConfig.getServletContext().getContextPath());

        super.init(filterConfig);
	}

	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
        final String functionName = Thread.currentThread().getStackTrace()[1].getMethodName();
        log.info("{} {} Remote address: [{}] accessed at: {}", CLASS_NAME, functionName, request.getRemoteAddr(), LocalDateTime.now());

		super.doFilter(request, response, chain);
	}
	
	@Override
	public void destroy() {
        log.info("{} destroyed at: {}", CLASS_NAME, LocalDateTime.now());
	}
}
