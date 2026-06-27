package com.abs.app.infrastructure.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI().toLowerCase();
        boolean skip = path.startsWith("/api/auth")
                || path.startsWith("/api/swagger-ui")
                || path.startsWith("/api/v3/api-docs")
                || path.startsWith("/api/swagger-resources")
                || path.startsWith("/api/webjars");

        log.debug("shouldNotFilter? path='{}' -> {}", path, skip);
        return skip;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI().toLowerCase();
        log.debug("doFilterInternal start for path='{}'", path);

        // extra safety
        if (path.startsWith("/api/auth")
                || path.startsWith("/api/swagger-ui")
                || path.startsWith("/api/v3/api-docs")) {
            log.debug("Skipping JWT processing for swagger/auth path '{}'", path);
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveToken(request);

        // NO TOKEN -> allow through
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (jwtTokenProvider.validateToken(token)) {
                String userId = jwtTokenProvider.getUserId(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.debug("Authentication set for userId='{}'", userId);
            } else {
                log.debug("Invalid/expired token");
            }
        } catch (Exception e) {
            // log full stacktrace (safer than System.out.println)
            log.error("JWT error while processing request to '{}': {}", path, e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        log.trace("Authorization header: {}", authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if ("accessToken".equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isBlank()) {
                return cookie.getValue();
            }
        }

        return null;
    }
}