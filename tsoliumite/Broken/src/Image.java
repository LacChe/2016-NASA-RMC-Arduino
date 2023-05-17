
public class Image extends File {

	private static final String EXT = ".ocpt";

	public Image(String name, String i[]) {
		super(name);
		ext = EXT;
		info = i;
	}

}
