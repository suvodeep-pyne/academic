#include <iostream>
#include <vector>
#include <algorithm>
#include <string>
#include <iomanip>
#include <sstream>

#define NDEBUG 
#include<assert.h>

using namespace std;

inline int read(int &x){
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


int main() 
{
	int nTestCases = 0;
	read(nTestCases);
	
	double p_win, p_win_prev, p_lose, p_lose_prev;
	p_win = p_win_prev = p_lose = p_lose_prev = 0.0;

	while(nTestCases--)
	{
		int nGames;
		double p;
		read(nGames); cin >> p;

		p_win_prev = p;
		p_lose_prev = 1 - p;

		for( int i = 2; i <= nGames; i++)
		{
			p_win  = p_win_prev * (1 - p) + p_lose_prev * p;
			p_lose = p_win_prev * p + p_lose_prev * (1 - p);

			p_win_prev = p_win;
			p_lose_prev = p_lose;
		}

		cout << p_win << endl; 
	}
 	// system("PAUSE");
	return 0;
}
