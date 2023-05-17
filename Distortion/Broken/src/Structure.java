import java.util.ArrayList;

public class Structure {

	private static ArrayList<FileNode> dir;
	private static FileNode current;
	private static FileNode FLobe;
	private static FileNode PLobe;
	private static FileNode OLobe;
	private static FileNode TLobe;

	public static void setDefault() {
		// set current to gyursH
		FLobe = new FileNode("FrtlLobe");
		PLobe = new FileNode("PrtlLobe");
		OLobe = new FileNode("OcptlLobe");
		TLobe = new FileNode("TmplLobe");

		// set Frontal Lobe
		FLobe.addFile(new Link(PLobe.getFullName(), new String[] { PLobe.getFullName() }, PLobe));
		FLobe.addFile(new Link(OLobe.getFullName(), new String[] { OLobe.getFullName() }, OLobe));
		FLobe.addFile(new Link(TLobe.getFullName(), new String[] { TLobe.getFullName() }, TLobe));
		FLobe.addFile(new Memory("Mtr", new String[] { "Health: ", "Mtr" }));

		// set Parietal Lobe
		PLobe.addFile(new Link(FLobe.getFullName(), new String[] { FLobe.getFullName() }, FLobe));
		PLobe.addFile(new Link(OLobe.getFullName(), new String[] { OLobe.getFullName() }, OLobe));
		PLobe.addFile(new Link(TLobe.getFullName(), new String[] { TLobe.getFullName() }, TLobe));
		// FileNode l = new FileNode("LLobe");
		// l.addFile(new Memory("Aglr", new String[] { "Health: ", "Aglr", }));
		// FileNode r = new FileNode("RLobe");
		// r.addFile(new Memory("Smt", new String[] { "Smt", "Health: " }));
		// superior parietal lobule
		// PLobe.addFile(l);
		// PLobe.addFile(r);

		// set Occipital Lobe
		OLobe.addFile(new Link(PLobe.getFullName(), new String[] { PLobe.getFullName() }, PLobe));
		OLobe.addFile(new Link(FLobe.getFullName(), new String[] { FLobe.getFullName() }, FLobe));
		OLobe.addFile(new Link(TLobe.getFullName(), new String[] { TLobe.getFullName() }, TLobe));
		OLobe.addFile(new Memory("RhtVs", new String[] { "Health: ", "RhtVs" }));
		OLobe.addFile(new Memory("LftVs", new String[] { "Health: ", "LftVs" }));

		// set Temporal Lobe
		TLobe.addFile(new Link(PLobe.getFullName(), new String[] { PLobe.getFullName() }, PLobe));
		TLobe.addFile(new Link(OLobe.getFullName(), new String[] { OLobe.getFullName() }, OLobe));
		TLobe.addFile(new Link(FLobe.getFullName(), new String[] { FLobe.getFullName() }, FLobe));
		TLobe.addFile(new Tone("Aud", new String[] { "Health: ", "Aud" }, "Sine", 200));
		TLobe.addFile(new Memory("Hpcps", new String[] { "Health: ", "Hpcps" }));

		switch ((int) (Math.random() * 4)) {
		case 0:
			current = FLobe;
			break;
		case 1:
			current = PLobe;
			break;
		case 2:
			current = OLobe;
			break;
		case 3:
			current = TLobe;
			break;
		}

		dir = new ArrayList<FileNode>();
		dir.add(FLobe);
		dir.add(PLobe);
		dir.add(TLobe);
		dir.add(OLobe);
	}

	public static ArrayList<FileNode> getDir() {
		return dir;
	}

	public static void setNode(String n) {
		current = new FileNode(n);
	}

	public static FileNode getCurrent() {
		return current;
	}

	public static String[] getCurrentString() {
		ArrayList<File> files = current.getFiles();
		String[] c = new String[files.size()];
		for (int i = 0; i < files.size(); i++) {
			c[i] = "" + files.get(i).getFullName();
		}
		return c;
	}

	public static void setCurrent(FileNode n) {
		current = n;
	}

	public static FileNode getFLobe() {
		return FLobe;
	}

	public static void setFLobe(FileNode fLobe) {
		FLobe = fLobe;
	}

	public static FileNode getPLobe() {
		return PLobe;
	}

	public static void setPLobe(FileNode pLobe) {
		PLobe = pLobe;
	}

	public static FileNode getOLobe() {
		return OLobe;
	}

	public static void setOLobe(FileNode oLobe) {
		OLobe = oLobe;
	}

	public static FileNode getTLobe() {
		return TLobe;
	}

	public static void setTLobe(FileNode tLobe) {
		TLobe = tLobe;
	}

}
