import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by spyne on 8/30/16.
 */
public class TestCompareVersionNumbers
{
    CompareVersionNumbers instance = new CompareVersionNumbers();
    @Test
    public void test1()
            throws Exception
    {
        assertEquals(instance.compareVersion("1", "0"), 1);
        assertEquals(instance.compareVersion("0", "1"), -1);
    }

    @Test
    public void test2()
            throws Exception
    {
        assertEquals(instance.compareVersion("1.1", "0"), 1);
    }

    @Test
    public void test3()
            throws Exception
    {
        assertEquals(instance.compareVersion("1.2.3", "1.2.1"), 1);
    }

}