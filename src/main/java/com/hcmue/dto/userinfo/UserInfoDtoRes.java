package com.hcmue.dto.userinfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDtoRes {

	private Long userId;

	private String username;

	private String firstName;

	private String lastName;

	private String email;
	
	private String phone;

	private String avatarImg;
}
