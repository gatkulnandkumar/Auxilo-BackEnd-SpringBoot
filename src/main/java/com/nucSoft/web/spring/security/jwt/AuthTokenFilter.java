package com.nucSoft.web.spring.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nucSoft.web.spring.security.services.CustomUserDetailService;


@Component
public class AuthTokenFilter extends OncePerRequestFilter {
	  @Autowired
	  private JwtUtils jwtUtils;

	  @Autowired
	  private CustomUserDetailService userDetailsService;

	  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);


	  @Override
	  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	      throws ServletException, IOException {
	    try {
	      String jwt = parseJwt(request);
	      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
	        String username = jwtUtils.getUserNameFromJwtToken(jwt);

	        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

	        UsernamePasswordAuthenticationToken authentication =
	            new UsernamePasswordAuthenticationToken(userDetails,
	                                                    null,
	                                                    userDetails.getAuthorities());

	        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

	        SecurityContextHolder.getContext().setAuthentication(authentication);
	      }
	    } catch (Exception e) {
	      logger.error("Cannot set user authentication: {}", e);
	    }

	    filterChain.doFilter(request, response);
	  }

	  private String parseJwt(HttpServletRequest request) {
	    String authorizationHeader = request.getHeader("Authorization");

	    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
	      return authorizationHeader.substring(7); // Extract JWT token without "Bearer " prefix
	    }

	    return null;
	  }
	}
