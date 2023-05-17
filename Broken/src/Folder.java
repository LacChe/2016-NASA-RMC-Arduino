
public class Folder extends File {

	private static final String EXT = ".grs";

	public Folder(String name, String i[]) {
		super(name);
		ext = EXT;
		info = i;
	}

}
