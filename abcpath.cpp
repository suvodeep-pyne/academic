#include <iostream>
#include <iomanip>
#include <cstdlib>
#include <sstream>

#define TESTRUN

#ifdef TESTRUN
#ifdef _MSC_VER // Compiling under Visual Studio
#include <Windows.h>
#endif

// #define NDEBUG
#endif

#include<assert.h>

using namespace std;

const int MAX_SIZE = 50;

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

inline void swap(int& a, int& b)
{
	int temp = a; a = b; b = temp;
}

int cmp(const void *v1, const void *v2)
{
	const int i1 = *(const int *)v1;
	const int i2 = *(const int *)v2;

	return i1 < i2 ? -1 : ( i1 > i2 );
}

inline int modValue(const int n)
{
	return n >= 0? n : -n;
}

class Point
{
public:
	int r, c;
	Point(int r, int c) : r(r), c(c) {}
	Point(const Point& o) : r(o.r), c(o.c)  
	{
	
	} 

	string toString() const
	{
		ostringstream oss (ostringstream::out);
		oss << "(" << r << ", " << c << ")"; 
		return oss.str();
	}

	Point& operator= (const Point& o)
	{
		r = o.r; c = o.c;
		return *this;
	}

	~Point()
	{
		// cout << "Destroyed " << this << " :: " << toString() << endl;
	}
#ifdef TESTRUN
#endif
};

const int MAX_QUEUE_SIZE = MAX_SIZE * MAX_SIZE;
class Queue
{
	int front, rear;
public:
	const Point *data[MAX_QUEUE_SIZE];

	Queue()
	{
		front = 0; rear = 0;
		for(int i = 0; i < MAX_QUEUE_SIZE; i++)
			data[i] = 0;
	}

	void push(const Point& p)
	{
		assert (!isFull());
		data[rear] = &p;

		if(rear == MAX_QUEUE_SIZE - 1)
			rear = 0;
		else
			rear++;
	}

	bool isEmpty() const
	{
		return front == rear;
	}

	bool isFull() const
	{
		return front == rear + 1 || (front == 0 && rear == MAX_QUEUE_SIZE - 1);
	}

	const Point& pop()
	{
		assert (!isEmpty());
		const Point& p = *data[front];
		if(front == MAX_QUEUE_SIZE - 1)
			front = 0;
		else
			front++;
		return p;
	}

	int size() const
	{
		if(front <= rear)
			return rear - front;
		else
			return rear + (MAX_QUEUE_SIZE - front);
	}

	string toString() const
	{
		ostringstream oss (ostringstream::out);
		oss << "Queue: Front(" << front << ")" << " Rear(" << rear << ") Data :: ";
		int f = front;
		while(f != rear)
		{
			oss << data[f]->toString() << " ";
			if(f == MAX_QUEUE_SIZE - 1)
				f = 0;
			else
				f++;
		}
		oss << endl;
		return oss.str();
	}

	~Queue()
	{
		for(int i = 0; i < MAX_QUEUE_SIZE; i++)
		{
			if(data[i]) delete data[i];
		}
	}
};

inline int findLongestPath(const char grid[MAX_SIZE + 1][MAX_SIZE + 1], const int nRows, const int nCols)
{
	bool visited[MAX_SIZE + 1][MAX_SIZE + 1];
	int maxLength = 0;
	Queue q;

	for(int i = 0; i < nRows; i++)
		for( int j = 0; j < nCols; j++)
		{
			if(grid[i][j] == 'A') 
			{
				//cout<< q.toString();
				q.push(*(new Point(i, j)));
				maxLength = 1;
			}
			visited[i][j] = false;
		}

	// cout<< q.toString();
	char curr;
	const Point* p;
	while (q.size() > 0)
	{
		p = &q.pop();

		if (!visited[p->r][p->c])
		{
			visited[p->r][p->c] = true;
			curr = grid[p->r][p->c];
			if(maxLength < curr - 'A' + 1) maxLength = curr - 'A' + 1;

			/* Add child nodes to the queue */
			for(int i = -1; i <= 1; i++)
				for(int j = -1; j <= 1; j++)
					if((i != 0 || j != 0) && p->r + i >= 0 && p->r + i < nRows && p->c + j >= 0 && p->c + j < nCols)
						if(!visited[p->r + i][p->c + j] && grid[p->r + i][p->c + j] - curr == 1)
							q.push(*(new Point(p->r + i, p->c + j)));

		}
	}
	return maxLength;
}

int main() 
{
	int nRows, nCols;
	char grid[MAX_SIZE + 1][MAX_SIZE + 1];

	/* Read in the number */
#ifdef TESTRUN
	srand((unsigned)time(NULL));
#endif
	int testcase = 1;
	while(1)
	{
#ifdef TESTRUN
		nRows = 50; nCols = 50;
#else
		read(nRows); read(nCols);
#endif
		if(nRows == 0 && nCols == 0) break;
		if(!nRows || !nCols) {cout << "Case " << testcase << ": " << 0 << endl; continue;}

#ifdef TESTRUN
		for(int i = 0; i < nRows; i++)
		{
			for( int j = 0; j < nCols; j++)
			{
				grid[i][j] = rand() % 7 + 'A';
				cout << grid[i][j];
			}
			cout << endl;
		}
		unsigned long startTime_, diffInMilliSec;
		startTime_ = GetTickCount();
#else
		/* Input rows and columns */
		for(int i = 0; i < nRows; i++)
		{
			for( int j = 0; j < nCols; )
			{
				grid[i][j] = getchar();
				if(grid[i][j] >= 'A' && grid[i][j] <= 'Z') j++;
			}
		}
#endif
		int result = findLongestPath(grid, nRows, nCols);
		cout << "Case " << testcase << ": " << result << endl;

		
#ifdef TESTRUN
		diffInMilliSec = GetTickCount() - startTime_;
		printf("Time taken : %lu millisec\n", diffInMilliSec);
		if(testcase == 1) break;
#endif
		testcase++;
	}
	system("PAUSE");
	return 0;
}
