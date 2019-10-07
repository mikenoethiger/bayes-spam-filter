public class Main {

    public static void main(String[] args) {
        BayesSpamFilter bayesSpamFilter = new BayesSpamFilter();
        bayesSpamFilter.learn();
        Result result = bayesSpamFilter.runTest();
        System.out.println("RESULT WITH DEFAULT VALUE");
        System.out.println(result);
        bayesSpamFilter.calibrate();
        System.out.println("RESULT AFTER CALIBRATE");
        result = bayesSpamFilter.runTest();

        System.out.println(result);
    }
}
