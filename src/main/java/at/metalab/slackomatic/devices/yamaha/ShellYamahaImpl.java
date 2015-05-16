package at.metalab.slackomatic.devices.yamaha;

import java.io.File;
import java.util.logging.Logger;

import at.metalab.slackomatic.Util;
import at.metalab.slackomatic.api.IInvoker;
import at.metalab.slackomatic.api.IToggle;
import at.metalab.slackomatic.rest.RestBuilder;

public class ShellYamahaImpl implements IYamaha {

	private final static Logger LOG = Logger.getLogger(ShellYamahaImpl.class
			.getCanonicalName());

	private final File inputsDir;

	private final File requestsDir;

	private final IInput input = new InputImpl();

	private final IVolume volume = new VolumeImpl();

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

	private class VolumeImpl implements IYamaha.IVolume {
		public IInvoker high() {
			return new IInvoker() {

				public void invoke() {
					ShellYamahaImpl.this.mute().off();
					sendRequest("volume_laut");
				}
			};
		}

		public IInvoker low() {
			return new IInvoker() {

				public void invoke() {
					ShellYamahaImpl.this.mute().off();
					sendRequest("volume_leise");
				}
			};
		}

		public IInvoker medium() {
			return new IInvoker() {

				public void invoke() {
					ShellYamahaImpl.this.mute().off();
					sendRequest("volume_medium");
				}
			};
		}

		// volume adjust throttling
		private final Object VOL_MONITOR = new Object();

		private volatile long tsLastVolumeAdjust = System.currentTimeMillis();

		private void updateTsLastVolumeAdjust() {
			tsLastVolumeAdjust = System.currentTimeMillis();
		}

		private boolean canAdjustVolume() {
			return System.currentTimeMillis() - tsLastVolumeAdjust >= 500;
		}

		public IInvoker increase() {
			return new IInvoker() {

				public void invoke() {
					synchronized (VOL_MONITOR) {
						if (canAdjustVolume()) {
							updateTsLastVolumeAdjust();
							Util.executeCommand(requestsDir,
									"./increase_volume.sh");
						} else {
							LOG.info("skipping volume increase due to rate limiting");
						}
					}
				}
			};
		}

		public IInvoker decrease() {
			return new IInvoker() {

				public void invoke() {
					synchronized (VOL_MONITOR) {
						if (canAdjustVolume()) {
							updateTsLastVolumeAdjust();
							Util.executeCommand(requestsDir,
									"./decrease_volume.sh");
						} else {
							LOG.info("skipping volume decrease due to rate limiting");
						}
					}
				}
			};
		}
	}

	private class InputImpl implements IYamaha.IInput {

		public IInvoker vAux() {
			return new IInvoker() {

				public void invoke() {
					sendInput("V-AUX");
				}
			};
		}
		
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

	public IVolume volume() {
		return volume;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.metalab.slackomatic.rest.IRestable#create(at.metalab.slackomatic.rest
	 * .RestBuilder)
	 */
	public void create(RestBuilder rest) {
		rest.add(mute(), "mute");
		rest.add(power(), "power");
		rest.add(getInput().audio1(), "input/audio1");
		rest.add(getInput().audio2(), "input/audio2");
		rest.add(getInput().audio3(), "input/audio3");
		rest.add(getInput().audio4(), "input/audio4");
		rest.add(getInput().vAux(), "input/v_aux");
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
		rest.add(volume().low(), "volume/low");
		rest.add(volume().medium(), "volume/medium");
		rest.add(volume().high(), "volume/high");
		rest.add(volume().increase(), "volume/adjust/increase");
		rest.add(volume().decrease(), "volume/adjust/decrease");
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
