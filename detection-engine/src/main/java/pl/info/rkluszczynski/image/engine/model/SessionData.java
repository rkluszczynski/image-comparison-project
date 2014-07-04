package pl.info.rkluszczynski.image.engine.model;

import com.google.common.collect.Lists;
import pl.info.rkluszczynski.image.engine.model.validators.ValidationDecision;

import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.List;

public class SessionData implements Serializable {

    private final HttpSession session;
    private final List<ImageStatisticData> imageStatistics = Lists.newArrayList();
    private BufferedImage inputImage;
    private BufferedImage templateImage;
    private BufferedImage resultImage;

    private ValidationDecision.MatchDecision matchDecision;
    private long progress;


    public SessionData(HttpSession session) {
        this.session = session;
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

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public ValidationDecision.MatchDecision getMatchDecision() {
        return matchDecision;
    }

    public void setMatchDecision(ValidationDecision.MatchDecision matchDecision) {
        this.matchDecision = matchDecision;
    }
}
