
public class Main {


    public static void main(String[] args) {
        BayesSpamFilter bayesSpamFilter = new BayesSpamFilter();

        // Run calibration
        bayesSpamFilter.calibrate(0.0051, 0.96, 0.01);

        // Before uncomment the following line, read the description of the method
        // bayesSpamFilter.autoCalibrate();

        bayesSpamFilter.learn();
        Result result = bayesSpamFilter.runTest("test");
        System.out.println("Resultate");
        System.out.println(result);
    }
}
