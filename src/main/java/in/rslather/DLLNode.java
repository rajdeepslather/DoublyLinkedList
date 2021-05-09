package in.rslather;

import java.io.Serializable;

public class DLLNode<A> implements Serializable {
	private static final long serialVersionUID = 1L;

	A item;
	DLLNode<A> prev = null;
	DLLNode<A> next = null;

	public DLLNode(A item) { this.item = item; }

	public DLLNode() { this.item = null; }

	public DLLNode<A> getNext() { return next; }

	public DLLNode<A> getPrev() { return prev; }

	public A getItem() { return item; }

	public void setItem(A item) { this.item = item; }

	public String toString() { return "Node [" + item + "]"; }
}