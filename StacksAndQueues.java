import java.util.*;

public class StacksAndQueues {
	public static void main(String[] args) {
		StacksAndQueues instance = new StacksAndQueues();
		instance.run();
	}

	void run() {
		problem4();
	}

	void problem4() {
		TowersOfHanoi instance = new TowersOfHanoi();

		for (int n = 5; n > 0; n--) {
			instance.s1.push(n);
		}

		print(instance.s1);
		print(instance.s2);
		print(instance.s3);
		instance.move(instance.s1, instance.s3);

		System.out.println("After move");
		print(instance.s1);
		print(instance.s2);
		print(instance.s3);
	}

	class TowersOfHanoi {
		Stack<Integer> s1 = new Stack<Integer>();
		Stack<Integer> s2 = new Stack<Integer>();
		Stack<Integer> s3 = new Stack<Integer>();

		void move(Stack<Integer> s1,
				  Stack<Integer> s2) {
			move(s1.size(), s1, s2);
		}

		void move(int n, Stack<Integer> s1, Stack<Integer> s2) {
			System.out.println("n: " + n + " s1: " + this.s1 + " s2 " + this.s2 + "s3: " + this.s3);
			// System.out.println("s1: " + s1 + " s2 " + s2);

			if (n == 1) {
				s2.push(s1.pop());
				return;
			}

			Stack<Integer> s3 = getOther(s1, s2);
			assert s3 != s1 && s3 != s2 && s1 != s2;
			move(n - 1, s1, s3);
			move(1, s1, s2);
			move(n - 1, s3, s2);
		}

		Stack<Integer> getOther(Stack<Integer> s1, Stack<Integer> s2) {
			boolean containsS1 = this.s1 == s1 || this.s1 == s2;
			boolean containsS2 = this.s2 == s1 || this.s2 == s2;
			boolean containsS3 = this.s3 == s1 || this.s3 == s2;

			if(containsS1 && containsS2) return this.s3;
			else if (containsS1) return this.s2;
			else return this.s1;
		}

	}

	void print(List<Integer> list) {
		System.out.print("List: ");
		for (Integer n : list) {
			System.out.print(n + " ");
		}
		System.out.println();
	}
}
