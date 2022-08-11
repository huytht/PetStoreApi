package com.hcmue.dto.user;

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
public class UserInfoDto {
	  private Long userId;
	  private String username;
	  private String firstName;
	  private String lastName;
	  private String email;
	  private String phone;
	  private String avatarImg;

	public static UserInfoDto CreateFromEntity(UserInfo src) {
		UserInfoDto dest = new UserInfoDto();

		dest.userId = src.getAppUser().getId();
		dest.username = src.getAppUser().getUsername();
		dest.firstName = src.getFirstName();
		dest.lastName = src.getLastName();
		dest.phone = src.getPhone();
		dest.avatarImg = AppUtils.createLinkOnCurrentHttpServletRequest(src.getAvatarImg());
		dest.email = src.getAppUser().getEmail();

		return dest;
	}
}
