package com.nucSoft.web.spring.payload.response;

import java.util.List;

public class UserInfoResponse {

	private int id;
	private String name;
	private String username;
	private String email;
	private Long contact_no;
	private List<String> roles;

	public UserInfoResponse(int id,String name, String username, String email,Long contact_no, List<String> roles) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.contact_no = contact_no; 
		this.email = email;
		this.roles = roles;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
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

	public Long getContact_no() {
		return contact_no;
	}

	public void setContact_no(Long contact_no) {
		this.contact_no = contact_no;
	}

	public List<String> getRoles() {
		return roles;
	}
	
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
