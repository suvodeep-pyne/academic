#include <iostream>
#include <utility>
#include <queue>
#include <stdio.h>

using namespace std;

const int MAXM = 183, MAXN = 183;
int dist[MAXN][MAXM];

void solve()
{
	// N rows, M columns
	int M, N;
	cin >> N >> M;

	char row[MAXM];
	queue<int> q1, q2;
	for (int i = 0; i < N; i++)
	{
		cin >> row;
		for (int j = 0; j < M; j++)
		{
			if (row[j] > '0')
			{
				q1.push(i * M + j);
				dist[i][j] = 0;
			}
			else
			{
				dist[i][j] = 1000;
			}
		}
		// cout << row << endl;
	}

	int d = 0;
	while(q1.size() > 0)
	{
		int n = q1.front();
		q1.pop();
		int &curr = dist[n / M][n % M];
		if (!d || d < curr)
		{
			curr = d;
			// Add successor nodes
			if (n >= M) 			
				q2.push(n - M);
			if (n < (N - 1) * M) 	
				q2.push(n + M);
			if (n % M != 0) 		
				q2.push(n - 1);
			if ((n + 1) % M != 0) 	
				q2.push(n + 1);
		}
		if (q1.size() == 0)
		{
			swap(q1, q2);
			d++;
		}
	}

	// cout << endl;
	for (int i = 0; i < N; i++)
	{
		for (int j = 0; j < M; j++)
		{
			cout << dist[i][j];
			if (j != M - 1) cout << " ";
		}
		cout << endl;
	}

	// cout << endl << endl;
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
