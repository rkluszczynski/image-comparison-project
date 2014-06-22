package pl.info.rkluszczynski.image.engine.model.strategies;

public class MatchScore implements Comparable {
    private final double score;
    private final int widthPosition;
    private final int heightPosition;
    private final double scaleFactor;

    private String description = "";

    public MatchScore(double score, int widthPosition, int heightPosition, double scaleFactor) {
        this.score = score;
        this.widthPosition = widthPosition;
        this.heightPosition = heightPosition;
        this.scaleFactor = scaleFactor;
    }

    @Override
    public int compareTo(Object o) {
        MatchScore matchScore = (MatchScore) o;
        if (score < matchScore.getScore()) {
            return -1;
        } else if (score > matchScore.getScore()) {
            return 1;
        }
        return 0;
    }

    public double getScore() {
        return score;
    }

    public int getWidthPosition() {
        return widthPosition;
    }

    public int getHeightPosition() {
        return heightPosition;
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
