import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ThreeSum {

  public static void main(String[] args) {
    System.out.println(new ThreeSum().threeSum(new int[]{-1, 0, 1, 0}));
  }

  public List<List<Integer>> threeSum(int[] nums) {
    Set<List<Integer>> results = new HashSet<>();
    if (nums.length < 3) {
      return new ArrayList<>(results);
    }

    final int n = nums.length;
    Arrays.sort(nums);

    for (int k = 0; k < n - 2; ++k) {
      int i = k + 1;
      int j = n - 1;

      final int nk = nums[k];
      while (i < j) {
        final int ni = nums[i];
        final int nj = nums[j];

        final int sum = ni + nj + nk;
        if (sum > 0) --j;
        else if (sum < 0) ++i;
        else {
          results.add(Arrays.asList(nk, ni, nj));
          ++i;
        }
      }
    }

    return new ArrayList<>(results);
  }
  public List<List<Integer>> threeSum2(int[] nums) {
    Set<List<Integer>> results = new HashSet<>();
    if (nums.length < 3) {
      return new ArrayList<>(results);
    }
    Map<Integer, Integer> m = new HashMap<>();
    final int n = nums.length;
    for (final int num : nums) {
      final Integer count = m.get(num);
      if (count == null) {
        m.put(num, 1);
      } else {
        m.put(num, count + 1);
      }
    }

    for (Map.Entry<Integer, Integer> e : m.entrySet()) {
      final int num = e.getKey();
      final int count = e.getValue();
      if (num == 0 && count >= 3) {
        results.add(Arrays.asList(0, 0, 0));
      } else if (num != 0 && count >= 2 && m.get(-2 * num) != null) {
        if (num < 0) {
          results.add(Arrays.asList(num, num, -2 * num));
        } else {
          results.add(Arrays.asList(-2 * num, num, num));
        }
      }
    }

    for (int i = 0; i < n - 2; ++i) {
      final int ni = nums[i];
      for (int j = i + 1; j < n - 1; ++j) {
        final int nj = nums[j];
        if (ni == nj) {
          continue;
        }

        final int nk = -(ni + nj);
        if (ni == nk || nj == nk) {
          continue;
        }

        if (m.get(nk) != null) {
          List<Integer> l = Arrays.asList(ni, nj, nk);
          Collections.sort(l);
          results.add(l);
        }
      }
    }
    return new ArrayList<>(results);
  }
}
