package com.hcmue.provider.file;

public class FileConstant {
	
	private static final String BASE_FOLDER = System.getProperty("user.home");

//	private static final String BASE_FOLDER = getClass().getResource("/images/");
	
	public static final String IMAGE_FOLDER = BASE_FOLDER + "/images/";
	
	public static final String TEMP_PROFILE_IMAGE_BASE_URL = "https://robohash.org/";

	public static final String USER_URL_PATH = "/myfile/images/";
}