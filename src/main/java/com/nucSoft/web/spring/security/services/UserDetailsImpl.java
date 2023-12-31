package com.nucSoft.web.spring.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nucSoft.web.spring.model.User;

public class UserDetailsImpl implements UserDetails {
	  private static final long serialVersionUID = 1L;

	  private int id;
	  
	  private String name;

	  private String username;

	  private String email;

	  @JsonIgnore
	  private String password;
	  
	  private Long contact_no;
	  

	  private Collection<? extends GrantedAuthority> authorities;

	  public UserDetailsImpl(int id,String name, String username, String email, String password,
			  Long contact_no,
	      Collection<? extends GrantedAuthority> authorities) {
	    this.id = id;
	    this.name = name;
	    this.username = username;
	    this.email = email;
	    this.password = password;
	    this.contact_no = contact_no;
	    this.authorities = authorities;
	  }
	  
	  public static UserDetailsImpl build(User user) {
		    List<GrantedAuthority> authorities = user.getRoles().stream()
		        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
		        .collect(Collectors.toList());

		    return new UserDetailsImpl(
		        user.getId(), 
		        user.getName(),
		        user.getUsername(), 
		        user.getEmail(),
		        user.getPassword(),
		        user.getContact_no(),
		        authorities);
	  }

	  @Override
	  public Collection<? extends GrantedAuthority> getAuthorities() {
	    return authorities;
	  }

	  public int getId() {
	    return id;
	  }

	  public String getName() {
		return name;
	}
	  
	  @Override
	  public String getUsername() {
	    return username;
	  }

	public String getEmail() {
	    return email;
	  }

	  @Override
	  public String getPassword() {
	    return password;
	  }
	  
	  public Long getContact_no() {
			return contact_no;
		}

	 

	  @Override
	  public boolean isAccountNonExpired() {
	    return true;
	  }

	  @Override
	  public boolean isAccountNonLocked() {
	    return true;
	  }

	  @Override
	  public boolean isCredentialsNonExpired() {
	    return true;
	  }

	  @Override
	  public boolean isEnabled() {
	    return true;
	  }

	  @Override
	  public boolean equals(Object o) {
	    if (this == o)
	      return true;
	    if (o == null || getClass() != o.getClass())
	      return false;
	    UserDetailsImpl user = (UserDetailsImpl) o;
	    return Objects.equals(id, user.id);
	  }
	}
