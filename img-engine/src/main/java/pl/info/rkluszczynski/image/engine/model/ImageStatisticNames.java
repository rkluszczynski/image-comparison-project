package pl.info.rkluszczynski.image.engine.model;

public enum ImageStatisticNames {

    DUMMY_RESULT("Dummy result value");



    private final String name;

    private ImageStatisticNames(String s) {
        name = s;
    }

    public String toString(){
        return name;
    }
}
