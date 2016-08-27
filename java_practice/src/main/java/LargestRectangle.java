import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Created by spyne on 8/27/16.
 */
public class LargestRectangle
{
    public int largestRectangleArea(int... heights) {
        Stack<Integer> s = new Stack<>();
        int max = 0;

        int i = 0;
        while (i < heights.length || !s.isEmpty()) {
            if (s.isEmpty() || (i < heights.length && heights[s.peek()] <= heights[i])) {
                s.push(i++);
            }
            else {
                final int t = s.pop();
                final int area = heights[t] * (s.isEmpty() ? i : i - s.peek() - 1);
                max = Math.max(max, area);
            }
        }
        return max;
    }

    public int largestRectangleArea2(int... heights) {
        final int N = heights.length;
        List<Integer> uniqueHeights = new ArrayList<>(Arrays.stream(heights).boxed().collect(Collectors.toSet()));
        Collections.sort(uniqueHeights);

        int max = 0;
        int A[][] = new int[heights.length][uniqueHeights.size()];
        for (int i = 0; i < heights.length; ++i) {
            for (int j = 0; j < uniqueHeights.size(); ++j) {
                Integer v = uniqueHeights.get(j);
                A[i][j] = v > heights[i]? 0 : i == 0? v : v + A[i - 1][j];
                max = Math.max(max, A[i][j]);
            }
        }
        int[][] matrix = A;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        return max;
    }

    public int largestRectangleArea3(int... heights) {
        List<Integer> uniqueHeights = new ArrayList<>(Arrays.stream(heights).boxed().collect(Collectors.toSet()));
        Collections.sort(uniqueHeights);

        int max = 0;
        int A[] = new int[uniqueHeights.size()];
        for (int i = 0; i < heights.length; ++i) {
            for (int j = 0; j < uniqueHeights.size(); ++j) {
                Integer v = uniqueHeights.get(j);
                A[j] = v > heights[i]? 0 : i == 0? v : v + A[j];
                max = Math.max(max, A[j]);
            }
        }
        return max;
    }

    public int largestRectangleArea4(int... heights) {
        Set<Integer> visited = new HashSet<>();
        int max = 0;
        for (int height : heights) {
            if (visited.contains(height)) {
                continue;
            }
            int A = 0;
            visited.add(height);
            for (int j = 0; j < heights.length; ++j) {
                A = height > heights[j] ? 0 : j == 0 ? height : height + A;
                max = Math.max(max, A);
            }
        }
        return max;
    }

    public int largestRectangleArea5(int... heights) {
        int max = 0;
        for (int height : heights) {
            int A = 0;
            for (int j = 0; j < heights.length; ++j) {
                A = height > heights[j] ? 0 : j == 0 ? height : height + A;
                max = Math.max(max, A);
            }
        }
        return max;
    }

    public int largestRectangleArea6(int... heights) {
        if (heights == null || heights.length == 0) {
            return 0;
        }

        Stack<Integer> stack = new Stack<Integer>();

        int max = 0;
        int i = 0;

        while (i < heights.length) {
            //push index to stack when the current height is larger than the previous one
            if (stack.isEmpty() || heights[i] >= heights[stack.peek()]) {
                stack.push(i);
                i++;
            } else {
                //calculate max value when the current height is less than the previous one
                int p = stack.pop();
                int h = heights[p];
                int w = stack.isEmpty() ? i : i - stack.peek() - 1;
                max = Math.max(h * w, max);
            }

        }

        while (!stack.isEmpty()) {
            int p = stack.pop();
            int h = heights[p];
            int w = stack.isEmpty() ? i : i - stack.peek() - 1;
            max = Math.max(h * w, max);
        }

        return max;
    }
}
