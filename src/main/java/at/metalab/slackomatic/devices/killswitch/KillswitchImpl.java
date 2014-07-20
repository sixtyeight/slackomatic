package at.metalab.slackomatic.devices.killswitch;

import java.io.File;

import at.metalab.slackomatic.Util;
import at.metalab.slackomatic.api.IInvoker;

public class KillswitchImpl implements IKillswitch {

	private final File workingDirectory;

	public KillswitchImpl(File workingDirectory) {
		this.workingDirectory = workingDirectory;
	}

	public IInvoker reset() {
		return new IInvoker() {

			public void invoke() {
				Util.executeCommand(workingDirectory, "./reset.sh");
			}
		};
	}

}
