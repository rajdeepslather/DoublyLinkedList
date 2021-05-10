package in.rslather.utils;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * A Dequeue backed by an array.</br>
 * The biggest benefit of this Dequeue is the O(1) get and set operations. </br>
 * Optimized for stack operations and not queue operations (add (addLast) is O(n) while push (addFirst) is O(1)).
 *
 * @param <E>
 * 
 * @author RAJDEEP
 */
public class ArrayDeque<E> extends AbstractCollection<E> implements Deque<E>, Serializable {
	private static final long serialVersionUID = 1L;
	ArrayList<E> array; // reversed from the Deque

	public ArrayDeque() { array = new ArrayList<>(); }

	public ArrayDeque(int initSize) { array = new ArrayList<>(initSize); }

	public ArrayDeque(Collection<? extends E> c) { array = new ArrayList<>(c); }

	public E get(int i) { return array.get(size() - 1 - i); }

	public E set(int i, E e) { return array.set(size() - 1 - i, e); }

	public E remove(int i) { return array.remove(size() - 1 - i); }

	protected E ifNullExcept(E e) {
		if (e == null)
			throw new NoSuchElementException();
		return e;
	}

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
		return array.remove(size() - 1);
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
		return array.get(size() - 1);
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
			for (int i = size() - 1; i >= 0; i--)
				if (array.get(i) == null) {
					array.remove(i);
					return true;
				}
		} else {
			for (int i = size() - 1; i >= 0; i--)
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
	public int size() { return array.size(); }

	@Override
	public Iterator<E> iterator() {
		// reverse of array iterator
		return new Iterator<E>() {
			ListIterator<E> itr = array.listIterator(size() - 1);

			public boolean hasNext() { return itr.hasPrevious(); }

			public E next() { return itr.previous(); }

			public void remove() { itr.remove(); }
		};
	}

	@Override
	public Iterator<E> descendingIterator() { return array.iterator(); }

	@Override
	public Object[] toArray() { return array.toArray(); }

	@Override
	public <T> T[] toArray(T[] a) { return array.toArray(a); }

	@Override
	public boolean containsAll(Collection<?> c) { return array.containsAll(c); }

	@Override
	public boolean addAll(Collection<? extends E> c) { return array.addAll(c); }

	@Override
	public boolean removeAll(Collection<?> c) { return array.removeAll(c); }

	@Override
	public boolean retainAll(Collection<?> c) { return array.retainAll(c); }

	@Override
	public void clear() { array.clear(); }
}
