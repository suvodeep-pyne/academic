import java.util.*;

public class BinaryTree {
	public static void main(String[] args) {
		BinaryTree instance = new BinaryTree();
		instance.populate(15);
		instance.printTree();

		instance.rotateLeft();
		instance.printTree();
	}

	final long SEED = 1250;
	final int MAX = 1024;
	int[] tree = new int[MAX];

	public BinaryTree() {
		
	}

	void rotateLeft() {
		final int root = 0;
		final int rRoot = rChild(root);
		final int lRoot = lChild(root);
		// cant left rotate if there is no root and 
		// right child in root
		if (tree[root] == 0 || tree[rRoot] == 0) return;

		int[] copy = new int[MAX];
		copy[root] = tree[rRoot];
		copy[lRoot] = tree[root];
		copySubTree(tree, rChild(rRoot), copy, rRoot);
		copySubTree(tree, lChild(root), copy, lChild(lRoot));
		copySubTree(tree, lChild(rRoot), copy, rChild(lRoot));

		// copy back to the main tree
		System.arraycopy(copy, 0, tree, 0, MAX);
	}

	class Pair {
		final int a, b;
		Pair(int a, int b) {this.a = a; this.b = b;}
	}

	void copySubTree(int[] sTree, int sPos, int[] dTree, int dPos) {
		if (sTree[sPos] == 0) return;
		Queue<Pair> q = new ArrayDeque<Pair>();
		q.offer(new Pair(sPos, dPos));
		while (q.size() != 0) {
			Pair p = q.poll();

			// Copy the element
			dTree[p.b] = sTree[p.a];

			if (sTree[lChild(p.a)] != 0) {
				q.offer(new Pair(lChild(p.a), lChild(p.b)));
			}
			if (sTree[rChild(p.a)] != 0) {
				q.offer(new Pair(rChild(p.a), rChild(p.b)));
			}
		}
	}

	int height(final int nodeIdx) {
		if (tree[nodeIdx] == 0) return 0;

		int h = 0;
		Queue<Integer> q1 = new ArrayDeque<Integer>();
		Queue<Integer> q2 = new ArrayDeque<Integer>();
		q1.offer(nodeIdx);
		while (q1.size() != 0) {
			int ii = q1.poll();

			if (tree[lChild(ii)] != 0) {
				q2.offer(lChild(ii));
			}
			if (tree[rChild(ii)] != 0) {
				q2.offer(rChild(ii));
			}

			if (q1.size() == 0) {
				Queue<Integer> t = q1;
				q1 = q2; q2 = t;

				++h;
			}
		}
		return h;
	}

	void populate(int N) {
		Random r = new Random(SEED);
		for(int i = 0; i < N; i++) {
			int value = 1 + r.nextInt(100);
			int ii = 0;
			while (ii < tree.length) {
				if (tree[ii] == 0) {
					tree[ii] = value;
					break;
				}
				if (value < tree[ii]) {
					ii = lChild(ii);
				}
				else {
					ii = rChild(ii);
				}
			}
		}
	}

	int lChild(final int ii) {
		return 2 * ii + 1;
	}

	int rChild(final int ii) {
		return 2 * ii + 2;
	}

	void printTree() {
		System.out.print("Tree(" + height(0) + "): ");
		for(int i = 0; i < tree.length; i++) {
			if (tree[i] != 0) 
				System.out.printf("%2d[%2d] ", i, tree[i]);
		}
		System.out.println();
	}
}
