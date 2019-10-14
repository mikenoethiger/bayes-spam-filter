public class WordCategorization {

    private double spam;
    private double ham;

    public WordCategorization() {
        this.spam = 0;
        this.ham = 0;
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
