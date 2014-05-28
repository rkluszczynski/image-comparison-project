package pl.info.rkluszczynski.image.engine.tasks.strategy;

/**
 * Created by Rafal on 2014-05-29.
 */
public class MatchItem implements Comparable {
    private final double result;
    private final int leftPosition;
    private final int topPosition;
    private final double scaleFactor;

    public MatchItem(double result, int leftPosition, int topPosition, double scaleFactor) {
        this.result = result;
        this.leftPosition = leftPosition;
        this.topPosition = topPosition;
        this.scaleFactor = scaleFactor;
    }

    @Override
    public int compareTo(Object o) {
        MatchItem item = (MatchItem) o;
        if (result < item.getResult()) {
            return 1;
        } else if (result > item.getResult()) {
            return -1;
        }
        return 0;
    }

    public double getResult() {
        return result;
    }

    public int getLeftPosition() {
        return leftPosition;
    }

    public int getTopPosition() {
        return topPosition;
    }

    public double getScaleFactor() {
        return scaleFactor;
    }
}
