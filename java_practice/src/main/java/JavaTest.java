import java.util.*;
import java.io.*;
import java.net.*;

public class JavaTest {
	public static void main(String[] args) {
		JavaTest instance = new JavaTest();
		instance.socketTest();
	}

	public void lhsTest() {
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

	void socketTest() {
		String serverHostname = new String ("127.0.0.1");

		Socket socket = null;
		PrintWriter out = null;
		BufferedReader in = null;

		try {
			socket = new Socket(serverHostname, 10007);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + serverHostname);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for "
					+ "the connection to: " + serverHostname);
			System.exit(1);
		}
	}
}
