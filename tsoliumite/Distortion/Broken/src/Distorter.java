import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Distorter {

	private static final int H = 431, W = 350;
	private static int degreeNoiseLeft = 0, degreeNoiseRight = 0;
	private static int degreeLines = 0, collisionChance = 0;
	private static int tin = 0, ptin = 0;
	private static int dysProb = 0;
	private static boolean right = false, left = false;
	private static boolean horizontalLines = false;
	private static ArrayList<Line2D> lines = new ArrayList<Line2D>();
	private static int shakeSeverity = 0, pShakeSeverity = 0;

	public static void noise(Graphics2D g) {
		int pDegreeNoiseLeft = degreeNoiseLeft;
		int pDegreeNoiseRight = degreeNoiseRight;
		degreeNoiseLeft = (100 - Structure.getOLobe().getFile("LftVs.ctx").getHealth()) * 1000;
		degreeNoiseRight = (100 - Structure.getOLobe().getFile("RhtVs.ctx").getHealth()) * 1000;
		left = (degreeNoiseLeft != 0);
		right = (degreeNoiseRight != 0);
		if (pDegreeNoiseLeft != degreeNoiseLeft) {
			Boot.getBox().addLine("               host_WARNING:_LFTVS_DECAY", 0);
		}
		if (pDegreeNoiseRight != degreeNoiseRight) {
			Boot.getBox().addLine("               host_WARNING:_RHTVS_DECAY", 0);
		}
		if (true) {
			g.setColor(Color.WHITE);
			if (left) {
				for (int i = 0; i < degreeNoiseLeft; i++) {
					int x = (int) (Math.random() * (W / 2) + (W / 2));
					int y = (int) (Math.random() * H);
					// System.out.println("right");
					g.drawLine(x, y, x, y);
				}
			}
			if (right) {
				for (int i = 0; i < degreeNoiseRight; i++) {
					int x = (int) (Math.random() * (W / 2));
					int y = (int) (Math.random() * H);
					// System.out.println("Left");
					g.drawLine(x, y, x, y);
				}
			}
		}
		// System.out.println("\n LR");
		// System.out.println(Structure.getOLobe().getFile("LftVs.ctx").getHealth()
		// + " " + degreeNoiseLeft);
		// System.out.println(Structure.getOLobe().getFile("RhtVs.ctx").getHealth()
		// + " " + degreeNoiseRight);
	}

	// shifts screen depending on severity of motor deterioration
	public static void shake() {
		int x = Boot.getOffX();
		int y = Boot.getOffY();
		shakeSeverity = (100 - Structure.getFLobe().getFile("Mtr.ctx").getHealth()) / 20;
		if (x == 0 && y == 0) {
			// System.out.println(((int) (Math.random() * 3) - 1));
			x = shakeSeverity * ((int) (Math.random() * 3) - 1);
			y = shakeSeverity * ((int) (Math.random() * 3) - 1);
		} else {
			x = 0;
			y = 0;
		}
		Boot.setOffX(x);
		Boot.setOffY(y);
		if (pShakeSeverity != shakeSeverity) {
			Boot.getBox().addLine("               host_WARNING:_COORDINATION_ERROR", 0);
		}
		pShakeSeverity = shakeSeverity;
	}

	public static void horizontalLines(Graphics2D g) {
		collisionChance = 100 - Structure.getCurrent().getHealth();
		if ((int) (Math.random() * 50000) + 1 <= collisionChance && !horizontalLines) {
			// System.out.println(Structure.getFLobe().getFile("Mtr.ctx").getHealth());
			// if (Structure.getPLobe().getFile("Smt.ctx").getHealth() > 50) {
			if (Structure.getFLobe().getFile("Mtr.ctx").getHealth() > 50) {
				Boot.getBox().addLine("               host_WARNING:_COLLISION", 0);
			}
			degreeLines = 3000;
		}
		if (collisionChance >= 80 && horizontalLines != true) {
			Boot.getBox().addLine("               host_WARNING:_UNSTABLE", 0);
			degreeLines = 300000;
			horizontalLines = true;
		}
		if (collisionChance < 80) {
			horizontalLines = false;
		}
		g.setColor(Color.WHITE);
		if (true) {
			int prob;
			if (lines.size() < degreeLines) {
				int x = (int) (Math.random() * 225) - 50;
				int y = (int) (Math.random() * 531) - 50;
				prob = (int) (Math.random() * 2);
				if (prob == 0) {
					if (true) {
						lines.add(new Line2D.Double(0, y, x, y));
					}
				} else {
					if (true) {
						lines.add(new Line2D.Double(x + 225, y, 350, y));
					}
				}
			} else if (lines.size() > degreeLines) {
				lines.remove(0);
			}
			int offX = 5 * ((int) (Math.random() * 3) - 1);
			int offY = 5 * ((int) (Math.random() * 3) - 1);
			for (Line2D l : lines) {
				l.setLine(l.getX1() + offX, l.getY1() + offY, l.getX2() + offX, l.getY2() + offY);
				g.draw(l);
			}
			prob = (int) (Math.random() * 10);
			if (prob == 0) {
				if (lines.size() != 0) {
					lines.remove(0);
				}
			}
			for (int i = 0; i < 5; i++) {
				if (lines.size() != 0 && degreeLines > 3000 && !horizontalLines) {
					lines.remove(0);
				}
			}
			if (degreeLines > 2 && !horizontalLines) {
				degreeLines -= 3;
				if (degreeLines > 3000) {
					degreeLines -= 300;
				}
				// System.out.println(degreeLines);
			}
		}
	}

	public static void Tinnitus() {
		tin = 100 - Structure.getTLobe().getFile("Aud.ctx").getHealth();
		if (ptin != tin) {
			((Tone) Structure.getTLobe().getFile("Aud.ctx")).setVol(tin);
		}
		ptin = tin;
	}

	public static void Dyslexia() {
		int severity = Structure.getTLobe().getFile("Hpcps.ctx").getHealth();
		int chance = (int) (Math.random() * severity * 10);
		// System.out.println(severity + " " + chance);
		if (chance == 0 && severity != 100) {
			// for (int i = 0; i < severity / 10; i++) {
			Box t = Boot.getBox();
			if (t.getLines().size() != 0) {
				int row1, row2, index1, index2;
				String line1, line2;

				if (t.getLines().size() < 31) {
					row1 = ((int) (Math.random() * (t.getLines().size())));
				} else {
					row1 = t.getLines().size() - 1 - (int) (Math.random() * 31);
				}
				if (t.getLines().size() < 31) {
					row2 = ((int) (Math.random() * (t.getLines().size())));
				} else {
					row2 = t.getLines().size() - 1 - (int) (Math.random() * 31);
				}

				line1 = Boot.getBox().getLines().get(row1);
				line2 = Boot.getBox().getLines().get(row2);

				index1 = (int) (Math.random() * line1.length());
				index2 = (int) (Math.random() * line2.length());

				if (line1.length() > 0 && line2.length() > 0) {
					char c1 = line1.charAt(index1);
					char c2 = line2.charAt(index2);

					line1 = line1.substring(0, index1) + c2 + line1.substring(index1 + 1);
					line2 = line2.substring(0, index2) + c1 + line2.substring(index2 + 1);

					Boot.getBox().setLine(row1, line1);
					Boot.getBox().setLine(row2, line2);
					// }
				}
			}
		}
	}

	public static int getDegreeNoiseLeft() {
		return degreeNoiseLeft;
	}

	public static void setDegreeNoiseLeft(int degreeNoiseLeft) {
		Distorter.degreeNoiseLeft = degreeNoiseLeft;
	}

	public static int getDegreeNoiseRight() {
		return degreeNoiseRight;
	}

	public static void setDegreeNoiseRight(int degreeNoiseRight) {
		Distorter.degreeNoiseRight = degreeNoiseRight;
	}

	public static int getDegreeLines() {
		return degreeLines;
	}

	public static void setDegreeLines(int d) {
		if (d >= 0) {
			degreeLines = d;
		}
	}

	public static boolean isRight() {
		return right;
	}

	public static void setRight(boolean right) {
		Distorter.right = right;
	}

	public static boolean isLeft() {
		return left;
	}

	public static void setLeft(boolean left) {
		Distorter.left = left;
	}

	public static boolean isHorizontalLines() {
		return horizontalLines;
	}

	public static void setHorizontalLines(boolean horizontalLines) {
		Distorter.horizontalLines = horizontalLines;
	}

	public static ArrayList<Line2D> getLines() {
		return lines;
	}

	public static void setLines(ArrayList<Line2D> lines) {
		Distorter.lines = lines;
	}

	public static int getDysProb() {
		return dysProb;
	}

}