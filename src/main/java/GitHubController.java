import java.io.*;

public class GitHubController extends AbstractController {

  public static final int MAX_PSEUDONYM_LENGTH = 30;

  @Override
  InputStream getCorpusStream() throws FileNotFoundException {
    return new FileInputStream("corpus.txt");
  }

  @Override
  void outputFailure(String message) {
    System.err.println("ERROR: " + message);
    System.exit(1);
  }

  @Override
  void outputResults(int numCollisions) {
    System.out.println("COLLISIONS: " + numCollisions);
  }

  private static boolean validatePseudonym() throws IllegalArgumentException {
    return validatePseudonym(MyString.getPseudonym(), System.out, System.err);
  }

  // If the pseudonym is valid, this outputs it and returns true.
  // Otherwise, this writes an error message and returns false.
  static boolean validatePseudonym(String pseudonym, PrintStream outputStream, PrintStream errorStream) {
    if (pseudonym == null || pseudonym.isBlank() || pseudonym.equalsIgnoreCase("CHANGEME")) {
      errorStream.println("ERROR: Please set your pseudonym in MyString.pseudonym()");
      return false;
    }
    // Eliminate leading and trailing white space and illegal characters.
    pseudonym = pseudonym.trim().replaceAll("[^a-zA-Z0-9_\\- ]", "").trim();
    if (pseudonym.length() > MAX_PSEUDONYM_LENGTH) {
      pseudonym = pseudonym.substring(0, MAX_PSEUDONYM_LENGTH);
    }
    if (pseudonym.isEmpty()) {
      errorStream.println(
          "ERROR: Pseudonym must contain at least one legal character (an alphanumeric character, underscore, or hyphen)");
      return false;
    }
    outputStream.println("PSEUDONYM: " + pseudonym);
    return true;
  }

  public static void main(String[] args) {
    if (!validatePseudonym()) {
      System.exit(1);
    }
    new GitHubController().simulateAllSizes();
  }
}
