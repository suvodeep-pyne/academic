import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * Definition for an interval.
 * public class Interval {
 * int start;
 * int end;
 * Interval() { start = 0; end = 0; }
 * Interval(int s, int e) { start = s; end = e; }
 * }
 * <p>
 * Created by spyne on 8/28/16.
 */
public class MergeIntervals
{
    static class IntervalBoundary {
        final int val;
        final boolean isStart; // true if this is the start of the interval

        IntervalBoundary(int val, boolean isStart) {
            this.val = val;
            this.isStart = isStart;
        }

        @Override
        public String toString()
        {
            return "[" + val + ", " + isStart + ']';
        }
    }

    private List<IntervalBoundary> toIntervalBoundaries(List<Interval> intervals)
    {
        List<IntervalBoundary> ibs = new ArrayList<>(intervals.size() * 2);
        for (Interval interval : intervals) {
            ibs.add(new IntervalBoundary(interval.start, true));
            ibs.add(new IntervalBoundary(interval.end, false));
        }
        return ibs;
    }

    public List<Interval> merge(List<Interval> intervals)
    {
        List<Interval> mergedIntervals = new ArrayList<>();
        if (intervals == null || intervals.isEmpty()) {
            return mergedIntervals;
        }

        List<IntervalBoundary> ibs = toIntervalBoundaries(intervals);
        PriorityQueue<IntervalBoundary> heap = new PriorityQueue<IntervalBoundary>(ibs.size(), (o1, o2) -> {
            if (o1.val != o2.val) {
                return o1.val - o2.val;
            } else {
                int b1 = o1.isStart? 1 : 0;
                int b2 = o2.isStart? 1 : 0;
                return b2 - b1;
            }
        });

        ibs.forEach(heap::offer);

        // Number of open boundaries
        int nOpen = 0, start = Integer.MIN_VALUE;
        while (!heap.isEmpty()) {
            IntervalBoundary ib = heap.poll();

            if (ib.isStart) {
                nOpen++;
            } else {
                nOpen--;
            }
            if (nOpen == 1 && ib.isStart) {
                start = ib.val;
            }
            else if (nOpen == 0 && !ib.isStart) {
                assert start != Integer.MIN_VALUE;
                mergedIntervals.add(new Interval(start, ib.val));
                start = Integer.MIN_VALUE;
            }
        }
        return mergedIntervals;
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
