import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by spyne on 8/27/16.
 */
public class TestLargestRectangle
{
    @Test
    public void test0()
            throws Exception
    {
        LargestRectangle lr = new LargestRectangle();
        assertEquals(lr.largestRectangleArea(1, 2, 4), 4);
    }

    @Test
    public void test1()
            throws Exception
    {
        LargestRectangle lr = new LargestRectangle();
        assertEquals(lr.largestRectangleArea(2, 1, 5, 6, 2, 3), 10);
    }

    @Test
    public void test2()
            throws Exception
    {
        LargestRectangle lr = new LargestRectangle();
        assertEquals(lr.largestRectangleArea(2, 2, 2, 4, 4, 4, 4, 4, 4, 100, 100), 200);
    }

    @Test
    public void test3()
            throws Exception
    {
        LargestRectangle lr = new LargestRectangle();
        assertEquals(lr.largestRectangleArea(2, 2, 2, 4, 4, 4, 4, 4, 4, 10, 10), 32);
    }

    @Test
    public void test4()
            throws Exception
    {
        int[] heights = new int[20000];
        for (int i = 0; i < heights.length; ++i) {
            heights[i] = i + 1;
        }
        LargestRectangle lr = new LargestRectangle();
        assertEquals(lr.largestRectangleArea(heights), 100010000);
    }


    @Test
    public void test5()
            throws Exception
    {
        LargestRectangle lr = new LargestRectangle();
        assertEquals(lr.largestRectangleArea(2, 1, 5, 6, 2, 3, 2, 2), 12);
    }

}