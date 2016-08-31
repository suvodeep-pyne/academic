/**
 * Created by spyne on 8/30/16.
 */
public class CompareVersionNumbers
{
    public int compareVersion(String version1, String version2) {
        String[] p1 = version1.split("\\.");
        String[] p2 = version2.split("\\.");

        int N = Math.min(p1.length, p2.length);
        for (int i = 0; i < N; ++i) {
            int v1 = Integer.parseInt(p1[i]);
            int v2 = Integer.parseInt(p2[i]);
            if (v1 != v2) {
                return v1 > v2 ? 1 : -1;
            }
        }
        if (p1.length > p2.length && !checkAllZeroes(p1, N)) {
            return 1;
        }
        else if (p1.length < p2.length && !checkAllZeroes(p2, N)) {
            return -1;
        }
        return 0;
    }

    private boolean checkAllZeroes(String[] parts, int p) {
        for (int i = p; i < parts.length; ++i) {
            if (Integer.parseInt(parts[i]) != 0) {
                return false;
            }
        }
        return true;
    }
}
