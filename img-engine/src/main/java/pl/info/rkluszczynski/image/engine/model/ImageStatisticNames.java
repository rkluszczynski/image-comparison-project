package pl.info.rkluszczynski.image.engine.model;

public enum ImageStatisticNames {

    DIFFERENCE_COEFFICIENT("Difference coefficient value"),

    DUMMY_RESULT("Dummy result value"),
    ERROR_RESULT("Error occurred");


    private final String name;

    private ImageStatisticNames(String s) {
        name = s;
    }

    public String toString(){
        return name;
    }
}
