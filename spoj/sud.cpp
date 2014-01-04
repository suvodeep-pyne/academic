#include <iostream>
#include <utility>
#include <queue>
#include <stdio.h>

using namespace std;

const int MAX = 82;
int poss[MAX];

inline bool is_fixed(int n)
{
	return !(n & (n - 1));
}

// val: Find the set bit
inline int set_bit(int n)
{
	int val = 0;
	while (!(n & ( 1 << val))) ++val;
	return val;
}

void reduce(int start)
{
	queue<int> q;
	q.push(start);

	while (q.size() > 0)
	{
		int p = q.front();
		q.pop();

		// Only 1 bit is set. It is a power of 2
		if (is_fixed(poss[p]))
		{
			int val = set_bit(poss[p]);

			for (int i = 0; i < 9; ++i)
			{
				int h = (p / 9) * 9 + i;
				if (h != p)
				{
					poss[h] &= ~(1 << val);
					if (is_fixed(h))
						q.push(h);
				}
				int v = p % 9 + i * 9;
				if (v != p)
				{
					poss[v] &= ~(1 << val);
					if (is_fixed(v))
						q.push(v);
				}
			}
			int r = p / 9, c = p % 9;
			int si = (r / 3) * 3, sj = (c / 3) * 3;
			for (int i = si; i < si + 3; ++i)
			{
				for (int j = sj; j < sj + 3; ++j)
				{
					int ii = si * 9 + sj;
					if (ii != p)
					{
						poss[ii] &= ~(1 << val);
						if (is_fixed(ii))
							q.push(ii);
					}
				}
			}
		}
	}
}

void solve()
{
	char prob[MAX];
	cin >> prob;

	for (int i = 0; i < MAX; i++)
	{
		if (prob[i] == '.')
			poss[i] = 0x1FF;
		else if (prob[i] >= '1' && prob[i] <= '9')
			poss[i] = 1 << (prob[i] - '1');
		else
			poss[i] = 0;
	}

	// reduce();
	// cout << endl << endl;
	for (int i = 0; i < MAX; i++)
	{
		printf("%5d ", poss[i]);
		if ((i + 1) % 9 == 0) cout << endl;
	}
}

int main() 
{
	int nTestCases = 0;
	cin >> nTestCases;

	for (int i = 1; i <= nTestCases; i++)
	{
		solve();
	}
	return 0;
}
