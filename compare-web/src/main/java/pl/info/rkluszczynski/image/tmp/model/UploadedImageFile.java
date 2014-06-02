package pl.info.rkluszczynski.image.tmp.model;

import org.springframework.web.multipart.MultipartFile;

class UploadedImageFile {

    private MultipartFile imageFile;

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}
