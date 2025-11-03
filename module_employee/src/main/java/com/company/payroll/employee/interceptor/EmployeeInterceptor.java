package com.company.payroll.employee.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class EmployeeInterceptor implements HandlerInterceptor {
    private static final String CLASS_NAME = "[EmployeeInterceptor]";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String functionName = Thread.currentThread().getStackTrace()[2].getMethodName();
        log.info("{} {} executed.", CLASS_NAME, functionName);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        final String functionName = Thread.currentThread().getStackTrace()[2].getMethodName();
        log.info("{} {} executed.", CLASS_NAME, functionName);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        final String functionName = Thread.currentThread().getStackTrace()[2].getMethodName();
        log.info("{} {} executed.", CLASS_NAME, functionName);
    }
}
