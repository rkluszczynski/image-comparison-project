package pl.info.rkluszczynski.image.standalone.db;

import java.util.HashMap;
import java.util.Map;

public enum ProcessingStatus {

    NEW(0, "new source image"),
    STARTED(1, "processing started..."),
    DONE(2, "processing done"),
    FAILED(3, "processing FAILED");

    private int code;
    private String description;

    /**
     * A mapping between the integer code and its corresponding status to facilitate lookup by code.
     */
    private static Map<Integer, ProcessingStatus> codeToStatusMapping;

    private ProcessingStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ProcessingStatus getStatus(int i) {
        if (codeToStatusMapping == null) {
            initMapping();
        }
        return codeToStatusMapping.get(i);
    }

    private static void initMapping() {
        codeToStatusMapping = new HashMap<Integer, ProcessingStatus>();
        for (ProcessingStatus s : values()) {
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
        return String.format("ProcessingStatus{code=%d, description='%s'}", code, description);
    }
}
