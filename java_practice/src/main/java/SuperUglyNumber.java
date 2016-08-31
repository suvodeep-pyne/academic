import java.util.PriorityQueue;

/**
 * Created by spyne on 8/29/16.
 */
public class SuperUglyNumber
{
    public int nthSuperUglyNumber(final int n, final int[] primes) {
        int ugly[] = new int[n];
        int index[] = new int[primes.length];
        int factor[] = new int[primes.length];

        ugly[0] = 1;
        System.arraycopy(primes, 0, factor, 0, primes.length);

        for (int i = 1; i < n; ++i) {
            int min = factor[0];
            for (int j = 1; j < primes.length; ++j) {
                min = Math.min(min, factor[j]);
            }
            ugly[i] = min;
            for (int j = 0; j < primes.length; ++j) {
                if (min == factor[j]) {
                    factor[j] = primes[j] * ugly[++index[j]];
                }
            }
        }
        return ugly[n - 1];
    }
}
