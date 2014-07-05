package pl.info.rkluszczynski.image.web.controller;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.info.rkluszczynski.image.engine.model.ImageStatisticData;
import pl.info.rkluszczynski.image.engine.model.SessionData;
import pl.info.rkluszczynski.image.web.model.ImageProcessingOperations;
import pl.info.rkluszczynski.image.web.model.ProcessingStatus;
import pl.info.rkluszczynski.image.web.model.TemplateImageResources;
import pl.info.rkluszczynski.image.web.model.view.ImageStatisticItem;

import javax.servlet.http.HttpSession;
import java.util.Collection;

import static pl.info.rkluszczynski.image.web.config.WebConstants.DETECT_CONTEXT_PATH__ROOT;
import static pl.info.rkluszczynski.image.web.config.WebConstants.USER_SESSION_ATTRIBUTE_NAME__IMAGE_DATA;

@Controller
@RequestMapping(value = DETECT_CONTEXT_PATH__ROOT)
public class MainController {

    private static final String PAGE_HEADER_TEXT = "Image Pattern Detection Test Page";

    @Autowired
    private TemplateImageResources templateImageResources;
    @Autowired
    private ImageProcessingOperations imageProcessingOperations;


    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
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
                if (isResultImageProcessed) {
                    setProperMatchResult(model, sessionData);
                }

                model.addAttribute("progressValue", sessionData.getProgress());
                model.addAttribute("resultStatistics", transformImageStatistics(sessionData));
            } catch (ClassCastException e) {
                sessionDataObject = null;
            }
        }

        if (sessionDataObject == null) {
            model.addAttribute("isInputImageUploaded", false);
            model.addAttribute("isTemplateImageChosen", false);
            model.addAttribute("isResultImageProcessed", false);

            model.addAttribute("progressValue", 0);
            model.addAttribute("resultStatistics", Lists.newArrayList());
        }
        model.addAttribute("headerText", PAGE_HEADER_TEXT);
        model.addAttribute("templateImageItems", templateImageResources.getTemplateItems());
        model.addAttribute("imageOperationItems", imageProcessingOperations.getOperationDescriptions());
        FooterHelper.setWebApplicationBuildDate(model);
        return "index";
    }

    private void setProperMatchResult(Model model, SessionData sessionData) {
        String message;
        String styleName;
        switch (sessionData.getMatchDecision()) {
            case VALID_MATCH:
                message = "Found valid match(es)!";
                styleName = "matchValid";
                break;
            case POSSIBLE_MATCH:
                message = "Found possible match(es)";
                styleName = "matchPossible";
                break;
            default:
                message = "No match detected!";
                styleName = "matchNone";
        }
        model.addAttribute("resultMessageString", message);
        model.addAttribute("resultMessageStyle", styleName);
    }

    @RequestMapping("/processingStatus")
    @ResponseBody
    public String processingStatus(HttpSession session) {
        Object sessionDataObject = session.getAttribute(USER_SESSION_ATTRIBUTE_NAME__IMAGE_DATA);
        ProcessingStatus processingStatusResponse = null;
        if (sessionDataObject != null) {
            try {
                SessionData sessionData = (SessionData) sessionDataObject;
                long progress = sessionData.getProgress();
                processingStatusResponse = new ProcessingStatus(progress, 1);
            } catch (ClassCastException e) {
                sessionDataObject = null;
            }
        }
        if (sessionDataObject == null || processingStatusResponse == null) {
            processingStatusResponse = new ProcessingStatus(0L, -1);
        }
        return processingStatusResponse.toString();
    }

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String redirectToMain() {
        return "redirect:detect/";
    }


    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public String status() {
        return "redirect:/status";
    }


    private Collection<ImageStatisticItem> transformImageStatistics(SessionData sessionData) {
        return Collections2.transform(sessionData.getImageStatistics(), new Function<ImageStatisticData, ImageStatisticItem>() {
            @Override
            public ImageStatisticItem apply(ImageStatisticData input) {
                return new ImageStatisticItem(
                        input.getStatisticName().toString(),
                        input.getStatisticValue().toEngineeringString()
                );
            }
        });
    }
}
