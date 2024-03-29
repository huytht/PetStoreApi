package com.hcmue.constant;

public class SecurityConstant {
	public final static String COMPANY = "PetStore";

	public final static String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
	
	public final static String AUTHORITIES = "authorities";
	
	public final static String OPTIONS_HTTP_METHOD ="OPTIONS";
	
	public final static String TOKEN_PREFIX = "Bearer ";
	
	public final static String APPLICATION_NAME = "PetStore";
	
	public final static Long EXPIRATION_TIME = 86400000L;
	
	public final static Long REFRESH_EXPIRATION_TIME = 604800000L;
	
	public final static String[] PUBLIC_URLS = { "/user/register", 
			"/user/login", "/user/verify/**", "/user/image/profile/**", "/user/image/**" ,"/user/image/**/**", "/user/reset-password/**", "/user/refresh-token", "/pay/**"};
	
	public final static String[] PUBLIC_GET_URLS = {
			"/product/**",
			"/common/**",
			"/myfile/images/**",
			"/pay",
			"/"
	};
	
	public final static String[] REQUIRE_ADMIN_ROLE_URLS = {
//			"/user/update-status",
//			"/app-status/**",
	};
}