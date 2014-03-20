package pl.info.rkluszczynski.image.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import static pl.info.rkluszczynski.image.web.config.ConfigConstants.STATUS_CONTEXT_PATH__ROOT;

@Controller
public class StatusController {

    @RequestMapping(value = STATUS_CONTEXT_PATH__ROOT, method = RequestMethod.GET)
    @ResponseBody
    public String status() {
        return "OK";
    }
}
