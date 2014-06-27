package at.metalab.slackomatic.devices.benq;

import java.io.File;

import at.metalab.slackomatic.Util;
import at.metalab.slackomatic.api.IInvoker;
import at.metalab.slackomatic.api.IToggle;
import at.metalab.slackomatic.rest.RestBuilder;

public class ShellBenqImpl implements IBenq {
	private final File baseDir;

	public ShellBenqImpl(String baseDir) {
		this.baseDir = new File(baseDir);

		if (!this.baseDir.canRead()) {
			throw new RuntimeException(String.format(
					"directory %s does not exist or is not readable", baseDir));
		}
	}

	public IToggle power() {
		return new IToggle() {

			public void on() {
				executeCommand("power_on");
			}

			public void off() {
				executeCommand("power_off");
			}
		};
	}

	public IDisplay display() {
		return new IDisplay() {

			public IToggle brilliant() {
				return new IToggle() {

					public void on() {
						executeCommand("brilliant_on");
					}

					public void off() {
						executeCommand("brilliant_off");
					}
				};
			}

			public IInput input() {
				return new IInput() {

					public IInvoker vga1() {
						return new IInvoker() {

							public void invoke() {
								executeCommand("input_vga1");
							}
						};
					}

					public IInvoker vga2() {
						return new IInvoker() {

							public void invoke() {
								executeCommand("input_vga2");
							}
						};
					}

					public IInvoker video() {
						return new IInvoker() {

							public void invoke() {
								executeCommand("input_video");
							}
						};
					}

					public IInvoker hdmi() {
						return new IInvoker() {

							public void invoke() {
								executeCommand("input_hdmi");
							}
						};
					}
				};
			}

			public IToggle blank() {
				return new IToggle() {

					public void on() {
						executeCommand("blank_on");
					}

					public void off() {
						executeCommand("blank_off");
					}
				};
			}
		};
	}

	public void create(RestBuilder rest) {
		rest.add(power(), "power");
		rest.add(display().brilliant(), "display/brilliant");
		rest.add(display().blank(), "display/blank");
		rest.add(display().input().hdmi(), "display/input/hdmi");
		rest.add(display().input().vga1(), "display/input/vga1");
		rest.add(display().input().vga2(), "display/input/vga2");
		rest.add(display().input().video(), "display/input/video");
	}

	private synchronized void executeCommand(String operation) {
		Util.executeCommand(baseDir, new String[] { "./" + operation });
	}
}
