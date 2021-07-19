public class LongestPalindromicSubstring {

  public static void main(String[] args) {
    System.out.println(new LongestPalindromicSubstring().longestPalindrome("aacabdkacaa"));
  }

  public String longestPalindrome(String s) {
    final int l = s.length();
    if (l <= 1) {
      return s;
    }

    int[][] mat = new int[l][l];
    int max = 1, maxi = 0, maxj = 0;

    for (int i = l - 1; i >= 0; --i) {
      for (int j = i; j < l; ++j) {
        if (i == j) {
          mat[i][j] = 1;
          continue;
        }
        final int v = s.charAt(i) == s.charAt(j) && mat[i + 1][j - 1] == j - i + 1 - 2 ? 2 : 0;
        mat[i][j] = Math.max(mat[i][j - 1], mat[i + 1][j]);
        mat[i][j] = Math.max(mat[i][j], mat[i + 1][j - 1] + v);

        if (v == 2) {
          if (max < mat[i][j]) {
            max = mat[i][j];
            maxi = i;
            maxj = j;
          }
        }
      }
    }
//    printMat(mat);
//    System.out.println(max);
//    System.out.println(maxi);
//    System.out.println(maxj);

//    return "" + mat[0][l - 1];
    return s.substring(maxi, maxj + 1);
  }
}
