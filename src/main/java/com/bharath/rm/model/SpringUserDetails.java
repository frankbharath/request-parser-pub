package com.bharath.rm.model;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jun 29, 2020 7:53:08 PM
 	* Class Description
*/
public class SpringUserDetails implements UserDetails {
	
	private String userName;
	private String password;
	private long userId;

	/**
	 * @param userName
	 */
	public SpringUserDetails(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userName;
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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "SpringUserDetails [userName=" + userName + ", password=" + password + ", userId=" + userId + "]";
	}

}
