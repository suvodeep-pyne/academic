#include <iostream>
#include <vector>
#include <algorithm>
#include <string>
#include <iomanip>
#include <sstream>

#define NO_TOSTRING
#define NDEBUG 
#include<assert.h>

using namespace std;

const int MINCOORD = 0, MAXCOORD = 1000 + 1;
bool L[MAXCOORD];

inline int max(int a, int b)
{
	return a > b? a : b;
}

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

class Point
{
public:
	int x, y, z;

	Point(int x, int y, int z) : x(x), y(y), z(z)
	{
		assert(x >= 0 && y >= 0 && z >= 0);
	}

#ifndef NO_TOSTRING
	string toString() const
	{
		ostringstream output;
		output<<"("<<x<<", "<<y<<", "<<z<<")";
		return output.str();
	}
#endif
};

class Line
{
public:
	int y, z;
	int x1, x2;

	Line(Point p1, Point p2)
	{
		assert(p1.y == p2.y && p1.z == p2.z);
		y = p1.y; z = p1.z;

		if(p1.x < p2.x) x1 = p1.x, x2 = p2.x;
		else x1 = p2.x, x2 = p1.x;
	}

	bool operator < (const Line& o) const
	{
		return y < o.y;
	}

#ifndef NO_TOSTRING
	string toString() const
	{
		ostringstream o;
		o << "([" << x1 << ", " << x2 << "], " << y << ", " << z << ")";
		return o.str();
	}
#endif
};

class Face
{
	int z;
	int xMin, xMax, yMin, yMax;
public:
	int getZ() const {return z;}
	
	int getxMin() const {return xMin;}
	int getxMax() const {return xMax;}
	int getyMin() const {return yMin;}
	int getyMax() const {return yMax;}

	vector<Point> points;

	Face() {}

	void add(Point p) { points.push_back(p);}

	void processIsZFace()
	{
		assert(points.size() > 2);
		bool zFace = true;
		yMin = points[0].y, yMax = points[0].y;
		xMin = points[0].x, xMax = points[0].x;

		for(unsigned int i = 0; i < points.size() - 1; i++)
		{
			if(xMin > points[i + 1].x) xMin = points[i + 1].x;
			if(xMax < points[i + 1].x) xMax = points[i + 1].x;			
			
			if(yMin > points[i + 1].y) yMin = points[i + 1].y;
			if(yMax < points[i + 1].y) yMax = points[i + 1].y;

			if(points[i].z - points[i + 1].z != 0) 
			{
				zFace = false; break;
			}
		}
		z = (zFace)? points[0].z : -1 ;
	}

	bool isZFace() const {return z >= 0;}

	bool operator < (const Face& o) const {return z < o.z;}

	void getYLines(vector<Line>& lines) const
	{
		/* For all the lines in order */
		for(int i = 0; i < points.size() - 1; i++)
			if(points[i].y == points[i + 1].y)
				lines.push_back(Line(points[i], points[i + 1]));

		/* For line from last point to the first point */
		if(points[0].y == points[points.size() - 1].y)
			lines.push_back(Line(points[0], points[points.size() - 1]));

		/* We need to traverse in sequence */
		sort(lines.begin(), lines.end());
	}

	
	void clearLineMask() const
	{
		for(int q = xMin; q <= xMax; q++) L[q] = false;
	}

#ifndef NO_TOSTRING
	string toString() const
	{
		ostringstream output;
		bool start = true;

		if(zFace) output<<"z = "<< zValue << " ";
		output<< "[ ";
		for( Point p : points)
		{
			output<< (start ? "" : ", ") << p.toString();
			start = false;
		}
		output<< " ]";
		return output.str();
	}
#endif
};

int minY = MAXCOORD + 1, maxY = MINCOORD - 1;
int minX = MAXCOORD + 1, maxX = MINCOORD - 1;

bool A[MAXCOORD] [MAXCOORD]; int sumA = 0;


inline void inputFaces(vector<Face>& zFaces)
{
	int nFaces = 0;
	read(nFaces);

	//assert (nFaces >= 6);
	for(int i = 0; i < nFaces; i++)
	{
		int nPoints = 0; read(nPoints);
		assert(nPoints >= 4);

		Face f;
		for(int p = 1, count = 0, x = -1, y = -1, z = -1; p <= nPoints;)
		{
			switch(count)
			{
			case 0: read(x); count++; if(minX > x) minX = x; if(maxX < x) maxX = x; break;
			case 1: read(y); count++; if(minY > y) minY = y; if(maxY < y) maxY = y; break;
			case 2: read(z); count++; break;
			case 3: f.add(Point(x,y,z)); count = 0; x = -1, y = -1, z = -1;  p++; break;
			}
		}
		f.processIsZFace();
		if(f.isZFace()) zFaces.push_back(f);
	}
}

inline void clearMask()
{
	for(int i = minX; i <= maxX; i++)
		for(int j = minY; j <= maxY; j++)
			A[i][j] = false;
}

inline void flipMask(int y_min, int y_max)
{
	for(int jj = y_min; jj < y_max; jj++)
		for(int ii = minX; ii <= maxX; ii++)
			if(L[ii])
			{
				A[jj][ii] ^= true;
				if(A[jj][ii]) sumA++; else sumA--;
			}
}


inline void flipLineMask(const Line& yLine)
{
	for(int i = yLine.x1; i < yLine.x2; i++) L[i] ^= true;
}

inline void updateMask(const Face& f)
{
	/* Clear the line mask for every face */
	f.clearLineMask();

	vector<Line> yLines;
	f.getYLines(yLines);

	assert(yLines.size() >= 2);
	int prevY = yLines[0].y;
	for(int j = 0; j < yLines.size(); j++)
	{
		Line& yLine = yLines[j];
		if( prevY == yLine.y)
		{
			/* Update the line mask */
			flipLineMask(yLine);
		}
		else
		{
			/* Sweep the line mask for covered area */
			flipMask(prevY, yLine.y);

			/* Update the line mask */
			flipLineMask(yLine);
			prevY = yLine.y;
		}
	}
}

int main() 
{
	int nTestCases = 0;
	read(nTestCases);
	while(nTestCases--)
	{
		sumA = 0;
		vector<Face> zFaces;
		inputFaces(zFaces);

		/* Algorithm */
		if(zFaces.size() < 2) {cout<<0<<endl; continue;}

		sort(zFaces.begin(), zFaces.end());
		clearMask();

		int prevZ = zFaces[0].getZ();

		int vol = 0;
		for(int k = 0; k < zFaces.size(); k++)
		{
			Face& f = zFaces[k];

			if(f.getZ() == prevZ)
			{
				updateMask(f);
			}
			else
			{
				//cout<<"sumA: "<<sumA<<" getMaskSum: " << getMaskSum()<<endl;
				vol += (f.getZ() - prevZ) * sumA;
				updateMask(f);
				prevZ = f.getZ();
			}
		}

		cout<<"The bulk is composed of "<<vol<<" units."<<endl;
		//cout<<"Printing zFaces"<<endl;
		//for(Face f : zFaces)
		//{
		//	cout<<f.toString()<<endl;
		//}
	}
	//system("PAUSE");
	return 0;
}
