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
	public void moveToFirst(DLLNode<A> node) {
		removeNode(node);
		addFirst(node);
	}

	/**
	 * Move node from anywhere in this object to the last position.
	 * 
	 * @param node
	 */
	public void moveToLast(DLLNode<A> node) {
		removeNode(node);
		addLast(node);
	}

	/**
	 * @param node node to be removed
	 */
	public void removeNode(DLLNode<A> node) {
		DLLNode<A> prev = node.prev;
		DLLNode<A> next = node.next;

		prev.next = next;
		next.prev = prev;

		length--;
	}

	/**
	 * @param oldNode node in the linked list to be replaced
	 * @param newNode replacement node
	 */
	public void replaceNode(DLLNode<A> oldNode, DLLNode<A> newNode) {
		newNode.next = oldNode.next;
		newNode.prev = oldNode.prev;

		oldNode.prev.next = newNode;
		oldNode.next.prev = newNode;
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

	protected void insertLeft(DLLNode<A> newNode, int index) {
		DLLNode<A> rightNode = (index == size()) ? tail : get(index);
		insertLeft(newNode, rightNode);
	}

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
	protected void removeRange(int fromIndex, int toIndex) {
		// overriding because of optimization
		DLLNode<A> left = get(fromIndex); // inclusive
		DLLNode<A> right = (toIndex == size()) ? tail : get(toIndex); // exclusive

		length -= toIndex - fromIndex; // removeRange(0, size());

		left.prev.next = right;
		right.prev = left.prev;
	}

	@Override
	public void clear() {
		// overriding because of optimization
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
		if (size() <= 0)
			return null;

		DLLNode<A> first = peekFirst();
		removeNode(first);
		return first;
	}

	@Override
	public DLLNode<A> pollLast() {
		if (size() <= 0)
			return null;

		DLLNode<A> last = peekLast();
		removeNode(last);
		return last;
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
	public boolean add(DLLNode<A> node) {
		// overriding because of optimization
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
	public boolean remove(Object o) {
		// overriding because we need to use == to do equals check
		return removeFirstOccurrence(o);
	}

	@Override
	public boolean contains(Object o) {
		// overriding because we need to use == to do equals check
		for (DLLNode<A> node : this) {
			if (node == o)
				return true;
		}
		return false;
	}

	@Override
	public int size() { return length; }

	@Override
	public DLLNode<A> get(int index) {
		// overriding because of optimization
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException();

		if (index < size() - index)
			return getForward(index);

		return getBackward(index);
	}

	@Override
	public DLLNode<A> set(int index, DLLNode<A> element) {
		// overriding because of optimization
		DLLNode<A> node = get(index);
		replaceNode(node, element);

		return node;
	}

	@Override
	public void add(int index, DLLNode<A> newNode) {
		// overriding because of optimization
		insertLeft(newNode, index);
	}

	@Override
	public DLLNode<A> remove(int index) {
		DLLNode<A> node = (index == size()) ? tail : get(index);
		removeNode(node);
		return node;
	}

	// iterators

	@Override
	public Iterator<DLLNode<A>> descendingIterator() { return descendingIterator(tail); }

	public Iterator<DLLNode<A>> descendingIterator(DLLNode<A> node) {
		return new Iterator<DLLNode<A>>() {
			DLLNode<A> prev = node.prev;
			DLLNode<A> old = node;

			@Override
			public boolean hasNext() { return prev != head; }

			@Override
			public DLLNode<A> next() {
				if (!hasNext())
					throw new NoSuchElementException();

				old = prev;
				prev = prev.prev;
				return old;
			}

			@Override
			public void remove() {
				if (old == null || old == tail)
					throw new IllegalStateException();
				removeNode(old);
				old = null;
			}
		};
	}

	@Override
	public ListIterator<DLLNode<A>> listIterator() {
		// overriding because of optimization
		return listIterator(head, 0);
	}

	@Override
	public ListIterator<DLLNode<A>> listIterator(int index) { return listIterator(get(index).prev, index); }

	ListIterator<DLLNode<A>> listIterator(DLLNode<A> node, int index) {
		return new ListIterator<DLLNode<A>>() {

			int nextI = index; // 0 <= i < size()
			DLLNode<A> curr = node; // can be head or tail
			DLLNode<A> old = null;
			boolean isForward = true;

			@Override
			public boolean hasNext() { return nextI < size(); }

			@Override
			public DLLNode<A> next() {
				if (!hasNext())
					throw new NoSuchElementException();

				nextI++;
				curr = curr.next;
				isForward = true;

				old = curr;
				return curr;
			}

			@Override
			public boolean hasPrevious() { return nextI > 0; }

			@Override
			public DLLNode<A> previous() {
				if (!hasPrevious())
					throw new NoSuchElementException();

				nextI--;
				curr = curr.prev;
				isForward = false;

				old = curr;
				return curr;
			}

			@Override
			public int nextIndex() { return nextI; }

			@Override
			public int previousIndex() { return nextI - 1; }

			@Override
			public void remove() {
				if (old == null)
					throw new IllegalStateException();

				removeNode(old);
				old = null;

				if (isForward)
					nextI--;
			}

			@Override
			public void set(DLLNode<A> newNode) {
				if (old == null)
					throw new IllegalStateException();
				replaceNode(old, newNode);
				old = newNode;
			}

			@Override
			public void add(DLLNode<A> newNode) {
				if (!hasNext()) {// i.e. this is the last node
					insertLeft(newNode, tail);
					curr = tail;
					nextI = size();
				} else {
					insertLeft(newNode, curr);
					nextI++;
				}
				old = null;
			}
		};
	}

	// Bulk methods

	@Override
	public boolean containsAll(Collection<?> c) {
		// overriding because of optimization
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
