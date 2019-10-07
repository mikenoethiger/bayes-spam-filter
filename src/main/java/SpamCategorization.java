public class SpamCategorization {

    private double spam;
    private double ham;

    public SpamCategorization(double alpha) {
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

    public SpamCategorization incSpam() {
        spam++;
        return this;
    }

    public SpamCategorization incHam() {
        ham++;
        return this;
    }
}
