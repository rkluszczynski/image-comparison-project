package pl.info.rkluszczynski.image.web.model;

/**
 * Created by Rafal on 2014-07-04.
 */
public class ProcessingStatus {
    private final long percent;
    private final int resultCode;

    public ProcessingStatus(long percent, int resultCode) {
        this.percent = percent;
        this.resultCode = resultCode;
    }

    public long getPercent() {
        return percent;
    }

    public int getResultCode() {
        return resultCode;
    }

    @Override
    public String toString() {
        return String.format("{ \"percent\": %d, \"resultCode\": %d }", percent, resultCode);
    }
}
