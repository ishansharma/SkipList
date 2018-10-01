package ixs171130;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SkipListTest {

    @Test
    void contains() {
        SkipList<String> s = new SkipList<>();
        assertFalse(s.contains("a"));
        assertTrue(s.add("a"));
        assertTrue(s.contains("a"));

        SkipList<Long> l = new SkipList<>();
        for (long i = 0L; i < 10000000L; i++) {
            assertFalse(l.contains(i));
            assertTrue(l.add(i));
            assertTrue(l.contains(i));
        }
    }

    @Test
    void add() {
        SkipList<Integer> s = new SkipList<>();
        assertTrue(s.add(1));
        assertTrue(s.add(2));
        assertFalse(s.add(1));

        // adding a lot of elements
        for (int i = 3; i < 1000000; i++) {
            assertTrue(s.add(i));
        }

        // testing for existing elements
        for (int i = 1; i < 1000000; i++) {
            assertFalse(s.add(i));
        }

        // testing for elements bigger than what we added!
        for (int i = 1000001; i < 10000000; i++) {
            assertTrue(s.add(i));
        }
    }

    @Test
    void first() {
        SkipList<Integer> s = new SkipList<>();
        assertNull(s.first());
        assertTrue(s.add(1));
        assertEquals(Integer.valueOf(1), s.first());
    }
}