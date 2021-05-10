package in.rslather.utils;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A collection for when order is not important.</br>
 * Unlike ArrayList this has an O(1) remove.</br>
 * Unlike HashSet this allows duplicates (and doesn't have a large constant cost to every operation.</br>
 *
 * @param <E>
 */
public class Bag<E> extends AbstractCollection<E> implements Serializable {
	private static final long serialVersionUID = 1L;

	final ArrayList<E> array;

	public Bag() { array = new ArrayList<>(); }

	public Bag(int initSize) { array = new ArrayList<>(initSize); }

	public Bag(Collection<? extends E> c) { array = new ArrayList<>(c); }

	public E get(int i) { return array.get(i); }

	/**
	 * @return A list which is a shallow copy of the Bag, the list operations will not effect the bag in any way.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<E> getClonedList() { return (ArrayList<E>) array.clone(); }

	public E set(int i, E e) { return array.set(i, e); }

	public E remove(int i) {
		E removed = set(i, get(size() - 1)); // last element is copied to pos i
		remove(size() - 1); // last element (duplicate) is removed
		return removed; // returned removed element
	}

	@Override
	public boolean add(E e) { return array.add(e); }

	@Override
	public boolean remove(Object o) {
		int i = array.indexOf(o);
		if (i < 0)
			return false;

		remove(i);
		return true;
	}

	@Override
	public Iterator<E> iterator() { return array.iterator(); }

	@Override
	public int size() { return array.size(); }

	// Bulk Operations

	@Override
	public boolean containsAll(Collection<?> c) { return array.containsAll(c); }

	@Override
	public boolean addAll(Collection<? extends E> c) { return array.addAll(c); }

	@Override
	public boolean removeAll(Collection<?> c) {
		int oldSize = size();
		for (int j = 0; j < array.size(); j++)
			if (c.contains(array.get(j)))
				remove(j);

		return oldSize < size();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		int oldSize = size();
		for (int j = 0; j < array.size(); j++)
			if (!c.contains(array.get(j)))
				remove(j);

		return oldSize < size();
	}

	@Override
	public void clear() { array.clear(); }
}
