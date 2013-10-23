import java.util.*;

public class Trie {
	class Node {
		Character key;
		final Map<Character, Node> children = new HashMap<Character, Node>(1);
		String value = null; // Applies only to leaves

		Node(char key) {
			this.key = key;
		}

		Node getChild(Character key) {
			return children.get(key);
		}

		Node addChild(Character key) {
			Node child = new Node(key);
			children.put(key, child);
			return child;
		}

		boolean isLeaf() {
			return children.isEmpty();
		}
	}

	final Node root = new Node('\0');

	Trie() {

	}

	public void add(String str) {
		Node n = root, t;
		char arr[] = str.toCharArray();
		for(int i = 0; i < arr.length; i++) {
			t = n.getChild(arr[i]);
			if (t == null) {
				t = n.addChild(arr[i]);
				if (i == arr.length - 1) {
					t.value = str;
				}
			}
			n = t;
		}
	}

	public void printAll() {
		Stack<Node> stack = new Stack<Node>();
		stack.push(root);
		print(stack, root);
	}

	private void print(Stack<Node> stack, Node n) {
		if(n.isLeaf()) {
			assert n.value != null;
			for(Node node : stack) {
				System.out.print(node.key + "->");
			}
			System.out.print(n.value);
			System.out.println();

			return;
		}
		for (Map.Entry<Character, Node> e : n.children.entrySet()) {
			stack.push(e.getValue());
			print(stack, e.getValue());
			stack.pop();
		}
	}

	public static void main(String[] args) {
		Trie trie = new Trie();
		trie.add("Causal");
		trie.add("absurd");
		trie.add("crappy");
		trie.add("spooky");
		trie.add("bullcrap");
		trie.add("bullshit");
		trie.add("Africa");
		trie.add("Asia");
		trie.add("SonOfOsiris");
		trie.add("Heman");
		trie.add("OhMyGod");

		trie.printAll();
	}
}
