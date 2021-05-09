package in.rslather;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractSequentialList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * @author RAJDEEP
 *
 * @param <A>
 */
public class DoublyLinkedList<A> extends AbstractSequentialList<DLLNode<A>>
		implements Serializable, List<DLLNode<A>>, Deque<DLLNode<A>> {
	private static final long serialVersionUID = 1L;

	final DLLNode<A> head = new DLLNode<>();
	final DLLNode<A> tail = new DLLNode<>();
	int length;

	int leftBound;
	int rightBound;

	public DoublyLinkedList() {
		head.next = tail;
		tail.prev = head;
		length = 0;

		leftBound = 0;
		rightBound = 0;
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

	void insertLeft(DLLNode<A> newNode, int index) {
		DLLNode<A> rightNode = (index == size()) ? tail : get(index);
		insertLeft(newNode, rightNode);
	}

	DLLNode<A> ifNullExcept(DLLNode<A> node) {
		if (node == null)
			throw new NoSuchElementException();
		return node;
	}

	DLLNode<A> getForward(int index) {
		int i = 0;
		for (DLLNode<A> node : this)
			if (index == i++)
				return node;
		return null;
	}

	DLLNode<A> getBackward(int index) {
		int i = size() - 1;
		for (Iterator<DLLNode<A>> revItr = descendingIterator(); revItr.hasNext();) {
			DLLNode<A> node = revItr.next();
			if (index == i++)
				return node;
		}
		return null;
	}

	DLLNode<A> getWithSentinals(int index) {
		if (index < -1 || index > size())
			throw new IndexOutOfBoundsException();

		if (index == -1)
			return head;

		if (index == size())
			return tail;

		if (index < size() - index)
			return getForward(index);

		return getBackward(index);
	}

	int indexOfWithSentinals(Object o) {
		if (o == head)
			return -1;

		if (o == tail)
			return size();

		int i = 0;
		for (DLLNode<A> node : this) {
			if (o == node)
				return i;
			i++;
		}

		return -2;
	}

	@Override
	public boolean isEmpty() { return size() <= 0; }

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
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("DoublyLinkedList [");

		for (DLLNode<A> node : this)
			stringBuilder.append(node).append(", ");

		stringBuilder.append("]");

		return stringBuilder.toString();
	}

	@Override
	public DLLNode<A> get(int index) {
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException();

		if (index < size() - index)
			return getForward(index);

		return getBackward(index);
	}

	@Override
	public DLLNode<A> set(int index, DLLNode<A> element) {
		DLLNode<A> node = get(index);
		replaceNode(node, element);

		return node;
	}

	@Override
	public void add(int index, DLLNode<A> newNode) { insertLeft(newNode, index); }

	@Override
	public DLLNode<A> remove(int index) {
		DLLNode<A> node = (index == size()) ? tail : get(index);
		removeNode(node);
		return node;
	}

	@Override
	public int indexOf(Object o) {
		int i = 0;
		for (DLLNode<A> node : this) {
			if (o == node)
				return i;
			i++;
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) { return indexOf(o); }

	// iterators

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
				if (old == null || old == head)
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
				if (old == null || old == tail)
					throw new IllegalStateException();
				removeNode(old);
				old = null;
			}
		};
	}

	@Override
	public ListIterator<DLLNode<A>> listIterator() { return listIterator(head.next); }

	@Override
	public ListIterator<DLLNode<A>> listIterator(int index) { return listIterator(get(index), index); }

	public ListIterator<DLLNode<A>> listIterator(DLLNode<A> node) { return listIterator(node, indexOf(node)); }

	ListIterator<DLLNode<A>> listIterator(DLLNode<A> node, int index) {
		return new ListIterator<DLLNode<A>>() {

			int i = index; // 0 <= i < size()
			DLLNode<A> curr = node; // cannot be head or tail
			DLLNode<A> old = null;

			@Override
			public boolean hasNext() { return curr.next != tail; }

			@Override
			public DLLNode<A> next() {
				if (!hasNext())
					throw new NoSuchElementException();

				old = curr;

				i++;
				curr = curr.next;
				return curr;
			}

			@Override
			public boolean hasPrevious() { return curr.prev != head; }

			@Override
			public DLLNode<A> previous() {
				if (!hasPrevious())
					throw new NoSuchElementException();

				old = curr;

				i--;
				curr = curr.prev;
				return curr;
			}

			@Override
			public int nextIndex() { return i + 1; }

			@Override
			public int previousIndex() { return i - 1; }

			@Override
			public void remove() {
				if (old == null)
					throw new IllegalStateException();

				removeNode(old);
				old = null;
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
				insertLeft(newNode, curr);
				i++;
				old = null;
			}
		};
	}

	// Collection methods

	@Override
	public boolean addAll(int index, Collection<? extends DLLNode<A>> c) {
		int oldSize = size();
		DLLNode<A> rightNode = (index == size()) ? tail : get(index);

		for (DLLNode<A> newNode : c)
			insertLeft(newNode, rightNode);

		return oldSize < size();
	}

	@Override
	public boolean addAll(Collection<? extends DLLNode<A>> c) {
		int oldSize = size();

		for (DLLNode<A> node : c)
			addFirst(node);

		return oldSize > size();
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
	@SuppressWarnings("unchecked")
	public Object[] toArray() {
		DLLNode<A>[] arr = new DLLNode[size()];

		int i = 0;
		for (DLLNode<A> node : this)
			arr[i++] = node;

		return arr;
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
}
