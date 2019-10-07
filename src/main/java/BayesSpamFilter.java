import java.util.Map;
import java.util.stream.Stream;

public class BayesSpamFilter {

    /**
     * Assumption for base probability of an email being spam
     */
    private double sProbability = 0.5;
    /**
     * Assumption for base probability of an email being ham
     */
    private double hProbability = 1-sProbability;
    /**
     * // TODO define meaningful threshold
     * Threshold between 0 and 1 which defines at which point an email is considered spam.
     * E.g. if the probability of an email being spam was calculated to be 0.6 and threshold is set
     * to 0.7, the email is considered spam.
     */
    private final double spamThreshold = 0.5;

    private Map<String, SpamCategorization> db;

    public Map<String, SpamCategorization> learn() {
        // TODO @Marc
        // stupid split by space
//        if (read())
//        persist();
        return null;
    }

    private void persistAnalysationData(Map<String, SpamCategorization> db) {
        // TODO @Marc
    }

    private void importAnalysationData(Map<String, SpamCategorization> db) {
        // TODO @Marc
    }

    public void calibrate() {
        // TODO @Mike calibrates the sProbability and hProbability to be as precise as possible
    }

    public Result classify(String email) {
        // TODO @Steve
        return null;
    }

    private Stream<String> readFromClasspath(String realativePath) {
        return null;
    }

}
