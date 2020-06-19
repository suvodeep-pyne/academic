public class EditDistance {

  public static void main(String[] args) {
    testcase("", "abcd");
    testcase("abcd", "");
    testcase("abcd", "abcd");
    testcase("abcd", "efgh");
    testcase("abcd", "abgd");
    testcase("abcd", "abgd");
    testcase("abcd", "abd");
    testcase("abcd", "abcde");
  }

  private static void testcase(final String s, final String s1) {
    System.out.println(s + " > " + s1 + " : " + new EditDistance().minDistance(s, s1));
  }

  public int minDistance(String word1, String word2) {
//    return editDistancebf(word1.toCharArray(), word2.toCharArray(), 0, 0);
    return editDistance(word1.toCharArray(), word2.toCharArray());
  }

  private int editDistance(char[] s1, char[] s2) {
    int[][] mat = new int[1 + s2.length][1 + s1.length];
    for (int i = 0; i < mat.length; i++) {
      for (int j = 0; j < mat[0].length; j++) {
        if (i == 0) {
          mat[i][j] = j;
        } else if (j == 0) {
          mat[i][j] = i;
        } else if (s2[i - 1] == s1[j - 1]) {
          mat[i][j] = mat[i - 1][j - 1];
        } else {
          mat[i][j] = 1 + Math.min(Math.min(
              mat[i][j - 1],
              mat[i - 1][j]
              ),
              mat[i - 1][j - 1]
          );
        }
      }
    }
//    print2D(mat);
    return mat[s2.length][s1.length];
  }

  private int editDistancebf(char[] s1, char[] s2, int i, int j) {
    if (i >= s1.length) {
      return s2.length - j;
    }
    if (j >= s2.length) {
      return s1.length - i;
    }
    if (s1[i] == s2[j]) {
      return editDistancebf(s1, s2, i + 1, j + 1);
    } else {
      return 1 + Math.min(Math.min(
          editDistancebf(s1, s2, i, j + 1),
          editDistancebf(s1, s2, i + 1, j)
          ),
          editDistancebf(s1, s2, i + 1, j + 1)
      );
    }
  }
}
