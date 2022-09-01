package com.hcmue.provider.file.image;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import com.hcmue.provider.file.FileConstant;
import com.hcmue.provider.file.FileService;
import com.hcmue.provider.file.MediaFile;
import com.hcmue.provider.file.UnsupportedFileTypeException;
import com.hcmue.provider.file.util.DateUtil;
import com.hcmue.provider.file.util.FileUtil;
import com.hcmue.util.StringUtil;

@Service
public final class ImageFileService implements FileService {
	private final String imageExtensionSave = ".jpg";

	private final String[] mimeTypeSupport = { MimeTypeUtils.IMAGE_JPEG_VALUE, MimeTypeUtils.IMAGE_GIF_VALUE,
			MimeTypeUtils.IMAGE_PNG_VALUE };

	@Autowired
	public ImageFileService() {

	}

	@Override
	public MediaFile upload(String fileName, MultipartFile file) throws IOException, UnsupportedFileTypeException, URISyntaxException {

		System.out.println("======>" + file.getOriginalFilename());
		if (!Arrays.asList(mimeTypeSupport).contains(file.getContentType())) {
			throw new UnsupportedFileTypeException(
					file.getOriginalFilename() + " is not an image file: [" + String.join("; ", mimeTypeSupport) + "]");
		}
		URL res = getClass().getResource("/images");
		System.out.println("======>" + Paths.get(res.toURI()));
		Path imageFolder = Paths.get(res.toURI());
		System.out.println(imageFolder);

		if (!Files.exists(imageFolder)) {
			Files.createDirectories(imageFolder);
		}

		fileName = StringUtil.normalizeUri(fileName) + "-" + DateUtil.GetCurrentTimeMillis() + imageExtensionSave;

		Files.deleteIfExists(Paths.get(imageFolder + fileName));
		Files.copy(file.getInputStream(), imageFolder.resolve(fileName), REPLACE_EXISTING);

		return new MediaFile(Paths.get(imageFolder.toString(), File.separator, fileName).toString(), 
				FileConstant.USER_URL_PATH + fileName);
	}

	@Override
	public Boolean remove(String pathFile) {
		return FileUtil.deleteFile(pathFile);
	}
}
