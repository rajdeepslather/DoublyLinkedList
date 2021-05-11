package in.rslather.utils;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.RandomAccess;

/**
 * @param <E>
 * 
 * @author RAJDEEP
 */
public abstract class AbstractListDeque<E> extends AbstractList<E>
		implements DefaultDeque<E>, RandomAccess, Serializable {
	private static final long serialVersionUID = 1L;
	ArrayList<E> array;

	public AbstractListDeque() {
		super();
		array = new ArrayList<E>();
	}

	public AbstractListDeque(int initSize) {
		super();
		array = new ArrayList<>(initSize);
	}

	public AbstractListDeque(Collection<? extends E> c) {
		super();
		array = new ArrayList<>(c);
	}

	public AbstractListDeque(ArrayList<E> array) {
		super();
		this.array = array;
	}

	abstract int firstI();

	abstract int lastI();

	abstract int toArrayI(int i);

	@Override
	public E pollFirst() {
		if (isEmpty())
			return null;
		return remove(firstI());
	}

	@Override
	public E pollLast() {
		if (isEmpty())
			return null;
		return remove(lastI());
	}

	@Override
	public E peekFirst() {
		if (isEmpty())
			return null;
		return get(firstI());
	}

	@Override
	public E peekLast() {
		if (isEmpty())
			return null;
		return get(lastI());
	}

	@Override
	public boolean contains(Object o) { return array.contains(o); }

	@Override
	public E get(int i) { return array.get(toArrayI(i)); }

	@Override
	public E set(int i, E e) { return array.set(toArrayI(i), e); }

	// iterators

	@Override
	public Iterator<E> descendingIterator() {
		return new Iterator<E>() {
			ListIterator<E> itr = listIterator(size());

			public boolean hasNext() { return itr.hasPrevious(); }

			public E next() { return itr.previous(); }

			public void remove() { itr.remove(); }
		};
	}

	// Bulk methods

	@Override
	public boolean containsAll(Collection<?> c) { return array.containsAll(c); }
}
