package utils;

public class Utils {

  private Utils() {
  }

  public static void printArr(int arr[]) {
    for (int e : arr) {
      System.out.print(e + " ");
    }
    System.out.println();
  }

  public static void printMat(int mat[][]) {
    for (int[] row : mat) {
      for (int x : row) {
        System.out.print(x + " ");
      }
      System.out.println();
    }
  }
}
