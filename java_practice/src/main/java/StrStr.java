import static utils.Utils.printArr;

public class StrStr {

  public static void main(String[] args) {
    System.out.println(new StrStr().strStr("hello", "ll"));
    System.out.println(new StrStr().strStr("abcabcd", "abcd"));
    System.out.println(new StrStr().strStr("abcabcd", "abcde"));
    System.out.println(new StrStr().strStr("onionionspl", "onions"));
    printArr(new StrStr().lps("aaaaa"));
    printArr(new StrStr().lps("onions"));
    printArr(new StrStr().lps("aabcaabcd"));
    printArr(new StrStr().lps("abcdabca"));
    printArr(new StrStr().lps("aabaabaaa"));
  }

  /**
   * Using KMP algorithm for string search
   */
  public int strStr(String haystack, String needle) {
    final int m = haystack.length();
    final int n = needle.length();
    if (n == 0) {
      return 0;
    }
    final int[] lps = lps(needle);

    int j = 0;
    for (int i = 0; i < m; i++) {
      while (j > 0 && haystack.charAt(i) != needle.charAt(j)) {
        j = lps[j - 1];
      }
      if (haystack.charAt(i) == needle.charAt(j)) {
        j++;
      }
      if (j == n) {
        return i - j + 1;
      }
    }
    return -1;
  }

  private int[] lps(String p) {
    final int l = p.length();
    int[] lps = new int[l];

    int q = 0;
    lps[0] = 0;
    for (int i = 1; i < l; i++) {
      while (q > 0 && p.charAt(i) != p.charAt(q)) {
        q = lps[q - 1];
      }
      if (p.charAt(i) == p.charAt(q)) {
        q++;
      }
      lps[i] = q;
    }
    return lps;
  }

  public int strStrBf(String haystack, String needle) {
    final int m = haystack.length();
    final int n = needle.length();

    for (int i = 0; i < m - n + 1; i++) {
      boolean found = true;
      for (int j = 0; j < n; j++) {
        if (haystack.charAt(i + j) != needle.charAt(j)) {
          found = false;
          break;
        }
      }
      if (found) {
        return i;
      }
    }
    return -1;
  }
}
