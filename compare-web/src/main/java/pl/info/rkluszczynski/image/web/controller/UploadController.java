package pl.info.rkluszczynski.image.web.controller;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.info.rkluszczynski.image.engine.model.SessionData;
import pl.info.rkluszczynski.image.engine.tasks.TasksManager;
import pl.info.rkluszczynski.image.web.model.ImageProcessingOperations;
import pl.info.rkluszczynski.image.web.model.TemplateImageResources;
import pl.info.rkluszczynski.image.web.validator.InputImageFileValidator;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import static pl.info.rkluszczynski.image.web.config.WebConstants.*;

@Controller
@RequestMapping(value = COMPARE_CONTEXT_PATH__ROOT)
public class UploadController {

    private static Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    @Qualifier("imageTasksManager")
    private TasksManager imageTasksManager;
    @Autowired
    private TemplateImageResources templateImageResources;
    @Autowired
    private ImageProcessingOperations imageProcessingOperations;

    @Autowired
    private InputImageFileValidator inputImageFileValidator;


    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
    public String redirectToCompare() {
        return "redirect:";
    }

    @RequestMapping(
            value = COMPARE_CONTEXT_PATH__USER_SESSION_INPUT_IMAGE,
            method = RequestMethod.POST
    )
    public String handleImageFileUploadAndStoreInSession(Model model,
                                                         @RequestParam(value = "imageFile") MultipartFile file,
                                                         @RequestParam(value = "templateFile", required = false) String templateFilename,
                                                         @RequestParam(value = "processingOperation", required = false) String operation,
//            BindingResult result,
                                                         HttpSession session
    ) {
        List<String> requestParamsErrors = getRequestParamsErrors(file, templateFilename, operation);
        if (!requestParamsErrors.isEmpty()) {
            model.addAttribute("errors", requestParamsErrors);
            FooterHelper.setWebApplicationBuildDate(model);
            return "errors";
        }

        logger.info("Processing image file upload during POST {}", COMPARE_CONTEXT_PATH__USER_SESSION_INPUT_IMAGE);
        logger.info(" -  content type: {}", file.getContentType());
        logger.info(" -          name: {}", file.getName());
        logger.info(" - original name: {}", file.getOriginalFilename());
        logger.info(" -          size: {}", String.valueOf(file.getSize()));

//        inputImageFileValidator.validate(file, result);
//        if (result.hasErrors()) {
//            return "redirect:";
//        }

        try {
            BufferedImage imgBuff = ImageIO.read(file.getInputStream());
            logger.info("Successfully read image: " + file.getOriginalFilename());

            SessionData sessionData = new SessionData(session);
            sessionData.setInputImage(imgBuff);
            sessionData.setTemplateImage(templateImageResources.getTemplateImage(templateFilename));
            session.setAttribute(USER_SESSION_ATTRIBUTE_NAME__IMAGE_DATA, sessionData);

            imageTasksManager.submitImageTask(
                    imageProcessingOperations.getProcessingTask(operation, sessionData)
            );

        } catch (IOException e) {
            logger.warn("Could not read image from inputStream!", e);
            return "redirect:/error";
        }
        return "redirect:";
    }

    private List<String> getRequestParamsErrors(MultipartFile file, String templateFilename, String operation) {
        List<String> errorsList = Lists.newArrayList();
        if (file.isEmpty()) {
            errorsList.add("Uploaded file has size 0!");
        }

        if (templateFilename == null || "".equalsIgnoreCase(templateFilename)) {
            errorsList.add("Template image not selected!");
        } else if (templateImageResources.isResourceKeyValueNotValid(templateFilename)) {
            errorsList.add("Template resource with key '" + templateFilename + "' not exists!");
        }

        if (operation == null) {
            errorsList.add("Processing operation not selected!");
        } else if (imageProcessingOperations.isOperationNameNotValid(operation)) {
            errorsList.add("Processing operation '" + operation + "' not supported!");
        }
        return errorsList;
    }
}
