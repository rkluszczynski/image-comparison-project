package pl.info.rkluszczynski.image.web.validator;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pl.info.rkluszczynski.image.web.model.InputImageFile;

@Component
@Qualifier(value = "inputImageFileValidator")
public class InputImageFileValidator implements Validator {

	@Override
	public boolean supports(Class<?> arg0) {
		return false;
	}

	@Override
	public void validate(Object imageFile, Errors errors) {
		InputImageFile file = (InputImageFile) imageFile;

		if (file.getImageFile().getSize() == 0) {
			errors.rejectValue("file",
                    "uploadForm.selectFile",
					"Please select a file with size > 0!");
		}
	}
}
