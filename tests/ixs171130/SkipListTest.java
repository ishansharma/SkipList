package ixs171130;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;

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
        for (int i = 2000000; i >= 1000000; i--) {
            assertTrue(s.add(i));
        }

        Integer[] out = new Integer[2000000];
        Integer[] expected = new Integer[2000000];

        Iterator<Integer> it = s.iterator();
        // make sure we have the correct order
        for (int i = 0; i < 2000000; i++) {
            out[i] = it.next();
            expected[i] = i + 1;
        }

        assertEquals(Arrays.toString(expected), Arrays.toString(out));
    }

    @Test
    void first() {
        SkipList<Integer> s = new SkipList<>();
        assertNull(s.first());
        assertTrue(s.add(1));
        assertEquals(Integer.valueOf(1), s.first());
    }

    @Test
    void iterator() {
        SkipList<Integer> integerSkipList1 = new SkipList<>();

        // add elements till 10000000
        for (int i = 0; i < 10000; i++) {
            integerSkipList1.add(i);
        }

        // see if the elements are in same order
        int index = 0;
        Iterator<Integer> it = integerSkipList1.iterator();
        while (it.hasNext()) {
            assertEquals((Integer) index, it.next());
            index++;
        }

        iteratorIntegerRandom();
    }

    private void iteratorIntegerRandom() {
        // build a skiplist with random order
        SkipList<Integer> integerSkipList2 = new SkipList<>();
        Integer[] toAdd = {1, 4, 2, 7, 5, 9, -10, 654, 34, 12, 86, -20, -999, 999, 100000, -1000000};
        Integer[] expected = {-1000000, -999, -20, -10, 1, 2, 4, 5, 7, 9, 12, 34, 86, 654, 999, 100000};

        for (int x : toAdd) {
            integerSkipList2.add(x);
        }

        Integer[] res = new Integer[16];
        Iterator<Integer> it = integerSkipList2.iterator();
        int index = 0;
        while (it.hasNext()) {
            res[index] = it.next();
            index++;
        }

        assertEquals(Arrays.toString(expected), Arrays.toString(res));
    }

    @Test
    void remove() {
        SkipList<Integer> i = new SkipList<>();

        // remove from empty list
        assertNull(i.remove(0));

        // remove when only one element is there
        i.add(1);
        assertEquals(Integer.valueOf(1), i.remove(1));

        // remove first element when multiple elements were added
        i.add(10);
        i.add(9);
        i.add(5);
        i.add(6);
        i.add(7);
        i.add(123);
        i.add(32);
        i.add(40);
        i.add(41);
        i.add(45);
        i.add(42);

        assertEquals(Integer.valueOf(5), i.remove(5));
        assertNull(i.remove(5));

        // remove last element with a list of multiple elements
        assertEquals(Integer.valueOf(123), i.remove(123));
        assertNull(i.remove(123));

        // make sure element can be added again after removal
        assertTrue(i.add(123));

        // remove an element from the middle
        assertEquals(Integer.valueOf(32), i.remove(32));
        assertNull(i.remove(32));
    }
}