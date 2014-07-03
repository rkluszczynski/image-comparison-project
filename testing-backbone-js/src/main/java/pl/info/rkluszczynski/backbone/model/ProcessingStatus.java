package pl.info.rkluszczynski.backbone.model;

public class ProcessingStatus {

    private final int percent;
    private final int code;

    public ProcessingStatus(int percent, int code) {
        this.percent = percent;
        this.code = code;
    }

    public int getPercent() {
        return percent;
    }

    public int getCode() {
        return code;
    }
}
