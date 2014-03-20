package pl.info.rkluszczynski.image.tmp.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.info.rkluszczynski.image.tmp.model.UploadedImageFile;

public class ImageFileValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object uploadedFile, Errors errors) {
		UploadedImageFile file = (UploadedImageFile) uploadedFile;

		if (file.getImageFile().getSize() == 0) {
			errors.rejectValue("file",
                    "uploadForm.selectFile",
					"Please select a file with size > 0!");
		}
	}
}
