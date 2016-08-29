import org.testng.annotations.Test;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;

/**
 * Created by spyne on 8/28/16.
 */
public class TestMergeIntervals
{
    MergeIntervals instance = new MergeIntervals();

    @Test
    public void test1()
            throws Exception
    {
        assertEquals(instance.merge(
                asList(I(1, 3), I(2, 6), I(8, 10), I(15, 18))),
                asList(I(1, 6), I(8, 10), I(15, 18)
        ));
    }

    @Test
    public void test2()
            throws Exception
    {
        assertEquals(instance.merge(
                asList(I(1, 3), I(2, 6), I(5, 10), I(9, 18))),
                asList(I(1, 18)
                ));
    }

    @Test
    public void test3()
            throws Exception
    {
        assertEquals(instance.merge(
                new ArrayList<>()),
                new ArrayList<MergeIntervals.Interval>()
                );
    }

    @Test
    public void test4()
            throws Exception
    {
        assertEquals(instance.merge(
                asList(I(1,5), I(1, 5))),
                asList(I(1,5))
        );
    }

    @Test
    public void test5()
            throws Exception
    {
        assertEquals(instance.merge(
                asList(I(1,4), I(4, 5))),
                asList(I(1,5))
        );
    }

    private MergeIntervals.Interval I(int s, int e) {return new MergeIntervals.Interval(s, e);}
}