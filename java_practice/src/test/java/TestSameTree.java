import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by spyne on 8/26/16.
 */
public class TestSameTree
{
    @Test
    public void test1()
            throws Exception
    {
        SameTree.TreeNode tree1 = SameTree.toTree(1, 2, 3);

        SameTree sameTree = new SameTree();
        assertTrue(sameTree.isSameTree(tree1, tree1));
    }

    @Test
    public void test2()
            throws Exception
    {
        SameTree.TreeNode tree1 = SameTree.toTree(1, 2, 3);
        SameTree.TreeNode tree2 = SameTree.toTree(1, 2, 4);

        SameTree sameTree = new SameTree();
        assertFalse(sameTree.isSameTree(tree1, tree2));
    }
}