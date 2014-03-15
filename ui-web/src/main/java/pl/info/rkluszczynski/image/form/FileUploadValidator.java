package pl.info.rkluszczynski.image.form;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Rafal on 15.03.14.
 */
public class FileUploadValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
        //just validate the FileUpload instances
        return UploadImage.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        UploadImage file = (UploadImage)target;

        if(file.getImageFileData().getSize()==0){
            errors.rejectValue("imageFileData", "required.imageUpload");
        }
    }
}
