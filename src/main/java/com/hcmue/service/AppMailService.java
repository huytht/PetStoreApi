package com.hcmue.service;

import com.hcmue.entity.AppUser;

public interface AppMailService {

	void sendMailVerify(AppUser appUser);
	
	void sendResetPassword(String email, String newPassword);
}
