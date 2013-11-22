
import java.util.*;

public class BasketballGame {
	static Scanner s = new Scanner( System.in);
	public static void main(String[] args) {
		String line = s.nextLine(); 
		int T = Integer.parseInt(line);
		
		int t = 1;
		while ( t <= T ) {
			BasketballGame instance = new BasketballGame();
			System.out.println("Case #" + t + ": " + instance.run());
			++t;
		}
	}

	class Player implements Comparable<Player> {
		final String name;
		final int r, h;

		int d = 0, t = 0;

		Player(String name, int r, int h) {
			this.name = name;
			this.r = r;
			this.h = h;
		}

		public int compareTo(Player p) {
			int result = p.r - this.r;
			if (result != 0) return result;
			else return p.h - this.h;
		}

		public String toString() {
			return name + " " + r + " " + h + "(" + d + "," + t + ")";
		}
	}

	class PComparator implements Comparator<Player> {
		public int compare(Player p1, Player p2) {
			int tdiff = p2.t - p1.t;
			if (tdiff != 0) return tdiff;
			else return p2.d - p1.d;
		}
	}

	class BComparator implements Comparator<Player> {
		public int compare(Player p1, Player p2) {
			int tdiff = p1.t - p2.t;
			if (tdiff != 0) return tdiff;
			else return p1.d - p2.d;
		}
	}

	class Team {
		List<Player> all;

		List<Player> playing = new ArrayList<Player>();
		List<Player> bench = new ArrayList<Player>();

		public Team(List<Player> all) {
			this.all = all;
		}

		void init() {
			for (int i = 0; i < all.size(); i++) {
				if (i < P) {
					playing.add(all.get(i));
				}
				else {
					bench.add(all.get(i));
				}
			}
		}

		void play() {
			for ( Player p : playing) {
				++p.t;
			}
		}

		void rotate() {
			if (bench.size() == 0) return;

			PriorityQueue<Player> pHeap = new PriorityQueue<Player>(P, new PComparator());
			PriorityQueue<Player> bHeap = new PriorityQueue<Player>(P, new BComparator());

			for ( Player p : playing )
				pHeap.offer(p);
			
			for ( Player p : bench )
				bHeap.offer(p);
			
			Player leaving = pHeap.poll();
			Player joining = bHeap.poll();

			pHeap.offer(joining);
			bHeap.offer(leaving);

			playing.clear(); playing.addAll(pHeap);
			bench.clear(); bench.addAll(bHeap);
		}

		void print() {
			System.out.println("All     : " + all);
			System.out.println("Playing : " + playing);
			System.out.println("Bench   : " + bench);
		}
	}

	int N, M, P;
	Map<Integer, Player> pMap = new HashMap<Integer, Player>();
	
	public String run() {
		String line = s.nextLine(); 
		String[] words = line.split("\\s+");
		N = Integer.parseInt(words[0]);
		M = Integer.parseInt(words[1]);
		P = Integer.parseInt(words[2]);

		List<Player> players = new ArrayList<Player>();
		for ( int i = 1; i <= N; i++) {
			words = s.nextLine().split("\\s+");

			String name = words[0];
			int r = Integer.parseInt(words[1]);
			int h = Integer.parseInt(words[2]);

			players.add(new Player(name, r, h));
		}
		Collections.sort(players);
		for (int i = 0; i < players.size(); i++) {
			players.get(i).d = i + 1;
		}
		// System.out.println(players);
		Team team1 = new Team(new ArrayList<Player>());
		Team team2 = new Team(new ArrayList<Player>());

		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).d % 2 != 0) team1.all.add(players.get(i));
			else team2.all.add(players.get(i));
		}

		team1.init();
		team2.init();

		for (int rot = 1; rot <= M; rot++) {
			team1.play();
			team2.play();

			team1.rotate();
			team2.rotate();

/*
			System.out.println("After round " + rot);
			team1.print();
			team2.print();
			*/
		}

		List<String> currPlaying = new ArrayList<String>();
		for (Player p : team1.playing) {
			currPlaying.add(p.name);
		}
		for (Player p : team2.playing) {
			currPlaying.add(p.name);
		}
		Collections.sort(currPlaying);

		StringBuilder o = new StringBuilder();
		for (int i = 0; i < currPlaying.size(); i++) {
			o.append(currPlaying.get(i));
			if (i != currPlaying.size() - 1)
				o.append(" ");
		}
		return o.toString();
	}
}
