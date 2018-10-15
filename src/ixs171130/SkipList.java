package ixs171130;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Implementation of Skip List. Skip List is a variation of Linked List that maintains multiple links to next
 * elements to make traversal efficient
 *
 * @param <T>
 * @author Ishan Sharma, Ravikiran Kolanpaka, Sharayu Mantri
 */
public class SkipList<T extends Comparable<? super T>> {
    static final int PossibleLevels = 33;

    Entry head, tail;
    int size, maxLevel;
    int[] skipped;  // maintains number of nodes we skipped after the last entry in last[i]. Required for span[]
    Entry[] last;
    Random random;

    /**
     * Default constructor, creates an empty SkipList
     */
    public SkipList() {
        head = new Entry<>(null, PossibleLevels);
        tail = new Entry<>(null, PossibleLevels);
        last = new Entry[PossibleLevels];
        tail.prev = head;
        size = 0;
        maxLevel = 1;
        skipped = new int[PossibleLevels];
        random = new Random();
    }

    /**
     * Find a node. Starts at maxLevel and notes down the node that directly links to the node we're looking for or
     * to node before where x should have been
     *
     * @param x Element to look for
     */
    public void find(T x) {
        Entry<T> p = head;

        for (int i = maxLevel - 1; i >= 0; i--) {
            int nodesSkipped = 0;
            while (p.next[i] != null && ((Comparable<? super T>) p.next[i].getElement()).compareTo(x) < 0) {
                nodesSkipped += p.span[i] + 1;
                p = p.next[i];
            }
            last[i] = p;
            skipped[i] = nodesSkipped;
        }
    }

    /**
     * Choose a level for a new entry
     * @return int level between 1 and PossibleLevels
     */
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

    /**
     * Add x to list. If x already exists, reject it. Returns true if new node is added to list
     * @param x Element to add
     * @return false if x is already there, true otherwise
     */
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

        if (ent.next[0] != null) {
            ent.next[0].prev = ent;
        } else {
            tail.prev = ent;
        }
        ent.prev = last[0];
        size = size + 1;
        return true;
    }

    /**
     * Update counts for any links that are broken by this entry. Called by add()
     *
     * @param e     Current entry being added
     * @param level Level for which span has to be calculated
     */
    private void populateSpan(Entry<T> e, int level) {
        if (level == 0) {
            e.span[level] = 0;
            return;
        }

        boolean nextLevelsChanged;
        boolean lastLevelsChanged = !last[level].equals(last[level - 1]);

        // guarding against some pointers pointing to tail
        if (e.next == null || e.next[level] == null) {
            nextLevelsChanged = e.next[level - 1] != null;
        } else {
            nextLevelsChanged = !e.next[level].equals(e.next[level - 1]);
        }

        if (nextLevelsChanged && lastLevelsChanged) {
            e.span[level] = last[level].span[level] - last[level - 1].span[level - 1] - skipped[level - 1];
        } else if (nextLevelsChanged) {
            e.span[level] = last[level].span[level] - last[level].span[level - 1];
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

    /**
     * Find smallest element that is greater or equal to x
     * @param x Element whose ceiling is needed
     * @return x if it is in the list, otherwise element immediately next to it (null if there's no such element)
     */
    public T ceiling(T x) {
        find(x);
        return last[0].next[0] != null ? ((T) last[0].next[0].element) : null;
    }

    /**
     * Returns true if the list contains x. False otherwise.
     * @param x Element to search for
     * @return boolean result
     */
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

    /**
     * Find largest element that is less than or equal to x
     * @param x Element to search for
     * @return boolean result
     */
    public T floor(T x) {
       find(x);
        if (last[0] != null && last[0].next[0] != null) {
            if (last[0].next[0].element.equals(x)) {
                return x;
            }
            else {
                return ((T) last[0].element);
            }
        }
        else if (last[0] != null){
            return ((T) last[0].element);
        }
        else 
            return null;
    }

    /**
     * Get the element at index n. Uses getLinear internally for now, will be replaced with getLog
     *
     * @param n Index of element to be retrieved
     * @return Element at index n
     */
    public T get(int n) {
        return getLog(n);
    }

    /**
     * Linear time algorithm to get element at index n
     * @param n index of element to be retrieved
     * @return Element at index n
     */
    public T getLinear(int n) {
        if (n < 0 || n > size - 1 || head.next[0] == null) {
            throw new NoSuchElementException("Invalid index");
        }

        Entry<T> p = head.next[0];

        for (int i = 0; i < n; i++)
            p = p.next[0];

        return (p.element);
    }

    /**
     * Check whether the list is empty or not
     * @return true if list is empty, false otherwise
     */
    public boolean isEmpty() {
        if (size == 0)
            return (true);
        return false;
    }

    // Optional operation: Eligible for EC.
    // O(log n) expected time for get(n). Requires maintenance of spans, as discussed in class.

    /**
     * Find the element at index n in expected O(log n) time. Uses span[] associated with each entry to
     * find the element.
     * @param n Index from where element is to be retrieved
     * @return Element at index n
     */
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
     * Return last element of the list. Since we are maintaining prev point, this will just be tail.prev.element
     * @return Last element of the list
     */
    public T last() {
        if (isEmpty()) {
            return null;
        }
        return (T) tail.prev.getElement();
    }

    /**
     * Reorganize the elements of the list into a perfect skip list
     */
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
            ent.span = new int[ent.level];

            if (ent.level > maxLevel) {
                for (int i = maxLevel; i < ent.level; i++) {
                    last[i] = head;
                }
                maxLevel = ent.level;
            }

            //updating next pointers of current and last element, updating last array
            for (int i = 0; i < ent.level ; i++) {
                ent.next[i] = last[i].next[i];
                last[i].next[i] = ent;
                last[i] = ent;
                populateSpan(ent, i);
            }

            // for every last that's above my level, add 1 to span
            for (int i = ent.level; i < PossibleLevels; i++) {
                // if last[i] is null, last element was head. Need to keep that in sync as well
                if (last[i] != null) {
                    last[i].span[i] += 1;
                } else {
                    head.span[i] += 1;
                }
            }

            nodeIndex += 1;
            size = size + 1;

            current = storeTemp;
        }
        System.out.println("Max Level reached for size :" + size + " is " + maxLevel);
    }

    /**
     * Remove x from the list
     *
     * @param x Element to be removed
     * @return Removed element if it's present, null otherwise
     */
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
        if (ent.next[0] != null) {
            ent.next[0].prev = last[0];
        }

        size = size - 1;

        return (ent.element);

    }

    /**
     * Return the size of the list
     * @return size of the list
     */
    public int size() {
        return (size);
    }

    /**
     * Entry class for list elements.
     * @param <E>
     */
    static class Entry<E> {
        E element;
        Entry[] next;
        Entry prev;
        int level;
        int[] span;

        public Entry(E x, int lev) {
            element = x;
            next = new Entry[lev];
            level = lev;
            span = new int[lev];
        }

        public E getElement() {
            return element;
        }
    }

    /**
     * Method used for printing list elements and their associated spans. Primarily for debugging.
     */
    public void printAllLevelOfCurrent() {
        Entry current = head;

        System.out.println(maxLevel);
        System.out.print("Head : ");
        for(int i =0; i < maxLevel; i++) {
            if ( head.next[i] != null) {
                System.out.print (head.next[i].element + " , ");
            }
        }
        System.out.println("");

        while (current != null && current.next[0] != null) {
            System.out.println("Element : " + current.next[0].element);
            for (int i = 0; i< maxLevel; i++) {
                if (i < current.next[0].level && current.next[0].next[i] != null) {
                    System.out.println("Level " + i + " : " + current.next[0].next[i].element + " span: " + current.next[0].span[i]);
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
     * An iterator for iterating in sorted order
     *
     * @return an instance of skipListIterator
     */
    public Iterator<T> iterator() {
        return new skipListIterator();
    }

    /**
     * Custom Interface class that extends Iterator
     * Needed because Java Iterator doesn't have hasPrev() or prev() and we need them for EC
     *
     * @param <T>
     */
    public interface customSLIterator<T> extends Iterator<T> {
        boolean hasNext();

        boolean hasPrev();

        T next();

        T prev();
    }

    /**
     * Iterator class for iterating through the list in sorted order
     *
     * @author Ishan
     */
    private class skipListIterator implements customSLIterator<T> {
        Entry current = head;

        /**
         * Does current entry have a next element
         * @return boolean
         */
        public boolean hasNext() {
            return current != null && current.next[0] != null;
        }

        /**
         * Return element next to current element
         * @return Next element in the list
         */
        public T next() {
            if (current == null || current.next == null) {
                throw new NoSuchElementException("No next element in the List");
            }

            for (int i = 0; i < current.level; i++) {
                last[i] = current;
            }

            T res = (T) current.next[0].element;
            current = current.next[0];
            return res;
        }

        /**
         * If current element has a previous element, return true. False otherwise
         *
         * @return boolean
         */
        public boolean hasPrev() {
            return current != null && current.prev != null && current.prev.element != null;
        }

        /**
         * Return element before current element in the list
         *
         * @return Previous element in the list
         */
        public T prev() {
            if (current == null || current.prev == null) {
                throw new NoSuchElementException("No previous element in the list");
            }

            T res = (T) current.prev.element;
            current = current.prev;
            return res;
        }
    }
}