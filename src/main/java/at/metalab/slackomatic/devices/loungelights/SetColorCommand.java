package at.metalab.slackomatic.devices.loungelights;

import java.util.logging.Logger;

public class SetColorCommand {

	private final static Logger LOG = Logger.getLogger(SetColorCommand.class
			.getCanonicalName());

	private String white = null;

	private String amber = null;

	private String r = null;

	private String g = null;

	private String b = null;

	public SetColorCommand setWhite(String white) {
		this.white = white;

		return this;
	}

	public SetColorCommand setAmber(String amber) {
		this.amber = amber;

		return this;
	}

	public SetColorCommand setRGB(String r, String g, String b) {
		this.r = r;
		this.g = g;
		this.b = b;

		return this;
	}

	public String buildMessage() {
		return String.format("%s;%s;%s;%s;%s", sanitize(white),
				sanitize(amber), sanitize(r), sanitize(g), sanitize(b));
	}

	private String sanitize(String value) {
		if (value == null) {
			return "";
		}

		try {
			value = value.trim();

			int intValue = Integer.valueOf(value);

			if (intValue < 0) {
				return "0";
			} else if (intValue > 255) {
				return "255";
			}

			return value;
		} catch (Exception exception) {
			LOG.severe("parsing value failed: " + value);
			exception.printStackTrace();
			return ""; // best effort ...
		}
	}
}
