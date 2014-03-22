package pl.info.rkluszczynski.image.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.info.rkluszczynski.image.engine.model.SessionData;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static pl.info.rkluszczynski.image.web.config.WebConstants.*;


@Controller
@RequestMapping(value = COMPARE_CONTEXT_PATH__ROOT)
public class StreamController {

    private static Logger logger = LoggerFactory.getLogger(StreamController.class);

    private static final String DEFAULT_BUFFERED_IMAGE_OUTPUT_FORMAT = "PNG";


    @RequestMapping(
            value = COMPARE_CONTEXT_PATH__USER_SESSION_TEMPLATE_IMAGE,
            method = RequestMethod.GET,
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public void streamUserSessionTemplateImage(HttpServletResponse response, HttpSession session)
    {
        SessionData imageData = getSessionImageDataObject(session, COMPARE_CONTEXT_PATH__USER_SESSION_TEMPLATE_IMAGE);
        if (imageData != null) {
            streamUserSessionImage(response, imageData.getTemplateImage());
        }
    }


    @RequestMapping(
            value = COMPARE_CONTEXT_PATH__USER_SESSION_INPUT_IMAGE,
            method = RequestMethod.GET,
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public void streamUserSessionInputImage(HttpServletResponse response, HttpSession session)
    {
        SessionData imageData = getSessionImageDataObject(session, COMPARE_CONTEXT_PATH__USER_SESSION_INPUT_IMAGE);
        if (imageData != null) {
            streamUserSessionImage(response, imageData.getInputImage());
        }
    }


    @RequestMapping(
            value = COMPARE_CONTEXT_PATH__USER_SESSION_RESULT_IMAGE,
            method = RequestMethod.GET,
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public void streamUserSessionResultImage(HttpServletResponse response, HttpSession session)
    {
        SessionData imageData = getSessionImageDataObject(session, COMPARE_CONTEXT_PATH__USER_SESSION_RESULT_IMAGE);
        if (imageData != null) {
            streamUserSessionImage(response, imageData.getResultImage());
        }
    }


    private SessionData getSessionImageDataObject(HttpSession session, String contextPath)
    {
        Object sessionAttributeObject = session.getAttribute(USER_SESSION_ATTRIBUTE_NAME__IMAGE_DATA);
        if (sessionAttributeObject != null) {
            logger.info("Attribute {} exists in session during GET {}", USER_SESSION_ATTRIBUTE_NAME__IMAGE_DATA, contextPath);
            return (SessionData)sessionAttributeObject;
        }
        else {
            logger.info("No attribute {} in session during GET {}", USER_SESSION_ATTRIBUTE_NAME__IMAGE_DATA, contextPath);
            return null;
        }
    }

    private void streamUserSessionImage(HttpServletResponse response, BufferedImage bufferedImage)
    {
        try {
            ImageIO.write(bufferedImage, DEFAULT_BUFFERED_IMAGE_OUTPUT_FORMAT, response.getOutputStream());
        } catch (IOException e) {
            logger.warn("Problem with streaming image!", e);
        }
    }
}
