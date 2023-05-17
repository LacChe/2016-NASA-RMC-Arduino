
public class File {

	protected String name, ext;
	protected String[] info;
	protected String[] infoE;
	protected int acc;// 2=Dura Mater, 1=Arachnoid, 0=Pia Mater

	public File(String n) {
		if (n.indexOf(".") != -1) {
			int index = n.indexOf(".");
			ext = n.substring(index);
			name = n.substring(0, index);
		} else {
			name = n;
		}
		acc = 0;
	}

	public String getFullName() {
		return name + ext;
	}

	public String getName() {
		return name;
	}

	public void setName(String n) {
		name = n;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String e) {
		ext = e;
	}

	public String[] getInfo(String ext) {
		if (ext.equals(".ctx")) {
			return info;
		}
		if (infoE == null) {
			infoE = encrypt(info);
		}
		return infoE;
	}

	public void setInfo(String[] i) {
		info = i;
	}

	private static String[] encrypt(String[] s) {
		int length = s.length;
		String[] fin = new String[length];
		for (int i = 0; i < length; i++) {
			String line = "\u00B6";
			for (int j = 0; j < s[i].length(); j++) {
				line += Math.abs((Character.getNumericValue(s[i].charAt(j)) % 10));
			}
			fin[i] = line;
		}
		return fin;
	}

}
// \u00AF 0// \u02CD 5