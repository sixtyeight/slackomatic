package at.metalab.slackomatic.devices.hdmiwhisperer;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;

public class Sindax {

	private final static Logger LOG = Logger.getLogger(Sindax.class.getName());

	private final String pcmDeviceName;

	private final Channel channel;

	private final File wavDir;

	public enum Channel {
		BOTH, LEFT, RIGHT
	}

	public enum Input {

		INPUT1(1), INPUT2(2), INPUT3(3), INPUT4(4), INPUT5(5);

		private Input(int num) {
			this.num = num;
		}

		private final int num;
	}

	public Sindax(File wavDir, String pcmDeviceName, Channel channel) {
		this.wavDir = wavDir;
		this.pcmDeviceName = pcmDeviceName;
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

	public String getPcmDeviceName() {
		return pcmDeviceName;
	}

	private String[] generateCmd(Input input) {
		System.out.println(String.format("sending %s on %s channel to '%s'",
				input.num, channel, pcmDeviceName));

		// input #, audio channel
		String filename = String.format("input_hdmi%d_%s.wav", input.num,
				channel.name().toLowerCase());

		return new String[] { "aplay",
				String.format("--device=%s", pcmDeviceName),
				new File(wavDir, filename).getAbsolutePath() };
	}

	public void selectInput(Input input) {
		synchronized (Sindax.class) {
			String[] cmd = generateCmd(input);

			try {
				LOG.info("generated command: " + Arrays.toString(cmd));
				Runtime.getRuntime().exec(cmd);
				Thread.sleep(500);
			} catch (Exception exception) {
				LOG.severe("could not execute aplay: "
						+ exception.getMessage());
				exception.printStackTrace(System.err);
			}
		}
	}
}
