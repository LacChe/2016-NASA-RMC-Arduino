import javax.sound.midi.*;

public class Tone extends File {

	private static final String EXT = ".snd";

	private Synthesizer synth;
	private Instrument[] instrs;
	private Instrument instr;
	private MidiChannel[] channels;
	private boolean initted = false;
	private volatile boolean play = false;
	private long time, start;
	private volatile int note, vol;
	private Thread thread;

	Tone(String sname, String name, int n) {
		super(sname);
		if (!initted) {
			init();
		}
		setInstr(name);
		note = n;
		ext = EXT;
		hasAction = false;
	}

	Tone(String sname, String i[], String name, int n) {
		super(sname);
		if (!initted) {
			init();
		}
		setInstr(name);
		note = n;
		ext = EXT;
		info = i;
		hasAction = false;
	}

	private void init() {
		try {
			synth = MidiSystem.getSynthesizer();
			synth.open();
			channels = synth.getChannels();
			Soundbank bank = synth.getDefaultSoundbank();
			synth.loadAllInstruments(bank);
			instrs = synth.getLoadedInstruments();
			instr = null;
			initted = true;
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		vol = 127;
	}

	public void play() {
		play = true;
		start = System.currentTimeMillis();
		time = System.currentTimeMillis();
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				channels[0].noteOn(note, vol);
				while (play) {
					time = System.currentTimeMillis();
				}
			}
		});
		thread.start();
	}

	public void play(long t) {
		play = true;
		start = System.currentTimeMillis();
		time = System.currentTimeMillis();
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				channels[0].noteOn(note, vol);
				while (time - start < t && play) {
					time = System.currentTimeMillis();
				}
				channels[0].noteOff(note);
				play = false;
			}
		});
		thread.start();
	}

	public void stop() {
		play = false;
		channels[0].noteOff(note);
		thread.interrupt();
	}

	public void setVol(int v) {
		channels[0].noteOff(note, vol);
		vol = v;
		channels[0].noteOn(note, vol);
	}

	public int getVol() {
		return vol;
	}

	public void setNote(int n) {
		note = n;
	}

	public int getNote() {
		return note;
	}

	public void listInstrs() {
		for (int n = 0; n < instrs.length; n++) {
			System.out.println("#" + instrs[n].getName() + "#");
		}
	}

	public void setInstr(String i) {
		for (int n = 0; n < instrs.length; n++) {
			if (instrs[n].getName().indexOf(i) == 0) {
				instr = instrs[n];
				break;
			}
		}
		if (instr != null) {
			Patch note = instr.getPatch();
			channels[0].programChange(note.getBank(), note.getProgram());
		}
	}

	public void setBend(int i) {
		channels[0].setPitchBend(i);
	}

	public int getBend() {
		return channels[0].getPitchBend();
	}

}