public class SpamCategorization {

    private int spam;
    private int ham;

    public SpamCategorization() {
        // TODD specify meaningful default values (maybe change to double)
        this.spam = 1;
        this.ham = 1;
    }

    public int getSpam() {
        return spam;
    }

    public int getHam() {
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
