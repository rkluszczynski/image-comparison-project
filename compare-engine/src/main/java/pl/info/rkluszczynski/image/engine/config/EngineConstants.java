package pl.info.rkluszczynski.image.engine.config;

public interface EngineConstants {

    String ENGINE_LIBRARY_BASE_PACKAGE = "pl.info.rkluszczynski.image.engine";


    int MAX_PIXEL_VALUE = 256;

    int TRANSPARENT_THRESHOLD_ALPHA_VALUE = 0x10;


//    Used by best localized scores strategy:

    int BEST_LOCALIZED_SCORES_STRATEGY_AMOUNT = 17;

    int BEST_LOCALIZED_SCORES_OFFSET = 0;
    double BEST_LOCALIZED_SCORES_MIN_SIZE_RATIO = 0.5;


//    Used by Size Supplier:

    int RATIO_2_3_SMALLER_SIZE = 300;
    int RATIO_3_4_SMALLER_SIZE = 300;
    int RATIO_1_1_SMALLER_SIZE = 300;

    int RATIO_2_3_LARGER_SIZE = 450;
    int RATIO_3_4_LARGER_SIZE = 400;
    int RATIO_1_1_LARGER_SIZE = 300;
}
