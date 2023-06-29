package com.nucSoft.web.spring.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private int id;
	  
	  @Column(name ="name",nullable=false)
	  private String name;

	  @Column(name ="username",nullable=false)
	  private String username;

	  @Column(name ="email",nullable=false)
	  private String email;

	  @Column(name ="password")
	  private String password;
	  
	  @Column(name ="contact_no")
	  private Long contact_no;
	  
      @Column(name = "login_time")
	  private LocalDateTime loginTime;

	  @Column(name = "logout_time")
	  private LocalDateTime logoutTime;

	  @ManyToMany(fetch = FetchType.LAZY)
	  @JoinTable(name = "user_roles", 
	             joinColumns = @JoinColumn(name = "user_id"),
	             inverseJoinColumns = @JoinColumn(name = "role_id"))
	  private Set<Role> roles = new HashSet<>();


	public User() {
		
	}

	public User(String name, String username, String email,
			 String password, Long contact_no) {
		
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
		this.contact_no = contact_no;
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public int getId() {
		return id;
	}

	   public LocalDateTime getLoginTime() {
	        return loginTime;
	    }

	    public void setLoginTime(LocalDateTime loginTime) {
	        this.loginTime = loginTime;
	    }

	    public LocalDateTime getLogoutTime() {
	        return logoutTime;
	    }

	    public void setLogoutTime(LocalDateTime logoutTime) {
	        this.logoutTime = logoutTime;
	    }
}
