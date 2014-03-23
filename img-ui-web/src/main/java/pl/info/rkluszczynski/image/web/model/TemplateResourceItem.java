package pl.info.rkluszczynski.image.web.model;

public class TemplateResourceItem {
    private String value;
    private String name;

    public TemplateResourceItem(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
