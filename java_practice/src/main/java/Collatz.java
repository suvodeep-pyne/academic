import java.util.HashMap;
import java.util.Map;

class Collatz {

  public static void main(String[] args) {
    final Collatz instance = new Collatz();

    int max = 0, max_i = -1;
    Map<Long, Integer> mem = new HashMap<>();
    mem.put(1L, 1);

    for (int i = 1; i <= 1e6; ++i) {
      final int length = instance.collatz(i, mem);
      System.out.printf("i: %3d  length: %3d\n", i, length);
      if (length > max) {
        max = length;
        max_i = i;
      }
    }
    System.out.println(max);
    System.out.println(max_i);
  }

  private int collatz(final long n,
      final Map<Long, Integer> mem) {
    Integer count = mem.get(n);
    if (count != null) {
      return count;
    }

    count = n % 2 == 0
        ? collatz(n / 2, mem)
        : collatz((3 * n) + 1, mem);
    ++count;
    mem.put(n, count);
    return count;
  }
}