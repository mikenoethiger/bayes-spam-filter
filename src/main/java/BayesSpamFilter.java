
import java.util.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
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
     * Threshold between 0 and 1 which defines at which point an email is considered spam.
     * E.g. if the probability of an email being spam was calculated to be 0.6 and threshold is set
     * to 0.7, the email is considered spam.
     */
    private double spamThreshold = 0.5;

    private double alpha = 0.5;
    /**
     * Storing analysed data
     */
    private Analysis db;

    /**
     * Reads the learning directories and updates the {@link Analysis} db.
     */
    public void learn() {

        final String hamDir  = "ham-anlern";
        final String spamDir = "spam-anlern";
        final Map<String, WordCategorization> map = new HashMap<>();

        final File [] filesHam  = listDirectory(hamDir);
        final File [] filesSpam = listDirectory(spamDir);

        indexMap(map, filesHam,  ( cat -> cat != null ? cat.incHam()  : new WordCategorization().incHam()));
        indexMap(map, filesSpam, ( cat -> cat != null ? cat.incSpam() : new WordCategorization().incSpam()));

        db = new Analysis(map, hamDir.length(), spamDir.length());
    }

    public void learnStatic() {
        final Map<String, WordCategorization> map = new HashMap<>();

        map.put("online", new WordCategorization().addToHam(3).addToSpam(8));
        map.put("haben", new WordCategorization().addToHam(30).addToSpam(7));
        db = new Analysis(map, 100, 100);
    }

    /**
     * Indexes the given map with the given files.
     *
     * @param map         Map with all the indexed data
     * @param files       All <code>files</code> to index.
     * @param addFuncion  Function which determines which value is put into the map.
     */
    private void indexMap (
            Map<String, WordCategorization> map,
            File[] files,
            Function<WordCategorization, WordCategorization> addFuncion) {

        Set<String> uniqueWords;

        for (File email : files) {
            uniqueWords = uniqueWordsFromEMail(email);

            for (String word : uniqueWords) {
                WordCategorization cat = map.get(word);
                map.put( word,  addFuncion.apply(cat));
            }
        }
    }

    /**
     * Reads the <code>emails</code> (files) content and returns all unique words contained in {@link Set<String>}
     *
     * @param email File to analyse
     * @return {@link Set<String>} with all unique words
     */
    private Set<String> uniqueWordsFromEMail(File email) {
        Set<String> uniqueWords = new HashSet<>();

        try(BufferedReader bufferedReader = getBufferedReader(email)) {

            String line;
            while(( line = bufferedReader.readLine()) != null) {

                String[] toIndex = line.split(" ");
                uniqueWords.addAll(Arrays.asList(toIndex));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return uniqueWords;
    }

    /**
     * List files in directory.
     *
     * @param dir Directory (relative to src/main/resources)
     * @return <code>File[]</code>: Files contained in <code>dir</code>
     */
    private File[] listDirectory(String dir) {
        return  new File(getClass().getClassLoader().getResource(dir).getFile()).listFiles();
    }

    /**
     * Creates a {@link BufferedReader} form the given <code>file</code>.
     *
     * @param file File located in directory (relative to src/main/resources)
     * @return {@link BufferedReader} to <code>file</code>
     * @throws IOException
     */
    private BufferedReader getBufferedReader(String file) throws IOException {
        URL url = getClass().getClassLoader().getResource(file);
        return getBufferedReader((url).getFile());
    }

    /**
     * Creates a {@link BufferedReader} form the given <code>file</code>.
     *
     * @param file File located in directory (relative to src/main/resources)
     * @return {@link BufferedReader} to <code>file</code>
     * @throws IOException
     */
    private BufferedReader getBufferedReader(File file) throws IOException {
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
        return new BufferedReader(reader);
    }

    private void persistAnalysationData(Map<String, WordCategorization> db) {
        // TODO @Marc
    }

    private void importAnalysationData(Map<String, WordCategorization> db) {
        // TODO @Marc
    }

    public void calibrate(double alpha, double sProbability, double spamThreshold) {
        // TODO @Mike calibrates the sProbability and hProbability to be as precise as possible
        this.alpha = alpha;
        this.sProbability = sProbability;
        this.hProbability = 1-sProbability;
        this.spamThreshold = spamThreshold;
    }

    /**
     * Classify all mails in src/main/resources/ham-test and src/main/resources/ham-test
     * using the {@link #classify(Set)} method and return threshold, alpha
     * as well as success rate in a {@link Result}.
     *
     * @return
     */
    public Result runTest() {
        File[] spamEmails = listDirectory("spam-test");
        File[] hamEmails = listDirectory("ham-test");
        int totalMails = spamEmails.length + hamEmails.length;
        int correctClassification = 0;
        for (File email : spamEmails) {
            boolean isSpam = classify(uniqueWordsFromEMail(email));
            if (isSpam) correctClassification++;
        }
        for (File email: hamEmails) {
            boolean isSpam = classify(uniqueWordsFromEMail(email));
            if (!isSpam) correctClassification++;
        }
        double successRate = (double) correctClassification / (double) totalMails;

        return new Result(spamThreshold, alpha, successRate);
    }

    /**
     * Classifies the email as Spam or no Spam
     * @param emailWords
     * @return
     */
    public boolean classify(Set<String> emailWords) {
        double spamProbability = calcProbability(emailWords);
        return spamProbability >= spamThreshold;
    }

    /**
     * Calculates the probability of being a spam according to http://www.math.kit.edu/ianm4/~ritterbusch/seite/spam/de
     * and http://www.math.kit.edu/ianm4/~ritterbusch/seite/spam/de
     * @param words
     * @return
     */
    public double calcProbability(Set<String> words){
                                            // Mathematical definitions according to wikipedia
                                            // Multiplied for each word as shown in math.kit.edu
        double dividend = sProbability;     // Pr(W|S) * Pr(S)
        double divisor1 = 0;                // Pr(W|S) * Pr(S)
        double divisor2 = hProbability;     // Pr(W|H) * Pr(H)
        double divisor = 0;                 // Pr(W|S) * Pr(S) + Pr(W|H) * Pr(H)

        for(String w : words){
            WordCategorization cat = db.getCategorization().get(w);
            double spam = cat != null ? cat.getSpam() : alpha;
            double ham  = cat != null ? cat.getHam()  : alpha;

            dividend *= ( spam == 0 ? alpha : spam / db.getNumberOfAnalyzedSpamMails());
            divisor2 *= ( ham  == 0 ? alpha : ham  / db.getNumberOfAnalyzedHamMails());
        }
        divisor1 = dividend;

        divisor = divisor1 + divisor2;
        return dividend / divisor;          //Pr(S | W)
    }

    private Stream<String> readFromClasspath(String realativePath) {
        return null;
    }

}
