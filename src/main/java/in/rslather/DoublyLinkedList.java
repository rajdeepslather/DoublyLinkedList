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
 * @author RAJDEEP
 *
 * @param <A>
 */
public class DoublyLinkedList<A> extends AbstractSequentialList<DLLNode<A>>
		implements Deque<DLLNode<A>>, Serializable {
	private static final long serialVersionUID = 1L;

	final DLLNode<A> head = new DLLNode<>();
	final DLLNode<A> tail = new DLLNode<>();
	int length = 0;

	public DoublyLinkedList() {
		head.next = tail;
		tail.prev = head;
	}

	public DoublyLinkedList(Collection<? extends DLLNode<A>> c) {
		head.next = tail;
		tail.prev = head;

		addAll(c);
	}

	/**
	 * Move node from anywhere in this object to the first position.
	 * 
	 * @param node
	 */
	public void moveToFirst(DLLNode<A> node) { addFirst(removeNode(node)); }

	/**
	 * Move node from anywhere in this object to the last position.
	 * 
	 * @param node
	 */
	public void moveToLast(DLLNode<A> node) { addLast(removeNode(node)); }

	/**
	 * @param node node to be removed
	 * @return removed node
	 */
	public DLLNode<A> removeNode(DLLNode<A> node) {
		DLLNode<A> prev = node.prev;
		DLLNode<A> next = node.next;

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
	public DLLNode<A> replaceNode(DLLNode<A> oldNode, DLLNode<A> newNode) {
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
	public void insertRight(DLLNode<A> leftNode, DLLNode<A> newNode) {
		DLLNode<A> next = leftNode.next;

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
	public void insertLeft(DLLNode<A> newNode, DLLNode<A> rightNode) { insertRight(rightNode.prev, newNode); }

	protected DLLNode<A> getForward(int index) {
		int i = 0;
		for (DLLNode<A> node : this)
			if (index == i++)
				return node;
		return null;
	}

	protected DLLNode<A> getBackward(int index) {
		int i = size() - 1;
		for (Iterator<DLLNode<A>> revItr = descendingIterator(); revItr.hasNext();) {
			DLLNode<A> node = revItr.next();
			if (index == i--)
				return node;
		}
		return null;
	}

	protected DLLNode<A> ifNullExcept(DLLNode<A> node) {
		if (node == null)
			throw new NoSuchElementException();
		return node;
	}

	@Override
	// overriding because of optimization
	protected void removeRange(int fromIndex, int toIndex) {
		DLLNode<A> left = get(fromIndex); // inclusive
		DLLNode<A> right = (toIndex == size()) ? tail : get(toIndex); // exclusive

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
	public void addFirst(DLLNode<A> node) { insertRight(head, node); }

	@Override
	public void addLast(DLLNode<A> node) { insertLeft(node, tail); }

	@Override
	public boolean offerFirst(DLLNode<A> node) {
		addFirst(node);
		return true;
	}

	@Override
	public boolean offerLast(DLLNode<A> node) {
		addLast(node);
		return true;
	}

	@Override
	public DLLNode<A> removeFirst() { return ifNullExcept(pollFirst()); }

	@Override
	public DLLNode<A> removeLast() { return ifNullExcept(pollLast()); }

	@Override
	public DLLNode<A> pollFirst() {
		if (isEmpty())
			return null;
		return removeNode(peekFirst());
	}

	@Override
	public DLLNode<A> pollLast() {
		if (isEmpty())
			return null;
		return removeNode(peekLast());
	}

	@Override
	public DLLNode<A> getFirst() { return ifNullExcept(peekFirst()); }

	@Override
	public DLLNode<A> getLast() { return ifNullExcept(peekLast()); }

	@Override
	public DLLNode<A> peekFirst() { return head.next; }

	@Override
	public DLLNode<A> peekLast() { return tail.prev; }

	@Override
	public boolean removeFirstOccurrence(Object o) {
		for (DLLNode<A> node : this)
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
	public boolean add(DLLNode<A> node) {
		addLast(node);
		return true;
	}

	@Override
	public boolean offer(DLLNode<A> node) { return offerLast(node); }

	@Override
	public DLLNode<A> remove() { return removeFirst(); }

	@Override
	public DLLNode<A> poll() { return pollFirst(); }

	@Override
	public DLLNode<A> element() { return getFirst(); }

	@Override
	public DLLNode<A> peek() { return peekFirst(); }

	@Override
	public void push(DLLNode<A> node) { addFirst(node); }

	@Override
	public DLLNode<A> pop() { return removeFirst(); }

	@Override
	// overriding because we need to use == to do equals check
	public boolean remove(Object o) { return removeFirstOccurrence(o); }

	@Override
	// overriding because we need to use == to do equals check
	public boolean contains(Object o) {
		for (DLLNode<A> node : this)
			if (node == o)
				return true;

		return false;
	}

	@Override
	public int size() { return length; }

	@Override
	// overriding because of optimization
	public DLLNode<A> get(int index) {
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException();

		if (index < size() - index)
			return getForward(index);

		return getBackward(index);
	}

	@Override
	// overriding because of optimization
	public DLLNode<A> set(int index, DLLNode<A> element) {
		return replaceNode(get(index), element);
	}

	@Override
	// overriding because of optimization
	public void add(int index, DLLNode<A> newNode) {
		insertLeft(newNode, (index == size()) ? tail : get(index));
	}

	@Override
	public DLLNode<A> remove(int index) { return removeNode(get(index)); }

	// iterators

	@Override
	public Iterator<DLLNode<A>> descendingIterator() {
		return new Iterator<DLLNode<A>>() {
			ListIterator<DLLNode<A>> itr = listIterator(tail.prev, size() - 1);

			public boolean hasNext() { return itr.hasPrevious(); }

			public DLLNode<A> next() { return itr.previous(); }

			public void remove() { itr.remove(); }
		};
	}

	@Override
	// overriding because of optimization
	public ListIterator<DLLNode<A>> listIterator() { return listIterator(head, 0); }

	@Override
	public ListIterator<DLLNode<A>> listIterator(int index) { return listIterator(get(index).prev, index); }

	/**
	 * @param node  node to start the iterator at, can be head but not tail (left bracket closed, right bracket open)
	 * @param index index of first element to be returned from the list iterator (by a call to the next method). </br>
	 *              0 <= index < size()
	 * @return a list iterator over the elements in this list (in proper sequence)
	 */
	ListIterator<DLLNode<A>> listIterator(DLLNode<A> node, int index) {
		return new ListIterator<DLLNode<A>>() {

			int nextI = index;
			DLLNode<A> curr = node;
			DLLNode<A> old = null;
			boolean isForward;

			public boolean hasNext() { return nextI < size(); }

			// returns the next node
			public DLLNode<A> next() {
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
			public DLLNode<A> previous() {
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

			public void set(DLLNode<A> newNode) {
				replaceNode(ifNullExcept(old), newNode);
				old = newNode;
			}

			public void add(DLLNode<A> newNode) {
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

		for (DLLNode<A> node : this)
			set.remove(node);

		return set.size() == 0;
	}
}
