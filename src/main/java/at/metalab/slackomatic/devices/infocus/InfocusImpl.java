package at.metalab.slackomatic.devices.infocus;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import at.metalab.slackomatic.api.IInvoker;
import at.metalab.slackomatic.api.IToggle;
import at.metalab.slackomatic.rest.RestBuilder;

public class InfocusImpl implements IInfocus {

	private final String ip;

	private IDisplay display = new IDisplay() {

		private IInput input = new IInput() {

			public IInvoker vga() {
				return new IInvoker() {

					public void invoke() {
						sendCommand("t:26,c:5,p:196610,v:0");
					}
				};
			}

			public IInvoker hdmi() {
				return new IInvoker() {

					public void invoke() {
						sendCommand("t:26,c:5,p:196610,v:2");
					}
				};
			}
		};

		public IInput input() {
			return input;
		}
	};

	private IToggle powerToggle = new IToggle() {

		public void on() {
			sendCommand("t:26,c:8,p:196609");
		}

		public void off() {
			sendCommand("t:26,c:7,p:196609");
		}
	};

	public InfocusImpl(String ip) {
		this.ip = ip;
	}

	public void create(RestBuilder restBuilder) {
		restBuilder.add(power(), "power");
		restBuilder.add(display().input().hdmi(), "display/input/hdmi");
		restBuilder.add(display().input().vga(), "display/input/vga");	
	}

	public IToggle power() {
		return powerToggle;
	}

	public IDisplay display() {
		return display;
	}

	private synchronized void sendCommand(String command) {
		try {
			URLConnection connection = new URL(buildUrl(command))
					.openConnection();
			connection.setDoInput(false);
			connection.setDoOutput(false);
			connection.setConnectTimeout(1000);
			connection.connect();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	private String buildUrl(String command) {
		return String.format("http://%s/cgi-bin/webctrl.cgi.elf?&%s", ip,
				command);
	}
}
