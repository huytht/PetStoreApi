package com.hcmue.dto.userinfo;

import com.hcmue.entity.UserInfo;
import com.hcmue.util.AppUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoForAdminDtoRes {

	private String firstName;

	private String lastName;

	private String avatarImg;
	
	public static UserInfoForAdminDtoRes CreateFromEntity(UserInfo src) {
		UserInfoForAdminDtoRes dest = new UserInfoForAdminDtoRes();

		dest.firstName = src.getFirstName();
		dest.lastName = src.getLastName();
		dest.avatarImg = AppUtils.createLinkOnCurrentHttpServletRequest(src.getAvatarImg());

		return dest;
	}
}
