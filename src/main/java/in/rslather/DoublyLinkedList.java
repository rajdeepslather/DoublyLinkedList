package in.rslather;

import java.io.Serializable;
import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @param <E>
 * 
 * @author RAJDEEP
 */
public class DoublyLinkedList<E> extends AbstractSequentialList<DLLNode<E>>
		implements Deque<DLLNode<E>>, Serializable {
	private static final long serialVersionUID = 1L;

	final DLLNode<E> head = new DLLNode<>();
	final DLLNode<E> tail = new DLLNode<>();
	int length = 0;

	public DoublyLinkedList() {
		super();
		head.next = tail;
		tail.prev = head;
	}

	public DoublyLinkedList(Collection<? extends DLLNode<E>> c) {
		super();
		head.next = tail;
		tail.prev = head;

		addAll(c);
	}

	/**
	 * Move node from anywhere in this object to the first position.
	 * 
	 * @param node
	 */
	public void moveToFirst(DLLNode<E> node) { addFirst(removeNode(node)); }

	/**
	 * Move node from anywhere in this object to the last position.
	 * 
	 * @param node
	 */
	public void moveToLast(DLLNode<E> node) { addLast(removeNode(node)); }

	/**
	 * @param node node to be removed
	 * @return removed node
	 */
	public DLLNode<E> removeNode(DLLNode<E> node) {
		DLLNode<E> prev = node.prev;
		DLLNode<E> next = node.next;

		prev.next = next;
		next.prev = prev;

		length--;
		return node;
	}

	/**
	 * @param oldNode node in the linked list to be replaced
	 * @param newNode replacement node
	 * @return oldNode
	 */
	public DLLNode<E> replaceNode(DLLNode<E> oldNode, DLLNode<E> newNode) {
		newNode.next = oldNode.next;
		newNode.prev = oldNode.prev;

		oldNode.prev.next = newNode;
		oldNode.next.prev = newNode;

		return oldNode;
	}

	/**
	 * Insert a node to the right of a given node.
	 * 
	 * @param leftNode node in the linked list
	 * @param newNode  node to be inserted to the right of the leftNode
	 */
	public void insertRight(DLLNode<E> leftNode, DLLNode<E> newNode) {
		DLLNode<E> next = leftNode.next;

		newNode.next = next;
		newNode.prev = leftNode;

		next.prev = newNode;
		leftNode.next = newNode;

		length++;
	}

	/**
	 * Insert a node to the left of a given node.
	 * 
	 * @param newNode   node to be inserted to the left of the rightNode
	 * @param rightNode node in the linked list
	 */
	public void insertLeft(DLLNode<E> newNode, DLLNode<E> rightNode) { insertRight(rightNode.prev, newNode); }

	protected DLLNode<E> getForward(int index) {
		int i = 0;
		for (DLLNode<E> node : this)
			if (index == i++)
				return node;
		return null;
	}

	protected DLLNode<E> getBackward(int index) {
		int i = size() - 1;
		for (Iterator<DLLNode<E>> revItr = descendingIterator(); revItr.hasNext();) {
			DLLNode<E> node = revItr.next();
			if (index == i--)
				return node;
		}
		return null;
	}

	protected DLLNode<E> ifNullExcept(DLLNode<E> node) {
		if (node == null)
			throw new NoSuchElementException();
		return node;
	}

	@Override
	// overriding because of optimization
	protected void removeRange(int fromIndex, int toIndex) {
		DLLNode<E> left = get(fromIndex); // inclusive
		DLLNode<E> right = (toIndex == size()) ? tail : get(toIndex); // exclusive

		length -= toIndex - fromIndex;

		left.prev.next = right;
		right.prev = left.prev;
	}

	@Override
	// overriding because of optimization
	public void clear() {
		head.next = tail;
		tail.prev = head;
		length = 0;
	}

	@Override
	public void addFirst(DLLNode<E> node) { insertRight(head, node); }

	@Override
	public void addLast(DLLNode<E> node) { insertLeft(node, tail); }

	@Override
	public boolean offerFirst(DLLNode<E> node) {
		addFirst(node);
		return true;
	}

	@Override
	public boolean offerLast(DLLNode<E> node) {
		addLast(node);
		return true;
	}

	@Override
	public DLLNode<E> removeFirst() { return ifNullExcept(pollFirst()); }

	@Override
	public DLLNode<E> removeLast() { return ifNullExcept(pollLast()); }

	@Override
	public DLLNode<E> pollFirst() {
		if (isEmpty())
			return null;
		return removeNode(peekFirst());
	}

	@Override
	public DLLNode<E> pollLast() {
		if (isEmpty())
			return null;
		return removeNode(peekLast());
	}

	@Override
	public DLLNode<E> getFirst() { return ifNullExcept(peekFirst()); }

	@Override
	public DLLNode<E> getLast() { return ifNullExcept(peekLast()); }

	@Override
	public DLLNode<E> peekFirst() { return head.next; }

	@Override
	public DLLNode<E> peekLast() { return tail.prev; }

	@Override
	public boolean removeFirstOccurrence(Object o) {
		for (DLLNode<E> node : this)
			if (node == o) {
				removeNode(node);
				return true;
			}
		return false;
	}

	@Override
	public boolean removeLastOccurrence(Object o) { return removeFirstOccurrence(o); }

	@Override
	// overriding because of optimization
	public boolean add(DLLNode<E> node) {
		addLast(node);
		return true;
	}

	@Override
	public boolean offer(DLLNode<E> node) { return offerLast(node); }

	@Override
	public DLLNode<E> remove() { return removeFirst(); }

	@Override
	public DLLNode<E> poll() { return pollFirst(); }

	@Override
	public DLLNode<E> element() { return getFirst(); }

	@Override
	public DLLNode<E> peek() { return peekFirst(); }

	@Override
	public void push(DLLNode<E> node) { addFirst(node); }

	@Override
	public DLLNode<E> pop() { return removeFirst(); }

	@Override
	// overriding because we need to use == to do equals check
	public boolean remove(Object o) { return removeFirstOccurrence(o); }

	@Override
	// overriding because we need to use == to do equals check
	public boolean contains(Object o) {
		for (DLLNode<E> node : this)
			if (node == o)
				return true;

		return false;
	}

	@Override
	public int size() { return length; }

	@Override
	// overriding because of optimization
	public DLLNode<E> get(int index) {
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException();

		if (index < size() - index)
			return getForward(index);

		return getBackward(index);
	}

	@Override
	// overriding because of optimization
	public DLLNode<E> set(int index, DLLNode<E> element) {
		return replaceNode(get(index), element);
	}

	@Override
	// overriding because of optimization
	public void add(int index, DLLNode<E> newNode) {
		insertLeft(newNode, (index == size()) ? tail : get(index));
	}

	@Override
	public DLLNode<E> remove(int index) { return removeNode(get(index)); }

	// iterators

	@Override
	public Iterator<DLLNode<E>> descendingIterator() {
		return new Iterator<DLLNode<E>>() {
			ListIterator<DLLNode<E>> itr = listIterator(tail.prev, size() - 1);

			public boolean hasNext() { return itr.hasPrevious(); }

			public DLLNode<E> next() { return itr.previous(); }

			public void remove() { itr.remove(); }
		};
	}

	@Override
	// overriding because of optimization
	public ListIterator<DLLNode<E>> listIterator() { return listIterator(head, 0); }

	@Override
	public ListIterator<DLLNode<E>> listIterator(int index) { return listIterator(get(index).prev, index); }

	/**
	 * @param node  node to start the iterator at, can be head but not tail (left bracket closed, right bracket open)
	 * @param index index of first element to be returned from the list iterator (by a call to the next method). </br>
	 *              0 <= index < size()
	 * @return a list iterator over the elements in this list (in proper sequence)
	 */
	ListIterator<DLLNode<E>> listIterator(DLLNode<E> node, int index) {
		return new ListIterator<DLLNode<E>>() {

			int nextI = index;
			DLLNode<E> curr = node;
			DLLNode<E> old = null;
			boolean isForward;

			public boolean hasNext() { return nextI < size(); }

			// returns the next node
			public DLLNode<E> next() {
				if (!hasNext())
					throw new NoSuchElementException();
				isForward = true;

				nextI++;
				curr = curr.next;
				old = curr;
				return old;
			}

			public boolean hasPrevious() { return previousIndex() >= 0; }

			// returns the current node
			public DLLNode<E> previous() {
				if (!hasPrevious())
					throw new NoSuchElementException();
				isForward = false;
				old = curr;

				nextI--;
				curr = curr.prev;
				return old;
			}

			public int nextIndex() { return nextI; }

			public int previousIndex() { return nextI - 1; /* current index */ }

			public void remove() {
				removeNode(ifNullExcept(old));
				old = null;
				if (isForward)
					nextI--;
			}

			public void set(DLLNode<E> newNode) {
				replaceNode(ifNullExcept(old), newNode);
				old = newNode;
			}

			public void add(DLLNode<E> newNode) {
				insertRight(curr, newNode);
				old = null;
				// move cursor to newNode
				curr = curr.next;
				nextI++;
			}
		};
	}

	// Bulk methods

	@Override
	// overriding because of optimization
	public boolean containsAll(Collection<?> c) {
		if (c.size() <= 0)
			return true;

		if (c.size() > size())
			return false;

		Set<Object> set = new HashSet<>(c); // making a shallow copy

		for (DLLNode<E> node : this)
			set.remove(node);

		return set.size() == 0;
	}
}
