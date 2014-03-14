package pl.info.rkluszczynski.image.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping(value = "/image")
public class FileUploadController {
    private static Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String handleFormUpload(@RequestParam("upFile") MultipartFile file) {
        if (! file.isEmpty()) {
            try {
                logger.info(file.getContentType());
                logger.info(file.getName());
                logger.info(file.getOriginalFilename());
                logger.info(String.valueOf(file.getSize()));

                byte[] bytes = file.getBytes();

            } catch (IOException e) {
                e.printStackTrace();
            }

            // store the bytes somewhere
            return "redirect:";
        }
        return "redirect:/status";
    }

}
