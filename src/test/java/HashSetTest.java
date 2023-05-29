import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HashSetTest {

    @Test
    public void testIntegersNoCollisions() {
        HashSet<Integer> set = new HashSet<>(10);
        set.add(1);
        set.add(2);
        assertTrue(set.contains(1));
        assertTrue(set.contains(2));
        assertFalse(set.contains(0));
        // hashCode() of an int is that int
        assertEquals(0, set.getNumCollisions());
    }

    @Test
    public void testIntegersWithCollisions() {
        HashSet<Integer> set = new HashSet<>(10);
        set.add(1);
        set.add(2);
        set.add(11);
        assertTrue(set.contains(1));
        assertTrue(set.contains(2));
        assertTrue(set.contains(11)); // collides with 1
        assertFalse(set.contains(0));
        assertEquals(1, set.getNumCollisions());
    }
}
