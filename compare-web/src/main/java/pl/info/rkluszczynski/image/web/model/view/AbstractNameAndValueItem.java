package pl.info.rkluszczynski.image.web.model.view;

class AbstractNameAndValueItem {
    protected String name;
    protected String value;

    public AbstractNameAndValueItem(String name, String value) {
        this.name = name;
        this.value = value;
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
