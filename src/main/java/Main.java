import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Main {

    // TODO marc remove IOException
    public static void main(String[] args) throws IOException {
        BayesSpamFilter bayesSpamFilter = new BayesSpamFilter();

        // Static Run
        bayesSpamFilter.learnStatic();
        bayesSpamFilter.calibrate(0.5, 0.9, 0.5);
        Set<String> words = new HashSet<>();
        words.add("online");
        words.add("haben");
        double p = bayesSpamFilter.calcProbability(words);
        System.out.println("STATIC RUN");
        System.out.println("probability=" + p);

        // First Run
        bayesSpamFilter.learn();
        Result result = bayesSpamFilter.runTest();
        System.out.println("1. RUN");
        System.out.println(result);

        // Second Run
        bayesSpamFilter.calibrate(0, 0.6, 0.8);
        bayesSpamFilter.learn();
        System.out.println("2. RUN");
        result = bayesSpamFilter.runTest();
        System.out.println(result);

        // Third Run
        bayesSpamFilter.calibrate(1, 0.1, 0.1);
        bayesSpamFilter.learn();
        System.out.println("3. RUN");
        result = bayesSpamFilter.runTest();
        System.out.println(result);
    }
}
