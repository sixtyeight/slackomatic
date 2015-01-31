package at.metalab.slackomatic.devices.metacade;

import java.io.File;

import at.metalab.slackomatic.Util;
import at.metalab.slackomatic.api.IToggle;
import at.metalab.slackomatic.rest.RestBuilder;

public class MetacadeImpl implements IMetacade {

	private final String mac;

	private final String shutdownUrl;

	public MetacadeImpl(String wakeupMacAddr, String shutdownUrl) {
		this.mac = wakeupMacAddr;
		this.shutdownUrl = shutdownUrl;
	}

	public IToggle power() {
		return new IToggle() {

			public void on() {
				Util.executeCommand(new File("/"), "wakeonlan", mac);
			}

			public void off() {
				Util.executeCommand(new File("/"), "curl", "-m", "1", "--data",
						"foo=bar", shutdownUrl);
			}
		};
	}

	public void create(RestBuilder rest) {
		rest.add(power(), "power");
	}
}
