import java.io.*;
import java.util.*;

abstract class AbstractController {
  private List<MyString> corpus;

  abstract void outputFailure(String message);

  abstract void outputResults(int numCollisions);

  // returns null on failure
  abstract InputStream getCorpusStream() throws IOException;

  private void initializeCorpus() throws IOException {
    try (InputStream is = getCorpusStream()) {
      corpus = new ArrayList<>();
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
        String line;
        while ((line = reader.readLine()) != null) {
          corpus.add(new MyString(line));
        }
      }
    }
  }

  private int simulate(int buckets) {
    HashSet<MyString> set = new HashSet<>(buckets);
    for (MyString line : corpus) {
      set.add(line);
    }
    return set.getNumCollisions();
  }

  private boolean isHashFunctionValid() {
    // Verify that two distinct strings with the same contents ("hello")
    // have the same hash code. This doesn't guarantee the correctness of
    // the code, of course, but provides a sanity check.
    MyString s1 = new MyString("hello");
    MyString s2 = new MyString(new StringBuilder("h").append("ello").toString());
    return s1.hashCode() == s2.hashCode();
  }

  void simulateAllSizes() {
    try {
      if (!isHashFunctionValid()) {
        outputFailure("hashCode returns different values for equal strings");
        return;
      }

      // Initialize the corpus.
      try {
        initializeCorpus();
      } catch (IOException e) {
        outputFailure("Problem loading corpus: " + e.getMessage());
        return;
      }

      // Simulate with different numbers of buckets.
      int numCollisions = simulate(corpus.size() / 2)
          + simulate(corpus.size())
          + simulate(corpus.size() * 2)
          + simulate(corpus.size() * 3);

      // Output the results.
      outputResults(numCollisions);
    } catch (Exception e) {
      // This could happen if MyString.hashCode() throws an exception.
      outputFailure("Unexpected error: " + e.getMessage());
    }
  }
}
