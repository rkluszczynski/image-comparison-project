package com.wordgraphs.fileupload;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
class ImageUploadController {
    private final Integer IMAGE_MAX_SIZE = 500000;

    // list of allowed file extensions
    private final Set<String> allowedImageExtensions;

    // list of error messages
    private List<String> errorMsgs = new ArrayList<>();

    private final String uploadsDir = "/opt/tomcat/uploads";

    public ImageUploadController() {
        // define allowed file extensions
        this.allowedImageExtensions = new HashSet<>();
        this.allowedImageExtensions.add("png");
        this.allowedImageExtensions.add("jpg");
        this.allowedImageExtensions.add("gif");

        // init error messages
        clearMessages();
    }

    // this method is prepares the upload form for display
    @RequestMapping(value = "/image/showUploadForm", method = RequestMethod.GET)
    public ModelAndView showUploadForm() {
        clearMessages();
        ModelAndView mv = new ModelAndView("image/uploadForm");

        // prepare an item that will store the uploaded file
        mv.addObject("uploadItem", new UploadItem());

        return mv;
    }

    // this method will be called when the upload form is submitted
    @RequestMapping(value = "/image/upload", method = RequestMethod.POST)
    public ModelAndView upload(UploadItem uploadItem, HttpSession session) {
        // the state of the controller is preserved between calls, so each time
        // we need to clear the error messages from the previous submission
        clearMessages();
        CommonsMultipartFile file = uploadItem.getFileData();

        try {
            if (file.getSize() > 0) {
                InputStream inputStream = file.getInputStream();

                String extension = FilenameUtils.getExtension(file.getOriginalFilename());

                if (!this.allowedImageExtensions.contains(extension)) {
                    ModelAndView mv = new ModelAndView("image/uploadForm");
                    addError("Incorrect file extension - only JPG, GIF, PNG i TIFF are allowed");
                    mv.addObject("errorMsgs", this.errorMsgs);
                    return mv;
                }

                if (file.getSize() > IMAGE_MAX_SIZE) {
                    ModelAndView mv = new ModelAndView("image/uploadForm");
                    addError("File size too large");
                    mv.addObject("errorMsgs", this.errorMsgs);
                    return mv;
                }

                String fileName = uploadsDir + "/" + file.getOriginalFilename();

                OutputStream outputStream = new FileOutputStream(fileName);

                int readBytes;
                byte[] buffer = new byte[IMAGE_MAX_SIZE];
                while ((readBytes = inputStream.read(buffer, 0, 10000)) != -1) {
                    outputStream.write(buffer, 0, readBytes);
                }
                outputStream.close();
                inputStream.close();

                return new ModelAndView("redirect:/image/uploadSuccess");
            } else {
                ModelAndView mv = new ModelAndView("image/uploadForm");
                addError("The file is empty");
                mv.addObject("errorMsgs", this.errorMsgs);
                return mv;
            }
        } catch (Exception e) {
            addError("Unknown error while uploading the file: " + e.getMessage());
            ModelAndView mv = new ModelAndView("image/uploadForm");
            mv.addObject("errorMsgs", this.errorMsgs);
            return mv;
        }
    }

//    private Image getImage(InputStream inputStream) throws IOException
//    {
//        BufferedImage bufferedImage;
//        bufferedImage = ImageIO.read(inputStream);
//        Image image = new Image();
//        image.setHeight(bufferedImage.getHeight());
//        image.setWidth(bufferedImage.getWidth());
//
//        return image;
//    }

    private void clearMessages() {
        this.errorMsgs = new ArrayList<>();
    }

    void addError(String msg) {
        this.errorMsgs.add(msg);
    }
}
