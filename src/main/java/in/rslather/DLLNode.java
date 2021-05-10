package in.rslather;

import java.io.Serializable;

/**
 * @author RAJDEEP
 *
 * @param <E>
 */
public class DLLNode<E> implements Serializable {
	private static final long serialVersionUID = 1L;

	E element;
	DLLNode<E> prev = null;
	DLLNode<E> next = null;

	public DLLNode(E item) { this.element = item; }

	public DLLNode() { this.element = null; }

	public DLLNode<E> getNext() { return next; }

	public DLLNode<E> getPrev() { return prev; }

	public E getItem() { return element; }

	public void setItem(E item) { this.element = item; }

	public String toString() { return "Node: " + element; }
}