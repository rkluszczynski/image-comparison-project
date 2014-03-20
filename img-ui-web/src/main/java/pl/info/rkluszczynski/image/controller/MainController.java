package pl.info.rkluszczynski.image.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

import static pl.info.rkluszczynski.image.config.ConfigConstants.USER_SESSION_IMAGE_ATTRIBUTE_NAME;

@Controller
@RequestMapping(value = "/compare")
public class MainController {

    private static final String HEADER_TEXT = "Image Comparison Test Page";


    @RequestMapping(value = { "/" }, method = RequestMethod.GET)
    public String showIndexPage(Model model, HttpSession session) {
        boolean isUserImageUploaded = session.getAttribute(USER_SESSION_IMAGE_ATTRIBUTE_NAME) != null;

        model.addAttribute("headerText", HEADER_TEXT);
        model.addAttribute("isUserImageUploaded", isUserImageUploaded);
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
