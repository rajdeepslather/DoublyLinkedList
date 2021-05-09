package in.rslather;

import java.io.Serializable;

public class Node<A> implements Serializable {
	private static final long serialVersionUID = 1L;

	A item;
	Node<A> prev = null;
	Node<A> next = null;

	public Node(A item) { this.item = item; }

	public Node() { this.item = null; }

	public Node<A> getNext() { return next; }

	public Node<A> getPrev() { return prev; }

	public A getItem() { return item; }

	public void setItem(A item) { this.item = item; }

	public String toString() { return "Node [" + item + "]"; }
}