package pl.info.rkluszczynski.image.standalone.db;

import java.util.HashMap;
import java.util.Map;

public enum ProcessedImageStatus {

    NEW(0, "New source image."),
    STARTED(1, "Image processing started..."),
    DONE(2, "Processing done."),
    FAILED(3, "Processing FAILED!");

    private int code;
    private String description;

    /**
     * A mapping between the integer code and its corresponding status to facilitate lookup by code.
     */
    private static Map<Integer, ProcessedImageStatus> codeToStatusMapping;

    private ProcessedImageStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ProcessedImageStatus getStatus(int i) {
        if (codeToStatusMapping == null) {
            initMapping();
        }
        return codeToStatusMapping.get(i);
    }

    private static void initMapping() {
        codeToStatusMapping = new HashMap<Integer, ProcessedImageStatus>();
        for (ProcessedImageStatus s : values()) {
            codeToStatusMapping.put(s.code, s);
        }
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("ProcessedImageStatus{code=%d, description='%s'}", code, description);
    }
}
