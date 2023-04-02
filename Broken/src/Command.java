
public class Command {

	public static int process(String command) {
		int report = 1;
		int numCommands = 1;
		int[] posCommands = new int[10];
		posCommands[0] = 0;
		for (int i = 0; i < command.length(); i++) {
			if (command.charAt(i) == ' ') {
				posCommands[numCommands] = (i + 1);
				numCommands++;
			}
		}
		String[] commands = new String[numCommands];
		for (int i = 0; i < numCommands; i++) {
			if (i == numCommands - 1) {
				commands[i] = command.substring(posCommands[i]);
			} else {
				commands[i] = command.substring(posCommands[i], posCommands[i + 1] - 1);
			}
		}

		switch (commands[0]) {
		case "die":
			Boot.getFrame().dispose();
			report = 0;
			break;
		case "look":
		case "l":
			report = 2;
			if (numCommands == 1) {
				String[] s = Structure.getCurrentString();
				Box.addLines(s, 10, 3);
				report = 0;
			} else if (numCommands == 2) {
				String ext;
				if (Structure.getCurrent().contains(commands[1])) {
					File f = Structure.getCurrent().getFile(commands[1]);
					if (commands[1].indexOf('.') != -1) {
						ext = commands[1].substring(commands[1].indexOf('.'));
					} else {
						ext = f.getExt();
					}
					String[] s = f.getInfo(ext);
					Box.addLines(s, 10, 3);
					report = 0;
				}
			}
			break;
		case "traverse":
			report = 3;
			if (numCommands == 2) {
				if (Structure.getCurrent().contains(commands[1])) {
					Structure.setCurrent((FileNode) Structure.getCurrent().getFile(commands[1]));
					report = 0;
				}
			}
			break;
		}
		return report;
	}

}
