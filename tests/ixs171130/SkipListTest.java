package ixs171130;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

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

//        s.rebuild();
        it = s.iterator();
        // make sure we have the correct order
        for (int i = 0; i < 2000000; i++) {
            out[i] = it.next();
            expected[i] = i + 1;
        }
//        s.printAllLevelOfCurrent();
        assertEquals(Arrays.toString(expected), Arrays.toString(out));
    }

    @Test
    void first() {
        SkipList<Integer> s = new SkipList<>();
        assertNull(s.first());
        assertTrue(s.add(1));
        assertEquals(Integer.valueOf(1), s.first());

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
        assertEquals(Integer.valueOf(-1000000), integerSkipList.first());

        Integer[] toAdd1 = {9, 1, 4, 2, 5, 6, 8, 3, 7, 10, -1, 0, 13};
        Integer[] expected1 = {-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13};
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
        assertEquals(Integer.valueOf(-1), integerSkipList1.first());

        Integer[] toAdd2 = {0};
        Integer[] expected2 = {0};
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
        assertEquals(Integer.valueOf(0), integerSkipList2.first());

        Integer[] toAdd3 = {0, 1, 3};
        Integer[] expected3 = {0, 1, 3};
        SkipList<Integer> integerSkipList3 = new SkipList<>();
        for (int x : toAdd3) {
            integerSkipList3.add(x);
        }

        Integer[] res3 = new Integer[3];
        Iterator<Integer> it3 = integerSkipList3.iterator();
        int index3 = 0;
        while (it3.hasNext()) {
            res3[index3] = it3.next();
            index3++;
        }
        assertEquals(Arrays.toString(expected3), Arrays.toString(res3));
        assertEquals(Integer.valueOf(0), integerSkipList3.first());

        SkipList<Integer> integerSkipList4 = new SkipList<>();
        Integer[] toAdd4 = {1};
        Integer[] expected4 = {1};

        for (int x : toAdd4) {
            integerSkipList4.add(x);
        }

        assertEquals(Integer.valueOf(1), integerSkipList4.first());
    }

    //TODO:
    @Test
    void last() {
        // build a skiplist with random order
        SkipList<Integer> s = new SkipList<>();
        assertNull(s.last());
        assertTrue(s.add(1));
        assertEquals(Integer.valueOf(1), s.last());

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
        assertEquals(Integer.valueOf(100000), integerSkipList.last());

        Integer[] toAdd1 = {9, 1, 4, 2, 5, 6, 8, 3, 7, 10, -1, 0, 13};
        Integer[] expected1 = {-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13};
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
        assertEquals(Integer.valueOf(13), integerSkipList1.last());

        Integer[] toAdd2 = {0};
        Integer[] expected2 = {0};
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
        assertEquals(Integer.valueOf(0), integerSkipList2.last());

        Integer[] toAdd3 = {0, 1, 3};
        Integer[] expected3 = {0, 1, 3};
        SkipList<Integer> integerSkipList3 = new SkipList<>();
        for (int x : toAdd3) {
            integerSkipList3.add(x);
        }

        Integer[] res3 = new Integer[3];
        Iterator<Integer> it3 = integerSkipList3.iterator();
        int index3 = 0;
        while (it3.hasNext()) {
            res3[index3] = it3.next();
            index3++;
        }
        assertEquals(Arrays.toString(expected3), Arrays.toString(res3));
        assertEquals(Integer.valueOf(3), integerSkipList3.last());

        SkipList<Integer> integerSkipList4 = new SkipList<>();
        Integer[] toAdd4 = {1};
        Integer[] expected4 = {1};

        for (int x : toAdd4) {
            integerSkipList4.add(x);
        }

        assertEquals(Integer.valueOf(1), integerSkipList4.last());
    }

    @Test
    void floor() {
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
        assertEquals(Integer.valueOf(7), integerSkipList.floor(7));
        assertEquals(Integer.valueOf(7), integerSkipList.floor(8));
        assertEquals(Integer.valueOf(-1000000), integerSkipList.floor(-1000000));
        assertEquals(Integer.valueOf(100000), integerSkipList.floor(100000));
        assertNull(integerSkipList.floor(-1000005));
        assertEquals(Integer.valueOf(100000), integerSkipList.floor(1000005));

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
        assertEquals(Integer.valueOf(7), integerSkipList1.floor(7));
        assertEquals(Integer.valueOf(10), integerSkipList1.floor(11));
        assertNull(integerSkipList1.floor(-2));
        assertEquals(Integer.valueOf(13), integerSkipList1.floor(14));
        assertEquals(Integer.valueOf(13), integerSkipList1.floor(13));
        assertEquals(Integer.valueOf(-1), integerSkipList1.floor(-1));

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
        assertEquals(Integer.valueOf(0), integerSkipList2.floor(0));
        assertEquals(Integer.valueOf(0), integerSkipList2.floor(1));
        assertNull(integerSkipList2.floor(-1));

        Integer[] toAdd3 = { 0, 1,  3 };
        Integer[] expected3 = { 0, 1,  3 };
        SkipList<Integer> integerSkipList3 = new SkipList<>();
        for (int x : toAdd3) {
            integerSkipList3.add(x);
        }

        Integer[] res3 = new Integer[3];
        Iterator<Integer> it3 = integerSkipList3.iterator();
        int index3 = 0;
        while (it3.hasNext()) {
            res3[index3] = it3.next();
            index3++;
        }
        assertEquals(Arrays.toString(expected3), Arrays.toString(res3));
        assertEquals(Integer.valueOf(1), integerSkipList3.floor(1));
        assertEquals(Integer.valueOf(1), integerSkipList3.floor(2));
        assertEquals(Integer.valueOf(3), integerSkipList3.floor(11));
        assertNull(integerSkipList3.floor(-2));

        SkipList<Integer> integerSkipList4 = new SkipList<>();
        Integer[] toAdd4 = {1 };
        Integer[] expected4 = {1 };

        for (int x : toAdd4) {
            integerSkipList4.add(x);
        }

        assertEquals(Integer.valueOf(1), integerSkipList4.floor(1));
        assertEquals(Integer.valueOf(1), integerSkipList4.floor(5));
        assertNull(integerSkipList4.floor(-1));
    }

    @Test
    void ceiling() {
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
        assertEquals(Integer.valueOf(7), integerSkipList.ceiling(7));
        assertEquals(Integer.valueOf(9), integerSkipList.ceiling(8));
        assertEquals(Integer.valueOf(-1000000), integerSkipList.ceiling(-1000000));
        assertEquals(Integer.valueOf(100000), integerSkipList.ceiling(100000));
        assertNull(integerSkipList.ceiling(1000005));
        assertEquals(Integer.valueOf(-1000000), integerSkipList.ceiling(-1000005));

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
        assertEquals(Integer.valueOf(7), integerSkipList1.ceiling(7));
        assertEquals(Integer.valueOf(13), integerSkipList1.ceiling(11));
        assertNull(integerSkipList1.ceiling(14));
        assertEquals(Integer.valueOf(13), integerSkipList1.ceiling(13));
        assertEquals(Integer.valueOf(-1), integerSkipList1.ceiling(-2));
        assertEquals(Integer.valueOf(-1), integerSkipList1.ceiling(-1));

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
        assertEquals(Integer.valueOf(0), integerSkipList2.ceiling(0));
        assertEquals(Integer.valueOf(0), integerSkipList2.ceiling(-1));
        assertNull(integerSkipList2.ceiling(1));

        Integer[] toAdd3 = { 0, 1,  3 };
        Integer[] expected3 = { 0, 1,  3 };
        SkipList<Integer> integerSkipList3 = new SkipList<>();
        for (int x : toAdd3) {
            integerSkipList3.add(x);
        }

        Integer[] res3 = new Integer[3];
        Iterator<Integer> it3 = integerSkipList3.iterator();
        int index3 = 0;
        while (it3.hasNext()) {
            res3[index3] = it3.next();
            index3++;
        }
        assertEquals(Arrays.toString(expected3), Arrays.toString(res3));
        assertEquals(Integer.valueOf(1), integerSkipList3.ceiling(1));
        assertEquals(Integer.valueOf(3), integerSkipList3.ceiling(2));
        assertEquals(Integer.valueOf(0), integerSkipList3.ceiling(-1));
        assertNull(integerSkipList3.ceiling(11));

        SkipList<Integer> integerSkipList4 = new SkipList<>();
        Integer[] toAdd4 = {1 };
        Integer[] expected4 = {1 };

        for (int x : toAdd4) {
            integerSkipList4.add(x);
        }

        assertEquals(Integer.valueOf(1), integerSkipList4.ceiling(1));
        assertEquals(Integer.valueOf(1), integerSkipList4.ceiling(-5));
        assertNull(integerSkipList4.ceiling(11));
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
//        Integer[] toAdd = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
//        Integer[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
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
        System.out.println("Rebuild : " + Arrays.toString(res));
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

        Integer[] toAdd3 = { 0, 1,  3 };
        Integer[] expected3 = { 0, 1,  3 };
        SkipList<Integer> integerSkipList3 = new SkipList<>();
        for (int x : toAdd3) {
            integerSkipList3.add(x);
        }

        Integer[] res3 = new Integer[3];
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

    @RepeatedTest(5)
    void getLinear() {
        SkipList<Integer> i = new SkipList<>();

        assertThrows(NoSuchElementException.class, () -> i.getLinear(0));
        assertThrows(NoSuchElementException.class, () -> i.getLinear(-1));

        i.add(1);
        i.add(2);
        i.add(3);
        assertEquals(Integer.valueOf(1), i.getLinear(0));
        assertEquals(Integer.valueOf(2), i.getLinear(1));

        for (int e = 4; e <= 10000; e++) {
            i.add(e);
        }

        assertEquals(Integer.valueOf(3), i.getLinear(2));
        assertEquals(Integer.valueOf(10000), i.getLinear(9999));
        assertEquals(Integer.valueOf(5000), i.getLinear(4999));
        assertEquals(Integer.valueOf(2555), i.getLinear(2554));
    }

    @RepeatedTest(5)
    void getLog() {
        SkipList<Integer> i = new SkipList<>();

        assertThrows(NoSuchElementException.class, () -> i.getLog(0));
        assertThrows(NoSuchElementException.class, () -> i.getLog(-1));

        for (int x = 0; x <= 10000; x++) {
            i.add(x);
        }

        assertEquals(Integer.valueOf(0), i.getLog(0));
        assertEquals(Integer.valueOf(1), i.getLog(1));
        assertEquals(Integer.valueOf(4), i.getLog(4));
        assertEquals(Integer.valueOf(99), i.getLog(99));
        assertEquals(Integer.valueOf(10000), i.getLinear(10000));
        assertEquals(Integer.valueOf(5000), i.getLinear(5000));
        assertEquals(Integer.valueOf(2555), i.getLinear(2555));
    }
}