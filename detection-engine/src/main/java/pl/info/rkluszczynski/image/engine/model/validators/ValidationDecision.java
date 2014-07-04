package pl.info.rkluszczynski.image.engine.model.validators;

/**
 * Created by Rafal on 2014-06-22.
 */
public class ValidationDecision {
    private MatchDecision matchDecision;
    private double scoreValue;

    public ValidationDecision(MatchDecision matchDecision, double scoreValue) {
        this.matchDecision = matchDecision;
        this.scoreValue = scoreValue;
    }

    public MatchDecision getMatchDecision() {
        return matchDecision;
    }

    public double getScoreValue() {
        return scoreValue;
    }

    public enum MatchDecision {
        VALID_MATCH,
        POSSIBLE_MATCH,
        NO_CLEAR_MATCH
    }
}
