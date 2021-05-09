package in.rslather;

import java.io.Serializable;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.lang.reflect.Array;

public class DoublyLinkedList<A> implements Serializable, Deque<Node<A>> {
	private static final long serialVersionUID = 1L;

	final Node<A> head = new Node<>();
	final Node<A> tail = new Node<>();
	int length;

	public DoublyLinkedList() {
		head.next = tail;
		tail.prev = head;
		length = 0;
	}

	public void moveToFirst(Node<A> node) {
		removeNode(node);
		addFirst(node);
	}

	public void moveToLast(Node<A> node) {
		removeNode(node);
		addLast(node);
	}

	public void removeNode(Node<A> node) {
		Node<A> prev = node.prev;
		Node<A> next = node.next;

		prev.next = next;
		next.prev = prev;

		length--;
	}

	Node<A> ifNullExcept(Node<A> node) {
		if (node == null)
			throw new NoSuchElementException();
		return node;
	}

	@Override
	public boolean isEmpty() { return length <= 0; }

	@Override
	@SuppressWarnings("unchecked")
	public Object[] toArray() {
		Node<A>[] nodes = new Node[length];

		int i = 0;
		for (Node<A> node : this)
			nodes[i++] = node;

		return nodes;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		T[] arr = (T[]) Array.newInstance(a.getClass(), length);
		int i = 0;

		// TODO how to ensure type safety below?
		for (Node<A> node : this)
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
	public boolean addAll(Collection<? extends Node<A>> c) {
		int oldLength = length;

		for (Node<A> node : c) {
			addFirst(node);
		}
		return oldLength > length;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		int oldLength = length;
		for (Object object : c) {
			removeFirstOccurrence(object);
		}
		return oldLength < length;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		int oldLength = length;
		for (Node<A> node : this) {
			if (!c.contains(node))
				removeNode(node);
		}
		return oldLength < length;
	}

	@Override
	public void clear() {
		head.next = tail;
		tail.prev = head;
		length = 0;
	}

	@Override
	public void addFirst(Node<A> node) {
		Node<A> secondNode = peekFirst();

		node.next = secondNode;
		node.prev = head;

		secondNode.prev = node;
		head.next = node;

		length++;
	}

	@Override
	public void addLast(Node<A> node) {
		Node<A> secondLastNode = peekLast();

		node.next = tail;
		node.prev = secondLastNode;

		secondLastNode.next = node;
		tail.prev = node;

		length++;
	}

	@Override
	public boolean offerFirst(Node<A> node) {
		addFirst(node);
		return true;
	}

	@Override
	public boolean offerLast(Node<A> node) {
		addLast(node);
		return true;
	}

	@Override
	public Node<A> removeFirst() { return ifNullExcept(pollFirst()); }

	@Override
	public Node<A> removeLast() { return ifNullExcept(pollLast()); }

	@Override
	public Node<A> pollFirst() {
		if (length <= 0)
			return null;

		Node<A> first = peekFirst();
		removeNode(first);
		return first;
	}

	@Override
	public Node<A> pollLast() {
		if (length <= 0)
			return null;

		Node<A> last = peekLast();
		removeNode(last);
		return last;
	}

	@Override
	public Node<A> getFirst() { return ifNullExcept(peekFirst()); }

	@Override
	public Node<A> getLast() { return ifNullExcept(peekLast()); }

	@Override
	public Node<A> peekFirst() { return head.next; }

	@Override
	public Node<A> peekLast() { return tail.prev; }

	@Override
	public boolean removeFirstOccurrence(Object o) {
		for (Node<A> node : this) {
			if (node == o) {
				removeNode(node);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean removeLastOccurrence(Object o) { return removeFirstOccurrence(o); }

	@Override
	public boolean add(Node<A> node) {
		addLast(node);
		return true;
	}

	@Override
	public boolean offer(Node<A> node) { return offerLast(node); }

	@Override
	public Node<A> remove() { return removeFirst(); }

	@Override
	public Node<A> poll() { return pollFirst(); }

	@Override
	public Node<A> element() { return getFirst(); }

	@Override
	public Node<A> peek() { return peekFirst(); }

	@Override
	public void push(Node<A> node) { addFirst(node); }

	@Override
	public Node<A> pop() { return removeFirst(); }

	@Override
	public boolean remove(Object o) { return removeFirstOccurrence(o); }

	@Override
	public boolean contains(Object o) {
		for (Node<A> node : this) {
			if (node == o)
				return true;
		}
		return false;
	}

	@Override
	public int size() { return length; }

	@Override
	public Iterator<Node<A>> iterator() { return iterator(peekFirst()); }

	@Override
	public Iterator<Node<A>> descendingIterator() { return descendingIterator(peekLast()); }

	public Iterator<Node<A>> iterator(Node<A> node) {
		return new Iterator<Node<A>>() {
			Node<A> current = node;
			Node<A> old = null;

			@Override
			public boolean hasNext() { return current != tail; }

			@Override
			public Node<A> next() {
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

	public Iterator<Node<A>> descendingIterator(Node<A> node) {
		return new Iterator<Node<A>>() {
			Node<A> current = node;
			Node<A> old = null;

			@Override
			public boolean hasNext() { return current != head; }

			@Override
			public Node<A> next() {
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

		for (Node<A> node : this) {
			stringBuilder.append(node).append(", ");
		}
		stringBuilder.append("]");

		return stringBuilder.toString();
	}
}
