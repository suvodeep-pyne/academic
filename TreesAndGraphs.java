
import java.util.*;

public class TreesAndGraphs {
	public static void main(String[] args) {
		TreesAndGraphs instance = new TreesAndGraphs();
		instance.run();
	}

	public void run() {
		problem9();
	}

	public void problem9() {
		List<Integer> A = new ArrayList<Integer>(Arrays.asList(
			8,4,12,2,6,10,14,1,3,5,7,9,11,13,15));
		
		BinaryTreeNode root = getBinarySearchTree(A);
		printBST(root);

		int sum = 18;
		printAllPaths(root, sum);
	}

	void printAllPaths(BinaryTreeNode node, int sum) {
		Map<Integer, List<BinaryTreeNode>> left = populateSumAndPaths(node.left);
		Map<Integer, List<BinaryTreeNode>> right = populateSumAndPaths(node.right);

		left.put(0, new ArrayList<BinaryTreeNode>());
		right.put(0, new ArrayList<BinaryTreeNode>());
		// System.out.println("Left: ");
		// printSumAndPathMap(left);
		// System.out.println("Right: ");
		// printSumAndPathMap(right);

		for ( int s1 : left.keySet()) {
			if (right.get(sum - s1 - node.data) != null) {
				List<BinaryTreeNode> solution = new ArrayList<BinaryTreeNode>(left.get(s1));
				Collections.reverse(solution);
				solution.add(node);
				solution.addAll(right.get(sum - s1 - node.data));

				System.out.println(solution);
			}
		}

		if (node.left != null) {
			printAllPaths(node.left, sum);
		}
		if (node.right != null) {
			printAllPaths(node.right, sum);
		}
	}

	void printSumAndPathMap(Map<Integer, List<BinaryTreeNode>> map) {
		for (Map.Entry<Integer, List<BinaryTreeNode>> e : map.entrySet()) {
			System.out.println(e.getKey() + " : " + e.getValue());
		}
	}

	Map<Integer, List<BinaryTreeNode>> populateSumAndPaths(BinaryTreeNode node) {
		Map<Integer, List<BinaryTreeNode>> sumPathMap = new HashMap<Integer, List<BinaryTreeNode>>();
		if (node == null) return sumPathMap;

		Map<BinaryTreeNode, Integer> sumMap = new HashMap<BinaryTreeNode, Integer>();

		Queue<BinaryTreeNode> q = new ArrayDeque<BinaryTreeNode>();
		q.offer(node);
		sumMap.put(node, node.data);
		sumPathMap.put(sumMap.get(node), getPath(node, node));

		BinaryTreeNode n;
		while (q.size() != 0) {
			n = q.poll();

			if (n.left != null) {
				sumMap.put(n.left, n.left.data + sumMap.get(n));
				sumPathMap.put(sumMap.get(n.left), getPath(n.left, node));
				q.offer(n.left);
			}

			if (n.right != null) {
				sumMap.put(n.right, n.right.data + sumMap.get(n));
				sumPathMap.put(sumMap.get(n.right), getPath(n.right, node));
				q.offer(n.right);
			}
		}
		return sumPathMap;
	}

	List<BinaryTreeNode> getPath(BinaryTreeNode start, BinaryTreeNode end) {
		List<BinaryTreeNode> path = new ArrayList<BinaryTreeNode>();
		while (start != end) {
			path.add(start);
			start = start.parent;
		}
		path.add(end);
		Collections.reverse(path);
		return path;
	}

	public void problem6() {
		List<Integer> A = new ArrayList<Integer>(Arrays.asList(
			8,4,12,2,6,10,14,1,3,5,7,9,11,13,15));
		// int A[] = {4,2,6,1,3,5,7};	
		

		BinaryTreeNode root = getBinarySearchTree(A);
		// BinaryTreeNode root = getBinarySearchTree(randArray(20));

		BinaryTreeNode node = root.right.right.right;
		BinaryTreeNode next = getInorderNext(node);
		printBST(root);
		System.out.println("Next of " + node.data + " is " + (next == null? "null" : next.data));
	}

	public BinaryTreeNode getInorderNext(BinaryTreeNode node) {
		assert node != null;
		if (node.right != null) {
			BinaryTreeNode t = node.right;
			while (t.left != null) {
				t = t.left;
			}
			return t;
		}
		else {
			BinaryTreeNode t = node;
			while (t.parent != null && t.parent.right == t) {
				t = t.parent;
			}
			t = t.parent;
			return t;
		}
	}

	public class Node {

	}

	public class BinaryTreeNode extends Node {
		final BinaryTreeNode parent;
		BinaryTreeNode left, right;
		int data;

		BinaryTreeNode(BinaryTreeNode parent, int data) {
			this.parent = parent;
			this.data = data;

			left = null; right = null;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(data);
			return sb.toString();
		}
	}

	public List<Integer> randArray(final int size) {
		Random rand = new Random();

		List<Integer> A = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			A.add(rand.nextInt(size));
		}
		return A;
	}

	public BinaryTreeNode getBinarySearchTree(List<Integer> A) {
		if (A.size() == 0) return null;

		final BinaryTreeNode head = new BinaryTreeNode(null, A.get(0));
		for (int i = 1; i < A.size(); i++) {
			BinaryTreeNode n = head;
			while (n != null) {
				if (A.get(i) <= n.data) {
					if (n.left == null) {
						n.left = new BinaryTreeNode(n, A.get(i));
						break;
					}
					else n = n.left;
				}
				else {
					if (n.right == null) {
						n.right = new BinaryTreeNode(n, A.get(i));
						break;
					}
					else n = n.right;
				}
			}

		}
		return head;
	}

	public int getBSTHeight(final BinaryTreeNode n) {
		return (n == null) ? 0:
			1 + Math.max(getBSTHeight(n.left),
					getBSTHeight(n.right));
	}

	public int getHeight(BinaryTreeNode n) {
		int c = 0;
		while (n != null) {
			n = n.parent; c++;
		}
		return c;
	}

	public String spaces(int count) {
		StringBuilder sb = new StringBuilder();
		while (count-- > 0) sb.append(" ");
		return sb.toString();
	}

	public void printBST(final BinaryTreeNode head) {
		final int WIDTH = 80;
		final int GAP = 2;
		final Map<BinaryTreeNode, Integer> indexMap = new HashMap<BinaryTreeNode, Integer>();

		Queue<BinaryTreeNode> q = new ArrayDeque<BinaryTreeNode>();
		Queue<BinaryTreeNode> qn = new ArrayDeque<BinaryTreeNode>();

		q.add(head);
		indexMap.put(head, 40);
		System.out.printf("%s%d\n", spaces(indexMap.get(head)),head.data);

		final int treeHeight = getBSTHeight(head);
		int offset, h, gap;
		StringBuilder sb = new StringBuilder();
		while (q.size() != 0) {
			BinaryTreeNode n = q.poll();
			h = getHeight(n);
			gap  = GAP + (int) (Math.pow(2,treeHeight - h));

			if (n.left != null) {
				qn.add(n.left);
				indexMap.put(n.left, indexMap.get(n) - gap);
				offset = indexMap.get(n.left);
				sb.append(spaces(offset - sb.length()));
				sb.append(n.left.data);
			}
			if (n.right != null) {
				qn.add(n.right);
				indexMap.put(n.right, indexMap.get(n) + gap);
				offset = indexMap.get(n.right);
				sb.append(spaces(offset - sb.length()));
				sb.append(n.right.data);
			}

			if (q.size() == 0) {
				System.out.println(sb.toString());
				sb.setLength(0);
				Queue<BinaryTreeNode> t = q; q = qn; qn = t;
			}
		}
	}
}
