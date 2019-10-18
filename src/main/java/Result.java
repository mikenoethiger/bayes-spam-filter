public class Result {

    private final double threshold;
    private final double alpha;
    private final double successRate;



    private final int spamRecognized;
    private final int spamTotal;
    private final int hamRecognized;
    private final int hamTotal;

    public Result(double threshold, double alpha, double successRate, int spamRecognized, int spamTotal, int hamRecognized, int hamTotal) {
        this.threshold = threshold;
        this.alpha = alpha;
        this.successRate = successRate;
        this.spamRecognized = spamRecognized;
        this.spamTotal = spamTotal;
        this.hamRecognized = hamRecognized;
        this.hamTotal = hamTotal;
    }

    public double getThreshold() {
        return threshold;
    }

    public double getAlpha() {
        return alpha;
    }

    public double getSuccessRate() {
        return successRate;
    }


    @Override
    public String toString() {
        String result = "";
        result += "Schwellenwert: " + threshold + System.lineSeparator();
        result += "Alpha: " + alpha + System.lineSeparator();
        result += "Erkennungsrate: " + successRate + System.lineSeparator();
        result += "Gefundene Spam Mails: " + spamRecognized + " von insgesammt: " + spamTotal + " Spam Mails" + System.lineSeparator();
        result += "Gefundene Ham Mails: " + hamRecognized + " von insgesammt: " + hamTotal + " Ham Mails" + System.lineSeparator();

        return result;
    }
}
