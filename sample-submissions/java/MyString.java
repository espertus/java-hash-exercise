// SPDX-License-Identifier: CC0-1.0
// This file is dedicated to the public domain under the CC0 1.0 Universal waiver.
// See https://creativecommons.org/publicdomain/zero/1.0/

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
        return s.hashCode();
    }

    public static void main(String[] args) {
        String[] testStrings = {"hello", "world"};
        for (String s : testStrings) {
            System.out.printf("%s -> %d\n", s, new MyString(s).hashCode());
        }
    }
}
