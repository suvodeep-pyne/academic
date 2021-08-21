public class FirstBadVersion {

  private final int firstBadVersion;

  public FirstBadVersion(int firstBadVersion) {
    this.firstBadVersion = firstBadVersion;
  }

  public static void main(String[] args) {
    System.out.println(new FirstBadVersion(1702766719).firstBadVersion(2126753390));
  }

  public int firstBadVersion(int n) {
    if (isBadVersion(1)) {
      return 1;
    }

    int i = 1;
    int j = n;

    while (i < j) {
      int mid = i + (j-i)/2;
      if (!isBadVersion(mid)) {
        i = mid;
        if (i + 1 <= n) {
          if (isBadVersion(i + 1)) {
            return i + 1;
          }
        }
      } else {
        j = mid;
      }
    }

    return -1;
  }

  private boolean isBadVersion(final int i) {
    return i >= firstBadVersion;
  }
}
