package pl.info.rkluszczynski.image.web.model.view;

public class ImageOperationItem {
    private String value;
    private String description;

    public ImageOperationItem(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
