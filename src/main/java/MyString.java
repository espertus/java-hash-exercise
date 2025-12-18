final class MyString {
    private final String s;

    public MyString(String s) {
        this.s = s;
    }

    /**
     * Shows the hashcode value of two strings.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        String[] testStrings = {"hello", "world"};
        for (String s : testStrings) {
            System.out.printf("%s -> %d\n", s, new MyString(s).hashCode());
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof MyString) {
            return s.equals(((MyString) other).s);
        }
        return false;
    }

    // Do not change anything above this comment.

    /**
     * Returns the pseudonym to display on the GitHub leaderboard.
     * Pseudonyms may be up to 30 characters long and should contain
     * only alphanumeric characters, space, underscore, and hyphen.
     * This is not used with Gradescope.
     *
     * @return the pseudonym for the leaderboard
     */
    public static String getPseudonym() {
        return "CHANGEME"; // Replace "CHANGEME" with your desired pseudonym
    }

    @Override
    public int hashCode() {
        // Try to make different strings return different values.
        // You must not call any other implementation of hashCode().
        return 37;
    }
}
