#include <iostream>

using namespace std;

#define MAX 20

int main()
{
	// T Test cases
	int T;
	cin >> T;

	int t = 1;

	while (t <= T) 
	{
		bool result = true;

		int N; // Dimension
		cin >> N;

		bool A[MAX][MAX];
		int l = 0;
		int tx = -1, ty = -1;
		int bx = -1, by = -1;
		for (int i = 0; i < N; i++)
		{
			int _l = 0;
			for (int j = 0; j < N; j++)
			{
				char ch;
				cin >> ch;
				switch (ch)
				{
					case '.': A[i][j] = false; break;
					case '#': A[i][j] = true; ++_l; 
							  if (tx == -1)
							  {
								  tx = i; ty = j;
							  }
							  break;
					default: cerr << "error" << endl;
				}
				if (l)
				{
					// first line done
					if ((i >= tx && i < tx + l) &&
							(j >= ty && j < ty + l))
					{
						if (!A[i][j]) result = false;
					}
					else
					{
						if (A[i][j]) result = false;
					}
				}
			}
			if (!l) l = _l;
		}

		cout << "Case #" << t << ": " << (result? "YES" : "NO") << endl;

		t++;
	}
	return 0;
}
