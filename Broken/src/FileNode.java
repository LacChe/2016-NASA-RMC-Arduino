import java.util.ArrayList;

public class FileNode extends File {

	private ArrayList<File> files;
	private static final String EXT = ".axn";

	public FileNode(String n) {
		super(n);
		setExt(EXT);
		files = new ArrayList<File>();
		setInfo();
	}

	public FileNode(String n, ArrayList<File> fs) {
		super(n);
		setExt(EXT);
		files = fs;
		setInfo();
	}

	public void setInfo() {
		String[] temp = new String[files.size()];
		for (int i = 0; i < files.size(); i++) {
			temp[i] = "" + files.get(i).getFullName();
		}
		info = temp;
	}

	public ArrayList<File> getFiles() {
		return files;
	}

	public void addFile(File f) {
		files.add(f);
		setInfo();
	}

	public File getFile(String n) {
		if (n.indexOf('.') != -1) {
			n = n.substring(0, n.indexOf('.'));
		}
		for (File f : files) {
			if (f.getName().equals(n)) {
				return f;
			}
		}
		return null;
	}

	public void remFile(File f) {
		files.remove(f);
		setInfo();
	}

	public boolean contains(String n) {
		if (getFile(n) != null) {
			return true;
		}
		return false;
	}

}
