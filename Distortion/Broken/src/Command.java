
public class Command {

	public static int process(String command) {
		
		// 0=success
		// 1=action error
		// 2=data error
		// 3=location error

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

		if (Structure.getCurrent().contains(command)) {
			File f = Structure.getCurrent().getFile(command);
			if (f.hasAction()) {
				switch (f.getExt()) {
				case ".axn":
					((Link) f).action();
					report = 0;
					break;
				case ".ctx":
					String[] s = ((Memory) f).action();
					Boot.getBox().addLines(s, 3, 1);
					report = 0;
					break;
				}
			}
		}

		switch (commands[0]) {

		// quit
		case "leave":
			Boot.getFrame().dispose();
			report = 0;
			break;

		// look in/at element
		case "look":
		case "l":
			report = 2;
			if (numCommands == 1) {
				String[] s = Structure.getCurrentString();
				Boot.getBox().addLines(s, 3, 1);
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
					Boot.getBox().addLines(s, 3, 1);
					report = 0;
				}
			}
			break;

		// interact with element
		case "probe":
		case "p":
			report = 2;
			if (commands.length == 2) {
				if (Structure.getCurrent().contains(commands[1])) {
					File x = Structure.getCurrent().getFile(commands[1]);
					if (!x.getExt().equalsIgnoreCase(".axn")) {
						x.setHealth(Structure.getCurrent().getFile(commands[1]).getHealth() - 10);
						report = 0;
					} else {
						for (File xMatch : Structure.getDir()) {
							if (x.getName().equalsIgnoreCase(xMatch.getName())) {
								xMatch.setHealth(xMatch.getHealth() - 10);
								report = 0;
							}
						}
					}
				}
				break;
			}
		}
		return report;
	}

}
