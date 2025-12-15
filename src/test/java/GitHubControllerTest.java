import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class GitHubControllerTest {

  ByteArrayOutputStream outBytes;
  ByteArrayOutputStream errBytes;
  PrintStream outStream;
  PrintStream errStream ;

  private void assertOutputOccurred() {
    assert(!outBytes.toString().isEmpty());
  }

  private void assertNoOutputOccurred() {
    assert(outBytes.toString().isEmpty());
  }

  private void assertErrorOccurred() {
    assert(!errBytes.toString().isEmpty());
  }

  private void assertNoErrorOccurred() {
    assert(errBytes.toString().isEmpty());
  }

  @BeforeEach
  void setUp() {
    outBytes = new ByteArrayOutputStream();
    errBytes = new ByteArrayOutputStream();
    outStream = new PrintStream(outBytes);
    errStream = new PrintStream(errBytes);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"!!@", "%", " ", "   "})
  void testInvalidPseudonym(String pseudonym) {
    assertFalse(GitHubController.validatePseudonym(pseudonym, outStream, errStream));
    assertErrorOccurred();
    assertNoOutputOccurred();
  }

  @ParameterizedTest
  @ValueSource(strings = {" x", "$$a%%", "a b"})
  void testValidPseudonym(String pseudonym) {
    assertTrue(GitHubController.validatePseudonym(pseudonym, outStream, errStream));
    assertOutputOccurred();
    assertNoErrorOccurred();
  }

  @ParameterizedTest
  @CsvSource({
      "'space s', '  space s '",
      "a, ##a!!",
      "hello-world, hello-world"
  })
  void testTransformPseudonym(String expected, String pseudonym) {
    assertTrue(GitHubController.validatePseudonym(pseudonym, outStream, errStream));
    assertOutputOccurred();
    assertNoErrorOccurred();
  }
}
