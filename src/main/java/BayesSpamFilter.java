
import java.util.*;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class BayesSpamFilter {

    /**
     * Assumption for base probability of an email being spam
     */
    private double sProbability = 0.5;
    /**
     * Assumption for base probability of an email being ham
     */
    private double hProbability = 1 - sProbability;
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

    private File[] spamEmails = null;
    private File[] hamEmails = null;

    /**
     * Set other threshold values.
     *
     * @param alpha
     * @param sProbability
     * @param spamThreshold
     */
    public void calibrate(double alpha, double sProbability, double spamThreshold) {
        this.alpha = alpha;
        this.sProbability = sProbability;
        this.hProbability = 1 - sProbability;
        this.spamThreshold = spamThreshold;
    }

    /**
     * Automatic Calibrates with the best alpha, sProbability and spamThreshold.
     * Calculated by trying values on the ham-/spam-kallibrierung.
     * This method is not time optimised and should only be use for retrieving the parameter.
     * This method needs around 15min to get results.
     */
    public void autoCalibrate() {
        double bestAlpha = 0;
        double bestsProbability = 0;
        double bestSpamThreshold = 0;
        double bestResult = 0;
        learn();
        for (double a = 0.0001; a < 0.01; a += 0.005) {
            calibrate(a, 0, 0);
//          do foreach to change alpha on all instead of learn new
            final double aF = a;
            db.getCategorization().values().stream()
                    .filter(wordCategorization -> wordCategorization.getHam() < 1)
                    .forEach(wordCategorization -> wordCategorization.addToHam((-1 * wordCategorization.getHam()) + aF));
            db.getCategorization().values().stream()
                    .filter(wordCategorization -> wordCategorization.getSpam() < 1)
                    .forEach(wordCategorization -> wordCategorization.addToSpam((-1 * wordCategorization.getSpam()) + aF));

            for (double sProb = 0.01; sProb < 1; sProb += 0.01) {
                for (double sThre = 0.01; sThre < 1; sThre += 0.05) {
                    calibrate(a, sProb, sThre);
                    //run on kallibrierung
                    Result res = runTest("kallibrierung");
                    if (res.getSuccessRate() > bestResult) {
                        bestAlpha = a;
                        bestsProbability = sProb;
                        bestSpamThreshold = sThre;
                        bestResult = res.getSuccessRate();
                    }
                }
            }
        }
        System.out.println("Best Calibration: ");
        System.out.println("Alpha: " + bestAlpha);
        System.out.println("sProbability: " + bestsProbability);
        System.out.println("Spam Threshold: " + bestSpamThreshold);
        System.out.println("Best Result: " + bestResult);
        calibrate(bestAlpha, bestsProbability, bestSpamThreshold);
    }

    /**
     * Classify all mails in src/main/resources/ham-test and src/main/resources/spam-test
     * using the {@link #classify(Set)} method and return threshold, alpha
     * as well as success rate in a {@link Result}.
     *
     * @return
     */
    public Result runTest(String folder) {
        File[] spamEmails;
        File[] hamEmails;


        int foundSpam = 0, foundHam = 0;
        if (folder.equals("kallibrierung") && this.spamEmails != null) {
            spamEmails = this.spamEmails;
            hamEmails = this.hamEmails;
        } else {
            spamEmails = listDirectory("spam-" + folder);
            hamEmails = listDirectory("ham-" + folder);
        }
        int totalMails = spamEmails.length + hamEmails.length;
        int correctClassification = 0;
        for (File email : spamEmails) {
            boolean isSpam = classify(uniqueWordsFromEMail(email));
            if (isSpam) {
                correctClassification++;
                foundSpam++;
            }
        }

        for (File email : hamEmails) {
            boolean isSpam = classify(uniqueWordsFromEMail(email));

            if (!isSpam) {
                correctClassification++;
                foundHam++;
            }
        }
        double successRate = (double) correctClassification / (double) totalMails;
        return new Result(spamThreshold, alpha, successRate, foundSpam, spamEmails.length, foundHam, hamEmails.length);
    }

    /**
     * Reads the learning directories and updates the {@link Analysis} db.
     */
    public void learn() {

        final String hamDir = "ham-anlern";
        final String spamDir = "spam-anlern";
        final Map<String, WordCategorization> map = new HashMap<>();

        final File[] filesHam = listDirectory(hamDir);
        final File[] filesSpam = listDirectory(spamDir);

        indexMap(map, filesHam, (cat -> cat != null ? cat.incHam() : new WordCategorization().incHam()));
        indexMap(map, filesSpam, (cat -> cat != null ? cat.incSpam() : new WordCategorization().incSpam()));

        db = new Analysis(map, filesHam.length, filesSpam.length);
    }

    /**
     * Indexes the given map with the given files.
     *
     * @param map        Map with all the indexed data
     * @param files      All <code>files</code> to index.
     * @param addFuncion Function which determines which value is put into the map.
     */
    private void indexMap(
            Map<String, WordCategorization> map,
            File[] files,
            Function<WordCategorization, WordCategorization> addFuncion) {

        Set<String> uniqueWords;

        for (File email : files) {
            uniqueWords = uniqueWordsFromEMail(email);

            for (String word : uniqueWords) {
                WordCategorization cat = map.get(word);
                map.put(word, addFuncion.apply(cat));
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

        try (BufferedReader bufferedReader = getBufferedReader(email)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {

                String[] toIndex = line.split(" ");
                uniqueWords.addAll(Arrays.asList(toIndex));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return uniqueWords;
    }

    /**
     * Classifies the email as Spam or no Spam
     *
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
     *
     * @param words
     * @return
     */
    public double calcProbability(Set<String> words) {
        // Mathematical definitions according to wikipedia
        // https://en.wikipedia.org/wiki/Naive_Bayes_spam_filtering
        // --> Other expression of the formula for combining individual probabilities

        double numberSpam = db.getNumberOfAnalyzedSpamMails();
        double numberHam  = db.getNumberOfAnalyzedHamMails();

        double eta = 0;
        double spamWordPropability = 0.0d;

        double spamWordPercentage = alpha;
        double hamWordPercentage  = alpha;
        WordCategorization wordCategory;

        for (String w : words) {
            wordCategory = db.getCategorization().get(w);
            spamWordPercentage         = alpha;
            hamWordPercentage          = alpha;

            if (wordCategory != null) {
                spamWordPercentage = wordCategory.getSpam() != 0 ? wordCategory.getSpam() / numberSpam : alpha;
                hamWordPercentage  = wordCategory.getHam()  != 0 ? wordCategory.getHam()  / numberHam : alpha;
            }

            spamWordPropability =
              ( spamWordPercentage * sProbability )
            / ( spamWordPercentage * sProbability + hamWordPercentage * hProbability );

            eta += Math.log(1 - spamWordPropability) - Math.log(spamWordPropability);
        }

        return 1 / ( 1 + Math.pow(Math.E, eta ));
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

    /**
     * List files in directory.
     *
     * @param dir Directory (relative to src/main/resources)
     * @return <code>File[]</code>: Files contained in <code>dir</code>
     */
    private File[] listDirectory(String dir) {
        return new File(getClass().getClassLoader().getResource(dir).getFile()).listFiles();
    }

    /**
     * Method used to test the example on http://www.math.kit.edu/ianm4/~ritterbusch/seite/spam/de
     */
    public void learnStatic() {
        final Map<String, WordCategorization> map = new HashMap<>();

        map.put("online", new WordCategorization().addToHam(3).addToSpam(8));
        map.put("haben", new WordCategorization().addToHam(30).addToSpam(7));
        db = new Analysis(map, 100, 100);
    }

}
