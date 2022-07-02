package com.hcmue.provider.file;

import org.springframework.stereotype.Component;

import com.hcmue.provider.file.image.ImageFileService;

@Component
public class FileServiceFactory {
	private FileServiceFactory() {

	}

	public final static FileService getFileService(FileType fileType) {
		switch (fileType) {
		case IMAGE:
			return new ImageFileService();

		default:
			throw new IllegalArgumentException("This FileType is unsupported!");
		}
	}
}
