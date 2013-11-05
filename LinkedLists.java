
import java.util.*;

public class LinkedLists {
	public static void main(String[] args) {
		LinkedLists instance = new LinkedLists();
		instance.run();
	}

	class Node {
		int val;
		Node next;

		Node(int val, Node next) {
			this.val = val;
			this.next = next;
		}
	}

	Node createLL(int size) {
		if (size == 0) return null;

		final Random rand = new Random();
		final Node head = new Node(rand.nextInt(size), null);
		
		Node n = head;
		for (int i = 1; i < size; i++) {
			n.next = new Node(rand.nextInt(size), null);
			n = n.next;
		}
		return head;
	}

	void printLL(final Node head) {
		System.out.print("LinkedList: ");
		Node n = head;
		while (n != null) {
			System.out.print(n.val + " > ");
			n = n.next;
		}
		System.out.println();
	}

	public void run() {

	}

	
	void problem2() {

	}

}
