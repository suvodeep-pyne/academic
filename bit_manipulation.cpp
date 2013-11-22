#include <stdio.h>
#include <assert.h>

// #define NDEBUG

void problem1()
{
	int N = 0x410;
	int M = 0x13;

	int i = 2, j = 6;

	
	int R = N & ~(((1 << (j - i + 1)) - 1) << i) | (M << i);
	printf("%x\n", R);
}

void print_binary(double d)
{
	const int MAX = 32;

	for (int i = 1; i <= MAX; i++)
	{
		if ((d *= 2) >= 1)
		{
			d -= 1.0;
			printf("1");
		}
		else
		{
			printf("0");
		}
		if (d == 0)
		{
			break;
		}
	}
	printf("\n");
}

void problem2()
{
	print_binary(0.5);
}

void problem5()
{
	int A = 31, B = 14;
	int c = 0;
	for (int i = 0; i < sizeof (int) * 8; i++)
	{
		if ((A & (1 << i)) != (B & (1 << i)))
			++c;
	}
	printf("Number of Bits: %d\n", c);
}

int main()
{
	problem5();
	return 0;
}

