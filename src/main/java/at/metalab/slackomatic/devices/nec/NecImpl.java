package at.metalab.slackomatic.devices.nec;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import at.metalab.slackomatic.api.IInvoker;
import at.metalab.slackomatic.api.IToggle;
import at.metalab.slackomatic.devices.nec.lowlevel.Message;
import at.metalab.slackomatic.rest.RestBuilder;

public class NecImpl implements INec {

	private final char deviceId;

	private final File serialDevice;

	public NecImpl(char deviceId, File serialDevice) {
		if (!isValidId(deviceId)) {
			throw new IllegalArgumentException("Invalid device Id specified");
		}

		if (!isValidSerialDevice(serialDevice)) {
			throw new IllegalArgumentException(
					"SerialDevice must not be empty or null");
		}

		this.deviceId = deviceId;
		this.serialDevice = serialDevice;
	}

	private boolean isValidSerialDevice(File serialDevice) {
		return serialDevice != null;
	}

	private boolean isValidId(char deviceId) {
		return "*ABCDEFGHIJKLMNOPQRSTUVWXYZ".contains(String.valueOf(deviceId));
	}

	private char getDeviceId() {
		return deviceId;
	}

	public IToggle power() {
		return new IToggle() {

			private void send(String param) {
				sendPacket(Message.createCommand(getDeviceId(),
						String.format("C203D6%s", param)));
			}

			public void on() {
				send("0001");
			}

			public void off() {
				send("0004");
			}
		};
	}

	public IToggle mute() {
		return new IToggle() {

			public void on() {
				sendPacket(Message.createSetParameter(getDeviceId(), 0x0, 0x8D,
						1));
			}

			public void off() {
				sendPacket(Message.createSetParameter(getDeviceId(), 0x0, 0x8D,
						0));
			}
		};
	}

	public IToggle signalInformation() {
		return new IToggle() {

			public void on() {
				sendPacket(Message.createSetParameter(getDeviceId(), 0x2, 0xEA,
						2));
			}

			public void off() {
				sendPacket(Message.createSetParameter(getDeviceId(), 0x2, 0xEA,
						1));
			}
		};
	}

	public IPip pip() {
		return new IPip() {

			public IMode mode() {
				return new IMode() {

					public IInvoker sideBySideFull() {
						return new IInvoker() {

							public void invoke() {
								sendPacket(Message.createSetParameter(
										getDeviceId(), 0x2, 0x72, 6));
							}
						};
					}

					public IInvoker sideBySideAspect() {
						return new IInvoker() {

							public void invoke() {
								sendPacket(Message.createSetParameter(
										getDeviceId(), 0x2, 0x72, 5));
							}
						};
					}

					public IInvoker popAspectSub() {
						return new IInvoker() {

							public void invoke() {
								sendPacket(Message.createSetParameter(
										getDeviceId(), 0x2, 0x72, 8));
							}
						};
					}

					public IInvoker popAspectMain() {
						return new IInvoker() {

							public void invoke() {
								sendPacket(Message.createSetParameter(
										getDeviceId(), 0x2, 0x72, 7));
							}
						};
					}

					public IInvoker pop() {
						return new IInvoker() {

							public void invoke() {
								sendPacket(Message.createSetParameter(
										getDeviceId(), 0x2, 0x72, 3));
							}
						};
					}

					public IInvoker pip() {
						return new IInvoker() {

							public void invoke() {
								sendPacket(Message.createSetParameter(
										getDeviceId(), 0x2, 0x72, 2));
							}
						};
					}
				};
			}

			public ISize size() {
				return new ISize() {

					public IInvoker small() {
						return new IInvoker() {

							public void invoke() {
								sendPacket(Message.createSetParameter(
										getDeviceId(), 0x2, 0x71, 1));
							}
						};
					}

					public IInvoker medium() {
						return new IInvoker() {

							public void invoke() {
								sendPacket(Message.createSetParameter(
										getDeviceId(), 0x2, 0x71, 2));
							}
						};
					}

					public IInvoker large() {
						return new IInvoker() {

							public void invoke() {
								sendPacket(Message.createSetParameter(
										getDeviceId(), 0x2, 0x71, 3));
							}
						};
					}
				};
			}

			public IToggle toggle() {
				return new IToggle() {

					public void on() {
						sendPacket(Message.createSetParameter(getDeviceId(),
								0x2, 0x72, 2));
					}

					public void off() {
						sendPacket(Message.createSetParameter(getDeviceId(),
								0x2, 0x72, 1));
					}
				};
			}

			public IInput input() {
				return new IInput() {

					public IInvoker video2() {
						return new IInvoker() {

							public void invoke() {
								sendPacket(Message.createSetParameter(
										getDeviceId(), 0x2, 0x73, 6));
							}
						};
					}

					public IInvoker video1() {
						return new IInvoker() {

							public void invoke() {
								sendPacket(Message.createSetParameter(
										getDeviceId(), 0x2, 0x73, 5));
							}
						};
					}

					public IInvoker vga() {
						return new IInvoker() {

							public void invoke() {
								sendPacket(Message.createSetParameter(
										getDeviceId(), 0x2, 0x73, 1));
							}
						};
					}

					public IInvoker hdmi() {
						return new IInvoker() {

							public void invoke() {
								sendPacket(Message.createSetParameter(
										getDeviceId(), 0x2, 0x73, 4));
							}
						};
					}

					public IInvoker dvi() {
						return new IInvoker() {

							public void invoke() {
								sendPacket(Message.createSetParameter(
										getDeviceId(), 0x2, 0x73, 3));
							}
						};
					}
				};
			}
		};
	}

	public IDisplay display() {
		return new IDisplay() {

			public IInput input() {
				return new IInput() {

					public IInvoker video2() {
						return new IInvoker() {

							public void invoke() {
								sendPacket(Message.createSetParameter(
										getDeviceId(), 0x0, 0x60, 6));
							}
						};
					}

					public IInvoker video1() {
						return new IInvoker() {

							public void invoke() {
								sendPacket(Message.createSetParameter(
										getDeviceId(), 0x0, 0x60, 5));
							}
						};
					}

					public IInvoker vga() {
						return new IInvoker() {

							public void invoke() {
								sendPacket(Message.createSetParameter(
										getDeviceId(), 0x0, 0x60, 1));
							}
						};
					}

					public IInvoker hdmi() {
						return new IInvoker() {

							public void invoke() {
								sendPacket(Message.createSetParameter(
										getDeviceId(), 0x0, 0x60, 4));
							}
						};
					}

					public IInvoker dvi() {
						return new IInvoker() {

							public void invoke() {
								sendPacket(Message.createSetParameter(
										getDeviceId(), 0x0, 0x60, 3));
							}
						};
					}
				};
			}
		};
	}

	public void borderColor(int brightness) {
		sendPacket(Message.createSetParameter(getDeviceId(), 0x2, 0xDF,
				brightness));
	}

	private synchronized void sendPacket(Message message) {
		OutputStream out = null;

		try {
			out = new FileOutputStream(serialDevice);

			byte[] packet = message.createPacket();
			logPacket(packet);
			out.write(packet);
			out.flush();
		} catch (Exception exception) {
			System.err.println(exception);
			exception.printStackTrace();
		} finally {
			System.err.println("command sent");
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException ioException) {
				// ignore
			}
		}
	}

	private void logPacket(byte[] packet) {
		try {
			String packetString = new String(packet, "US-ASCII");

			StringBuilder hexdump = new StringBuilder();
			StringBuilder humanReadable = new StringBuilder();

			for (int i = 0; i < packetString.length(); i++) {
				char c = packetString.charAt(i);

				// add printable characters (or a dot if not)
				if (!Character.isISOControl(c) && !Character.isWhitespace(c)) {
					humanReadable.append(c);
				} else {
					humanReadable.append('.');
				}

				// add the byte value to the hexdump
				if (i != 0) {
					hexdump.append(' ');
				}
				hexdump.append(String.format("%02X", (int) c));
			}

			System.err.println(String.format("logPacket: |%s|%s| to %s (%s)",
					hexdump.toString(), humanReadable.toString(), deviceId,
					serialDevice));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}

	}

	public void create(RestBuilder rest) {
		rest.add(power(), "power");
		rest.add(mute(), "mute");
		rest.add(signalInformation(), "signal_information");

		rest.add(display().input().dvi(), "display/input/dvi");
		rest.add(display().input().hdmi(), "display/input/hdmi");
		rest.add(display().input().vga(), "display/input/vga");
		rest.add(display().input().video1(), "display/input/video1");
		rest.add(display().input().video2(), "display/input/video2");

		rest.add(pip().toggle(), "pip");

		rest.add(pip().input().dvi(), "pip/input/dvi");
		rest.add(pip().input().hdmi(), "pip/input/hdmi");
		rest.add(pip().input().vga(), "pip/input/vga");
		rest.add(pip().input().video1(), "pip/input/video1");
		rest.add(pip().input().video2(), "pip/input/video2");

		rest.add(pip().size().small(), "pip/size/small");
		rest.add(pip().size().medium(), "pip/size/medium");
		rest.add(pip().size().large(), "pip/size/large");

		rest.add(pip().mode().pip(), "pip/mode/pip");
		rest.add(pip().mode().pop(), "pip/mode/pop");
		rest.add(pip().mode().popAspectMain(), "pip/mode/pop_aspect_main");
		rest.add(pip().mode().popAspectSub(), "pip/mode/pop_aspect_sub");
		rest.add(pip().mode().sideBySideAspect(),
				"pip/mode/side_by_side_aspect");
		rest.add(pip().mode().sideBySideFull(), "pip/mode/side_by_side_full");
	}
}
