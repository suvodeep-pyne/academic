import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by spyne on 8/29/16.
 */
public class TestSuperUglyNumber
{
    @Test
    public void test1()
            throws Exception
    {
        SuperUglyNumber instance = new SuperUglyNumber();
        assertEquals(instance.nthSuperUglyNumber(12, new int[] {2, 7, 13, 19}), 32);
    }

    @Test
    public void test2()
            throws Exception
    {
        SuperUglyNumber instance = new SuperUglyNumber();
        assertEquals(instance.nthSuperUglyNumber(800, new int[] {37,43,59,61,67,71,79,83,89,97,101,103,113,127,131,157,163,167,173,179,191,193,197,199,211,229,233,239,251,257}),
                411811);
    }
}