import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by spyne on 8/31/16.
 */
public class TestMaxProduct
{
    MaxProduct mp = new MaxProduct();
    @Test
    public void test1()
            throws Exception
    {
        assertEquals(mp.maxProduct(new int[] {-2, 0, -3, 4, 5, -5, -60}), 6000);
    }

    @Test
    public void test2()
            throws Exception
    {
        assertEquals(mp.maxProduct(new int[] {-2, 0, -3, 4, 5}), 20);
    }
}