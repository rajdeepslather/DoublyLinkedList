package in.rslather.example;

import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

import in.rslather.DoublyLinkedList;
import in.rslather.DLLNode;

public class LRUCache<K, V> {
	final DoublyLinkedList<Pair<K, V>> deList;
	final Map<K, DLLNode<Pair<K, V>>> map;

	int size;

	public LRUCache(int size) {
		this.size = size;
		map = new HashMap<>(size);
		deList = new DoublyLinkedList<>();
	}

	public V get(K key) {
		DLLNode<Pair<K, V>> node = map.get(key);
		if (node == null)
			return null;

		deList.moveToFirst(node);
		return node.getItem().b;
	}

	public V peek() { return deList.peekFirst().getItem().b; }

	public void put(K key, V value) {
		DLLNode<Pair<K, V>> newNode = map.get(key);
		if (newNode != null) {
			newNode.setItem(new Pair<>(key, value));
			deList.moveToFirst(newNode);
		} else {
			if (deList.size() > size - 1)
				map.remove(deList.pollLast().getItem().a);

			newNode = new DLLNode<>(new Pair<>(key, value));
			deList.addFirst(newNode);
			map.put(key, newNode);
		}
	}

	public static void main(String[] args) {
		LRUCache<Integer, String> obj = new LRUCache<>(10);
		System.out.println("1: " + obj.get(1));

		obj.put(1, "item_a");
		System.out.println("1: " + obj.get(1));

		System.out.println(obj.deList);

		obj.put(2, "item_b");
		System.out.println(obj.deList);
		obj.put(1, "item_c");
		System.out.println(obj.deList);
		obj.put(3, "item_d");
		System.out.println(obj.deList);
		obj.put(4, "item_e");
		System.out.println(obj.deList);
		obj.put(5, "item_f");
		System.out.println(obj.deList);
		obj.put(6, "item_g");
		System.out.println(obj.deList);
		obj.put(7, "item_h");
		System.out.println(obj.deList);
		obj.put(8, "item_i");
		System.out.println(obj.deList);
		obj.put(9, "item_j");
		System.out.println(obj.deList);
		obj.put(10, "item_k");
		System.out.println(obj.deList);
		obj.get(5);
		System.out.println(obj.deList);

		System.out.println("First item: " + obj.peek());

		for (ListIterator<DLLNode<Pair<Integer, String>>> itr = obj.deList.listIterator(); itr.hasNext();) {
			itr.next();
			if (itr.nextIndex() == obj.deList.size()) {
				itr.add(new DLLNode<>());
				itr.add(new DLLNode<>());
				System.out.println(itr.previousIndex() + " : " + itr.previous());
				break;
			}

		}
		System.out.println(obj.deList);

		System.out.println(obj.deList.get(8));
	}
}
