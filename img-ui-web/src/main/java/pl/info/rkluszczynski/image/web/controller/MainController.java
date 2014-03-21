package pl.info.rkluszczynski.image.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

import static pl.info.rkluszczynski.image.web.config.WebConstants.*;

@Controller
@RequestMapping(value = COMPARE_CONTEXT_PATH__ROOT)
public class MainController {

    private static final String HEADER_TEXT = "Image Comparison Test Page";


    @RequestMapping(value = { "/" }, method = RequestMethod.GET)
    public String showIndexPage(Model model, HttpSession session) {
        boolean isInputImageUploaded =
                session.getAttribute(USER_SESSION_ATTRIBUTE_NAME__INPUT_IMAGE) != null;
        boolean isTemplateImageChosen =
                session.getAttribute(USER_SESSION_ATTRIBUTE_NAME__TEMPLATE_IMAGE) != null;
        boolean isResultImageProcessed =
                session.getAttribute(USER_SESSION_ATTRIBUTE_NAME__RESULT_IMAGE) != null;

        model.addAttribute("headerText", HEADER_TEXT);
        model.addAttribute("isInputImageUploaded", isInputImageUploaded);
        model.addAttribute("isTemplateImageChosen", isTemplateImageChosen);
        model.addAttribute("isResultImageProcessed", isResultImageProcessed);
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
