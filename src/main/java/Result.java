public class Result {

    private final double threshold;
    private final double alpha;
    private final double successRate;

    public Result(double threshold, double alpha, double successRate) {
        this.threshold = threshold;
        this.alpha = alpha;
        this.successRate = successRate;
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
        return result;
    }
}
