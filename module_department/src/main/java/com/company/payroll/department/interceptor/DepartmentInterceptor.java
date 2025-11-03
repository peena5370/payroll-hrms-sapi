package com.company.payroll.department.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class DepartmentInterceptor implements HandlerInterceptor {
    private static final String CLASS_NAME = "[DepartmentInterceptor]";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String functionName = Thread.currentThread().getStackTrace()[2].getMethodName();
        log.info("{} {} executed.", CLASS_NAME, functionName);
        // check rights here, return true if allow access the api, return false if the user not allow to access the api
        String authorizationHeader = request.getHeader("Authorization");

        log.info("{} {}  token header: {}", CLASS_NAME, functionName, authorizationHeader);

//        if(authorizationHeader != null) {
//            String token = authorizationHeader.substring(7);
//            Claims claims = jwtTokenUtil.getClaims(token);
//
//            if (claims != null) {
//                Long userId = claims.get("uid", Long.class);
//
//                // @TODO temporary use for validate userId, will implement rights checking after this
//                return Long.valueOf(39259111069716480L).equals(userId);
//            }
//        }

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
