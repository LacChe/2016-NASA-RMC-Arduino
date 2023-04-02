
public class Memory extends File {

	private static final String EXT = ".ctx";

	public Memory(String name) {
		super(name);
		ext = EXT;
	}

	public Memory(String name, String i[]) {
		super(name);
		ext = EXT;
		info = i;
	}

	public String[] getInfo() {
		return info;
	}

}
