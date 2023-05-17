
public class Memory extends File {

	private static final String EXT = ".ctx";

	public Memory(String name) {
		super(name);
		ext = EXT;
		hasAction = true;
	}

	public Memory(String name, String i[]) {
		super(name);
		ext = EXT;
		info = i;
		hasAction = true;
	}

	public String[] getInfo() {
		info[0] = "Health: " + health;
		return info;
	}

	public String[] action() {
		info[0] = "Health: " + health;
		return info;
	}

}
