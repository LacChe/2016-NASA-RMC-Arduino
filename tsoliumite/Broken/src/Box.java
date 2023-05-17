import java.util.ArrayList;

public class Box {

	private static int top = 0, size = 0;
	private static final int BOXSIZE = 30;
	private static long time, timeC;

	private static ArrayList<String> lines = new ArrayList<String>();

	public static ArrayList<String> getLines() {
		return lines;
	}

	public static String getLine(int i) {
		return lines.get(i);
	}

	public static String setLine(int x, String i) {
		return lines.set(x, i);
	}

	public static void addLine(String s, int speed) {
		timeC = System.currentTimeMillis();
		int i = 0;
		while (i <= s.length()) {
			if (System.currentTimeMillis() - timeC > speed) {
				timeC = System.currentTimeMillis();
				if (i == 0) {
					lines.add("");
					size++;
					if (size > 30) {
						top++;
					}
				}
				setLine(size - 1, s.substring(0, i));
				i++;
			}
		}
	}

	public static void addLines(String[] s, int speed, int charSpeed) {
		time = System.currentTimeMillis();
		int i = 0;
		while (i < s.length) {
			if (System.currentTimeMillis() - time > speed) {
				time = System.currentTimeMillis();
				addLine(s[i], charSpeed);
				i++;
			}
		}
	}

	public static int getTop() {
		return top;
	}

	public static void setTop(int i) {
		
	}

	public static int getSize() {
		return size;
	}

	public static String[] getBox() {
		String[] box = new String[BOXSIZE];
		for (int i = top; i < size; i++) {
			box[i - top] = lines.get(i);
		}
		return box;
	}

}
