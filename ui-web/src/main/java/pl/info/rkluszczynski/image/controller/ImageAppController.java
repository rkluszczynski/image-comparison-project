package pl.info.rkluszczynski.image.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/image")
public class ImageAppController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showIndexPage(Model model) {
        model.addAttribute("message", "Hello!");
        return "index";
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public String status() {
        return "redirect:/status";
    }
}
