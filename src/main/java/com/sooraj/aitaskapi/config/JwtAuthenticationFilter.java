package com.sooraj.aitaskapi.config;

import com.sooraj.aitaskapi.entity.User;
import com.sooraj.aitaskapi.repository.UserRepository;
import com.sooraj.aitaskapi.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private static final Logger log =
            LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authToken = authHeader.substring(7);

        try {
            String email = jwtService.extractEmail(authToken);

            log.info(
                    "JWT filter: method={}, uri={}, extractedEmail={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    email
            );

            if(email != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

                User user = userRepository
                            .findByEmail(email)
                            .orElse(null);

                if(user != null &&
                        jwtService.isTokenValid(authToken,email)) {

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            java.util.Collections.emptyList()
                    );

                    authentication.setDetails(new WebAuthenticationDetailsSource()
                            .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);

                    log.info(
                            "JWT authentication successful for email={} on {}",
                            email,
                            request.getRequestURI()
                    );


                }
            }
        } catch (Exception ex) {
            log.error(
                    "JWT filter failed: method={}, uri={}, error={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    ex.getMessage(),
                    ex
            );
        }

        filterChain.doFilter(request, response);
    }
}
