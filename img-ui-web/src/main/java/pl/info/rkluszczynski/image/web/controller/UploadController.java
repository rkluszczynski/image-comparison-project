package pl.info.rkluszczynski.image.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.info.rkluszczynski.image.engine.model.SessionData;
import pl.info.rkluszczynski.image.tmp.validator.ImageFileValidator;
import pl.info.rkluszczynski.image.web.model.ImageProcessingOperations;
import pl.info.rkluszczynski.image.web.model.TemplateImageResources;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static pl.info.rkluszczynski.image.web.config.WebConstants.*;

@Controller
@RequestMapping(value = COMPARE_CONTEXT_PATH__ROOT)
public class UploadController {

    private static Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    ImageFileValidator imageFileValidator;

    @Autowired
    @Qualifier("taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    private TemplateImageResources templateImageResources;
    @Autowired
    private ImageProcessingOperations imageProcessingOperations;


    @RequestMapping(
            value = COMPARE_CONTEXT_PATH__USER_SESSION_INPUT_IMAGE,
            method = RequestMethod.POST
    )
    public String handleImageFileUploadAndStoreInSession(
            @RequestParam("imageFile") MultipartFile file,
            @RequestParam("templateFile") String templateFilename,
            @RequestParam("processingOperation") String operation,
            HttpSession session
        )
    {
        logger.info("Processing image file upload during POST {}", COMPARE_CONTEXT_PATH__USER_SESSION_INPUT_IMAGE);
        if (! file.isEmpty()) {
            logger.info(" -  content type: {}", file.getContentType());
            logger.info(" -          name: {}", file.getName());
            logger.info(" - original name: {}", file.getOriginalFilename());
            logger.info(" -          size: {}", String.valueOf(file.getSize()));

            try {
                BufferedImage imgBuff = ImageIO.read(file.getInputStream());
                logger.info("Successfully read image: " + file.getOriginalFilename());

                SessionData sessionData = new SessionData(session);
                sessionData.setInputImage(imgBuff);
                sessionData.setTemplateImage(templateImageResources.getTemplateImage(templateFilename));
                session.setAttribute(USER_SESSION_ATTRIBUTE_NAME__IMAGE_DATA, sessionData);

                taskExecutor.execute(
                        imageProcessingOperations.getProcessingTask(operation, sessionData)
                );

            } catch (IOException e) {
                logger.warn("Could not read image from inputStream!", e);
                return "redirect:/error";
            }
            return "redirect:";
        }
        return "redirect:/status";
    }
}
