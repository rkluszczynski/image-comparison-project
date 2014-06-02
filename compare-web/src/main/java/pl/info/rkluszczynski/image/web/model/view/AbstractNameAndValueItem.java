package pl.info.rkluszczynski.image.web.model.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AbstractNameAndValueItem implements Comparable {
    private static final Logger logger = LoggerFactory.getLogger(AbstractNameAndValueItem.class);

    private String name;
    private String value;

    protected AbstractNameAndValueItem(String name, String value) {
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

    @Override
    public int compareTo(Object o) {
        AbstractNameAndValueItem nameAndValueItem;
        try {
            nameAndValueItem = (AbstractNameAndValueItem) o;
        } catch (ClassCastException e) {
            logger.warn("Problem with comparing NameAndValueItems !", e);
            return 0;
        }

        int compareResult = name.compareTo(nameAndValueItem.getName());
        if (compareResult == 0) {
            return value.compareTo(nameAndValueItem.getValue());
        }
        return compareResult;
    }
}
