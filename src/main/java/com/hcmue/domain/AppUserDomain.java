package com.hcmue.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hcmue.entity.AppUser;

public class AppUserDomain implements UserDetails {

	private static final long serialVersionUID = 1L;

	private AppUser appUser;

	public AppUserDomain() {
		super();
	}

	public AppUserDomain(AppUser appUser) {
		super();
		this.appUser = appUser;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();

		if (appUser.getAppRoles() != null)
			appUser.getAppRoles().forEach(item -> {
				grantedAuthorityList.add(new SimpleGrantedAuthority(item.getName()));
			});

		return grantedAuthorityList;
	}

	@Override
	public String getPassword() {
		return this.appUser.getPassword();
	}

	@Override
	public String getUsername() {
		return this.appUser.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.appUser.getAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.appUser.getEnabled();
	}

	public String getEmail() {
		return this.appUser.getEmail();
	}

	public Long getUserId() {
		return this.appUser.getId();
	}
	
	public String getAvatar() {
		return this.appUser.getUserInfo().getAvatarImg().equalsIgnoreCase("https://robohash.org/" + this.appUser.getUsername()) 
				? this.appUser.getUserInfo().getAvatarImg() 
				: "http://localhost8081/api" + this.appUser.getUserInfo().getAvatarImg();
	}

}
