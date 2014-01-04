#include <iostream>
#include <utility>
#include <queue>
#include <stdio.h>

using namespace std;

const int MAX = 82;
int poss[MAX];

inline bool is_fixed(int n)
{
	return !(poss[n] & (poss[n] - 1));
}

// val: Find the set bit
inline int set_bit(int n)
{
	int val = 0;
	while (!(poss[n] & ( 1 << val))) ++val;
	return val;
}

void reduce(vector<int> given)
{
	queue<int> q;
	for (vector<int>::iterator ii = given.begin();
		ii != given.end(); ++ii)
	{
		q.push(*ii);
	}

	while (q.size() > 0)
	{
		int p = q.front();
		q.pop();

		// Only 1 bit is set. It is a power of 2
		if (is_fixed(p))
		{
			int val = set_bit(p);

			for (int i = 0; i < 9; ++i)
			{
				int h = (p / 9) * 9 + i;
				if (h != p && !is_fixed(h))
				{
					poss[h] &= ~(1 << val);
					if (is_fixed(h))
						q.push(h);
				}
				int v = p % 9 + i * 9;
				if (v != p && !is_fixed(v))
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
					int ii = i * 9 + j;
					if (ii != p && !is_fixed(ii))
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

void showPoss()
{
	long double posscount = 1;
	for (int i = 0; i < 81; i++)
	{
		printf("%3x ", poss[i]);
		int c = 0;
		for(int j = 0; j < 9; j++)
		{
			if(poss[i] & (1 << j))
			{
				printf("%d", 1 + j);
				++c;
			}
			else printf(".");
		}
		printf(" ");
		if ((i + 1) % 9 == 0) cout << endl;
		posscount *= c;
	}
	cout << endl;
	cout << "Possibility Count:" << posscount << endl;
}

void printPoss()
{
	for (int i = 0; i < 81; i++)
	{
		printf("%3x ", poss[i]);
		if ((i + 1) % 9 == 0) cout << endl;
	}
	cout << endl;
}

void print()
{
	for (int i = 0; i < 81; i++)
	{
		if (is_fixed(i))
			printf("%d", 1 + set_bit(i));
		else
			printf(".");
		if ((i + 1) % 9 == 0) cout << endl;
	}
	cout << endl;
}

void solve()
{
	char prob[MAX];
	cin >> prob;

	vector<int> given;
	for (int i = 0; i < MAX; i++)
	{
		if (prob[i] == '.')
			poss[i] = 0x1FF;
		else if (prob[i] >= '1' && prob[i] <= '9')
		{
			poss[i] = 1 << (prob[i] - '1');
			given.push_back(i);
		}
		else
			poss[i] = 0;
	}

	print();
	// printPoss();
	showPoss();

	reduce(given);
	
	print();
	// printPoss();
	showPoss();
}

int main() 
{
	int nTestCases = 0;
	cin >> nTestCases;

	for (int i = 1; i <= nTestCases; i++)
	{
		solve();
		break;
	}
	return 0;
}
