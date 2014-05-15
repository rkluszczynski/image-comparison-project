package pl.info.rkluszczynski.image.engine.model;

import com.google.common.collect.Lists;

import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.List;

public class SessionData implements Serializable {

    private final HttpSession session;

    private BufferedImage inputImage;
    private BufferedImage templateImage;
    private BufferedImage resultImage;

    private List<ImageStatisticData> imageStatistics = Lists.newArrayList();
    private long progress;


    public SessionData(HttpSession session) {
        this.session = session;
    }

    public String getDataUniqueKey() {
        return session.getId();
    }

    public HttpSession getSession() {
        return session;
    }

    public List<ImageStatisticData> getImageStatistics() {
        return imageStatistics;
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


    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getProgress() {
        return progress;
    }
}
