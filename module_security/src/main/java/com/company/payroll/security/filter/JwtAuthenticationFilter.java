package com.company.payroll.security.filter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.company.payroll.security.service.JwtUserDetailsService;
import com.company.payroll.security.service.JwtUserVerificationService;
import com.company.payroll.user.cron.JwkCacheService;
import com.company.payroll.user.dto.JwkKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String CLASS_NAME = "[JwtAuthenticationFilter]";

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwkCacheService jwkCacheService;

    public JwtAuthenticationFilter(JwtUserDetailsService jwtUserDetailsService, JwkCacheService jwkCacheService) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwkCacheService = jwkCacheService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("{} started.", CLASS_NAME);
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                JwtUserVerificationService service = new JwtUserVerificationService();

                List<JwkKey> jwkKeys = jwkCacheService.getCachedJwkUri();

                Optional<Jws<Claims>> jwsClaims = Optional.empty();
                if (jwkKeys != null) {
                    jwsClaims = service.verifyToken(token, jwkKeys);
                }

                if (jwsClaims.isPresent()) {
                    Claims claims = jwsClaims.get().getPayload();
                    String subject = claims.getSubject();
                    List<String> roles = claims.get("roles", List.class);

                    if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        // Use the new service to build a UserDetails object from the JWT claims
                        UserDetails userDetails = jwtUserDetailsService.buildUser(subject, roles);

                        // Proceed with setting the authentication in the SecurityContextHolder
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } else {
                    log.warn("{} token not valid or expired", CLASS_NAME);
                    throw new BadCredentialsException("Invalid or expired JWT token provided.");
                }
            } catch (Exception e) {
                // Log and handle invalid or expired tokens
                log.error("{} JWT authentication failed={}", CLASS_NAME, e.getMessage());
            }
        }

        log.info("{} end.", CLASS_NAME);
        filterChain.doFilter(request, response);
    }
}
