#include <cstdlib>
#include <stdio.h>
#include <stdint.h>

#include <iostream>

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

const int MAX = 1 << 15;
int sqr[192], ways[MAX];
int main()
{
	int N;

	int i, j, k, l;
	for(i = 0; i < 192; i++) sqr[i] = i*i;
	for(i = 0; 4*sqr[i] < MAX; i++)
		for(j = i; sqr[i]+3*sqr[j] < MAX; j++)
			for(k = j; sqr[i]+sqr[j]+2*sqr[k] < MAX; k++)
				for(l = k; sqr[i]+sqr[j]+sqr[k]+sqr[l] < MAX; l++)
					ways[sqr[i]+sqr[j]+sqr[k]+sqr[l]]++;

	while(1)
	{
		read (N);
		if ( N == 0 ) break;

		cout << ways[N] << endl;
	}

	return 0;
}
