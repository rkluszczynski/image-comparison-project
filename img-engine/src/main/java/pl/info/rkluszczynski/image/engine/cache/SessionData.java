package pl.info.rkluszczynski.image.engine.cache;

import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

public class SessionData {

    private HttpSession session;

    private BufferedImage inputImage;
    private BufferedImage templateImage;
    private BufferedImage resultImage;


    public String getDataUniqueKey() {
        return session.getId();
    }


    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public BufferedImage getInputImage() {
        return inputImage;
    }

    public void setInputImage(BufferedImage inputImage) {
        this.inputImage = inputImage;
    }

    public BufferedImage getTemplateImage() {
        return templateImage;
    }

    public void setTemplateImage(BufferedImage templateImage) {
        this.templateImage = templateImage;
    }

    public BufferedImage getResultImage() {
        return resultImage;
    }

    public void setResultImage(BufferedImage resultImage) {
        this.resultImage = resultImage;
    }
}
