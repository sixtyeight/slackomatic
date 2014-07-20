package at.metalab.slackomatic.devices.yamaha;

import java.io.File;

import at.metalab.slackomatic.Util;
import at.metalab.slackomatic.api.IInvoker;
import at.metalab.slackomatic.api.IToggle;
import at.metalab.slackomatic.rest.RestBuilder;

public class ShellYamahaImpl implements IYamaha {

	private final File inputsDir;

	private final File requestsDir;

	private final IInput input = new InputImpl();

	public ShellYamahaImpl(File baseDir) {
		this.inputsDir = new File(baseDir, "inputs");
		this.requestsDir = new File(baseDir, "requests");

		if (!this.inputsDir.canRead()) {
			throw new RuntimeException(String.format(
					"inputsDir directory %s does not exist or is not readable",
					baseDir));
		}

		if (!this.requestsDir.canRead()) {
			throw new RuntimeException(
					String.format(
							"requestsDir directory %s does not exist or is not readable",
							baseDir));
		}
	}

	private class InputImpl implements IYamaha.IInput {

		public IInvoker av1() {
			return new IInvoker() {

				public void invoke() {
					sendInput("AV1");
				}
			};
		}

		public IInvoker av2() {
			return new IInvoker() {

				public void invoke() {
					sendInput("AV2");
				}
			};
		}

		public IInvoker av3() {
			return new IInvoker() {

				public void invoke() {
					sendInput("AV3");
				}
			};
		}

		public IInvoker av4() {
			return new IInvoker() {

				public void invoke() {
					sendInput("AV4");
				}
			};
		}

		public IInvoker av5() {
			return new IInvoker() {

				public void invoke() {
					sendInput("AV5");
				}
			};
		}

		public IInvoker av6() {
			return new IInvoker() {

				public void invoke() {
					sendInput("AV6");
				}
			};
		}

		public IInvoker hdmi1() {
			return new IInvoker() {

				public void invoke() {
					sendInput("HDMI1");
				}
			};
		}

		public IInvoker hdmi2() {
			return new IInvoker() {

				public void invoke() {
					sendInput("HDMI2");
				}
			};
		}

		public IInvoker hdmi3() {
			return new IInvoker() {

				public void invoke() {
					sendInput("HDMI3");
				}
			};
		}

		public IInvoker hdmi4() {
			return new IInvoker() {

				public void invoke() {
					sendInput("HDMI4");
				}
			};
		}

		public IInvoker hdmi5() {
			return new IInvoker() {

				public void invoke() {
					sendInput("HDMI5");
				}
			};
		}

		public IInvoker audio1() {
			return new IInvoker() {

				public void invoke() {
					sendInput("AUDIO1");
				}
			};
		}

		public IInvoker audio2() {
			return new IInvoker() {

				public void invoke() {
					sendInput("AUDIO2");
				}
			};
		}

		public IInvoker audio3() {
			return new IInvoker() {

				public void invoke() {
					sendInput("AUDIO3");
				}
			};
		}

		public IInvoker audio4() {
			return new IInvoker() {

				public void invoke() {
					sendInput("AUDIO4");
				}
			};
		}

		public IInvoker dock() {
			return new IInvoker() {

				public void invoke() {
					sendInput("DOCK");
				}
			};
		}
	}

	public IInput getInput() {
		return input;
	}

	public IToggle power() {
		return new IToggle() {

			public void on() {
				sendRequest("on");
			}

			public void off() {
				sendRequest("standby");
			}
		};
	}

	public IToggle mute() {
		return new IToggle() {

			public void on() {
				sendRequest("muteon");
			}

			public void off() {
				sendRequest("muteoff");
			}
		};
	}

	/* (non-Javadoc)
	 * @see at.metalab.slackomatic.rest.IRestable#create(at.metalab.slackomatic.rest.RestBuilder)
	 */
	public void create(RestBuilder rest) {
		rest.add(mute(), "mute");
		rest.add(power(), "power");
		rest.add(getInput().audio1(), "input/audio1");
		rest.add(getInput().audio2(), "input/audio2");
		rest.add(getInput().audio3(), "input/audio3");
		rest.add(getInput().audio4(), "input/audio4");
		rest.add(getInput().av1(), "input/av1");
		rest.add(getInput().av2(), "input/av2");
		rest.add(getInput().av3(), "input/av3");
		rest.add(getInput().av4(), "input/av4");
		rest.add(getInput().av5(), "input/av5");
		rest.add(getInput().av6(), "input/av6");
		rest.add(getInput().hdmi1(), "input/hdmi1");
		rest.add(getInput().hdmi2(), "input/hdmi2");
		rest.add(getInput().hdmi3(), "input/hdmi3");
		rest.add(getInput().hdmi4(), "input/hdmi4");
		rest.add(getInput().hdmi5(), "input/hdmi5");
		rest.add(getInput().dock(), "input/dock");
	}

	private synchronized String executeCommand(File workingDirectory,
			String template) {
		return Util.executeCommand(workingDirectory, new String[] {
				"./send.sh", template });
	}

	private String sendInput(String template) {
		return executeCommand(inputsDir, template);
	}

	private String sendRequest(String template) {
		return executeCommand(requestsDir, template);
	}

}
