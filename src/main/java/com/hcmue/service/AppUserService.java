 package com.hcmue.service;

import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import com.hcmue.domain.AppBaseResult;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.dto.user.AppUserForAdminDto;
import com.hcmue.dto.user.ChangePassword;
import com.hcmue.dto.user.RemarkProduct;
import com.hcmue.dto.user.UserRegister;
import com.hcmue.dto.user.UserStatus;
import com.hcmue.dto.userinfo.UserInfoDtoReq;
import com.hcmue.dto.userinfo.UserInfoDtoRes;
import com.hcmue.entity.AppUser;
import com.hcmue.provider.file.UnsupportedFileTypeException;

public interface AppUserService {
	
	AppServiceResult<List<AppUserForAdminDto>> getUsers();
	
	AppServiceResult<List<AppUser>> getUserList();
	
	AppBaseResult register(UserRegister userRegister);

	AppBaseResult verifyEmail(String token);
	
	AppServiceResult<UserInfoDtoRes> getProfile(Long userId);
	
	AppBaseResult saveProfile(UserInfoDtoReq userInfo);
	
	AppBaseResult changePassword(ChangePassword changePassword);
	
	AppBaseResult resetPassword(String email);
	
	AppBaseResult saveRemark(RemarkProduct remarkProduct);
	
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
	
	AppServiceResult<String> uploadImage(MultipartFile file) throws UnsupportedFileTypeException, URISyntaxException;
	
	AppBaseResult updateActive(UserStatus userStatus);
}
