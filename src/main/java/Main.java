
public class Main {


    public static void main(String[] args) {
        BayesSpamFilter bayesSpamFilter = new BayesSpamFilter();

        // Run calibration
        bayesSpamFilter.calibrate(1.0E-4, 0.8600000000000005, 0.26);


        // Before uncomment the following line, read the description of the method
        // bayesSpamFilter.autoCalibrate();

        bayesSpamFilter.learn();
        Result result = bayesSpamFilter.runTest("test");
        System.out.println("Resultate");
        System.out.println(result);
    }
}
