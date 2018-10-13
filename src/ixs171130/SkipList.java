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
    int[] skipped;  // maintains number of nodes we skipped after the last entry in last[i]. Required for span[]

    // Constructor
    public SkipList() {
        head = new Entry<>(null, PossibleLevels);
        tail = new Entry<>(null, PossibleLevels);
        size = 0;
        maxLevel = 1;
        last = new Entry[PossibleLevels];
        random = new Random();
        skipped = new int[PossibleLevels];
    }

    Entry[] last;
    Random random;

    public void find(T x) {
        Entry<T> p = head;

        for (int i = maxLevel - 1; i >= 0; i--) {
            int nodesSkipped = 0;
            while (p.next[i] != null && ((Comparable<? super T>) p.next[i].getElement()).compareTo(x) < 0) {
                nodesSkipped += p.span[i];
                p = p.next[i];
            }
            last[i] = p;
            skipped[i] = nodesSkipped;
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
        if (contains(x))
            return (false);

        int lev = chooseLevel();

        Entry<T> ent = new Entry<>(x, lev);

        for (int i = 0; i < lev; i++) {
            ent.next[i] = last[i].next[i];
            last[i].next[i] = ent;
            populateSpan(ent, i);
        }

        // for every last that's above my level, add 1 to span
        for (int i = lev; i < PossibleLevels; i++) {
            // if last[i] is null, last element was head. Need to keep that in sync as well
            if (last[i] != null) {
                last[i].span[i] += 1;
            } else {
                head.span[i] += 1;
            }
        }

        if (ent.next[0] != null)
            ent.next[0].prev = ent;
        ent.prev = last[0];
        size = size + 1;
        return true;

    }

    private void populateSpan(Entry<T> e, int level) {
        if (level == 0) {
            e.span[level] = 0;
            return;
        }

        boolean nextLevelsChanged;
        boolean lastLevelsChanged = last[level].equals(last[level - 1]);

        // guarding against some pointers pointing to tail
        if (e.next == null || e.next[level] == null) {
            nextLevelsChanged = e.next[level - 1] != null;
        } else {
            nextLevelsChanged = e.next[level].equals(e.next[level - 1]);
        }

        if (nextLevelsChanged && lastLevelsChanged) {
            e.span[level] = last[level].span[level] - skipped[level - 1];
        } else if (nextLevelsChanged) {
            e.span[level] = last[level].span[level] - skipped[level - 1];
        } else if (lastLevelsChanged) {
            e.span[level] = e.span[level - 1];
        } else {
            e.span[level] = e.span[level - 1];
        }

        last[level].span[level] = last[level].span[level] - e.span[level];
        if (last[level].span[level] < 0) {
            last[level].span[level] = 0;
        }
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
           if (last[0].next[0].element.equals(x)) {
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
        if (n < 0 || n > size - 1 || head.next[0] == null) {
            throw new NoSuchElementException("Invalid index");
        }

        Entry<T> p = head.next[0];

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
        if (n < 0 || n > size - 1 || head.next[0] == null) {
            throw new NoSuchElementException("Invalid index");
        }

        int i = maxLevel, traversed = 0;
        Entry<T> p = head;
        while (traversed <= n) {
            if (p.span.length > i && p.span[i] <= n - traversed) {
                traversed += p.span[i] + 1;
                p = p.next[i];
            } else {
                // need extra check to make sure that we can safely travel on bottom most level
                if (i > 0) {
                    i--;
                }
            }
        }

        return p.element;
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
        int logN = ((int) (Math.log(rebuildSize)/Math.log(2))) + 1;
        Entry current = head.next[0];

        head = new Entry<>(null, PossibleLevels);
        tail = new Entry<>(null, PossibleLevels);
        last = new Entry[PossibleLevels];
        for (int i = 0; i < logN; i++) {
            last[i] = head;
        }
        size = 0;
        maxLevel = 1;
        int nodeIndex = 1;

        while(current != null) {
            Entry ent = current;

            //calculating levels in particular node
            int level = 0;
            int number = 2;
            while (nodeIndex % number == 0 && level < logN) {
                level = level + 1;
                number  = number * 2;
            }

            //storing next element in temp, needed in next iteration
            Entry storeTemp = ent.next[0];

            //creating new levels for current node
            ent.next = new Entry[level + 1];
            ent.level = level + 1;
            ent.prev = last[0];

            if (ent.level > maxLevel) {
                maxLevel = ent.level;
            }

            //updating next pointers of current and last element, updating last array
            for (int i = 0; i < ent.level ; i++) {
                ent.next[i] = last[i].next[i];
                last[i].next[i] = ent;
                last[i] = ent;
            }

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

        // update spans before removing
        for (int i = 1; i < ent.level; i++) {
            // decreases are done only if we have more than 0 elements
            // in span
            if (last[i] == null) {
                // if last[i] is null, that's pointing to head.
                head.span[i] = head.span[i] + ent.span[i];
            } else {
                last[i].span[i] = last[i].span[i] + ent.span[i];
            }
        }

        // for every other last not involving the element, just decrease by 1
        for (int i = ent.level; i < PossibleLevels; i++) {
            if (last[i] == null) {
                // if last[i] is null, that's pointing to head.
                head.span[i] = head.span[i] - 1;
            } else {
                last[i].span[i] = last[i].span[i] - 1;
            }
        }


        for (int i = 0; i <= ent.next.length - 1; i++) {
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
        int[] span;

        public Entry(E x, int lev) {
            element = x;
            next = new Entry[lev];
            level = lev;
            span = new int[lev];
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