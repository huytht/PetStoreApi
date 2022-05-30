package com.hcmue.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hcmue.constant.FileConstant;
import com.hcmue.domain.AppBaseResult;
import com.hcmue.domain.AppServiceResult;
import com.hcmue.domain.AppUserDomain;
import com.hcmue.dto.HttpResponse;
import com.hcmue.dto.HttpResponseError;
import com.hcmue.dto.HttpResponseSuccess;
import com.hcmue.dto.pagination.PageDto;
import com.hcmue.dto.pagination.PageParam;
import com.hcmue.dto.user.AppUserForAdminDto;
import com.hcmue.dto.user.ChangePassword;
import com.hcmue.dto.user.UserLogin;
import com.hcmue.dto.user.UserLoginRes;
import com.hcmue.dto.user.UserRegister;
import com.hcmue.dto.user.UserStatus;
import com.hcmue.dto.user.UserWhiteList;
import com.hcmue.dto.userinfo.UserInfoDtoReq;
import com.hcmue.dto.userinfo.UserInfoDtoRes;
import com.hcmue.infrastructure.AppJwtTokenProvider;
import com.hcmue.provider.file.UnsupportedFileTypeException;
import com.hcmue.service.AppUserService;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping("/user")
public class UserController {

	private AppUserService appUserService;

	private AuthenticationManager authenticationManager;

	private AppJwtTokenProvider appJwtTokenProvider;

	@Value("${app.config.url.loginapp}")
	private String urlLoginApp;

	@Autowired
	public UserController(AppUserService appUserService, AuthenticationManager authenticationManager,
			AppJwtTokenProvider appJwtTokenProvider) {
		this.appJwtTokenProvider = appJwtTokenProvider;
		this.authenticationManager = authenticationManager;
		this.appUserService = appUserService;
	}
	
	@GetMapping(path = "/list")
	public ResponseEntity<HttpResponse> getUsers() {

		AppServiceResult<List<AppUserForAdminDto>> result = appUserService.getUsers();
		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<AppUserForAdminDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@PostMapping("/register")
	public ResponseEntity<HttpResponse> register(@Valid @RequestBody UserRegister userRegister) {

		AppBaseResult result = appUserService.register(userRegister);

		return result.isSuccess()
				? ResponseEntity.ok(new HttpResponseSuccess<String>("Register succeed!, please verify email to login!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@GetMapping("/verify/{token}")
	public ResponseEntity<Void> verifyEmail(@PathVariable(name = "token", required = true) UUID token) {

		AppBaseResult result = appUserService.verifyEmail(token);
		String url = urlLoginApp + (result.isSuccess() ? "=success" : "=fail");

		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();
	}

	@PostMapping("/login")
	public ResponseEntity<HttpResponse> login(@Valid @RequestBody UserLogin userLogin) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));

		AppUserDomain appUserDetails = (AppUserDomain) authentication.getPrincipal();

		String userToken = appJwtTokenProvider.generateJwtToken(appUserDetails);
		UserLoginRes res = new UserLoginRes(appUserDetails.getUserId(), appUserDetails.getUsername(), userToken);

		return ResponseEntity.ok(new HttpResponseSuccess<UserLoginRes>(res));
	}

	@GetMapping("/profiles")
	public ResponseEntity<HttpResponse> getProfiles(@Valid @RequestParam(value = "id") Long userId) {

		AppServiceResult<UserInfoDtoRes> result = appUserService.getProfile(userId);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<UserInfoDtoRes>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
//	@GetMapping("/white-list")
//	public ResponseEntity<HttpResponse> getWhiteList(
//			@RequestParam(name = "page-number", required = false, defaultValue = "0") int pageNumber,
//			@RequestParam(name = "page-size", required = false, defaultValue = "30") int pageSize) {
//
//		PageParam pageParam = new PageParam();
//		pageParam.setPageIndex(pageNumber);
//		pageParam.setPageSize(pageSize);
//		
//		AppServiceResult<PageDto<TrackShort>> result = trackService.getAllTrackLiked(pageParam);
//
//		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<PageDto<TrackShort>>(result.getData()))
//				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
//	}

	@PutMapping("/profiles")
	public ResponseEntity<HttpResponse> saveProfiles(@Valid @RequestBody UserInfoDtoReq userInfo) {

		AppBaseResult result = appUserService.saveProfile(userInfo);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Success!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@PutMapping("/password")
	public ResponseEntity<HttpResponse> changePassword(@Valid @RequestBody ChangePassword changePassword) {

		AppBaseResult result = appUserService.changePassword(changePassword);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Success!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@PostMapping("/upload-profile-image")
	public ResponseEntity<HttpResponse> uploadImage(@RequestParam("profileImage") MultipartFile file)
			throws UnsupportedFileTypeException {

		AppServiceResult<String> result = appUserService.uploadImage(file);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
	public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
		URL url = new URL(FileConstant.TEMP_PROFILE_IMAGE_BASE_URL + username);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		try (InputStream inputStream = url.openStream()) {
			byte[] chunk = new byte[1024];
			int bytesRead;
			while ((bytesRead = inputStream.read(chunk)) > 0) {
				byteArrayOutputStream.write(chunk, 0, bytesRead);
			}
		}
		return byteArrayOutputStream.toByteArray();
	}

	@PostMapping("/reset-password/{email}")
	public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email) {

		AppBaseResult result = appUserService.resetPassword(email);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Succeed!, please check email!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

//	@PutMapping("/white-list")
//	public ResponseEntity<HttpResponse> updateWhiteList(@Valid @RequestBody UserWhiteList dto) {
//
//		AppBaseResult result = trackService.updateWhiteList(dto);
//
//		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Succeed!"))
//				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
//	}
	
	@PostMapping("/update-status")
	public ResponseEntity<HttpResponse> updateStatus(@Valid @RequestBody UserStatus userStatus) {

		AppBaseResult result = appUserService.updateActive(userStatus);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Succeed!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
//	@GetMapping(path = "/track-liked")
//	public ResponseEntity<HttpResponse> getTrackLiked() {
//
//		AppServiceResult<Long[]> result = trackService.getTrackIdsLiked();
//
//		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<Long[]>(result.getData()))
//				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
//	}
}