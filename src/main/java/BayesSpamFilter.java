import java.util.*;
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

    /**
     * // TODO define meaningful alpha
     */
    private double alpha = 0.5;

    private Analysis db;

    public Analysis learn() {
        // TODO @Marc
        // stupid split by space
//        if (read())
//        persist();
        return null;
    }

    private void persistAnalysationData(Map<String, WordCategorization> db) {
        // TODO @Marc
    }

    private void importAnalysationData(Map<String, WordCategorization> db) {
        // TODO @Marc
    }

    public void calibrate() {
        // TODO @Mike calibrates the sProbability and hProbability to be as precise as possible
    }

    /**
     * Classify all mails in src/test/resources/ham-test and src/test/resources/ham-test
     * using the {@link #classify(String)} method and return threshold, alpha
     * as well as success rate in a {@link Result}
     *
     * @return
     */
    public Result runTest() {
        List<String> spamEmails = new ArrayList<>();
        List<String> hamEmails = new ArrayList<>();
        int totalMails = spamEmails.size() + hamEmails.size();
        int correctClassification = 0;
        for (String email : spamEmails) {
            boolean isSpam = classify(email);
            if (isSpam) correctClassification++;
        }
        for (String email: hamEmails) {
            boolean isSpam = classify(email);
            if (!isSpam) correctClassification++;
        }
        double successRate = (double) correctClassification / (double) totalMails;

        return new Result(spamThreshold, alpha, successRate);
    }

    /**
     * Classifies the email as Spam or no Spam
     * @param email
     * @return
     */
    public boolean classify(String email) {
        String[] words = email.split(" ");
        Set<String> wordsElementary = new HashSet<String>();
        for(String w : words){
            wordsElementary.add(w);
        }
        double spamProbability = calcProbability(wordsElementary);
        return spamProbability >= spamThreshold;
    }

    /**
     * Calculates the probability of being a spam according to http://www.math.kit.edu/ianm4/~ritterbusch/seite/spam/de
     * @param words
     * @return
     */
    private double calcProbability(Set<String> words){
        double dividend = sProbability; //
        double divisor1 = 0;
        double divisor2 = hProbability;
        double divisor = 0;

        for(String w : words){
            dividend *= (db.getCategorization().get(w).getSpam() / db.getNumberOfAnalyzedSpamMails());
            divisor2 *= (db.getCategorization().get(w).getHam() / db.getNumberOfAnalyzedHamMails());
        }
        divisor1 = dividend;

        divisor = divisor1 + divisor2;
        return dividend / divisor;
    }

    private Stream<String> readFromClasspath(String realativePath) {
        return null;
    }

}
