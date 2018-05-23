package edu.hm.bartolov.se2.miner.player.common;

import edu.hm.cs.rs.se2.miner.common.Position;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

/**
 *
 * @author Edo
 */
public class FlexibleRoute2 implements Route, Cloneable {
    
    private Position[] route;
    
    private int size;
    
    public FlexibleRoute2(){
        this(0);
    }
    
    public FlexibleRoute2(int size){
        if (size < 0)
            throw new IllegalArgumentException("Illegal Capacity: "+ size);
        route = new Position[size];
        this.size = 0;
    }
    
    public FlexibleRoute2(Collection<Position> collection){
        route = collection.toArray(new Position[0]);
        size = collection.size();
    }
    
    public FlexibleRoute2(Position... positions){
        size = positions.length;
        route = new Position[size];
        System.arraycopy(positions, 0, route, 0, size);
    }
    
    /**
     * Trims the capacity of this Route instance to be the
     * list's current size.  An application can use this operation to minimize
     * the storage of an Route instance.
     */
    public void trimToSize() {
        int oldCapacity = route.length;
        if (size < oldCapacity)
            route = Arrays.copyOf(route, size);
    }
    
    /**
     * Increases the capacity of this Route instance, if
     * necessary, to ensure that it can hold at least the number of elements
     * specified by the minimum capacity argument.
     *
     * @param   minCapacity   the desired minimum capacity
     */
    public void ensureCapacity(int minCapacity) {
        int oldCapacity = route.length;
        if (minCapacity > oldCapacity) {
            int newCapacity = (oldCapacity * 3)/2 + 1;
            if (newCapacity < minCapacity)
                newCapacity = minCapacity;
            // minCapacity is usually close to size, so this is a win:
            route = Arrays.copyOf(route, newCapacity);
        }
    }
    
    /**
     * Returns <tt>true</tt> if this list contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this list contains
     * at least one element <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
     *
     * @param o element whose presence in this list is to be tested
     * @return <tt>true</tt> if this list contains the specified element
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /**
     * Returns the index of the first occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the lowest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     */
    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++)
            if (o.equals(route[i]))
                return i;
        
        return -1;
    }
    
    /**
     * Returns the index of the last occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the highest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     */
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size-1; i >= 0; i--)
                if (o.equals(route[i]))
                    return i;
        
        return -1;
    }

    /**
     * Returns a shallow copy of this <tt>ArrayList</tt> instance.  (The
     * elements themselves are not copied.)
     *
     * @return a clone of this <tt>ArrayList</tt> instance
     * @throws java.lang.CloneNotSupportedException
     */
    @Override
    public FlexibleRoute2 clone() throws CloneNotSupportedException {
        try {
            @SuppressWarnings("unchecked")
                FlexibleRoute2 v = (FlexibleRoute2) super.clone();
            v.route = Arrays.copyOf(route, size);
            return v;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }
    
    /**
     * Returns an array containing all of the elements in this list
     * in proper sequence (from first to last element).
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this list.  (In other words, this method must allocate
     * a new array).  The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * @return an array containing all of the elements in this list in
     *         proper sequence
     */
    @Override
    public Position[] toArray() {
        return Arrays.copyOf(route, size);
    }

    
    /**
     * Returns an array containing all of the elements in this list in proper
     * sequence (from first to last element); the runtime type of the returned
     * array is that of the specified array.  If the list fits in the
     * specified array, it is returned therein.  Otherwise, a new array is
     * allocated with the runtime type of the specified array and the size of
     * this list.
     *
     * <p>If the list fits in the specified array with room to spare
     * (i.e., the array has more elements than the list), the element in
     * the array immediately following the end of the collection is set to
     * <tt>null</tt>.  (This is useful in determining the length of the
     * list <i>only</i> if the caller knows that the list does not contain
     * any null elements.)
     *
     * @param <T>
     * @param a the array into which the elements of the list are to
     *          be stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose.
     * @return an array containing the elements of the list
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this list
     * @throws NullPointerException if the specified array is null
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(route, size, a.getClass());
        System.arraycopy(route, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    
    /**
     * Returns the element at the specified position in this list.
     *
     * @param  index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public Position get(int index) {
        return route[index];
    }
    
    /**
     * Replaces the element at the specified position in this list with
     * the specified element.
     *
     * @param index index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public Position set(int index, Position element) {
        final Position oldValue = route[index];
        route[index] = element;
        return oldValue;
    }
    
    /**
     * Appends the specified element to the end of this list.
     *
     * @param e element to be appended to this list
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     */
    @Override
    public boolean add(Position e) {
        ensureCapacity(size + 1); 
        route[size++] = e;
        return true;
    }
    
    /**
     * Inserts the specified element at the specified position in this
     * list. Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     *
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public void add(int index, Position element) {
        ensureCapacity(size+1);  // Increments modCount!!
        System.arraycopy(route, index, route, index + 1,size - index);
        route[index] = element;
        size++;
    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their
     * indices).
     *
     * @param index the index of the element to be removed
     * @return the element that was removed from the list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public Position remove(int index) {
        final Position oldValue = route[index];

        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(route, index+1, route, index, numMoved);
        route[--size] = null; // Let gc do its work

        return oldValue;
    }
    

    
    /**
     * Removes the first occurrence of the specified element from this list,
     * if it is present.  If the list does not contain the element, it is
     * unchanged.  More formally, removes the element with the lowest index
     * <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
     * (if such an element exists).  Returns <tt>true</tt> if this list
     * contained the specified element (or equivalently, if this list
     * changed as a result of the call).
     *
     * @param o element to be removed from this list, if present
     * @return <tt>true</tt> if this list contained the specified element
     */
    @Override
    public boolean remove(Object o) {
        for (int index = 0; index < size; index++){
            if (o.equals(route[index])) {
                fastRemove(index);
                return true;
            }
        }
        return false;
    }

    /*
     * Private remove method that skips bounds checking and does not
     * return the value removed.
     */
    private void fastRemove(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(route, index+1, route, index, numMoved);
        route[--size] = null; // Let gc do its work
    }

    /**
     * Removes all of the elements from this list.  The list will
     * be empty after this call returns.
     */
    @Override
    public void clear() {
        // Let gc do its work
        for (int i = 0; i < size; i++)
            route[i] = null;

        size = 0;
    }

    /**
     * Appends all of the elements in the specified collection to the end of
     * this list, in the order that they are returned by the
     * specified collection's Iterator.  The behavior of this operation is
     * undefined if the specified collection is modified while the operation
     * is in progress.  (This implies that the behavior of this call is
     * undefined if the specified collection is this list, and this
     * list is nonempty.)
     *
     * @param c collection containing elements to be added to this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean addAll(Collection<? extends Position> c) {
        final Object[] a = c.toArray();
        final int numNew = a.length;
        ensureCapacity(size + numNew);  // Increments modCount
        System.arraycopy(a, 0, route, size, numNew);
        size += numNew;
        return numNew != 0;
    }

    /**
     * Inserts all of the elements in the specified collection into this
     * list, starting at the specified position.  Shifts the element
     * currently at that position (if any) and any subsequent elements to
     * the right (increases their indices).  The new elements will appear
     * in the list in the order that they are returned by the
     * specified collection's iterator.
     *
     * @param index index at which to insert the first element from the
     *              specified collection
     * @param c collection containing elements to be added to this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws NullPointerException if the specified collection is null
     */
    @Override
    public boolean addAll(int index, Collection<? extends Position> c) {

        final Object[] a = c.toArray();
        final int numNew = a.length;
        ensureCapacity(size + numNew);  // Increments modCount

        final int numMoved = size - index;
        if (numMoved > 0)
            System.arraycopy(route, index, route, index + numNew, numMoved);

        System.arraycopy(a, 0, route, index, numNew);
        size += numNew;
        return numNew != 0;
    }

    /**
     * Removes from this list all of the elements whose index is between
     * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive.
     * Shifts any succeeding elements to the left (reduces their index).
     * This call shortens the list by {@code (toIndex - fromIndex)} elements.
     * (If {@code toIndex==fromIndex}, this operation has no effect.)
     *
     * @param fromIndex
     * @param toIndex
     * @throws IndexOutOfBoundsException if {@code fromIndex} or
     *         {@code toIndex} is out of range
     *         ({@code fromIndex < 0 ||
     *          fromIndex >= size() ||
     *          toIndex > size() ||
     *          toIndex < fromIndex})
     */
    protected void removeRange(int fromIndex, int toIndex) {
        final int numMoved = size - toIndex;
        System.arraycopy(route, toIndex, route, fromIndex, numMoved);

        // Let gc do its work
        int newSize = size - (toIndex-fromIndex);
        while (size != newSize)
            route[--size] = null;
    }
    
    /** Calculate the total Distance of an Route.
     * @param startPosition
     * @param endPosition
     * @return currentDistance in a Route
     */
    @Override
    public int totalDistance(Position startPosition, Position endPosition){
        if(isEmpty())
            return 0;
        
        int currentDistance = Route.distanceBetweenPositions(startPosition,route[0]);
        for(int counter = 0;counter+1 < size();counter++){
            currentDistance += Route.distanceBetweenPositions(route[counter],route[counter+1]);
        }
        currentDistance += Route.distanceBetweenPositions(route[size()-1], endPosition);
        return currentDistance;
    }
    
    /** Calculate the total Distance of an Route.
     * @return currentDistance in a Route
     */
    @Override
    public int totalDistance(){
        if(isEmpty())
            return 0;
        
        int currentDistance = 0;
        for(int counter = 0;counter+1 < size();counter++){
            currentDistance += Route.distanceBetweenPositions(route[counter],route[counter+1]);
        }
        
        return currentDistance;
    }
    
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    
    /**
     * Removes from this list all of its elements that are contained in the
     * specified collection.
     *
     * @param c collection containing elements to be removed from this list
     * @return {@code true} if this list changed as a result of the call
     * @throws ClassCastException if the class of an element of this list
     *         is incompatible with the specified collection (optional)
     * @throws NullPointerException if this list contains a null element and the
     *         specified collection does not permit null elements (optional),
     *         or if the specified collection is null
     * @see Collection#contains(Object)
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        return batchRemove(c, false);
    }

    /**
     * Retains only the elements in this list that are contained in the
     * specified collection.  In other words, removes from this list all
     * of its elements that are not contained in the specified collection.
     *
     * @param c collection containing elements to be retained in this list
     * @return {@code true} if this list changed as a result of the call
     * @throws ClassCastException if the class of an element of this list
     *         is incompatible with the specified collection (optional)
     * @throws NullPointerException if this list contains a null element and the
     *         specified collection does not permit null elements (optional),
     *         or if the specified collection is null
     * @see Collection#contains(Object)
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        return batchRemove(c, true);
    }

    private boolean batchRemove(Collection<?> c, boolean complement) {
        final Position[] route = this.route;
        int r = 0;
        int w = 0;
        boolean modified = false;
        try {
            for (; r < size; r++)
                if (c.contains(route[r]) == complement)
                    route[w++] = route[r];
        } finally {
            // Preserve behavioral compatibility with AbstractCollection,
            // even if c.contains() throws.
            if (r != size) {
                System.arraycopy(route, r,route, w,size - r);
                w += size - r;
            }
            if (w != size) {
                for (int i = w; i < size; i++)
                    route[i] = null;
                size = w;
                modified = true;
            }
        }
        return modified;
    }
    

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence), starting at the specified position in the list.
     * The specified index indicates the first element that would be
     * returned by an initial call to {@link ListIterator#next next}.
     * An initial call to {@link ListIterator#previous previous} would
     * return the element with the specified index minus one.
     *
     * <p>The returned list iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public ListIterator<Position> listIterator(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index: "+index);
        return new ListItr(index);
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence).
     *
     * <p>The returned list iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @see #listIterator(int)
     */
    @Override
    public ListIterator<Position> listIterator() {
        return new ListItr(0);
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * <p>The returned iterator is <a href="#fail-fast"><i>fail-fast</i></a>.
     *
     * @return an iterator over the elements in this list in proper sequence
     */
    @Override
    public Iterator<Position> iterator() {
        return new Itr();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * An optimized version of AbstractList.Itr
     */
    private class Itr implements Iterator<Position> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Position next() {
            int i = cursor;
            if (cursor >= size)
                throw new NoSuchElementException();
            final Position[] route = FlexibleRoute2.this.route;
            if (cursor >= route.length)
                throw new ConcurrentModificationException();
            cursor = cursor + 1;
            return route[lastRet = i];
        }

        @Override
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            try {
                FlexibleRoute2.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /**
     * An optimized version of AbstractList.ListItr
     */
    private class ListItr extends Itr implements ListIterator<Position> {
        
        ListItr(int index) {
            super();
            cursor = index;
        }

        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Position previous() {
            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            Position[] route = FlexibleRoute2.this.route;
            if (i >= route.length)
                throw new ConcurrentModificationException();
            cursor = i;
            return route[lastRet = i];
        }

        @Override
        public void set(Position e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            try {
                FlexibleRoute2.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void add(Position e) {

            try {
                int i = cursor;
                FlexibleRoute2.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /**
     * Returns a view of the portion of this list between the specified
     * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive.  (If
     * {@code fromIndex} and {@code toIndex} are equal, the returned list is
     * empty.)  The returned list is backed by this list, so non-structural
     * changes in the returned list are reflected in this list, and vice-versa.
     * The returned list supports all of the optional list operations.
     *
     * <p>This method eliminates the need for explicit range operations (of
     * the sort that commonly exist for arrays).  Any operation that expects
     * a list can be used as a range operation by passing a subList view
     * instead of a whole list.  For example, the following idiom
     * removes a range of elements from a list:
     * <pre>
     *      list.subList(from, to).clear();
     * </pre>
     * Similar idioms may be constructed for {@link #indexOf(Object)} and
     * {@link #lastIndexOf(Object)}, and all of the algorithms in the
     * {@link Collections} class can be applied to a subList.
     *
     * <p>The semantics of the list returned by this method become undefined if
     * the backing list (i.e., this list) is <i>structurally modified</i> in
     * any way other than via the returned list.  (Structural modifications are
     * those that change the size of this list, or otherwise perturb it in such
     * a fashion that iterations in progress may yield incorrect results.)
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws IllegalArgumentException {@inheritDoc}
     */
    @Override                                   // is not tested!!!!!!!!!!!!!
    public List<Position> subList(int fromIndex, int toIndex) {
        final Position[] subList = new Position[toIndex-fromIndex];
        System.arraycopy(route, 0, subList, fromIndex, toIndex-fromIndex);
        return new FlexibleRoute2(subList);
    }

}
