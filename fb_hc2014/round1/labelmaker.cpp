#include <iostream>
#include <cstring>
#include <stdlib.h>
#include <stdio.h>

using namespace std;
#define MAX 25
#define MAXO 25
#define MAXN 9223372036854775807L

typedef unsigned long UL;

void computeLabel(char* L, UL N, char* O)
{
	int nAlpha = strlen(L);
	int nDigits = 1;
	UL power = nAlpha;
	bool overflow = false;
	while (N >= power) {
		++nDigits;
		N -= power;

		if (power > MAXN / nAlpha) {
			overflow = true;
			break;
		}
		power *= nAlpha;
	}
	// cout << "#Digits: " << nDigits << " " << N << endl;

	UL q, r;
	if(!overflow) 
		power /= nAlpha;
	int i = 0;
	while (nDigits--) {
		// cout << "N: " << N << " " << power << endl;
		if (power) {
			q = N / power;
			r = N % power;
		}
		O[i++] = L[q];

		power /= nAlpha;
		N = r;
	}
}

int main()
{
	// T Test cases
	int T;
	cin >> T;

	int t = 1;

	UL N;
	char L[MAX];
	char O[1000];
	while (t <= T) 
	{
		memset(L, 0, MAX * sizeof(char));
		memset(O, 0, MAXO * sizeof(char));
		cin >> L;
		cin >> N;

		computeLabel(L, N - 1, O);
		// cout << L << " " << strlen(L) << " " << N << endl;
		cout << "Case #" << t << ": " << O << endl;

		t++;
	}
	return 0;
}
