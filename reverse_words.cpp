#include <iostream>
#include <string.h>
#include <stdio.h>

#define MAX 80
using namespace std;

void reverse(char * const str, int start, int end)
{
	for (int i = start, j = end; i < j; i++, j--)
	{
		char t = str[i]; str[i] = str[j]; str[j] = t;
	}
}

int main()
{
	char line[MAX + 1];
	cin.getline(line, MAX);

	const int length = strlen(line);
	reverse(line, 0, length - 1);
	int ws = -1;
	for(int i = 0; i < length; ++i)
	{
		if (ws < 0 && line[i] != ' ')
		{
			ws = i;
		}
		if (ws >= 0 && line[i] != ' ' && (i + 1 >= length || line[i + 1] == ' '))
		{
			reverse(line, ws, i);
			ws = -1;
		}
	}
	cout << line << endl;
	return 0;
}
