package in.rslather.utils;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * A Dequeue backed by an array.</br>
 * The biggest benefit of this Dequeue is the O(1) get and set operations. </br>
 * Optimized for stack operations and not queue operations (add (addLast) is O(n) while push (addFirst) is O(1)).
 *
 * @param <E>
 * 
 * @author RAJDEEP
 */
public class ArrayListDeque<E> extends AbstractList<E>
		implements List<E>, Deque<E>, RandomAccess, Serializable {
	private static final long serialVersionUID = 1L;
	final ArrayList<E> array; // reversed from the Deque

	public ArrayListDeque() {
		super();
		array = new ArrayList<>();
	}

	public ArrayListDeque(int initSize) {
		super();
		array = new ArrayList<>(initSize);
	}

	public ArrayListDeque(Collection<? extends E> c) {
		super();
		array = new ArrayList<>(c);
		Collections.reverse(array);
	}

	// protected because it does not reverse the array
	protected ArrayListDeque(ArrayList<E> array) {
		super();
		this.array = array;
	}

	protected E ifNullExcept(E e) {
		if (e == null)
			throw new NoSuchElementException();
		return e;
	}

	int lastI() { return array.size() - 1; }

	int toArrayI(int i) { return array.size() - 1 - i; }

	@Override
	public int size() { return array.size(); }

	@Override
	public void addFirst(E e) { array.add(e); }

	@Override
	public void addLast(E e) { array.add(0, e); }

	@Override
	public boolean offerFirst(E e) {
		addFirst(e);
		return true;
	}

	@Override
	public boolean offerLast(E e) {
		addLast(e);
		return true;
	}

	@Override
	public E removeFirst() { return ifNullExcept(pollFirst()); }

	@Override
	public E removeLast() { return ifNullExcept(pollLast()); }

	@Override
	public E pollFirst() {
		if (isEmpty())
			return null;
		return array.remove(lastI());
	}

	@Override
	public E pollLast() {
		if (isEmpty())
			return null;
		return array.remove(0);
	}

	@Override
	public E getFirst() { return ifNullExcept(peekFirst()); }

	@Override
	public E getLast() { return ifNullExcept(peekLast()); }

	@Override
	public E peekFirst() {
		if (isEmpty())
			return null;
		return array.get(lastI());
	}

	@Override
	public E peekLast() {
		if (isEmpty())
			return null;
		return array.get(0);
	}

	@Override
	public boolean removeFirstOccurrence(Object o) {
		// remove last occurrence in the array
		if (o == null) {
			for (int i = lastI(); i >= 0; i--)
				if (array.get(i) == null) {
					array.remove(i);
					return true;
				}
		} else {
			for (int i = lastI(); i >= 0; i--)
				if (o.equals(array.get(i))) {
					array.remove(i);
					return true;
				}
		}
		return false;
	}

	@Override
	public boolean removeLastOccurrence(Object o) { return array.remove(o); }

	@Override
	public boolean add(E e) {
		addLast(e);
		return true;
	}

	@Override
	public boolean offer(E e) { return offerLast(e); }

	@Override
	public E remove() { return removeFirst(); }

	@Override
	public E poll() { return pollFirst(); }

	@Override
	public E element() { return getFirst(); }

	@Override
	public E peek() { return peekFirst(); }

	@Override
	public void push(E e) { addFirst(e); }

	@Override
	public E pop() { return removeFirst(); }

	@Override
	public boolean remove(Object o) { return array.remove(o); }

	@Override
	public boolean contains(Object o) { return array.contains(o); }

	@Override
	public E get(int i) { return array.get(toArrayI(i)); }

	@Override
	public E set(int i, E e) { return array.set(toArrayI(i), e); }

	@Override
	public void add(int index, E element) { array.add(toArrayI(index), element); }

	@Override
	public E remove(int i) { return array.remove(toArrayI(i)); }

	@Override
	public int indexOf(Object o) { return toArrayI(array.lastIndexOf(o)); }

	@Override
	public int lastIndexOf(Object o) { return toArrayI(array.indexOf(o)); }

	// iterators

	@Override
	public Iterator<E> iterator() { return listIterator(0); }

	@Override
	public Iterator<E> descendingIterator() { return array.iterator(); }

	@Override
	public ListIterator<E> listIterator() { return listIterator(0); }

	@Override
	public ListIterator<E> listIterator(int index) {
		// reverse of array iterator
		return new ListIterator<E>() {
			ListIterator<E> itr = array.listIterator(array.size() - index);

			public boolean hasNext() { return itr.hasPrevious(); }

			public E next() { return itr.previous(); }

			public boolean hasPrevious() { return itr.hasNext(); }

			public E previous() { return itr.next(); }

			public int nextIndex() { return toArrayI(itr.previousIndex()); }

			public int previousIndex() { return toArrayI(itr.nextIndex()); }

			public void remove() { itr.remove(); }

			public void set(E e) { itr.set(e); }

			public void add(E e) {
				// FIXME: The add method is probably out of spec
				itr.add(e);
			}
		};
	}

	// Bulk methods

	@Override
	public Object[] toArray() { return array.toArray(); }

	@Override
	public <T> T[] toArray(T[] a) { return array.toArray(a); }

	@Override
	public boolean containsAll(Collection<?> c) { return array.containsAll(c); }

	@Override
	public boolean addAll(Collection<? extends E> c) { return addAll(0, c); }

	@Override
	public boolean removeAll(Collection<?> c) { return array.removeAll(c); }

	@Override
	public boolean retainAll(Collection<?> c) { return array.retainAll(c); }

	@Override
	public void clear() { array.clear(); }

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		Collections.reverse(array);
		boolean isChanged = array.addAll(c);
		Collections.reverse(array);

		return isChanged;
	}
}
