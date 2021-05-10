package in.rslather.utils;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
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
public class ArrayListDeque<E> extends AbstractCollection<E> implements List<E>, Deque<E>, Serializable {
	private static final long serialVersionUID = 1L;
	final ArrayList<E> array; // reversed from the Deque

	public ArrayListDeque() { array = new ArrayList<>(); }

	public ArrayListDeque(int initSize) { array = new ArrayList<>(initSize); }

	public ArrayListDeque(Collection<? extends E> c) {
		array = new ArrayList<>(c);
		Collections.reverse(array);
	}

	// protected because it does not reverse array
	protected ArrayListDeque(ArrayList<E> array) { this.array = array; }

	public E get(int i) { return array.get(toArrayI(i)); }

	public E set(int i, E e) { return array.set(toArrayI(i), e); }

	public E remove(int i) { return array.remove(toArrayI(i)); }

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
	public int size() { return array.size(); }

	int lastI() { return array.size() - 1; }

	int toArrayI(int i) { return array.size() - 1 - i; }

	@Override
	public Iterator<E> iterator() { return listIterator(0); }

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

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		Collections.reverse(array);
		boolean isChanged = array.addAll(c);
		Collections.reverse(array);

		return isChanged;
	}

	@Override
	public void add(int index, E element) { array.add(toArrayI(index), element); }

	@Override
	public int indexOf(Object o) { return toArrayI(array.lastIndexOf(o)); }

	@Override
	public int lastIndexOf(Object o) { return toArrayI(array.indexOf(o)); }

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

			public int nextIndex() { return itr.previousIndex(); }

			public int previousIndex() { return itr.nextIndex(); }

			public void remove() { itr.remove(); }

			public void set(E e) { itr.set(e); }

			public void add(E e) { // TODO Auto-generated method stub
			}
		};
	}

	@Override
	// 1, size() -1 >-sub-> 0, size() -2 >-toArrayI-> size() -1, 1 >-flip-> 1, size() -1
	// 0, size() -1 >-sub-> -1, size() -2 >-toArrayI-> size(), 1 >-flip-> 1, size()
	// 0, size() >-sub-> -1, size() -1 >-toArrayI-> size(), 0 >-flip-> 0, size()
	public List<E> subList(int fromIndex, int toIndex) {
		return new ArrayListDeque<>(new ArrayList<>(array.subList(array.size() - toIndex, array.size() - fromIndex)));
	}
}
