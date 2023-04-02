
public class Link extends File {

	private static final String EXT = ".axn";
	private FileNode link;

	public Link(String name, String i[]) {
		super(name);
		ext = EXT;
		setInfo(i);
	}

	public Link(String name, String i[], FileNode f) {
		super(name);
		ext = EXT;
		setInfo(i);
		setLink(f);
	}

	public void setLink(FileNode f) {
		link = f;
		hasAction = true;
	}

	public FileNode getLink() {
		return link;
	}

	public void action() {
		Structure.setCurrent(link);
	}

}
