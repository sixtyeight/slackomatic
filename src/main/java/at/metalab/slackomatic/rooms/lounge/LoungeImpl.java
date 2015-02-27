package at.metalab.slackomatic.rooms.lounge;

import at.metalab.slackomatic.api.IInvoker;
import at.metalab.slackomatic.api.IToggle;
import at.metalab.slackomatic.devices.benq.IBenq;
import at.metalab.slackomatic.devices.killswitch.IKillswitch;
import at.metalab.slackomatic.devices.metacade.IMetacade;
import at.metalab.slackomatic.devices.nec.INec;
import at.metalab.slackomatic.devices.yamaha.IYamaha;
import at.metalab.slackomatic.rest.RestBuilder;

public class LoungeImpl implements ILounge {

	private final IBenq benq;

	private final IYamaha yamaha;

	private final INec nec;

	private final IMetacade metacade;

	private final IKillswitch killswitch;

	private final IToggle lamp1;

	public LoungeImpl(IBenq benq, IYamaha yamaha, INec nec, IMetacade metacade,
			IKillswitch killswitch, IToggle lamp1) {
		this.benq = benq;
		this.yamaha = yamaha;
		this.nec = nec;
		this.metacade = metacade;
		this.killswitch = killswitch;
		this.lamp1 = lamp1;
	}

	private final IDevices devices = new IDevices() {

		public IInvoker screeninvader() {
			return new IInvoker() {

				public void invoke() {
					wakeUp();
					showHDMImirrored();
					getYamaha().getInput().hdmi2().invoke();
				}
			};
		};

		public IInvoker screeninvaderStable() {
			return new IInvoker() {

				public void invoke() {
					wakeUp();
					showHDMImirrored();
					getYamaha().getInput().hdmi3().invoke();
				}
			};

		};

		public IInvoker ps2() {
			return new IInvoker() {

				public void invoke() {
					wakeUp();
					showHDMImirrored();
					getYamaha().getInput().av5().invoke();
				}
			};
		}

		public IInvoker chromecast() {
			return new IInvoker() {

				public void invoke() {
					wakeUp();
					showHDMImirrored();
					getYamaha().getInput().hdmi4().invoke();
				}
			};
		}

		public IInvoker ps3() {
			return new IInvoker() {

				public void invoke() {
					wakeUp();
					showHDMImirrored();
					getYamaha().getInput().hdmi5().invoke();
				}
			};
		}

		public IInvoker wii() {
			return new IInvoker() {

				public void invoke() {
					wakeUp();
					showHDMImirrored();
					getYamaha().getInput().av6().invoke();
				}
			};

		};

		public IInvoker ownHDMI() {
			return new IInvoker() {

				public void invoke() {
					wakeUp();
					showHDMImirrored();
					getYamaha().getInput().hdmi1().invoke();
				}
			};
		}

		public IInvoker ownAudioKlinke() {
			return new IInvoker() {

				public void invoke() {
					wakeUp();
					getYamaha().getInput().audio2().invoke();
				}
			};
		}

		public IInvoker bt() {
			return new IInvoker() {

				public void invoke() {
					wakeUp();
					getYamaha().getInput().audio1().invoke();
				}
			};
		};

	};

	public IToggle power() {
		return new IToggle() {

			public void on() {
				powerSaving().powerTv().on();
				powerSaving().powerProjector().on();
				powerSaving().powerYamaha().on();
				powerSaving().powerMetacade().on();
			}

			public void off() {
				powerSaving().powerTv().off();
				powerSaving().powerProjector().off();
				powerSaving().powerYamaha().off();
				powerSaving().powerMetacade().off();
				powerSaving().powerLamp1().off();
			}
		};
	}

	private final IPowerSaving powerSaving = new IPowerSaving() {

		public IToggle blankProjector() {
			return getBenq().display().blank();
		}

		public IToggle powerProjector() {
			return getBenq().power();
		}

		public IToggle powerTv() {
			return getNec().power();
		};

		public IToggle powerYamaha() {
			return getYamaha().power();
		}

		public IToggle powerMetacade() {
			return getMetacade().power();
		}

		public IToggle powerLamp1() {
			return lamp1;
		}

		public IInvoker resetKillswitch() {
			return killswitch.reset();
		}
	};

	private void wakeUp() {
		getYamaha().volume().low();
		getYamaha().mute().off();
		getYamaha().power().on();
	}

	protected void showHDMIonProjector() {
		getBenq().display().input().hdmi().invoke();
	}

	protected void showVGA1onProjector() {
		getBenq().display().input().vga1().invoke();
	}

	protected void showVGA2onProjector() {
		getBenq().display().input().vga2().invoke();
	}

	protected void showHDMImirrored() {
		showHDMIonTV();
		showHDMIonProjector();
	}

	protected void showHDMIonTV() {
		getNec().display().input().hdmi().invoke();
	}

	protected void showDVIonTV() {
		getNec().display().input().dvi().invoke();
	}

	protected void showVGAonTV() {
		getNec().display().input().vga().invoke();
	}

	public void create(RestBuilder rest) {
		rest.add(power(), "power");

		rest.add(devices().bt(), "devices/bt");
		rest.add(devices().ownAudioKlinke(), "devices/own_audio_klinke");
		rest.add(devices().ownHDMI(), "devices/own_hdmi");
		rest.add(devices().ps2(), "devices/ps2");
		rest.add(devices().ps3(), "devices/ps3");
		rest.add(devices().screeninvader(), "devices/screeninvader");
		rest.add(devices().screeninvaderStable(),
				"devices/screeninvader_stable");
		rest.add(devices().wii(), "devices/wii");
		rest.add(devices().chromecast(), "devices/chromecast");

		rest.add(powerSaving().blankProjector(), "powersaving/projector/blank");
		rest.add(powerSaving().powerProjector(), "powersaving/projector/power");
		rest.add(powerSaving().powerTv(), "powersaving/tv/power");
		rest.add(powerSaving().powerMetacade(), "powersaving/metacade/power");
		rest.add(powerSaving().powerYamaha(), "powersaving/yamaha/power");
		rest.add(powerSaving().powerLamp1(), "powersaving/lamp1/power");

		rest.add(yamaha.volume().low(), "volume/low");
		rest.add(yamaha.volume().medium(), "volume/medium");
		rest.add(yamaha.volume().high(), "volume/high");
		
		rest.add(powerSaving().resetKillswitch(),
				"powersaving/killswitch/reset");
	}

	public IDevices devices() {
		return devices;
	}

	public IPowerSaving powerSaving() {
		return powerSaving;
	}

	protected IBenq getBenq() {
		return benq;
	}

	protected IYamaha getYamaha() {
		return yamaha;
	}

	protected INec getNec() {
		return nec;
	}

	protected IMetacade getMetacade() {
		return metacade;
	}
}
