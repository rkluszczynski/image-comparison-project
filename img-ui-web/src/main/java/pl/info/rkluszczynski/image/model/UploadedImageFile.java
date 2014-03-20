package pl.info.rkluszczynski.image.model;

import org.springframework.web.multipart.MultipartFile;

public class UploadedImageFile {

	private MultipartFile imageFile;

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}
}
