package at.metalab.slackomatic;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

public class Util {
	private final static Logger LOG = Logger.getLogger(Util.class.getName());

	public static String executeCommand(File workingDirectory, String... args) {
		try {
			LOG.info(String.format("executing command: %s (dir: %s)",
					Arrays.deepToString(args), workingDirectory.getAbsolutePath()));

			Process process = Runtime.getRuntime().exec(args, null,
					workingDirectory);

			try {
				process.waitFor();
				LOG.info(String.format("command exit value: %d",
						process.exitValue()));
			} catch (InterruptedException interruptedException) {
				LOG.severe(String.format("waiting for command was interrupted",
						Arrays.deepToString(args)));

				throw new RuntimeException(interruptedException);
			}
		} catch (IOException ioException) {
			LOG.severe(String.format("command could not be executed: %s",
					ioException.getMessage()));
			// ioException.printStackTrace();
		}

		return ""; // TODO
	}
}
