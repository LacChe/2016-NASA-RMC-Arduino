import java.util.ArrayList;

public class Box {

	private int top = 0, size = 0;
	private static final int BOXSIZE = 30;
	private long time, timeC;

	private ArrayList<String> lines = new ArrayList<String>();

	public Box() {

	}

	public ArrayList<String> getLines() {
		return lines;
	}

	public String getLine(int i) {
		return lines.get(i);
	}

	public String setLine(int x, String i) {
		// lines.set(x, i);

		// String ei = i;
		// int prob = Distorter.getDysProb();
		// int ran = (int) (Math.random() * 100);
		// int charIndex = (int) (Math.random() * i.length());
		// char[] chars = new char[i.length()];

		// System.out.println(i.substring(i.length() - 1, i.length()));

		return lines.set(x, i);
	}

	public void addLine(String s, int speed) {
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

	public void addLines(String[] s, int speed, int charSpeed) {
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

	public int getTop() {
		return top;
	}

	public void setTop(int i) {

	}

	public int getSize() {
		return size;
	}

	public String[] getBox() {
		String[] box = new String[BOXSIZE];
		for (int i = top; i < size; i++) {
			box[i - top] = lines.get(i);
		}
		return box;
	}

}
