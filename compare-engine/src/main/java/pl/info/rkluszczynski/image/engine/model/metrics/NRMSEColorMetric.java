package pl.info.rkluszczynski.image.engine.model.metrics;

public class NRMSEColorMetric extends RMSEColorMetric {
    private double maxValue;
    private double minValue;

    @Override
    public void resetValue() {
        super.resetValue();
        maxValue = Double.MIN_VALUE;
        minValue = Double.MAX_VALUE;
    }

    @Override
    public double calculateValue() {
        return (super.calculateValue() * super.maxValue()) / (maxValue - minValue);
    }

    @Override
    public double maxValue() {
        return 1.;
    }

    @Override
    public void addPointDifference(double value1, double value2) {
        super.addPointDifference(value1, value2);

        maxValue = Math.max(maxValue, value1);
        maxValue = Math.max(maxValue, value2);

        minValue = Math.min(minValue, value1);
        minValue = Math.min(minValue, value2);
    }

    @Override
    public String getName() {
        return "NRMSE";
    }
}
