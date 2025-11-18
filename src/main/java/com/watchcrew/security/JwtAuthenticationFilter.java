package com.watchcrew.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  
  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer ";
  
  private final JwtTokenUtil jwtTokenUtil;
  private final CustomUserDetailsService userDetailsService;
  
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    
    final String requestTokenHeader = request.getHeader(AUTHORIZATION_HEADER);
    
    log.debug("Request URI: {}, Authorization Header: {}", request.getRequestURI(), 
        requestTokenHeader != null ? "Present" : "Missing");
    
    String username = null;
    String jwtToken = null;
    
    if (requestTokenHeader != null && requestTokenHeader.startsWith(BEARER_PREFIX)) {
      jwtToken = requestTokenHeader.substring(7);
      try {
        username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        log.debug("JWT token extracted for user: {}", username);
      } catch (Exception e) {
        log.error("JWT Token validation error: {}", e.getMessage());
      }
    } else if (requestTokenHeader != null) {
      log.warn("Authorization header does not start with Bearer: {}", requestTokenHeader.substring(0, Math.min(20, requestTokenHeader.length())));
    }
    
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      
      if (Boolean.TRUE.equals(jwtTokenUtil.validateToken(jwtToken, userDetails))) {
        log.debug("JWT token validated successfully for user: {}", username);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      } else {
        log.warn("JWT token validation failed for user: {}", username);
      }
    }
    chain.doFilter(request, response);
  }
}
