package com.nucSoft.web.spring.payload.request;


public class SignupRequest {
    
	private String name;
	
    private String username;
    
    private String email;
    
    private String password;
    
	private Long contact_no;
    
    private String role;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Long getContact_no() {
  		return contact_no;
  	}

  	public void setContact_no(Long contact_no) {
  		this.contact_no = contact_no;
  	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
    
   
}
