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

    // This is the JDK 1 String.hashCode() implementation.
    // https://github.com/barismeral/Java-JDK-1.0-src/blob/master/src/java/lang/String.java
    @Override
    public int hashCode() {
        int h = 0;
        int off = 0;
        char val[] = s.toCharArray();
        int len = s.length();

        if (len < 16) {
            for (int i = len ; i > 0; i--) {
                h = (h * 37) + val[off++];
            }
        } else {
            // only sample some characters
            int skip = len / 8;
            for (int i = len ; i > 0; i -= skip, off += skip) {
                h = (h * 39) + val[off];
            }
        }

        return h;
    }

    // This is used only for the GitHub Classroom implementation, not Gradescope.
    public static String getPseudonym() {
        return "Java 1";
    }

    public static void main(String[] args) {
        String[] testStrings = {"hello", "world"};
        for (String s : testStrings) {
            System.out.printf("%s -> %d\n", s, new MyString(s).hashCode());
        }
    }
}
