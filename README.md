# Java Hash Exercise

This material scaffolds an in-class exercise on writing Java `hashCode()` functions.
Its purposes are:

* illustrating how user-written hash functions affect hash set performance
* challenging students to think about what makes a good hash function for a given
  corpus

## Classroom use

The recommended usage (supported by these [PowerPoint slides](java-hash-exercise.pptx)) is:

1. Going over the implementation of [HashSet.java](src/main/java/HashSet.java)
   with students, showing them how the `hashCode()` function is used.
2. Discussing the contract for `hashCode()`, which requires `equals()` objects
   to have equal hash codes. Point out that this is a legal implementation but
   that it will lead to maximal collisions, reducing the set table's performance
   to that of a linked list:

```java
@Override
public int hashCode(){
    return 42;
}
```

3. Providing students with [MyString.java](src/main/java/MyString.java) and
   challenge them to write a better hash function without calling any existing
   implementations of `hashCode()`. (It is up to you whether to provide any
   examples of good hash functions or allow them access to online resources.)
4. Having students submit their files to a Gradescope assignment you create for
   immediate feedback on the number of collisions their function causes on a
   hidden (or visible) corpus.
5. Sharing the implementations that perform the best, either in that class
   session or the next one.

## Creating the Gradescope assignment

Follow [video instructions to create a Gradescope
assignment](https://northeastern.hosted.panopto.com/Panopto/Pages/Viewer.aspx?id=e634c84d-611f-4958-a79e-b011012b70c5),
which can be summarized as follows:

1. Download `autograder.zip` from the latest release or build your zip file
   by cloning this repository and running `./make_autograder.sh`.
2. Create
   a [Programming Assignment](https://help.gradescope.com/article/ujutnle52h-instructor-assignment-programming)
   on Gradescope, enabling a leaderboard.
3. Configure the autograder, which includes:
    1. Selecting the latest Ubuntu image.
    2. Selecting JDK 17.
    3. Uploading `autograder.zip`.
    4. Clicking on `Update Autograder`.
    5. Uploading a test submission from
       a [sample-submissions](sample-submissions)
       subdirectory.

## Customizations

Here are ideas for customizing the assignment:

1. Replacing the corpus (currently a list of winners of SIGCSE prizes) with
   another corpus, such as the names of your students or a list of courses
   offered at your university.
2. De-generifying `HashSet` to simplify the code.
3. Replacing `MyString` with a class students have used for another assignment.

## Recommended Reading

Anyone interested in hash functions, especially for Java, should read
**Item 11: Always override hashCode when you override equals** in
*Effective Java* (3rd edition) by Joshua Bloch (Pearson, 2018).

The [Java 1 `String.hashCode()` implementation](
https://github.com/barismeral/Java-JDK-1.0-src/blob/master/src/java/lang/String.java)
is an interesting historical relic that has the curious
property of O(1) runtime.
