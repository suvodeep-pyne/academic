/**
 * Problems in chapter 1: Arrays and Strings
 */

import java.util.*;

public class ArraysAndStrings {
	public static void main(String[] args) {
		ArraysAndStrings instance = new ArraysAndStrings();

		instance.run();
	}
	
	void run() {
		problem7();
	}

	void problem7() {
		int A[][] = 
		{
			{11, 12, 13, 14},
			{21,  0,  0, 24},
			{31, 32, 33, 34},
			{41, 42, 43, 44},
		};

		System.out.println("Before");
		printMat(A);

		setZero(A);

		System.out.println("After");
		printMat(A);
	}

	void setZero(int A[][]) {
		Set<Integer> rows = new HashSet<Integer>();
		Set<Integer> cols = new HashSet<Integer>();

		for (int i = 0; i < A.length; i++)
			for (int j = 0; j < A[i].length; j++)
				if (A[i][j] == 0) {
					rows.add(i); cols.add(j);
				}

		for (int i = 0; i < A.length; i++)
			for (int j = 0; j < A[i].length; j++)
				if (rows.contains(i) || cols.contains(j))
					A[i][j] = 0;
	}

	void problem6() {
		int A[][] = 
		{
			{11, 12, 13, 14},
			{21, 22, 23, 24},
			{31, 32, 33, 34},
			{41, 42, 43, 44},
		};

		int B[][] = 
		{
			{1}
		};

		int C[][] = 
		{
			{1, 2},
			{3, 4}
		};

		int D[][] = 
		{
			{1, 2, 3},
			{4, 5, 6},
			{7, 8, 9}
		};

		System.out.println("Before Rotation");
		printMat(A);

		rightRotate(A);

		System.out.println("After Rotation");
		printMat(A);
	}

	void rightRotate(int A[][]) {
		int N = A.length;
		// System.out.println("Matrix size: " + N);

		for (int k = 0; k < N/2 ; k++) {
			for (int j = k; j < N - 1 - k; j++) {
				// System.out.println("j: " + j + " k: " + k);
				int t = A[k][j];
				int row = k, col = j;
				for (int i = 1; i <= 4; i++) {
					// System.out.println("row: " + row + " col: " + col);
					int newrow = N - 1 - col;
					int newcol = row;
					A[row][col] = i != 4? A[N - 1 - col][row]: t;
					row = newrow; col = newcol;
				}

			}
		}
	}

	void printMat(int A[][]) {
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < A[i].length; j++) {
				System.out.print(A[i][j] + " ");
			}
			System.out.println();
		}
	}

	void problem5() {
		String str = "aabcccccaaa";

		System.out.println(compress(str));
	}

	String compress(String str) {
		StringBuilder sb = new StringBuilder();
		int n = 0;
		char prev = 0;
		for( char c : str.toCharArray()) {
			if (prev == c) {
				++n;
			}
			else {
				if (n != 0) sb.append(n);
				sb.append(c); prev = c;
				n = 1;
			}
		}
		if (n != 0) sb.append(n);

		String compressed = sb.toString();
		return compressed;
		// return compressed.length() < str.length()? compressed : str;
	}
	
	void problem4() {
		char[] arr = "Mr John Smith     ".toCharArray();
		int length = 13;

		System.out.println(arr);
		encode(arr, length);
		System.out.println(arr);
	}

	void encode(char[] arr, int length) {
		int count = 0;
		for ( int i = 0; i < length; i++)
			if ( arr[i] == ' ') ++count;

		int offset = count * 2;
		for ( int i = length - 1; i >= 0; --i) {
			if (arr[i] == ' ') {
				offset -= 2;

				arr[i + offset    ] = '%';
				arr[i + offset + 1] = '2';
				arr[i + offset + 2] = '0';
			}
			else
				arr[i + offset] = arr[i];
		}

	}

	void problem3() {
		String s1 = "abcdefgh";
		String s2 = "atchgdef";

		System.out.println("checkPermute: " + checkPermute(s1, s2));
	}

	boolean checkPermute(String s1, String s2) {
		Map<Character, Integer> count = new HashMap<Character, Integer>();

		for (char c : s1.toCharArray()) {
			if (count.get(c) == null) count.put(c, 1);
			else count.put(c, 1 + count.get(c));
		}

		for (char c : s2.toCharArray()) {
			Integer n  = count.get(c);
			if(n == null) return false;

			if (n > 1) count.put(c, n - 1);
			else count.remove(c);
		}
		return count.size() == 0;
	}
}
