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
        // Try to make different strings return different values.
        // You must not call any other implementation of hashCode().
        return 37;
    }

    public static void main(String[] args) {
        String[] testStrings = {"hello", "world"};
        for (String s : testStrings) {
            System.out.printf("%s -> %d\n", s, new MyString(s).hashCode());
        }
    }
}
