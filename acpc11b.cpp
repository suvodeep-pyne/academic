#include <cstdlib>
#include <stdio.h>
#include <stdint.h>

#include <iostream>
#define MAX_LENGTH 1001

using namespace std;

int read(int &x){
	x = 0; // reset x;
	int sign, ch = getchar();
	while ((ch < '0' || ch > '9') && ch != '-' && ch != EOF) ch = getchar();
	if (ch=='-') sign = -1, ch = getchar();
	else sign = 1;
	do x = (x << 3) + (x << 1) + ch - '0';
	while((ch=getchar())>='0' && ch<='9');
	x *= sign;
	return 1;
}

/* qsort int comparison function */
int int_cmp(const void *a, const void *b)
{
	const int *ia = (const int *)a; // casting pointer 
	const int *ib = (const int *)b; 
	return *ia - *ib; 
}

int find_closest(const int* const A, const int alen, const int val)
{
	int t, closest = A[0];
	int l = 0, r = alen - 1, mid;

	while (l <= r)
	{
		mid = (l + r) / 2;
		t = A[mid];
		if(abs(closest - val) > abs(t - val))
			closest = t;

		if(t == val) return t;
		else if( t > val) r = mid - 1;
		else l = mid + 1;
	}
	return closest;
}


int find_min_diff(const int* const A, const int alen,
		const int* const B, const int blen)
{
	int min_d = abs(A[0] - B[0]), t = min_d;

	for(int i = 0; i < blen; i++)
	{
		if (min_d == 0) return 0;
		t = abs(find_closest(A, alen, B[i]) - B[i]);
		if (min_d > t) min_d = t;
	}
	return min_d;
}

int main()
{
	int A[MAX_LENGTH], alen;
	int B[MAX_LENGTH], blen;

	int nTestCases;
	read (nTestCases);
	while(nTestCases--)
	{
		read (alen);
		for( int i = 0; i < alen; i++) read(A[i]);
		read (blen);
		for( int i = 0; i < blen; i++) read(B[i]);

		/* sort array using qsort functions */
		if (alen >= blen)
			qsort(A, alen, sizeof(int), int_cmp);
		else
			qsort(B, blen, sizeof(int), int_cmp);

		int d; 
		if(alen >= blen)
			d = find_min_diff(A, alen, B, blen);
		else
			d = find_min_diff(B, blen, A, alen);
		cout << d << endl;
	}

	return 0;
}
