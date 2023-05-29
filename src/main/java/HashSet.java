import java.util.*;

/**
 * A simplified implementation of hash sets instrumented to count the number
 * of collisions. Unlike {@link java.util.HashSet}, the capacity of this table
 * never grows beyond its initial capacity.
 *
 * @param <T> the type of object stored in the set
 */
public class HashSet<T> {
    private final int capacity;
    private final List<T>[] elements;
    private int numCollisions;

    /**
     * Constructs a hash set with the specified capacity (number of
     * buckets).
     *
     * @param capacity the capacity
     */
    public HashSet(int capacity) {
        this.capacity = capacity;
        elements = (List<T>[]) (new List[capacity]);
        numCollisions = 0;
    }

    /**
     * Adds the specified value to the hash set if it is not already present.
     *
     * @param value the value to add
     */
    public void add(T value) {
        if (contains(value)) {
            return;
        }
        int index = Math.abs(value.hashCode() % capacity);
        if (elements[index] == null) {
            elements[index] = new LinkedList<T>();
        } else {
            numCollisions++;
        }
        elements[index].add(value);
    }

    /**
     * Tests whether the hash set contains the specified value.
     *
     * @param value the value
     * @return true if the value is in the set, false otherwise
     */
    public boolean contains(T value) {
        int index = Math.abs(value.hashCode() % capacity);
        List<T> bucket = elements[index];
        if (bucket == null) {
            return false;
        }
        for (T item : bucket) {
            if (item.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the number of times distinct values hashed to the same
     * bucket during calls of {@link #add(Object)}.
     *
     * @return the number of collisions
     */
    public int getNumCollisions() {
        return numCollisions;
    }
}
