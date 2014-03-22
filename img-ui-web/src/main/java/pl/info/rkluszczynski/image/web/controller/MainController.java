package pl.info.rkluszczynski.image.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.info.rkluszczynski.image.engine.model.SessionData;

import javax.servlet.http.HttpSession;

import static pl.info.rkluszczynski.image.web.config.WebConstants.COMPARE_CONTEXT_PATH__ROOT;
import static pl.info.rkluszczynski.image.web.config.WebConstants.USER_SESSION_ATTRIBUTE_NAME__IMAGE_DATA;

@Controller
@RequestMapping(value = COMPARE_CONTEXT_PATH__ROOT)
public class MainController {

    private static final String HEADER_TEXT = "Image Comparison Test Page";


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
