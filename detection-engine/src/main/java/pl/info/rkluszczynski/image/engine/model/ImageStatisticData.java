package pl.info.rkluszczynski.image.engine.model;

import java.math.BigDecimal;

public class ImageStatisticData {
    private final ImageStatisticNames statisticName;
    private final BigDecimal statisticValue;

    public ImageStatisticData(ImageStatisticNames statisticName, BigDecimal statisticValue) {
        this.statisticName = statisticName;
        this.statisticValue = statisticValue;
    }

    public ImageStatisticNames getStatisticName() {
        return statisticName;
    }

    public BigDecimal getStatisticValue() {
        return statisticValue;
    }
}
