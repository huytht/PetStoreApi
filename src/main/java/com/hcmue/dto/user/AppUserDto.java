package com.hcmue.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {
	private Long userId;
	private String username;
	private String email;
	private String token;
	private String firstName;
	private String lastName;
	private String avatarImg;
	private String story;
}
