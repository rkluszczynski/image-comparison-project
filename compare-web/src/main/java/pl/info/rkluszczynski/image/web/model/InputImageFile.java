package pl.info.rkluszczynski.image.web.model;

import org.springframework.web.multipart.MultipartFile;

public class InputImageFile {

	private MultipartFile imageFile;

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}
}
