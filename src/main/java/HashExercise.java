import java.io.*;
import java.util.*;

public class HashExercise {
    public static final int NUM_BUCKETS = 50;
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
    private static List<MyString> corpus;

    private static void outputFailure(String message) {
        System.out.printf(FAILURE_TEMPLATE, message);
    }

    private static void outputResults(int numCollisions) {
        System.out.printf(RESULTS_TEMPLATE, numCollisions);
    }

    private static void initializeCorpus() throws IOException, NullPointerException {
        try (InputStream is = HashExercise.class.getResourceAsStream("corpus.txt")) {
            corpus = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    corpus.add(new MyString(line));
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Verify that two distinct strings with the same contents ("hello")
            // have the same hash code. This doesn't guarantee the correctness of
            // the code, of course, but provides a sanity check.
            MyString s1 = new MyString("hello");
            MyString s2 = new MyString(new StringBuilder("h").append("ello").toString());
            if (s1.hashCode() != s2.hashCode()) {
                outputFailure("hashCode returns different values for equal FundiesStrings");
                return;
            }

            // Initialize the corpus.
            try {
                initializeCorpus();
            } catch (IOException | NullPointerException e) {
                outputFailure("Problem loading corpus: " + e.getMessage());
                return;
            }

            // Add every line in the corpus to the hash set.
            HashSet<MyString> set = new HashSet<>(NUM_BUCKETS);
            for (MyString line : corpus) {
                set.add(line);
            }

            // Output the results.
            outputResults(set.getNumCollisions());
        } catch (Exception e) {
            // This could happen if MyString.hashCode() throws an exception.
            outputFailure("Unexpected error: " + e.getMessage());
        }
    }

    static class HashSet<T> {
        private final int capacity;
        private final List<T>[] elements;
        private int numCollisions;

        public HashSet(int capacity) {
            this.capacity = capacity;
            elements = (List<T>[]) (new List[capacity]);
            numCollisions = 0;
        }

        public void add(T value) {
            int index = Math.abs(value.hashCode() % capacity);
            if (elements[index] == null) {
                elements[index] = new LinkedList<T>();
            } else {
                numCollisions++;
            }
            elements[index].add(value);
        }

        public int getNumCollisions() {
            return numCollisions;
        }
    }
}
