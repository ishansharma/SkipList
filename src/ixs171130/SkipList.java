/* Starter code for LP2 */

// Change this to netid of any member of team
package ixs171130;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

// Skeleton for skip list implementation.

public class SkipList<T extends Comparable<? super T>> {
    static final int PossibleLevels = 33;

    Entry head, tail;
    int size, maxLevel;
    // Constructor
    public SkipList() {
        head = new Entry<>(null, PossibleLevels);
        tail = new Entry<>(null, PossibleLevels);
        size = 0;
        maxLevel = 1;
        last = new Entry[PossibleLevels];
        random = new Random();

    }

    Entry[] last;
    Random random;

    public void find(T x) {
        Entry<T> p = head;

        for (int i = maxLevel - 1; i >= 0; i--) {
            while (p.next[i] != null && ((Comparable<? super T>) p.next[i].getElement()).compareTo(x) < 0)
                p = p.next[i];
            last[i] = p;
        }
    }

    public int chooseLevel() {
        int lev = 1 + Integer.numberOfTrailingZeros(random.nextInt());
        if (lev > maxLevel) {
            for (int i = maxLevel; i < lev; i++) {
                last[i] = head;
            }
            maxLevel = lev;

        }

        return (lev);

    }

    // Add x to list. If x already exists, reject it. Returns true if new node is added to list
    public boolean add(T x) {

        if (contains(x) == true)
            return (false);

        int lev = chooseLevel();


        Entry<T> ent = new Entry(x, lev);

        for (int i = 0; i < lev; i++) {
            ent.next[i] = last[i].next[i];
            last[i].next[i] = ent;
        }
        if (ent.next[0] != null)
            ent.next[0].prev = ent;
        ent.prev = last[0];
        size = size + 1;
        return true;

    }

    // Find smallest element that is greater or equal to x
    public T ceiling(T x) {
        find(x);
        return last[0].next[0] != null ? ((T) last[0].next[0].element) : null;
    }

    // Does list contain x?
    public boolean contains(T x) {
        find(x);
        if (last[0].next[0] == null)
            return false;
        else
            return (((Comparable<? super T>) last[0].next[0].element).compareTo(x) == 0);
    }

    /**
     * Return first element of the list
     *
     * @return If there's an element next to sentinel head, return that. Else return null.
     */
    public T first() {
        if (head.next[0] != null) {
            return (T) head.next[0].element;
        }

        return null;
    }

    // Find largest element that is less than or equal to x
    public T floor(T x) {
       find(x);
       if (last[0] != null && last[0].next[0] != null) {
           if (last[0].next[0].element == x) {
               return x;
           }
           else if (last[0].next[0].prev != null){
               return ((T) last[0].next[0].prev.element);
           }
       }
       else if (last[0] != null) {
           return ((T) last[0].element);
       }
       return null;
    }

    /**
     * Get the element at index n. Uses getLinear internally for now, will be replaced with getLog
     *
     * @param n Index of element to be retrieved
     * @return Element at index n
     */
    public T get(int n) {
        return getLinear(n);
    }

    // O(n) algorithm for get(n)
    public T getLinear(int n) {
        if (n < 0 || n > size - 1)
            throw new NoSuchElementException("Element Not Found");
        Entry<T> p = head;

        for (int i = 0; i < n; i++)
            p = p.next[0];

        return (p.element);
    }

    // Is the list empty?
    public boolean isEmpty() {
        if (size == 0)
            return (true);
        return false;
    }

    // Optional operation: Eligible for EC.
    // O(log n) expected time for get(n). Requires maintenance of spans, as discussed in class.
    public T getLog(int n) {
        return null;
    }

    /**
     * An iterator for iterating in sorted order
     *
     * @return an instance of skipListIterator
     */
    public Iterator<T> iterator() {
        return new skipListIterator();
    }

    // Return last element of list
    public T last() {
        Entry<T> p = head;

        for (int i = maxLevel - 1; i >= 0; i--) {
            while (p.next[i] != null)
                p = p.next[i];
            last[i] = p;
        }
        return ((T) last[0].element);
    }

    // Optional operation: Reorganize the elements of the list into a perfect skip list
    // Not a standard operation in skip lists. Eligible for EC.
    public void rebuild() {

        int rebuildSize = size;
        Entry current = head.next[0];
        head = new Entry<>(null, PossibleLevels);
        tail = new Entry<>(null, PossibleLevels);
        maxLevel = 1;
        last = new Entry[PossibleLevels];
        size = 0;

        int nodeIndex = 1;
        while(current != null) {
            Entry ent = current;

            int level = 0;
            int number = 2;
            while (nodeIndex <= rebuildSize && nodeIndex % number == 0 && level < Math.log(rebuildSize)) {
                level = level + 1;
                number  = number * 2;
            }

            Entry storeTemp = ent.next[0];
            ent.next = new Entry[level + 1];
            ent.level = level + 1;

            find(((T) ent.element));

            if (ent.level > maxLevel) {
                for (int i = maxLevel; i < ent.level; i++) {
                    last[i] = head;
                }
                maxLevel = ent.level;
            }

            for (int i = 0; i < ent.level ; i++) {
                ent.next[i] = last[i].next[i];
                last[i].next[i] = ent;
            }
            if (ent.next[0] != null)
                ent.next[0].prev = ent;
            ent.prev = last[0];

            nodeIndex += 1;
            size = size + 1;

            current = storeTemp;
        }
        System.out.println("Max Level reached for size :" + size + " is " + maxLevel);
    }

    // Remove x from list.  Removed element is returned. Return null if x not in list
    public T remove(T x) {
        if (!contains(x))
            return (null);
        Entry<T> ent = last[0].next[0];
        for (int i = 0; i < ent.next.length - 1; i++) {
            last[i].next[i] = ent.next[i];
        }
        size = size - 1;
        return (ent.element);

    }

    // Return the number of elements in the list
    public int size() {
        return (size);
    }

    static class Entry<E> {
        E element;
        Entry[] next;
        Entry prev;
        int level = 0;

        public Entry(E x, int lev) {
            element = x;
            next = new Entry[lev];
            level = lev;
            // add more code if needed
        }

        public E getElement() {
            return element;
        }
    }


    public void printAllLevelOfCurrent() {
        Entry current = head;
        while (current != null && current.next[0] != null) {
            System.out.println("Element : " + current.next[0].element);
            for (int i = 0; i< maxLevel; i++) {
                if (i < current.next[0].level && current.next[0].next[i] != null) {
                    System.out.println("Level " + i + " : " + current.next[0].next[i].element);
                }
                else {
                    break;
                }
            }
            System.out.println("");
            current = current.next[0];
        }

    }

    /**
     * Iterator class for iterating through the list in sorted order
     *
     * @author Ishan
     */
    private class skipListIterator implements Iterator<T> {
        Entry current = head;

        public boolean hasNext() {
            if (current != null && current.next[0] != null) {
                return true;
            }
            return false;
        }

        public T next() {
            if (current == null || current.next == null) {
                throw new NoSuchElementException("No next element in the List");
            }

            T res = (T) current.next[0].element;
            current = current.next[0];
            return res;
        }


    }
}