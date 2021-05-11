package in.rslather.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * A Dequeue backed by an array.</br>
 * The biggest benefit of this Dequeue is the O(1) get and set operations. </br>
 * Optimized for stack operations and not queue operations (add (addLast) is O(n) while push (addFirst) is O(1)).
 *
 * @param <E>
 * 
 * @author RAJDEEP
 */
public class ArrayListDeque<E> extends AbstractListDeque<E> {
	private static final long serialVersionUID = 1L;

	public ArrayListDeque() { super(); }

	public ArrayListDeque(int initSize) { super(initSize); }

	public ArrayListDeque(Collection<? extends E> c) {
		super(c);
		Collections.reverse(array);
	}

	// protected because it does not reverse the array
	protected ArrayListDeque(ArrayList<E> array) { super(array); }

	@Override
	int lastI() { return 0; }

	@Override
	int firstI() { return array.size() - 1; }

	@Override
	int toArrayI(int i) { return array.size() - 1 - i; }

	@Override
	public int size() { return array.size(); }

	@Override
	public void addFirst(E e) { array.add(e); }

	@Override
	public void addLast(E e) { array.add(0, e); }

	@Override
	public boolean removeFirstOccurrence(Object o) {
		// remove last occurrence in the array
		if (o == null) {
			for (int i = firstI(); i >= 0; i--)
				if (array.get(i) == null) {
					array.remove(i);
					return true;
				}
		} else {
			for (int i = firstI(); i >= 0; i--)
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
	public boolean remove(Object o) { return array.remove(o); }

	@Override
	public void add(int index, E element) { array.add(toArrayI(index), element); }

	@Override
	public E remove(int i) { return array.remove(toArrayI(i)); }

	@Override
	public int indexOf(Object o) { return toArrayI(array.lastIndexOf(o)); }

	@Override
	public int lastIndexOf(Object o) { return toArrayI(array.indexOf(o)); }

	// Bulk methods

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

	// iterators
}
