#include <iostream>
#include <iomanip>
#include <cstdlib>

// #define TESTRUN

#ifdef TESTRUN
#ifdef _MSC_VER // Compiling under Visual Studio
#include <Windows.h>
#endif

// #define NDEBUG
#endif

#include<assert.h>

using namespace std;

const int MAX_N = 100;
const int UL = 30000;
const int LL = -30000;



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

inline bool binarySearch(const int* const arr, const int N, const int ele, int* const closest = 0, int* const count = 0)
{
	if(count) *count = 0;

	bool found = false;
	int l = 0, r = N - 1, mid;
	if(ele < arr[0])
	{
		if(closest) *closest = 0;
		return false;
	}
	if(ele > arr[r])
	{
		if(closest) *closest = r;
		return false;
	}
	while ( l <= r )
	{
		mid = (l + r) / 2;
		
		if(arr[mid] == ele) 
		{
			found = true; 
			if(count)
			{
				*count = 1;
				int i = mid - 1;
				while (i >= 0 && arr[i] == ele) (*count)++, i--;
				i = mid + 1;
				while (i < N && arr[i] == ele) (*count)++, i++;
			}
			break;
		}
		else if(arr[mid] > ele) r = mid - 1;
		else l = mid + 1;
	}
	if(closest)
	{
		*closest = mid;
		if(mid + 1 < N && modValue(arr[mid] - ele) > modValue(arr[mid + 1] - ele))
		{
			*closest = mid + 1;
		}
	}
	return found;
}


/* Find Min and Max of an array */
inline void findMinMax(const int* const arr, const int a_size, int& min, int& max)
{
	min = max = arr[0];

	for(int i = 1; i < a_size; i++)
	{
		if(min < arr[i]) min = arr[i];
		if(max > arr[i]) max = arr[i];
	}
}

/* Find min and max value of cartesian product. */
inline void findMinMaxProd(const int* const a1, const int a1_size,
						   const int* const a2, const int a2_size,
						   int& min, int& max)
{
	int v[4] = { a1[0] * a2[0], a1[0] * a2[a2_size - 1], a1[a1_size - 1] * a2[0], a1[a2_size - 1] * a2[a1_size - 1] };

	if( v[0] > v[1] && v[0] > v[2] && v[0] > v[3] ) max = v[0];
	else if (v[1] > v[2] && v[1] > v[3]) max = v[1];
	else if (v[2] > v[3]) max = v[2];
	else max = v[3];

	if( v[0] < v[1] && v[0] < v[2] && v[0] < v[3] ) min = v[0];
	else if (v[1] < v[2] && v[1] < v[3]) min = v[1];
	else if (v[2] < v[3]) min = v[2];
	else min = v[3];
}

inline long method1(const int* const arr, const int N);
inline long method2(const int* const arr, const int N);
inline long method3(const int* const arr, const int N);
inline long method4(const int* const arr, const int N);
inline long method5(const int* const arr, const int N);

int main() 
{
	int N;
	int S[MAX_N + 1]; // Set

#ifdef TESTRUN
	int nTestCases = 1; while (nTestCases--) {cout << nTestCases << " test cases remaining.. " << endl;
#endif
	/* Read in the number */
#ifdef TESTRUN
	N = 100;
	// cout << "N : " << N << endl;
	srand((unsigned)time(NULL));
	for(int i = 0; i < N;)
	{
		int t = (rand() + rand()) % 60001 - 30000;
		bool unique = true;
		for(int j = 0; j < i; j++)
		{
			if(S[j] == t) {unique = false; break;}
		}
		if(unique) 
		{
			// cout<< t << " ";
			S[i] = t; i++;
		}
	}
	// cout << endl;
	// S[20] = 0;
	// N = 3; S[0] = 5, S[1] = 7, S[2] = 10;
	// N = 3; S[0] = 3, S[1] = 5, S[2] = 7;
	// N = 2; S[0] = -1, S[1] = 1;

	unsigned long startTime_, diffInMilliSec;
	startTime_ = GetTickCount();
#else
	read(N);
	if(N == 0) {cout << 0 << endl; return 0;}
	for(int i = 0; i < N; i++) read(S[i]);
#endif
	qsort(S, N, sizeof (int), cmp);

	long count = N > 50? method5(S, N) : method3(S, N);

	cout << count << endl;
	
#ifdef TESTRUN // Compiling under Visual Studio
	diffInMilliSec = GetTickCount() - startTime_;
	printf("Time taken : %lu millisec\n", diffInMilliSec);

	startTime_ = GetTickCount();
	int count_ =  method3(S, N) ;
	// cout << method1(S, N) << endl;

	diffInMilliSec = GetTickCount() - startTime_;
	printf("Time taken : %lu millisec\n", diffInMilliSec);

	if(count_ != count)
	{
		cerr << "Problem. Expected Result: " << count_ << endl;
	}
	}
#endif
	// system("PAUSE");
	return 0;
}

inline long method1(const int* const S, const int N)
{
	long count = 0;
	int a, b, d, e, f, ab, sum_e_f, def;

	for(int d_ = 0; d_ < N; d_++)
	{
		d = S[d_];
		for(int e_ = 0; e_ < N; e_++)
		{
			e = S[e_];
			for(int f_ = 0; f_ < N; f_++)
			{
				f = S[f_]; sum_e_f = e + f; def = d * sum_e_f;
				for(int a_ = 0; a_ < N; a_++)
				{
					a = S[a_];
					for(int b_ = 0; b_ < N; b_++)
					{
						b = S[b_]; ab = a * b;
						if(binarySearch(S, N, def - ab)) count++;
					}
				}
			}
		}
	}
	return count;
}

inline long method2(const int* const S, const int N)
{
	int prod_ii[MAX_N], sum_ii[MAX_N];
	for(int i = 0; i < N; i++)
	{
		prod_ii[i] = S[i] * S[i];
		sum_ii[i]  = S[i] + S[i];
	}
	qsort(prod_ii, N, sizeof (int), cmp);
	qsort(sum_ii, N, sizeof (int), cmp);

	int prod_ij[(MAX_N * (MAX_N - 1)) / 2 + 1], p = 0;
	for(int i = 0; i < N; i++)
		for( int j = 0 ; j < i ; j++)
			prod_ij[p++] = S[i] * S[j];
	const int n_ij_terms = (N * (N - 1)) / 2;
	qsort(prod_ij, n_ij_terms, sizeof (int), cmp);

	long count = 0;
	int d, e, f, sum_e_f, def, lidx;

	for(int d_ = 0; d_ < N; d_++)
	{
		d = S[d_];
		for(int e_ = 0; e_ < N; e_++)
		{
			e = S[e_];
			for(int f_ = 0; f_ < N; f_++)
			{
				f = S[f_]; sum_e_f = e + f; def = d * sum_e_f;

				binarySearch(prod_ij, n_ij_terms, def + LL - 1, &lidx);
				if(lidx > 0) lidx--;

				assert (lidx >= 0);
				for(p = lidx; p < n_ij_terms; p++)
				{
					if(prod_ij[p] > def + UL) break;
					if(binarySearch(S, N, def - prod_ij[p])) count+=2;
				}

				for(p = 0; p < N; p++)
				{
					if(prod_ii[p] > def + UL) break;
					if(binarySearch(S, N, def - prod_ii[p])) count++;
				}
			}
		}
	}
	return count;
}

inline long method3(const int* const S, const int N)
{
	const int min_S = S[0], max_S = S[N - 1];

	int prod_ii[MAX_N], sum_ii[MAX_N];
	for(int i = 0; i < N; i++)
	{
		prod_ii[i] = S[i] * S[i];
		sum_ii[i]  = 2 * S[i];
	}
	qsort(prod_ii, N, sizeof (int), cmp);
	qsort(sum_ii, N, sizeof (int), cmp);

	int prod_ij[(MAX_N * (MAX_N - 1)) / 2 + 1], sum_ij[(MAX_N * (MAX_N - 1)) / 2 + 1], p = 0;
	for(int i = 0; i < N; i++)
		for( int j = 0 ; j < i ; j++)
		{
			prod_ij[p] = S[i] * S[j];
			sum_ij[p] = S[i] + S[j];
			p++;
		}
	const int n_ij_terms = (N * (N - 1)) / 2;
	qsort(prod_ij, n_ij_terms, sizeof (int), cmp);
	qsort(sum_ij, n_ij_terms, sizeof (int), cmp);

	int minp, maxp;
	findMinMaxProd(S, N, S, N, minp, maxp);

	long count = 0;
	int d, def, lidx;

	for(int d_ = 0; d_ < N; d_++)
	{
		d = S[d_];
		if(d == 0) continue;
		for(int sum_ef_idx = 0; sum_ef_idx < n_ij_terms; sum_ef_idx++)
		{
			def = d * sum_ij[sum_ef_idx];
			if( d > 0 && def > maxp + max_S) break;
			if( d < 0 && def < minp + min_S) break;


			binarySearch(prod_ij, n_ij_terms, def - max_S - 1, &lidx);
			if(lidx > 0) lidx--;

			assert (lidx >= 0);
			for(p = lidx; p < n_ij_terms; p++)
			{
				if(prod_ij[p] > def - min_S) break;
				if(binarySearch(S, N, def - prod_ij[p])) 
					count+=4;
			}

			for(p = 0; p < N; p++)
			{
				if(prod_ii[p] > def - min_S) break;
				if(binarySearch(S, N, def - prod_ii[p])) 
					count+=2;
			}
		}
		for(int sum_ef_idx = 0; sum_ef_idx < N; sum_ef_idx++)
		{
			def = d * sum_ii[sum_ef_idx];

			binarySearch(prod_ij, n_ij_terms, def - max_S - 1, &lidx);
			if(lidx > 0) lidx--;

			assert (lidx >= 0);
			for(p = lidx; p < n_ij_terms; p++)
			{
				if(prod_ij[p] > def - min_S) break;
				if(binarySearch(S, N, def - prod_ij[p])) 
					count+=2;
			}

			for(p = 0; p < N; p++)
			{
				if(prod_ii[p] > def - min_S) break;
				if(binarySearch(S, N, def - prod_ii[p])) 
					count++;
			}
		}
	}
	return count;
}

inline long method4(const int* const S, const int N)
{
	
	int prod_ij[MAX_N * MAX_N + 1], sum_ij[MAX_N * MAX_N + 1];
	for(int i = 0, p = 0; i < N; i++)
		for( int j = 0 ; j < N ; j++)
		{
			prod_ij[p] = S[i] * S[j];
			sum_ij[p] = S[i] + S[j];
			p++;
		}

	const int n_ij_terms = N * N;
	qsort(prod_ij, n_ij_terms, sizeof (int), cmp);
	// qsort(sum_ij, n_ij_terms, sizeof (int), cmp);
	
	int* const def_term = new int[n_ij_terms * N + 1];
	for(int i = 0, q = 0; i < N; i++)
		for( int j = 0 ; j < n_ij_terms ; j++)
			def_term[q++] = S[i] * sum_ij[j];
	qsort(def_term, N * n_ij_terms, sizeof (long), cmp);
	
	long count = 0;
	
	int c = 0;
	for(int i = 0, q = 0; i < N; i++)
		for( int j = 0 ; j < n_ij_terms ; j++)
			if(binarySearch(def_term, N * n_ij_terms, S[i] + prod_ij[j], 0, &c))
				count+= c;

	delete def_term;
	return count;
}

const int DEFAULT_COUNT = -1;
// const int DEFAULT_HASH_VALUE = 2147483647;
const int MAX_COLLISIONS = 20;
const int HT_SIZE = 1 << 18;

struct HashTable
{
	int value, count;
} ht[HT_SIZE][MAX_COLLISIONS];
int maxCollisions, totalCollisions;

unsigned int hashCode(unsigned int x) 
{
	x = ((x >> 16) ^ x) * 0x3335b369 ;
	x = ((x >> 16) ^ x) * 0x3335b369 ;
	x = ((x >> 16) ^ x);
	//return x * 2654435761;
	return x;
}

void init()
{
	for(int i = 0; i < HT_SIZE; i++)
		for( int j = 0 ; j < MAX_COLLISIONS ; j++)
			ht[i][j].count = DEFAULT_COUNT;

	maxCollisions = 0; totalCollisions = 0;
}

void insertElement(const int t)
{
	const int hidx = hashCode(t) % HT_SIZE;
	int pos = 0;
	while( pos < MAX_COLLISIONS && ht[hidx][pos].count != DEFAULT_COUNT) 
	{
		if(ht[hidx][pos].value == t)
		{
			ht[hidx][pos].count++;
			return;
		}
		pos++;
	}

	assert (ht[hidx][pos].count == DEFAULT_COUNT);

	ht[hidx][pos].value = t;
	ht[hidx][pos].count = 1;
#ifdef TESTRUN
	totalCollisions += pos;
	if(maxCollisions < pos) maxCollisions = pos;
#endif
}

int findElement(const int term)
{
	const int hidx = hashCode(term) % HT_SIZE;
		
	int pos = 0;
	while( ht[hidx][pos].count != DEFAULT_COUNT && pos < MAX_COLLISIONS)
	{
		if( ht[hidx][pos].value == term)
		{
			return ht[hidx][pos].count;
		}
		pos++;
	}

	return 0;
}

inline long method5(const int* const S, const int N)
{
	const int min_S = S[0], max_S = S[N - 1];
	init();

	int prod_ii[MAX_N], sum_ii[MAX_N];
	for(int i = 0; i < N; i++)
	{
		prod_ii[i] = S[i] * S[i];
		sum_ii[i]  = 2 * S[i];
	}

	int prod_ij[(MAX_N * (MAX_N - 1)) / 2 + 1], sum_ij[(MAX_N * (MAX_N - 1)) / 2 + 1], p = 0;
	for(int i = 0; i < N; i++)
	{
		for( int j = 0 ; j < i ; j++)
		{
			prod_ij[p] = S[i] * S[j];
			sum_ij[p] = S[i] + S[j];
			p++;
		}
	}
	const int n_ij_terms = p;
	int minp, maxp;
	findMinMaxProd(S, N, S, N, minp, maxp);

	const int min_abc = minp + min_S, max_abc = maxp + max_S;

	int def_ii[MAX_N * MAX_N], ii_idx = 0, ij_idx = 0;
	static int def_ij[MAX_N * (MAX_N * (MAX_N - 1)) / 2 + 1];
	
	for(int i = 0; i < N; i++)
	{
		if(S[i] != 0)
		{
			for( int j = 0 ; j < N ; j++)
			{
				def_ii[ii_idx] = S[i] * sum_ii[j];
				if(def_ii[ii_idx] >= min_abc && def_ii[ii_idx] <= max_abc)
					ii_idx++;
			}
		}
		for( int j = 0 ; j < n_ij_terms ; j++)
		{
			if(S[i] != 0)
			{
				def_ij[ij_idx] = S[i] * sum_ij[j];
				if(def_ij[ij_idx] >= min_abc && def_ij[ij_idx] <= max_abc)
					ij_idx++;
			}
			
			insertElement(S[i] + prod_ij[j]);
		}
	}
#ifdef TESTRUN
	cout<< "Total # of collisions: " << totalCollisions << endl;
	cout<< "Max # of collisions: " << maxCollisions << endl;
#endif

	const int def_ii_size = ii_idx;
	const int def_ij_size = ij_idx;
	int count = 0;
	for( int i = 0; i < def_ij_size; i++ )
	{
		count += 4 * findElement(def_ij[i]);
	}
	for( int i = 0; i < def_ii_size; i++ )
	{
		count += 2 * findElement(def_ii[i]);
	}
	
	/* clean the hash table */
	init();

	for(int i = 0; i < N; i++)
	{
		for( int j = 0 ; j < N ; j++)
		{
			insertElement(S[i] + prod_ii[j]);
		}
	}

	for( int i = 0; i < def_ij_size; i++ )
	{
		count += 2 * findElement(def_ij[i]);
	}
	for( int i = 0; i < def_ii_size; i++ )
	{
		count += findElement(def_ii[i]);
	}
	return count;
}
