package com.nucSoft.web.spring.security.jwt;


import java.security.Key;
import java.util.Date;
import java.util.function.Function;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.nucSoft.web.spring.security.services.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtUtils {
	
	 private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	    @Value("${nucSoft.app.jwtSecret}")
	    private String jwtSecret;

	    @Value("${nucSoft.app.jwtExpirationMs}")
	    private int jwtExpirationMs;

	    public String extractUsername(String token) {
	        return extractClaim(token, Claims::getSubject);
	    }

	    public Date extractExpiration(String token) {
	        return extractClaim(token, Claims::getExpiration);
	    }

	    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = extractAllClaims(token);
	        return claimsResolver.apply(claims);
	    }

	    private Claims extractAllClaims(String token) {
	        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
	    }

	    public String generateToken(Authentication authentication) {
	        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
	        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	        return Jwts.builder()
	                .setSubject(userPrincipal.getUsername())
	                .setIssuedAt(new Date())
	                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
	                .signWith(key, SignatureAlgorithm.HS256)
	                .compact();
	    }

	    public String getUserNameFromJwtToken(String token) {
	        return Jwts.parserBuilder().setSigningKey(key()).build()
	                .parseClaimsJws(token).getBody().getSubject();
	    }
	    
	    public Key key() {
	        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
	    }

	    public boolean validateJwtToken(String authToken) {
	        try {
	            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
	            return true;
	        } catch (Exception e) {
	            logger.error("Invalid JWT token: {}", e.getMessage());
	        }
	        return false;
	    }


	
}
