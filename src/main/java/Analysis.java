import java.util.Map;

public class Analysis {

    private final Map<String, WordCategorization> categorization;
    private final int numberOfAnalyzedSpamMails;
    private final int numberOfAnalyzedHamMails;

    public Analysis(Map<String, WordCategorization> categorization, int numberOfAnalyzedSpamMails, int numberOfAnalyzedHamMails) {
        this.categorization = categorization;
        this.numberOfAnalyzedSpamMails = numberOfAnalyzedSpamMails;
        this.numberOfAnalyzedHamMails = numberOfAnalyzedHamMails;
    }

    public Map<String, WordCategorization> getCategorization() {
        return categorization;
    }

    public int getNumberOfAnalyzedSpamMails() {
        return numberOfAnalyzedSpamMails;
    }

    public int getNumberOfAnalyzedHamMails() {
        return numberOfAnalyzedHamMails;
    }
}
