package pl.info.rkluszczynski.image.engine.model;

public enum ImageStatisticNames {

    METRIC_VALUE_ABS("Metric ABS value"),
    METRIC_VALUE_RMSE("Metric RMSE value"),
    METRIC_VALUE_NRMSE("Metric NRMSE value"),
    METRIC_VALUE_PSNR("Metric PSNR value"),

    DIFFERENCE_COEFFICIENT("Difference coefficient value"),

    DUMMY_RESULT("Dummy result value"),
    ERROR_RESULT("Error occurred"),

    CALCULATION_TIME("Calculation time (in sec.)");

    private final String name;

    private ImageStatisticNames(String s) {
        name = s;
    }

    public String toString() {
        return name;
    }
}
