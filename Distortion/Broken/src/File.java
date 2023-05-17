
public class File {

	protected String name, ext;
	protected String[] info;
	protected String[] infoE;
	protected int acc;// 2=Dura Mater, 1=Arachnoid, 0=Pia Mater
	protected boolean hasAction = false;
	protected int health;

	public File(String n) {
		if (n.indexOf(".") != -1) {
			int index = n.indexOf(".");
			ext = n.substring(index);
			name = n.substring(0, index);
		} else {
			name = n;
		}
		acc = 0;
		health = 100;
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
			info[0] = "Health: " + health;
			return info;
		}
		if (infoE == null) {
			info[0] = "Health: " + health;
			System.out.println(info[0]);
			infoE = encrypt(info);
		}
		return infoE;
	}

	public void setInfo(String[] in) {
		if (info == null) {
			info = new String[in.length + 1];
		}
		info[0] = "Health: " + health;
		for (int i = 0; i < in.length; i++) {
			info[i + 1] = in[i];
		}
	}

	private static String[] encrypt(String[] s) {
		int length = s.length;
		String[] fin = new String[length];
		for (int i = 0; i < length; i++) {
			String line = "\u00B6";
			// System.out.println(i + "<" + s.length + " " + j + "<" +
			// s[i].length());
			System.out.println(i + "<" + s.length + " i:" + s[0]);
			for (int j = 0; j < s[i].length(); j++) {
				line += Math.abs((Character.getNumericValue(s[i].charAt(j)) % 10));
			}
			fin[i] = line;
		}
		return fin;
	}

	public boolean hasAction() {
		return hasAction;
	}

	public int getHealth() {
		if (name.equalsIgnoreCase("FrtlLobe") && ext.equalsIgnoreCase(".fold")) {
			// System.out.println(name + " ### " + health);
		}
		return health;
	}

	public void setHealth(int h) {
		if (h < 0) {
			h = 0;
		} else if (h > 100) {
			health = 100;
		} else {
			health = h;
		}
		//System.out.println(name + " " + health);
	}

}
// \u00AF 0// \u02CD 5