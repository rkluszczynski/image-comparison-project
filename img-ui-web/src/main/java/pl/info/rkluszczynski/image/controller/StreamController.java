package pl.info.rkluszczynski.image.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static pl.info.rkluszczynski.image.config.ConfigConstants.*;


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
        streamUserSessionImage(response, session, USER_SESSION_ATTRIBUTE_NAME__TEMPLATE_IMAGE, COMPARE_CONTEXT_PATH__USER_SESSION_TEMPLATE_IMAGE);
    }


    @RequestMapping(
            value = COMPARE_CONTEXT_PATH__USER_SESSION_INPUT_IMAGE,
            method = RequestMethod.GET,
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public void streamUserSessionInputImage(HttpServletResponse response, HttpSession session)
    {
        streamUserSessionImage(response, session, USER_SESSION_ATTRIBUTE_NAME__INPUT_IMAGE, COMPARE_CONTEXT_PATH__USER_SESSION_INPUT_IMAGE);
    }


    @RequestMapping(
            value = COMPARE_CONTEXT_PATH__USER_SESSION_RESULT_IMAGE,
            method = RequestMethod.GET,
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public void streamUserSessionResultImage(HttpServletResponse response, HttpSession session)
    {
        streamUserSessionImage(response, session, USER_SESSION_ATTRIBUTE_NAME__RESULT_IMAGE, COMPARE_CONTEXT_PATH__USER_SESSION_RESULT_IMAGE);
    }


    private void streamUserSessionImage(
            HttpServletResponse response,
            HttpSession session,
            String userSessionAttributeImageName,
            String contextPath
        )
    {
        Object sessionAttributeObject = session.getAttribute(userSessionAttributeImageName);
        if (sessionAttributeObject != null) {
            logger.info("Attribute {} exists in session during GET {}", userSessionAttributeImageName, contextPath);
            BufferedImage imgBuff = (BufferedImage) sessionAttributeObject;
            try {
                ImageIO.write(imgBuff, DEFAULT_BUFFERED_IMAGE_OUTPUT_FORMAT, response.getOutputStream());
            } catch (IOException e) {
                logger.warn("Problem with streaming image!", e);
            }
        }
        else {
            logger.info("No attribute {} in session during GET {}", userSessionAttributeImageName, contextPath);
        }
    }
}
