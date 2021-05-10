package in.rslather.utils;

import java.io.Serializable;
import java.util.Deque;
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
public abstract interface DefaultDeque<E> extends Deque<E>, Serializable {
	default E ifNullExcept(E e) {
		if (e == null)
			throw new NoSuchElementException();
		return e;
	}

	@Override
	public default boolean offerFirst(E e) {
		addFirst(e);
		return true;
	}

	@Override
	public default boolean offerLast(E e) {
		addLast(e);
		return true;
	}

	@Override
	public default E removeFirst() { return ifNullExcept(pollFirst()); }

	@Override
	public default E removeLast() { return ifNullExcept(pollLast()); }

	@Override
	public default E getFirst() { return ifNullExcept(peekFirst()); }

	@Override
	public default E getLast() { return ifNullExcept(peekLast()); }

	@Override
	public default boolean add(E e) {
		addLast(e);
		return true;
	}

	@Override
	public default boolean offer(E e) { return offerLast(e); }

	@Override
	public default E remove() { return removeFirst(); }

	@Override
	public default E poll() { return pollFirst(); }

	@Override
	public default E element() { return getFirst(); }

	@Override
	public default E peek() { return peekFirst(); }

	@Override
	public default void push(E e) { addFirst(e); }

	@Override
	public default E pop() { return removeFirst(); }
}
