import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by spyne on 8/27/16.
 */
public class TestLargestNumber
{
    @Test
    public void test1()
            throws Exception
    {
        assertEquals(new LargestNumber().largestNumber(3, 30, 34, 5, 9), "9534330");
    }

    @Test
    public void test2()
            throws Exception
    {
        assertEquals(new LargestNumber().largestNumber(3, 30, 34, 5, 9, 99, 989), "999989534330");
    }

    @Test
    public void test3()
            throws Exception
    {
        assertEquals(new LargestNumber().largestNumber(100, 3, 30, 34, 5, 9, 99, 989), "999989534330100");
    }

    @Test
    public void test4()
            throws Exception
    {
        assertEquals(new LargestNumber().largestNumber(0, 0), "0");
    }

    @Test
    public void test5()
            throws Exception
    {
        assertEquals(new LargestNumber().largestNumber(824,938,1399,5607,6973,5703,9609,4398,8247),
                "9609938824824769735703560743981399");
    }


    @Test
    public void test6()
            throws Exception
    {
        assertEquals(new LargestNumber().largestNumber(824,8247),
                "8248247");
    }

    @Test
    public void test7()
            throws Exception
    {
        assertEquals(new LargestNumber().largestNumber(310,3109),
                "3109310");
    }
}