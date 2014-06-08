package pl.info.rkluszczynski.image.engine.model;

public enum ImageStatisticNames {

    METRIC_VALUE_ABS("Color metric ABS value"),
    METRIC_VALUE_RMSE("Color metric RMSE value"),
    METRIC_VALUE_NRMSE("Color metric NRMSE value"),
    METRIC_VALUE_PSNR("Color metric PSNR value"),

    METRIC_VALUE_ABS_GS("Grayscale metric ABS value"),
    METRIC_VALUE_RMSE_GS("Grayscale metric RMSE value"),
    METRIC_VALUE_NRMSE_GS("Grayscale metric NRMSE value"),
    METRIC_VALUE_PSNR_GS("Grayscale metric PSNR value"),


    METRIC_VALUE_HammingDistance("Hamming distance metric"),
    METRIC_VALUE_SUM("Sum of used metrics"),

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
