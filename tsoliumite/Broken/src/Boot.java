import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Boot extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	private static Image offScreenBuffer;
	private static Graphics offScreenGraphics;
	private static Boot b;
	private static final int H = 431, W = 350;
	private static String input;
	private static JFrame window;
	@SuppressWarnings("unused")
	private static boolean ready = false, processing = false, shift = false;
	private static long finProcessTime;

	public static void main(String[] args) {
		window = new JFrame("Host");
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
		Structure.setDefault();
		input = "";
		ready = true;
		repaint();
	}

	public void paint(Graphics g) {
		if (ready) {
			offScreenGraphics.clearRect(0, 0, W, H);
			draw((Graphics2D) offScreenGraphics);
			g.drawImage(offScreenBuffer, 0, 0, this);
			repaint();
		}
	}

	private void draw(Graphics2D g) {
		g.setFont(new Font("TimesRoman", Font.PLAIN, g.getFont().getSize()));
		g.clearRect(0, 0, W, H);
		g.setColor(Color.lightGray);
		String[] lines = Box.getBox();
		int y;
		for (y = 0; y < Box.getSize() - Box.getTop(); y++) {
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
						g.drawLine(x, 10 - Integer.parseInt(lines[y].substring(i, i + 1)) + y * 13, x + 5,
								10 - Integer.parseInt(lines[y].substring(i + 1, i + 2)) + y * 13);
						x += 5;
					}
				} else {
					g.drawString(space + lines[y], 0, 10 + y * 13);
				}
			}
		}
		if (!processing && System.currentTimeMillis() - finProcessTime > 10) {
			g.drawString(">" + Structure.getCurrent().getFullName() + "(" + input + ")<", 0, 10 + y * 13);
		}
	}

	public static Boot getBoot() {
		return b;
	}

	public static Graphics getOffScreenGraphics() {
		return offScreenGraphics;
	}

	private static void command(String s) {
		if (!processing) {
			processing = true;
			Thread command = new Thread(new Runnable() {
				@Override
				public void run() {
					Box.addLine(">" + Structure.getCurrent().getFullName() + "(" + s + ")<", 0);
					if (!s.equals("")) {
						Structure.getCurrent().getFullName();
						switch (Command.process(s)) {
						case 1:
							Box.addLine("     unknownaction", 3);
							break;
						case 2:
							Box.addLine("     unknowndata", 3);
							break;
						case 3:
							Box.addLine("     unknownlocation", 3);
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
		if (key == KeyEvent.VK_SHIFT) {
			shift = true;
		}
		if (key == KeyEvent.VK_ENTER) {
			command(input);
		} else if (key == KeyEvent.VK_BACK_SPACE) {
			if (input.length() > 0) {
				input = input.substring(0, input.length() - 1);
			}
		} else if (!(key == KeyEvent.VK_SHIFT || key == KeyEvent.VK_CONTROL || key == KeyEvent.VK_ALT
				|| key == KeyEvent.VK_DELETE || key == KeyEvent.VK_ESCAPE || key == KeyEvent.VK_CAPS_LOCK
				|| key == KeyEvent.VK_UP || key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT
				|| key == KeyEvent.VK_DOWN)) {
			input += e.getKeyChar();
		}
		repaint();
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_SHIFT) {
			shift = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	public static JFrame getFrame() {
		return window;
	}

}// end class Boot