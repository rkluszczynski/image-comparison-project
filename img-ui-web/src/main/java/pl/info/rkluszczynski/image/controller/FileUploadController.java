package pl.info.rkluszczynski.image.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
@RequestMapping(value = "/image")
public class FileUploadController {
    private static Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String handleFormUpload(@RequestParam("imageFile") MultipartFile file) {
        if (! file.isEmpty()) {
            logger.info(file.getContentType());
            logger.info(file.getName());
            logger.info(file.getOriginalFilename());
            logger.info(String.valueOf(file.getSize()));
            try {
                BufferedImage imgBuff = ImageIO.read(file.getInputStream());

            } catch (IOException e) {
                logger.warn("Could not read image from inputStream!", e);
                return "redirect:/error";
            }

            return "redirect:";
        }
        return "redirect:/status";
    }

}
