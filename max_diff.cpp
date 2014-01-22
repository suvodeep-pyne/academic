#include <iostream>
#include <stdio.h>

using namespace std;

int main()
{
	int A[] = { 11, 5, 4, 3, 6, 8, 10, 5, 6, 6, 13, 15, 1, 13, 14, 5, 6, 7, 8, 10 };
	const int N = sizeof (A) / sizeof (int);

	int maxdiff = -1000, maxi = -1;
	int lowest = 1000, li = -1;
	for (int i = 0; i < N; ++i)
	{
		if (A[i] < lowest)
		{
			lowest = A[i];
			li = i;
		}
		if (A[i] - lowest > maxdiff)
		{
			maxdiff = A[i] - lowest;
			maxi = i;
		}
	}
	
	cout << "Max diff: ("<< li << ", " << maxi <<")" << maxdiff << endl;

	return 0;
}
