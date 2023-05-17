import java.awt.Graphics2D;
import java.util.ArrayList;

public class Image extends File {

	private static final String EXT = ".ocpt";
	private static final int LENGTH = 200;

	private ArrayList<Graphics2D> imgs;

	public Image(String name, String i[]) {
		super(name);
		ext = EXT;
		info = i;
		imgs = new ArrayList<Graphics2D>();
	}

	public ArrayList<Graphics2D> getImgs() {
		return imgs;
	}

	public Graphics2D getImg(int i) {
		return imgs.get(i);
	}

	public void addImg(Graphics2D g) {
		imgs.add(g);
	}

	public static int length() {
		return LENGTH;
	}

}
