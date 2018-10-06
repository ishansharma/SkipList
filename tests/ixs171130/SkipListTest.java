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

        s.rebuild();
        it = s.iterator();
        // make sure we have the correct order
        for (int i = 0; i < 2000000; i++) {
            out[i] = it.next();
            expected[i] = i + 1;
        }
        System.out.println(Arrays.toString(out));
//        s.printAllLevelOfCurrent();
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
        SkipList<Integer> integerSkipList = new SkipList<>();
        Integer[] toAdd = {1, 4, 2, 7, 5, 9, -10, 654, 34, 12, 86, -20, -999, 999, 100000, -1000000};
        Integer[] expected = {-1000000, -999, -20, -10, 1, 2, 4, 5, 7, 9, 12, 34, 86, 654, 999, 100000};

        for (int x : toAdd) {
            integerSkipList.add(x);
        }

        Integer[] res = new Integer[16];
        Iterator<Integer> it = integerSkipList.iterator();
        int index = 0;
        while (it.hasNext()) {
            res[index] = it.next();
            index++;
        }
        assertEquals(Arrays.toString(expected), Arrays.toString(res));

        integerSkipList.rebuild();
        System.out.println(Arrays.toString(res));
        integerSkipList.printAllLevelOfCurrent();

        Integer[] toAdd1 = { 9, 1, 4, 2, 5, 6, 8, 3,7, 10, -1, 0, 13};
        Integer[] expected1 = { -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13};
        SkipList<Integer> integerSkipList1 = new SkipList<>();
        for (int x : toAdd1) {
            integerSkipList1.add(x);
        }

        Integer[] res1 = new Integer[13];
        Iterator<Integer> it1 = integerSkipList1.iterator();
        int index1 = 0;
        while (it1.hasNext()) {
            res1[index1] = it1.next();
            index1++;
        }
        assertEquals(Arrays.toString(expected1), Arrays.toString(res1));

        integerSkipList1.rebuild();
        System.out.println(Arrays.toString(res1));
        integerSkipList1.printAllLevelOfCurrent();


        Integer[] toAdd2 = { 0 };
        Integer[] expected2 = { 0 };
        SkipList<Integer> integerSkipList2 = new SkipList<>();
        for (int x : toAdd2) {
            integerSkipList2.add(x);
        }

        Integer[] res2 = new Integer[1];
        Iterator<Integer> it2 = integerSkipList2.iterator();
        int index2 = 0;
        while (it2.hasNext()) {
            res2[index2] = it2.next();
            index2++;
        }
        assertEquals(Arrays.toString(expected2), Arrays.toString(res2));

        integerSkipList2.rebuild();
        System.out.println(Arrays.toString(res2));
        integerSkipList2.printAllLevelOfCurrent();

        Integer[] toAdd3 = { 0, 1, 2, 3 };
        Integer[] expected3 = { 0, 1, 2, 3 };
        SkipList<Integer> integerSkipList3 = new SkipList<>();
        for (int x : toAdd3) {
            integerSkipList3.add(x);
        }

        Integer[] res3 = new Integer[4];
        Iterator<Integer> it3 = integerSkipList3.iterator();
        int index3 = 0;
        while (it3.hasNext()) {
            res3[index3] = it3.next();
            index3++;
        }
        assertEquals(Arrays.toString(expected3), Arrays.toString(res3));

        integerSkipList3.rebuild();
        System.out.println(Arrays.toString(res3));
        integerSkipList3.printAllLevelOfCurrent();
    }
}