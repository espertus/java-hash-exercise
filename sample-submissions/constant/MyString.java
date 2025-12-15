class MyString {
    private final String s;

    public MyString(String s) {
        this.s = s;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof MyString) {
            return s.equals(((MyString) other).s);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 37;
    }

    // This is used only for the GitHub Classroom implementation, not Gradescope.
    public static String getPseudonym() {
        return "constant";
    }

    public static void main(String[] args) {
        String[] testStrings = {"hello", "world"};
        for (String s : testStrings) {
            System.out.printf("%s -> %d\n", s, new MyString(s).hashCode());
        }
    }
}
