 package com.hcmue.service;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import com.hcmue.domain.AppBaseResult;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.user.AppUserForAdminDto;
import com.hcmue.dto.user.ChangePassword;
import com.hcmue.dto.user.UserRegister;
import com.hcmue.dto.user.UserStatus;
import com.hcmue.dto.userinfo.UserInfoDtoReq;
import com.hcmue.dto.userinfo.UserInfoDtoRes;
import com.hcmue.provider.file.UnsupportedFileTypeException;

public interface AppUserService {
	
	AppServiceResult<List<AppUserForAdminDto>> getUsers();
	
	AppBaseResult register(UserRegister userRegister);

	AppBaseResult verifyEmail(UUID token);
	
	AppServiceResult<UserInfoDtoRes> getProfile(Long userId);
	
	AppBaseResult saveProfile(UserInfoDtoReq userInfo);
	
	AppBaseResult changePassword(ChangePassword changePassword);
	
	AppBaseResult resetPassword(String email);
	
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
	
	AppServiceResult<String> uploadImage(MultipartFile file) throws UnsupportedFileTypeException;
	
	AppBaseResult updateActive(UserStatus userStatus);
}