#include <iostream>
#include <cstring>
#include <stdlib.h>
#include <stdio.h>

using namespace std;
#define MAXC 1000

int compute2(const int N, const int K, const int C) 
{
	int k, c, r;
	int result = C;
	for (int n = 2; n <= N; n++) {
		k = K, c = C;

		int r = c;
		int q = (c / n) * n;
		k -= q;
		c -= q;

		if (k < n && c > 0) {
			r += n - k;
		}
		result = ((1 + result) < r)? (1 + result) : r;
	}
	return result;
}

int compute(int N, int K, int C)
{
	if (N == 1) return C;
	if (K == 1) return N;

	int r1 = 1 + compute(N - 1, K, C);

	int r2 = C;
	int q = (C / N) * N;
	K -= q;
	C -= q;

	if (K < N && C > 0) {
		r2 += N - K;
	}

	return r1 < r2? r1 : r2;
}

int main()
{
	int T;
	cin >> T;

	int t = 1;
	int N, C, K, P;
	while (t <= T) 
	{
		cin >> N >> K >> C;

		int result = compute2(N, K, C);
		cout << "Case #" << t << ": " << result << endl;

		t++;
	}
	return 0;
}
