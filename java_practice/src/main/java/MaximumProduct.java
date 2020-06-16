class MaximumProduct {

  public static void main(String[] args) {
    System.out.println(new MaximumProduct().maximumProduct(new int[]{1, 2, 3, 4, -5, -6}));
    System.out.println(new MaximumProduct().maximumProduct(new int[]{1, 2, 3, 4, 5, 6}));
    System.out.println(new MaximumProduct().maximumProduct(new int[]{6, 5, 4, 3, 2, 1}));
    System.out.println(new MaximumProduct().maximumProduct(new int[]{-6, -5, -4, -3, -2, -1}));
  }

  public int maximumProduct(int[] nums) {
    int min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;
    int max1 = Integer.MIN_VALUE, max2 = Integer.MIN_VALUE, max3 = Integer.MIN_VALUE;

    for (final int num : nums) {
      if (num < min1) {
        min2 = min1;
        min1 = num;
      } else if (num < min2) {
        min2 = num;
      }

      if (num > max1) {
        max3 = max2;
        max2 = max1;
        max1 = num;
      } else if (num > max2) {
        max3 = max2;
        max2 = num;
      } else if (num > max3) {
        max3 = num;
      }
    }
    System.out.printf("%d %d %d %d %d\n", min1, min2, max1, max2, max3);

    return Math.max(min1 * min2 * max1, max1 * max2 * max3);
  }
}