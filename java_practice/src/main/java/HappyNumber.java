import java.util.HashSet;
import java.util.Set;

class HappyNumber {

  public static void main(String[] args) {
    System.out.println(new HappyNumber().isHappy(2));
  }
  public boolean isHappy(int n) {
    Set<Integer> set = new HashSet<>();

    while(n != 1 && !set.contains(n)) {
      set.add(n);
      n = sumDigitSquares(n);
      System.out.println(n);
    }

    return n == 1;
  }

  private int sumDigitSquares(int n) {
    int sum = 0;
    while (n != 0) {
      int d = n % 10;
      sum += d * d;
      n /= 10;
    }
    return sum;
  }
}
