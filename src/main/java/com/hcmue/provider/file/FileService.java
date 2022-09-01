package com.hcmue.provider.file;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	MediaFile upload(String fileName, MultipartFile file) throws IOException, UnsupportedFileTypeException, URISyntaxException;

	Boolean remove(String pathFile);

}
