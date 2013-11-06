
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

	final boolean isRandom = false;

	Node createLL(int[] A) {
		if (A.length == 0) return null;

		final Node head = new Node(A[0], null);
		
		Node n = head;
		for (int i = 1; i < A.length; i++) {
			n.next = new Node(A[i], null);
			n = n.next;
		}
		return head;
	}

	Node createLL(int size) {
		if (size == 0) return null;

		Random r = new Random();
		final Node head = new Node(isRandom? r.nextInt(size) : 0, null);
		
		Node n = head;
		for (int i = 1; i < size; i++) {
			n.next = new Node(isRandom? r.nextInt(size) : i, null);
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
		problem6();
	}

	void problem6() {
		final Node head = createLL(80);
		
		int k = 32;
		Node kNode = null, end = head;
		while (end.next != null) {
			if(k-- == 0) kNode = end;
			end = end.next;
		}

		// Create a loop from end to kth node
		end.next = kNode;

		Node loopStart = detectLoop(head);
		System.out.println("K-Node: " + kNode.val + " loopStart: " + loopStart.val);
		
	}

	Node detectLoop(final Node head) {
		if (head == null) return null;

		Node slow = head, fast = head;

		while (fast != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next != null? fast.next.next : null;
			if (slow == fast) break;
		}
		if (slow != fast) return null;

		/* Loop exists. Now find where */
		System.out.println("Cycle Exists!");
		slow = head;
		while (slow != fast) {
			slow = slow.next;
			fast = fast.next;
		}

		return slow;
	}

	void problem5b() {
		Node n1 = new Node(6, new Node(1, new Node(7, null)));
		Node n2 = new Node(2, new Node(9, new Node(5, null)));

		printLL(n1);
		printLL(n2);
		Node sum = sumLLForward(n1, n2);
		printLL(sum);
	}

	Node sumLLForward(Node n1, Node n2) {
		normalize(n1, n2);	
		Node sum = addNodes(n1, n2);
		return sum;
	}

	void normalize(Node n1, Node n2) {
		int l1 = lengthLL(n1);
		int l2 = lengthLL(n2);

		if (l1 > l2) {
			insertZeros(n2, l1 - l2);
		}
		else if (l1 < l2) {
			insertZeros(n1, l2 - l1);
		}
		assert lengthLL(n1) == lengthLL(n2);
	}

	void insertZeros(Node head, int n) {
		if (head == null) return;

		while (n != 0) {
			Node t = new Node(head.val, head.next);
			head.val = 0;
			head.next = t;
		}
	}

	int lengthLL(Node head) {
		int length = 0;
		while (head != null) {
			++length;
			head = head.next;
		}
		return length;
	}

	Node addNodes(Node n1, Node n2) {
		if (n1 == null && n2 == null) {
			return null;
		}
		Node next = addNodes(n1.next, n2.next);
		int carry = 0;
		if (next != null) {
			if (next.val >= 10) {
				next.val -= 10;
				carry = 1;
			}
		}
		Node sumLL = new Node(n1.val + n2.val + carry, next);
		return sumLL;
	}

	void problem5a() {
		Node n1 = new Node(7, new Node(1, new Node(6, null)));
		Node n2 = new Node(5, new Node(9, new Node(2, null)));

		printLL(n1);
		printLL(n2);
		Node sum = sumLLReverse(n1, n2);
		printLL(sum);
	}

	Node sumLLReverse(Node n1, Node n2) {
		Node sumNode = null, sumHead = null;

		int carry = 0, sum = 0;
		while (n1 != null || n2 != null) {
			int v1 = n1 != null? n1.val : 0;
			int v2 = n2 != null? n2.val : 0;

			sum = v1 + v2 + carry;
			
			if (sum >= 10) {
				carry = 1;
				sum -= 10;
			}
			else carry = 0;

			n1 = n1.next;
			n2 = n2.next;
			if(sumHead == null) sumHead = sumNode = new Node(sum, null);
			else {
				sumNode.next = new Node(sum, null);
				sumNode = sumNode.next;
			}
		}
		return sumHead;
	}

	void problem4() {
		// Node head = createLL(20);
		int[] A = {11, 9, 13, 11, 18, 5, 15, 11, 18, 5, 1, 7, 19, 16, 17, 14, 9, 4, 12, 12};
		Node head = createLL(A);
		printLL(head);
		final Node pivot = head;
		System.out.println("Pivot: " + pivot.val);
		head = partition(head, pivot);
		printLL(head);
	}

	Node partition(Node head, Node pivot) {
		Node e 	   = pivot, g 	  = null, l 	= null;
		Node ehead = pivot, ghead = null, lhead = null;

		Node n = head;
		while (n != null) {
			if (n == pivot) {
				n = n.next; continue;
			}

			if (n.val < pivot.val) {
				if (lhead == null) lhead = l = n;
				else {
					l.next = n; l = l.next;
				}
			}
			else if (n.val == pivot.val) {
				e.next = n;
				e = e.next;
			}
			else {
				if (ghead == null) ghead = g = n;
				else {
					g.next = n; g = g.next;
				}
			}
			n = n.next;
		}

		if (l != null) l.next = ehead;
		e.next = ghead;
		if(g != null) g.next = null;

		return lhead != null? lhead : ehead;
	}

	void problem3() {
		final Node head = createLL(4);
		printLL(head);

		deleteNode(head.next.next.next);
		printLL(head);
	}

	void deleteNode(Node n) {
		if ( n == null ) return;

		Node t = null;
		while (n.next != null) {
			n.val = n.next.val;
			t = n;
			n = n.next;
		}
		if (t != null) t.next = null;
		else n.val = -1;
	}
	
	void problem2() {
		final Node head = createLL(10);
		printLL(head);

		final int k = 5;
		System.out.println("K: " + k);
		Node n = findKthFromEnd(head, k);
		printLL(n);
	}

	Node findKthFromEnd(final Node head, int k) {
		Node nk = head, nend = head;

		int c = 0;
		while(nend != null) {
			if (c <= k) ++c;
			else nk = nk.next;
			nend = nend.next;
		}
		return c > k? nk : null;
	}

}
