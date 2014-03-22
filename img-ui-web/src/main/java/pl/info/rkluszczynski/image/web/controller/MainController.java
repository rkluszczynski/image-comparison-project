package pl.info.rkluszczynski.image.web.controller;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.info.rkluszczynski.image.engine.model.SessionData;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static pl.info.rkluszczynski.image.web.config.WebConstants.COMPARE_CONTEXT_PATH__ROOT;
import static pl.info.rkluszczynski.image.web.config.WebConstants.USER_SESSION_ATTRIBUTE_NAME__IMAGE_DATA;

@Controller
@RequestMapping(value = COMPARE_CONTEXT_PATH__ROOT)
public class MainController {
    private static Logger logger = LoggerFactory.getLogger(MainController.class);

    private static final String HEADER_TEXT = "Image Comparison Test Page";

    private ArrayList<Resource> resourceArrayList;

    @Autowired
    public MainController(ApplicationContext applicationContext) {
        resourceArrayList = Lists.newArrayList();
        try {
            Resource[] resources = applicationContext.getResources("classpath:image-data/patterns/*");
            for (Resource resource : resources) {
                resourceArrayList.add(resource);
                logger.info("RESOURCE: {}", resource.getURL().toString());
            }
        } catch (IOException e) {
            logger.warn("Problem with loading image patterns resources!", e);
        }
    }

    @RequestMapping(value = { "/" }, method = RequestMethod.GET)
    public String showIndexPage(Model model, HttpSession session) {
        Object sessionDataObject = session.getAttribute(USER_SESSION_ATTRIBUTE_NAME__IMAGE_DATA);
        if (sessionDataObject != null) {
            try {
                SessionData sessionData = (SessionData) sessionDataObject;
                boolean isInputImageUploaded = sessionData.getInputImage() != null;
                boolean isTemplateImageChosen = sessionData.getTemplateImage() != null;
                boolean isResultImageProcessed = sessionData.getResultImage() != null;

                model.addAttribute("isInputImageUploaded", isInputImageUploaded);
                model.addAttribute("isTemplateImageChosen", isTemplateImageChosen);
                model.addAttribute("isResultImageProcessed", isResultImageProcessed);
            }
            catch (ClassCastException e) {
                sessionDataObject = null;
            }
        }

        if (sessionDataObject == null) {
            model.addAttribute("isInputImageUploaded", false);
            model.addAttribute("isTemplateImageChosen", false);
            model.addAttribute("isResultImageProcessed", false);
        }
        model.addAttribute("headerText", HEADER_TEXT);

        List<String> prods = Lists.newArrayList();
        for (Resource resource : resourceArrayList) {
            prods.add(resource.toString());
        }
        model.addAttribute("prods", prods);
        return "index";
    }

    @RequestMapping(value = { "" }, method = RequestMethod.GET)
    public String redirectToMain() {
        return "redirect:compare/";
    }


    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public String status() {
        return "redirect:/status";
    }
}
