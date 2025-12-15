import java.io.*;

public class GradescopeController extends AbstractController {
    private static final String RESULTS_TEMPLATE = """
            {
                "score": 1.0,
                "output": "Successful run with %1$d collisions",
                "leaderboard": [
                    {"name": "Collisions",  "value": %1$d, "order": "asc"}
                ]
            }
            """;
    private static final String FAILURE_TEMPLATE = """
            {
                "score": 1.0,
                "name": "%s"
            }
            """;

    @Override
    void outputFailure(String message) {
        System.out.printf(FAILURE_TEMPLATE, message);
    }

    @Override
    void outputResults(int numCollisions) {
        System.out.printf(RESULTS_TEMPLATE, numCollisions);
    }

    @Override
    InputStream getCorpusStream() throws IOException {
        InputStream is = GradescopeController.class.getResourceAsStream("corpus.txt");
        if (is == null) {
            throw new IOException("Unable to open corpus.txt");
        } else {
            return is;
        }
    }

    public static void main(String[] args) {
        new GradescopeController().simulateAllSizes();
    }
}
