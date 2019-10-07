import java.io.IOException;

public class Main {

    // TODO marc remove IOException
    public static void main(String[] args) throws IOException {
        BayesSpamFilter bayesSpamFilter = new BayesSpamFilter();
        bayesSpamFilter.learn();
        Result result = bayesSpamFilter.classify("");
        bayesSpamFilter.calibrate();
        result = bayesSpamFilter.classify("");

        System.out.println(result);
    }
}
