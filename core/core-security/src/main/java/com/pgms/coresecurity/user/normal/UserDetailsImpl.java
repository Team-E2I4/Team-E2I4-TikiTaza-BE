package com.pgms.coresecurity.user.normal;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import net.minidev.json.annotate.JsonIgnore;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDetailsImpl implements UserDetails {

	private Long id;
	private String email;
	private String nickname;
	@JsonIgnore
	private String password;
	private Collection<? extends GrantedAuthority> authorities;

	@Builder
	public UserDetailsImpl(
		Long id,
		String email,
		String nickname,
		String password,
		Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.email = email;
		this.nickname = nickname;
		this.password = password;
		this.authorities = authorities;
	}

	public String getAuthority() {
		return authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
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
}
