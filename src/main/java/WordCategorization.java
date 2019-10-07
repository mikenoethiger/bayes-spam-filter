public class WordCategorization {

    private double spam;
    private double ham;

    public WordCategorization(double alpha) {
        // TODD specify meaningful default values (maybe change to double)
        this.spam = alpha;
        this.ham = alpha;
    }

    public double getSpam() {
        return spam;
    }

    public double getHam() {
        return ham;
    }

    public WordCategorization incSpam() {
        spam++;
        return this;
    }

    public WordCategorization addToSpam(double value) {
        spam += value;
        return this;
    }

    public WordCategorization incHam() {
        ham++;
        return this;
    }

    public WordCategorization addToHam(double value) {
        ham += value;
        return this;
    }
}
