import java.util.*;

public class JavaTest {
	public static void main(String[] args) {
		Set<Integer> s = new LinkedHashSet<Integer>();
		s.add(1);
		s.add(2);
		s.add(3);
		s.add(4);

		s.remove(3);
		System.out.println(s);
		System.out.println(s.iterator().next());
		System.out.println(s.iterator().next());
	}
}
