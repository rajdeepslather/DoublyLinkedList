package in.rslather;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class DoublyLinkedList<A> implements Serializable, Deque<DLLNode<A>> {
	private static final long serialVersionUID = 1L;

	final DLLNode<A> head = new DLLNode<>();
	final DLLNode<A> tail = new DLLNode<>();
	int length;

	public DoublyLinkedList() {
		head.next = tail;
		tail.prev = head;
		length = 0;
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

	DLLNode<A> ifNullExcept(DLLNode<A> node) {
		if (node == null)
			throw new NoSuchElementException();
		return node;
	}

	@Override
	public boolean isEmpty() { return size() <= 0; }

	@Override
	@SuppressWarnings("unchecked")
	public Object[] toArray() {
		DLLNode<A>[] nodes = new DLLNode[size()];

		int i = 0;
		for (DLLNode<A> node : this)
			nodes[i++] = node;

		return nodes;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		T[] arr = (T[]) Array.newInstance(a.getClass(), size());
		int i = 0;

		// TODO how to ensure type safety below?
		for (DLLNode<A> node : this)
			arr[i++] = (T) node;

		return arr;
	}

	@Override
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

	@Override
	public boolean addAll(Collection<? extends DLLNode<A>> c) {
		int oldSize = size();

		for (DLLNode<A> node : c)
			addFirst(node);

		return oldSize > size();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		int oldSize = size();

		for (Iterator<DLLNode<A>> iterator = this.iterator(); iterator.hasNext();) {
			DLLNode<A> node = iterator.next();
			if (c.contains(node))
				iterator.remove();
		}

		return oldSize < size();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		int oldSize = size();
		for (DLLNode<A> node : this)
			if (!c.contains(node))
				removeNode(node);

		return oldSize < size();
	}

	@Override
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
	public boolean remove(Object o) { return removeFirstOccurrence(o); }

	@Override
	public boolean contains(Object o) {
		for (DLLNode<A> node : this) {
			if (node == o)
				return true;
		}
		return false;
	}

	@Override
	public int size() { return length; }

	@Override
	public Iterator<DLLNode<A>> iterator() { return iterator(head); }

	@Override
	public Iterator<DLLNode<A>> descendingIterator() { return descendingIterator(tail); }

	public Iterator<DLLNode<A>> iterator(DLLNode<A> node) {
		return new Iterator<DLLNode<A>>() {
			DLLNode<A> next = node.next;
			DLLNode<A> old = node;

			@Override
			public boolean hasNext() { return next != tail; }

			@Override
			public DLLNode<A> next() {
				if (!hasNext())
					throw new NoSuchElementException();

				old = next;
				next = next.next;
				return old;
			}

			@Override
			public void remove() {
				if (old == null)
					throw new IllegalStateException();
				removeNode(old);
				old = null;
			}
		};
	}

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
				if (old == null)
					throw new IllegalStateException();
				removeNode(old);
				old = null;
			}
		};
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("DoublyLinkedList [");

		for (DLLNode<A> node : this)
			stringBuilder.append(node).append(", ");

		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}
