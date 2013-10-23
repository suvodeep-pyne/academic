#include <iostream>
#include <cstdio>
#include <stdint.h>

#define MAXSIZE 5000

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

int main()
{
	int a, b, c, dr;
	int length = 0;

	while(1)
	{
		read (a);
		read (b);
		read (c);

		if(a == b && b == c && c == 0) break;

		dr = b - a;
		if ( dr == c - b)
		{
			cout << "AP " << c + dr << endl;
		}
		else
		{
			dr = c / b;
			cout << "GP " << c * dr << endl;
		}
	}

	return 0;
}
