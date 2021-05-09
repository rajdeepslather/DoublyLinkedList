package in.rslather;

import java.io.Serializable;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.reflect.Array;

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

	public void moveToFirst(DLLNode<A> node) {
		removeNode(node);
		addFirst(node);
	}

	public void moveToLast(DLLNode<A> node) {
		removeNode(node);
		addLast(node);
	}

	public void removeNode(DLLNode<A> node) {
		DLLNode<A> prev = node.prev;
		DLLNode<A> next = node.next;

		prev.next = next;
		next.prev = prev;

		length--;
	}

	public void replaceNode(DLLNode<A> node, DLLNode<A> newNode) {
		newNode.next = node.next;
		newNode.prev = node.prev;

		node.prev.next = newNode;
		node.next.prev = newNode;
	}

	public void insertRight(DLLNode<A> leftNode, DLLNode<A> newNode) {
		DLLNode<A> next = leftNode.next;

		newNode.next = next;
		newNode.prev = leftNode;

		next.prev = newNode;
		leftNode.next = newNode;

		length++;
	}

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
		for (Object object : c)
			if (!contains(object))
				return false;
		return true;
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
		for (Object object : c)
			removeFirstOccurrence(object);

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
	public Iterator<DLLNode<A>> iterator() { return iterator(peekFirst()); }

	@Override
	public Iterator<DLLNode<A>> descendingIterator() { return descendingIterator(peekLast()); }

	public Iterator<DLLNode<A>> iterator(DLLNode<A> node) {
		return new Iterator<DLLNode<A>>() {
			DLLNode<A> current = node;
			DLLNode<A> old = null;

			@Override
			public boolean hasNext() { return current != tail; }

			@Override
			public DLLNode<A> next() {
				if (!hasNext())
					throw new NoSuchElementException();

				old = current;
				current = current.next;
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
			DLLNode<A> current = node;
			DLLNode<A> old = null;

			@Override
			public boolean hasNext() { return current != head; }

			@Override
			public DLLNode<A> next() {
				if (!hasNext())
					throw new NoSuchElementException();

				old = current;
				current = current.prev;
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
