public class Main {

    public static void main(String[] args) {
        BayesSpamFilter bayesSpamFilter = new BayesSpamFilter();
        bayesSpamFilter.learn();
        Result result = bayesSpamFilter.classify("");
        bayesSpamFilter.calibrate();
        result = bayesSpamFilter.classify("");

        System.out.println(result);
    }
}
