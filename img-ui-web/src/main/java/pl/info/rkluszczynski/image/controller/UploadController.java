package pl.info.rkluszczynski.image.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pl.info.rkluszczynski.image.validator.ImageFileValidator;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static pl.info.rkluszczynski.image.config.ConfigConstants.COMPARE_CONTEXT_PATH__USER_SESSION_IMAGE;
import static pl.info.rkluszczynski.image.config.ConfigConstants.USER_SESSION_IMAGE_ATTRIBUTE_NAME;

@Controller
@RequestMapping(value = "/compare")
public class UploadController {

    private static Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    ImageFileValidator imageFileValidator;


    @RequestMapping(
            value = COMPARE_CONTEXT_PATH__USER_SESSION_IMAGE,
            method = RequestMethod.POST
    )
    public String handleImageFileUploadAndStoreInSession(
            @RequestParam("imageFile") MultipartFile file,
            HttpSession session
        )
    {
        logger.info("Processing image file upload during POST {}", COMPARE_CONTEXT_PATH__USER_SESSION_IMAGE);
        if (! file.isEmpty()) {
            logger.info(" -  content type: {}", file.getContentType());
            logger.info(" -          name: {}", file.getName());
            logger.info(" - original name: {}", file.getOriginalFilename());
            logger.info(" -          size: {}", String.valueOf(file.getSize()));

            try {
                BufferedImage imgBuff = ImageIO.read(file.getInputStream());
                logger.info("Successfully read image: " + file.getOriginalFilename());
                session.setAttribute(USER_SESSION_IMAGE_ATTRIBUTE_NAME, imgBuff);
            } catch (IOException e) {
                logger.warn("Could not read image from inputStream!", e);
                return "redirect:/error";
            }
            return "redirect:";
        }
        return "redirect:/status";
    }


    @RequestMapping(
            value = COMPARE_CONTEXT_PATH__USER_SESSION_IMAGE,
            method = RequestMethod.GET,
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public void streamStoredUserSessionImage(
            HttpServletResponse response,
            HttpSession session
        )
    {
        if (session.getAttribute(USER_SESSION_IMAGE_ATTRIBUTE_NAME) != null) {
            logger.info("Attribute {} exists in session during GET {}", USER_SESSION_IMAGE_ATTRIBUTE_NAME, COMPARE_CONTEXT_PATH__USER_SESSION_IMAGE);
            BufferedImage imgBuff = (BufferedImage) session.getAttribute(USER_SESSION_IMAGE_ATTRIBUTE_NAME);
            try {
                ImageIO.write(imgBuff, "PNG", response.getOutputStream());
            } catch (IOException e) {
                logger.warn("Problem with streaming image!", e);
            }
        }
        else {
            logger.info("No attribute {} in session during GET {}", USER_SESSION_IMAGE_ATTRIBUTE_NAME, COMPARE_CONTEXT_PATH__USER_SESSION_IMAGE);
        }
    }
}
