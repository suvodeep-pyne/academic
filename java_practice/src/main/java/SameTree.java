import java.util.stream.Stream;

/**
 * Created by spyne on 8/26/16.
 */
public class SameTree
{
    public boolean isSameTree(TreeNode p, TreeNode q)
    {
        if (p != null ^ q != null) {
            return false;
        }
        if (p == null) {
            return true;
        }
        if (p.val != q.val) {
            return false;
        }
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    static TreeNode toTree(Integer... values)
    {
        int N = values.length;
        TreeNode[] tree = new TreeNode[N];
        for (int i = 0; i < N; ++i) {
            if (values[i] != null) {
                tree[i] = new TreeNode(values[i]);
            }
            else {
                tree[i] = null;
            }
        }
        for (int i = 0; i < N; ++i) {
            if (tree[i] == null) {
                continue;
            }
            if (2 * i + 1 < N) {
                tree[i].left = tree[2 * i + 1];
                if (2 * i + 2 < N) {
                    tree[i].right = tree[2 * i + 2];
                }
            }
        }
        return tree[0];
    }

    static class TreeNode
    {
        TreeNode left, right;
        final int val;

        TreeNode(int val)
        {
            this.val = val;
        }
    }
}
