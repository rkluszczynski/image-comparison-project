package pl.info.rkluszczynski.image.form;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class UploadImage {
    private String imageFilename;
    private CommonsMultipartFile imageFileData;

    public CommonsMultipartFile getImageFileData() {
        return imageFileData;
    }

    public void setImageFileData(CommonsMultipartFile imageFileData) {
        this.imageFileData = imageFileData;
    }

    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }
}
