package com.nucSoft.web.spring.payload.request;

public class JwtResponse {

	private final String token;

    public JwtResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
	
	
}
