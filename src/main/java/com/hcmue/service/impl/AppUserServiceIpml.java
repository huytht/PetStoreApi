package com.hcmue.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.hcmue.constant.AppError;
import com.hcmue.constant.BeanIdConstant;
import com.hcmue.constant.FileConstant;
import com.hcmue.constant.RoleConstant;
import com.hcmue.domain.AppBaseResult;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.domain.AppUserDomain;
import com.hcmue.dto.user.AppUserForAdminDto;
import com.hcmue.dto.user.ChangePassword;
import com.hcmue.dto.user.RemarkProduct;
import com.hcmue.dto.user.UserRegister;
import com.hcmue.dto.user.UserStatus;
import com.hcmue.dto.userinfo.UserInfoDtoReq;
import com.hcmue.dto.userinfo.UserInfoDtoRes;
import com.hcmue.entity.AppRole;
import com.hcmue.entity.AppUser;
import com.hcmue.entity.AppUserProduct;
import com.hcmue.entity.AppUserProductId;
import com.hcmue.entity.Product;
import com.hcmue.entity.UserInfo;
import com.hcmue.entity.VerificationToken;
import com.hcmue.provider.file.FileServiceFactory;
import com.hcmue.provider.file.FileType;
import com.hcmue.provider.file.FileService;
import com.hcmue.provider.file.MediaFile;
import com.hcmue.provider.file.UnsupportedFileTypeException;
import com.hcmue.repository.AppRoleRepository;
import com.hcmue.repository.AppUserProductRepository;
import com.hcmue.repository.AppUserRepository;
import com.hcmue.repository.ProductRepository;
import com.hcmue.repository.VerificationTokenRepository;
import com.hcmue.service.AppUserService;
import com.hcmue.service.AppMailService;
import com.hcmue.util.AppUtils;
import com.hcmue.util.StringUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Service
@Qualifier(BeanIdConstant.USER_DETAIL_SERVICE)
public class AppUserServiceIpml implements AppUserService, UserDetailsService {

	private final Logger logger = LoggerFactory.getLogger(AppUserServiceIpml.class);

	private AppUserRepository appUserRepository;

	private VerificationTokenRepository verificationTokenRepository;

	private AppRoleRepository appRoleRepository;

	private AppMailService appMailService;

	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private ProductRepository productRepository;
	
	private AppUserProductRepository userProductRepository;

	private FileService imageFileService;

	@Autowired
	public AppUserServiceIpml(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository,
			VerificationTokenRepository verificationTokenRepository, AppMailService appMailService,
			BCryptPasswordEncoder bCryptPasswordEncoder, ProductRepository productRepository, 
			AppUserProductRepository userProductRepository) {
		this.appUserRepository = appUserRepository;
		this.verificationTokenRepository = verificationTokenRepository;
		this.appRoleRepository = appRoleRepository;
		this.appMailService = appMailService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.productRepository = productRepository;
		this.userProductRepository = userProductRepository;
		this.imageFileService = FileServiceFactory.getFileService(FileType.IMAGE);
	}
	
	@Override
	public AppServiceResult<List<AppUserForAdminDto>> getUsers() {
		try {
			List<AppUser> users = appUserRepository.findAll();
			List<AppUserForAdminDto> result = new ArrayList<AppUserForAdminDto>();
			
			if(users != null && users.size() > 0)
				users.forEach(item -> result.add(AppUserForAdminDto.CreateFromEntity(item)));
			
			return new AppServiceResult<List<AppUserForAdminDto>>(true, 0, "Succeed!", result);
		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<List<AppUserForAdminDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}
	
	@Override
	public AppBaseResult register(UserRegister userRegister) {
		try {
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);

			AppUser userByEmail = appUserRepository.findByEmail(userRegister.getEmail());
			if (userByEmail != null) {
				logger.warn("Email is exist: " + userRegister.getEmail() + ", Cannot further process!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(),
						"Email " + userRegister.getEmail() + " đã được sử dụng. Vui lòng nhập email khác");
			}

			// TODO: Check email confirm, return message or resend mail

			AppUser userByUsername = appUserRepository.findByUsername(userRegister.getUsername());
			if (userByUsername != null) {
				logger.warn("Username is exist: " + userRegister.getUsername() + ", Cannot further process!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(),
						"Tên tài khoản " + userRegister.getUsername() + " đã tồn tại.");
			}

			AppUser userNew = mapper.convertValue(userRegister, AppUser.class);

			AppRole defaultRole = appRoleRepository.findByName(RoleConstant.ROLE_MEMBER);
			userNew.setAppRoles(Stream.of(defaultRole).collect(Collectors.toSet()));

			// TODO: Set authorities

			userNew.setUserInfo(new UserInfo());
			// Random image
			userNew.getUserInfo().setAvatarImg(FileConstant.TEMP_PROFILE_IMAGE_BASE_URL + userRegister.getUsername());

			userNew.setEnabled(false);

			userNew.setAccountNonLocked(true);

			// endcode password
			userNew.setPassword(bCryptPasswordEncoder.encode(userRegister.getPassword()));

			// Save user
			appUserRepository.save(userNew);

			// Send mail verify
			appMailService.sendMailVerify(userNew);

			return AppBaseResult.GenarateIsSucceed();

		} catch (Exception e) {
			e.printStackTrace();

			return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}
	}

	@Override
	public AppBaseResult verifyEmail(UUID token) {
		VerificationToken vToken = verificationTokenRepository.findByToken(token);

		if (vToken != null) {
			if (vToken.getVerifyDate() != null) {
				logger.warn("Token verified!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), "Token verified!");
			}

			vToken.setIsVerify(Boolean.TRUE);
			vToken.setVerifyDate(AppUtils.getNow());

			vToken.getAppUser().setEnabled(Boolean.TRUE);

			verificationTokenRepository.save(vToken);

			return AppBaseResult.GenarateIsSucceed();
		} else {
			logger.warn("Token is not exist: " + token.toString());
			return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), "Token is not exist!");
		}
	}

	@SneakyThrows
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser = appUserRepository.findByUsername(username);

		appUserRepository.findByUsername(username);

		if (appUser == null) {
			logger.warn("User not found by username: " + username + ". Cannot further process!");
			throw new UsernameNotFoundException("User not found by username: " + username);
		}

		AppUserDomain appUserDomain = new AppUserDomain(appUser);

		return appUserDomain;
	}

	@Override
	public AppServiceResult<UserInfoDtoRes> getProfile(Long userId) {

		UserInfoDtoRes userInfoDto = new UserInfoDtoRes();
		try {
			AppUser user = appUserRepository.findById(userId).orElse(null);

			if (user == null) {
				logger.warn("AppUser is null, Cannot further process!");
				return new AppServiceResult<UserInfoDtoRes>(false, AppError.Validattion.errorCode(),
						"User is not exist!", null);
			}

			userInfoDto.setUserId(user.getId());
			userInfoDto.setEmail(user.getEmail());
			userInfoDto.setUsername(user.getUsername());

			if (user.getUserInfo() != null) {

				// TODO: Implement mapping
				userInfoDto.setFirstName(user.getUserInfo().getFirstName());
				userInfoDto.setLastName(user.getUserInfo().getLastName());
				userInfoDto.setAvatarImg(user.getUserInfo().getAvatarImg().contains(FileConstant.TEMP_PROFILE_IMAGE_BASE_URL)
						? user.getUserInfo().getAvatarImg()
						: AppUtils.createLinkOnCurrentHttpServletRequest(user.getUserInfo().getAvatarImg()));
			}

			return new AppServiceResult<UserInfoDtoRes>(true, 0, "Success", userInfoDto);

		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<UserInfoDtoRes>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppBaseResult saveProfile(UserInfoDtoReq userInfo) {
		try {
			String currentUsername = AppUtils.getCurrentUsername();

			if (currentUsername != null) {

				AppUser user = appUserRepository.findByUsername(currentUsername);
				if (userInfo.getUserId() != user.getId()) {
					logger.warn("Not match UserId, Cannot further process!");

					return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), "User is not match id");
				}

				// TODO: Implement mapping
				AppUser userByEmail = appUserRepository.findByEmail(userInfo.getEmail());
				if (userByEmail != null && userByEmail.getId() != user.getId()) {
					logger.warn("Email is exist: " + userInfo.getEmail() + ", Cannot further process!");

					return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(),
							"Email " + userInfo.getEmail() + " đã được sử dụng. Vui lòng nhập email khác");
				}

				user.setEmail(userInfo.getEmail());
				user.getUserInfo().setFirstName(userInfo.getFirstName());
				user.getUserInfo().setLastName(userInfo.getLastName());

				user.getUserInfo().setUserEdit(currentUsername);

				// Save user
				appUserRepository.save(user);

				return AppBaseResult.GenarateIsSucceed();
			}
		} catch (Exception e) {
			e.printStackTrace();

			return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}

		return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
	}

	@Override
	public AppBaseResult changePassword(ChangePassword changePassword) {
		try {
			String currentUsername = AppUtils.getCurrentUsername();

			if (!currentUsername.equals(changePassword.getUsername())) {
				logger.warn("Not match UserId, Cannot further process!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), "Not match UserId");
			}

			if (changePassword.getNewPassword().equals(changePassword.getOldPassword())) {
				logger.warn("New password equals old password, Cannot further process!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(),
						"Mật khẩu mới trùng với mật khẩu cũ!");
			}

			AppUser user = appUserRepository.findByUsername(currentUsername);

			if (user == null) {
				logger.warn("User is not exist!, Cannot further process!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), "User is not exist!");
			}

			if (!bCryptPasswordEncoder.matches(changePassword.getOldPassword(), user.getPassword())) {
				logger.warn("Password incorrect, Cannot further process!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), "Sai mật khẩu!");
			}

			user.setPassword(bCryptPasswordEncoder.encode(changePassword.getNewPassword()));
			user.setUserEdit(currentUsername);

			appUserRepository.save(user);

			return AppBaseResult.GenarateIsSucceed();
		} catch (Exception e) {
			e.printStackTrace();

			return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}

	}

	@Override
	public AppServiceResult<String> uploadImage(MultipartFile file) throws UnsupportedFileTypeException {
		try {
			if (file != null) {

				AppUser user = appUserRepository.findByUsername(AppUtils.getCurrentUsername());
				if (user == null) {
					logger.warn("User is not exist, Cannot further process!");

					return new AppServiceResult<String>(false, AppError.Validattion.errorCode(), " User is not exist",
							null);
				}

				MediaFile mediaFile = imageFileService.upload(user.getUsername(), file);
				user.getUserInfo().setAvatarImg(mediaFile.getPathUrl());
				user.getUserInfo().setUserEdit(AppUtils.getCurrentUsername());

				appUserRepository.save(user);

				return new AppServiceResult<String>(true, 0, "Succeed!", ServletUriComponentsBuilder
						.fromCurrentContextPath().path(mediaFile.getPathUrl()).toUriString());
			} else {
				logger.warn("Image file is not null, Cannot further process!");

				return new AppServiceResult<String>(false, AppError.Validattion.errorCode(), "Image file is not null",
						null);
			}
		} catch (IOException e) {
			e.printStackTrace();

			return new AppServiceResult<String>(false, AppError.Unknown.errorCode(), AppError.Unknown.errorMessage(),
					null);
		}
	}

	@Override
	@Transactional
	public AppBaseResult resetPassword(String email) {
		try {
			AppUser user = appUserRepository.findByEmail(email);

			if (user == null) {
				logger.warn("Email is not exist: " + email + ", Cannot further process!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), "Email is not exist: " + email);
			}

			String newPassword = StringUtil.RandomString(15);
			String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
			user.setPassword(encodedPassword);

			if (AppUtils.getCurrentUsername() != null) {
				user.setUserEdit(AppUtils.getCurrentUsername());
			}

			appUserRepository.save(user);
			appMailService.sendResetPassword(email, newPassword);

			return AppBaseResult.GenarateIsSucceed();
		} catch (Exception e) {
			e.printStackTrace();

			return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), "Email is not exist: " + email);
		}

	}

	@Override
	public AppBaseResult updateActive(UserStatus userStatus) {
		try {
			AppUser user = appUserRepository.findById(userStatus.getUserId()).orElse(null);
			if (user == null) {
				logger.warn("UserId is not exist: " + userStatus.getUserId() + ", Cannot further process!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(),
						"UserId is not exist: " + userStatus.getUserId());
			}

			user.setEnabled(userStatus.getIsActive());
			appUserRepository.save(user);

			return AppBaseResult.GenarateIsSucceed();
		} catch (Exception e) {
			e.printStackTrace();

			return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}
	}

	@Override
	public AppBaseResult saveRemark(RemarkProduct remarkProduct) {
		try {
			AppUser appUser = appUserRepository.findByUsername(AppUtils.getCurrentUsername());
			if (appUser == null) {
				logger.warn("Not logged in!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), "Not logged in!");
			}
			
			Product product = productRepository.findById(remarkProduct.getProductId()).orElse(null);
			
			if (product == null) {
				logger.warn("Product Id is not exist: " + remarkProduct.getProductId() + ", Cannot further process!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(),
						"Product Id is not exist: " + remarkProduct.getProductId());
			}
			
			AppUserProduct newRemark = new AppUserProduct();
			newRemark.setAppUser(appUser);
			newRemark.setProduct(product);
			newRemark.setAppUserProductId(new AppUserProductId(appUser.getId(), product.getId()));
			newRemark.setRate(remarkProduct.getRate());
			newRemark.setRemark(remarkProduct.getRemark());
			
			userProductRepository.save(newRemark);
			
			return AppBaseResult.GenarateIsSucceed();
		} catch (Exception e) {
			e.printStackTrace();

			return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}
	}
}
