import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Boot extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	private static Image offScreenBuffer;
	private static Graphics offScreenGraphics;
	private static Boot b;
	private static final int H = 431, W = 350;
	private static String input;
	private static ArrayList<String> commands;
	private static int commandIndex, inputIndex;
	private static JFrame window;
	private static boolean ready = false, processing = false, shift = false, control = false;
	private static long finProcessTime;
	private static Box box;
	private static int frameName;

	// shake distortion
	private static int offX, offY;

	public static void main(String[] args) {
		initFrame();
	}

	public static void initFrame() {
		ready = false;
		frameName = (int) (Math.random() * 7100000000L);
		window = new JFrame("T.Soliumite_" + frameName);
		window.setBounds(100, 100, W, H);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);

		b = new Boot();
		window.getContentPane().add(b);
		window.setBackground(Color.BLACK);
		window.setVisible(true);
		window.addKeyListener(b);
		b.init();
	}

	public void init() {
		offScreenBuffer = createImage(getWidth(), getHeight());
		offScreenGraphics = offScreenBuffer.getGraphics();
		box = new Box();
		Structure.setDefault();
		input = "";
		ready = true;
		repaint();
		commands = new ArrayList<String>();
		commandIndex = 0;
		inputIndex = 0;
	}

	public void paint(Graphics g) {
		if (ready) {
			offScreenGraphics.clearRect(0, 0, W, H);

			Distorter.horizontalLines((Graphics2D) offScreenGraphics);
			Distorter.noise((Graphics2D) offScreenGraphics);
			Distorter.Tinnitus();
			Distorter.Dyslexia();
			Distorter.shake();
			g.drawImage(offScreenBuffer, 0, 0, this);
			draw((Graphics2D) g, 0 + offX, 0 + offY);

			// Graphics graph1 = g.create();
			// Graphics graph2 = g.create();
			// draw((Graphics2D) graph1, 0, -(H - 30) + 25);
			// draw((Graphics2D) graph2, 0, 30);

			repaint();
		}
	}

	private void draw(Graphics2D g, int xOff, int yOff) {
		// g.clearRect(0, 0, W, H);
		g.setColor(Color.WHITE);
		String[] lines = box.getBox();
		int y;
		for (y = 0; y < box.getSize() - box.getTop(); y++) {
			String space;
			if (lines[y] != null) {
				if (lines[y].equals("")) {
					break;
				} else if (lines[y].charAt(0) == '>') {
					space = "";
				} else {
					space = "     ";
				}
				if (lines[y].charAt(0) == '\u00B6') {
					int x = 15;
					for (int i = 1; i < lines[y].length() - 1; i++) {
						g.drawLine(x + xOff, 10 - Integer.parseInt(lines[y].substring(i, i + 1)) + y * 13 + yOff,
								x + 5 + xOff, 10 - Integer.parseInt(lines[y].substring(i + 1, i + 2)) + y * 13 + yOff);
						x += 5;
					}
				} else {
					g.drawString(space + lines[y], 0 + xOff, 10 + y * 13 + yOff);
				}
			}
		}
		if (!processing && System.currentTimeMillis() - finProcessTime > 10) {
			g.drawString(">" + Structure.getCurrent().getFullName() + "(" + input.substring(0, inputIndex) + "|"
					+ input.substring(inputIndex) + ")<", 0 + xOff, 10 + y * 13 + yOff);
		}
	}

	public static Boot getBoot() {
		return b;
	}

	public static Graphics getOffScreenGraphics() {
		return offScreenGraphics;
	}

	public static Image getOffScreenBuffer() {
		return offScreenBuffer;
	}

	private static void command(String s) {
		inputIndex = 0;
		if (!processing) {
			commands.add(s);
			commandIndex = commands.size();
			processing = true;
			Thread command = new Thread(new Runnable() {
				@Override
				public void run() {
					box.addLine(">" + Structure.getCurrent().getFullName() + "(" + s + ")<", 0);
					if (!s.equals("")) {
						// Structure.getCurrent().getFullName();
						switch (Command.process(s)) {
						case 1:
							box.addLine("               unknownaction", 3);
							break;
						case 2:
							box.addLine("               unknowndata", 3);
							break;
						case 3:
							box.addLine("               unknownlocation", 3);
							break;
						}
					}
					input = "";
					processing = false;
					finProcessTime = System.currentTimeMillis();
				}
			});
			command.start();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (e != null) {
			if (key == KeyEvent.VK_SHIFT) {
				shift = true;
			}
			if (key == KeyEvent.VK_CONTROL) {
				control = true;
			}
			if (key == KeyEvent.VK_ENTER) {
				command(input);
			} else if (key == KeyEvent.VK_BACK_SPACE) {
				if (inputIndex > 0) {
					input = input.substring(0, inputIndex - 1) + input.substring(inputIndex);
					inputIndex--;
				}
			} else if (!(key == KeyEvent.VK_SHIFT || control || key == KeyEvent.VK_ALT || key == KeyEvent.VK_DELETE
					|| key == KeyEvent.VK_ESCAPE || key == KeyEvent.VK_CAPS_LOCK || key == KeyEvent.VK_UP
					|| key == KeyEvent.VK_DOWN || key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT)) {
				input = input.substring(0, inputIndex) + e.getKeyChar() + input.substring(inputIndex);
				inputIndex++;
			} else if (!(shift || control || key == KeyEvent.VK_ALT || key == KeyEvent.VK_DELETE
					|| key == KeyEvent.VK_ESCAPE || key == KeyEvent.VK_CAPS_LOCK)) {
				if (key == KeyEvent.VK_UP) {
					if (commandIndex > 0) {
						commandIndex--;
						input = commands.get(commandIndex);
						inputIndex = input.length();
					}
				} else if (key == KeyEvent.VK_DOWN) {
					if (commandIndex < commands.size()) {
						commandIndex++;
					}
					if (commandIndex == commands.size()) {
						input = "";
						inputIndex = input.length();
					} else {
						input = commands.get(commandIndex);
						inputIndex = input.length();
					}
				} else if (key == KeyEvent.VK_LEFT) {
					if (inputIndex > 0) {
						inputIndex--;
					}
				} else if (key == KeyEvent.VK_RIGHT) {
					if (inputIndex < input.length()) {
						inputIndex++;
						// input = commands.get(commandIndex);
					}
				}
			}
		}
		repaint();
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_SHIFT) {
			shift = false;
		}
		if (key == KeyEvent.VK_CONTROL) {
			Distorter.setDegreeLines(3000);
			control = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	public static JFrame getFrame() {
		return window;
	}

	public static Box getBox() {
		return box;
	}

	public static int getOffX() {
		return offX;
	}

	public static int getOffY() {
		return offY;
	}

	public static void setOffX(int x) {
		offX = x;
	}

	public static void setOffY(int y) {
		offY = y;
	}

}// end class Boot