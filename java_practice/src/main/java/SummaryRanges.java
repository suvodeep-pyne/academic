import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Created by spyne on 8/28/16.
 */
public class SummaryRanges
{
    private final TreeMap<Integer, Interval> map = new TreeMap<>();

    /** Initialize your data structure here. */
    public SummaryRanges() {

    }

    public void addNum(int val) {

    }

    public List<Interval> getIntervals() {
        return null;
    }

    public static class Interval
    {
        final int start;
        final int end;

        Interval()
        {
            start = 0;
            end = 0;
        }

        Interval(int s, int e)
        {
            start = s;
            end = e;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Interval interval = (Interval) o;
            return start == interval.start &&
                    end == interval.end;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(start, end);
        }

        @Override
        public String toString()
        {
            return "[" + start + ", " + end + ']';
        }
    }

}
