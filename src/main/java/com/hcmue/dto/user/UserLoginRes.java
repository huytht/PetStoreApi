package com.hcmue.dto.user;

import com.hcmue.entity.RefreshToken;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRes {
	private Long userId;
	private String username;
	private String avatarImg;
	private String token;
	private String refreshToken;
}
