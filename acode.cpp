#include <iostream>
#include <cstdio>
#include <stdint.h>

#define MAXSIZE 5000

using namespace std;

int read(uint8_t* msg, int &length)
{
	length = 0;
	int sign, ch = getchar();
	while ((ch < '0' || ch > '9') && ch != '-' && ch != EOF) ch = getchar();
	do msg[length++] = ch - '0';
	while((ch=getchar())>='0' && ch<='9');
	return 1;
}

int main()
{
	uint8_t msg[MAXSIZE];
	int length = 0;

	while(1)
	{
		read (msg, length);
		if (length == 1 && msg[0] == 0) break;

		uint64_t sum[2], t = 0;
		sum[0] = 1; sum[1] = 0;
		for (int i = 0; i < length; i++)
		{
			t = 0;
			if (msg[i] != 0) t += sum[0];
			if (i > 0 && msg[i - 1] != 0 && 
				msg[i - 1] * 10 + msg[0] <= 26)
				t += sum[1];
			sum[1] = sum[0];
			sum[0] = t;	
		}

		cout << t << endl;
	}
		
	return 0;
}
