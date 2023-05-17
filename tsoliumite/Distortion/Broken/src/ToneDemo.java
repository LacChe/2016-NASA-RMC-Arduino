import java.util.Scanner;

public class ToneDemo {

	public static int lengthN = 300;
	public static int lengthW = 290;

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Tone[] tones = new Tone[1];
		tones[0] = new Tone("name", "Sine", 200);// Crystal, Choir Aahs 2, Star
		// Theme, Halo Pad,
		tones[0].play();
		sc.close();
	}

}