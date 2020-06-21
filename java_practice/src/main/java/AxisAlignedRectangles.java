import java.util.*;

public class AxisAlignedRectangles {
	public static void main(String[] args) {
		AxisAlignedRectangles instance = new AxisAlignedRectangles();
		instance.run();
	}

	class Point {
		final int x, y;
		Point(int x, int y) {
			this.x = x; this.y = y;
		}

		@Override
		public String toString() {
			return "(" + x + "," + y + ")";
		}
	}

	class Rectangle {
		final Point leftBottom, rightTop;
		Rectangle(Point leftBottom, Point rightTop) {
			this.leftBottom = leftBottom;
			this.rightTop = rightTop;
		}

		@Override
		public String toString() {
			return leftBottom + "-" + rightTop;
		}
	}

	final int MAX = 100;
	final int SEED = 1234;
	final Random random = new Random(SEED);
	final Point origin = new Point(0,0);

	Point randomPoint(Point p) {
		return new Point(p.x + 1 + random.nextInt(MAX), p.y + 1 + random.nextInt(MAX));
	}

	Rectangle randomRectangle() {
		Point leftBottom = randomPoint(origin);
		return new Rectangle(leftBottom, randomPoint(leftBottom));
	}

	enum Dim { X, Y };
	enum Type { START, END }
	private Dim dim;
	
	class ScanlineObject {
		final Type type;
		final Rectangle r;
		final Point p;

		ScanlineObject(Type type, Rectangle r) {
			this.type = type; this.r = r;

			if (type == Type.START) p = r.leftBottom;
			else p = r.rightTop;
		}

		int getCoord() {
			if (dim == Dim.X) return p.x;
			else return p.y;
		}

		@Override
		public String toString() {
			return (type == Type.START? "S" : "E") + r;
		}
	}

	Comparator<ScanlineObject> comparator = new Comparator<ScanlineObject>() {
		public int compare(ScanlineObject o1, ScanlineObject o2) {
			return o1.getCoord() - o2.getCoord();
		}
	};

	public void run() {
		final int N = 10;
		List<Rectangle> rectangles = new ArrayList<Rectangle>(N);
		for (int i = 1; i <= N; ++i) {
			rectangles.add(randomRectangle());
		}

		System.out.print("Rectangles: ");
		System.out.println(rectangles);

		// Scanline Algorithm

		// First sort in X
		dim = Dim.X;
		List<ScanlineObject> objects = new ArrayList<ScanlineObject>();
		for (Rectangle r : rectangles) {
			objects.add(new ScanlineObject(Type.START, r)); 
			objects.add(new ScanlineObject(Type.END, r)); 
		}
		Collections.sort(objects, comparator);

		// Active rectangles
		System.out.println("Intersects:");
		dim = Dim.Y;
		Set<Rectangle> active = new HashSet<Rectangle>();
		for (ScanlineObject slo : objects) {
			if (slo.type == Type.START) {
				active.add(slo.r);
			}
			else {
				// Check for overlap
				if (active.size() > 1) {
					List<ScanlineObject> yObjects = new ArrayList<ScanlineObject>();
					for (Rectangle r : active) {
						yObjects.add(new ScanlineObject(Type.START, r)); 
						yObjects.add(new ScanlineObject(Type.END, r)); 
					}
					Collections.sort(yObjects, comparator);

					Set<Rectangle> intersect = new HashSet<Rectangle>();
					for (ScanlineObject yslo : yObjects) {
						if (yslo.type == Type.START) {
							intersect.add(yslo.r);
						}
						else {
							if (intersect.size() > 1) {
								System.out.println(intersect);
							}

							intersect.remove(yslo.r);
						}
					}
				}
				// Remove Current Rectangle
				boolean result = active.remove(slo.r);
				assert result;
			}
		}
	}
}
