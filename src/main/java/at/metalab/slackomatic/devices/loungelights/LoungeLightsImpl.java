package at.metalab.slackomatic.devices.loungelights;

import java.util.logging.Logger;

import org.fusesource.mqtt.client.MQTT;

import at.metalab.slackomatic.MqttUnreliablePublisher;
import at.metalab.slackomatic.api.IInvoker;
import at.metalab.slackomatic.rest.RestBuilder;

public class LoungeLightsImpl implements ILoungeLights {

	private final static Logger LOG = Logger.getLogger(LoungeLightsImpl.class
			.getCanonicalName());

	private final MqttUnreliablePublisher publisher;

	private final SetColorCommand ALL_OFF = new SetColorCommand().setAmber("0")
			.setWhite("0").setRGB("0", "0", "0");
	private final SetColorCommand WHITE_OFF = new SetColorCommand()
			.setWhite("0");
	private final SetColorCommand AMBER_OFF = new SetColorCommand()
			.setAmber("0");
	private final SetColorCommand RGB_OFF = new SetColorCommand().setRGB("0",
			"0", "0");

	public LoungeLightsImpl(MQTT mqtt, String topic) {
		this.publisher = new MqttUnreliablePublisher(mqtt, topic);
	}

	public void create(RestBuilder rest) {
		rest.add(power().off(), "power/off");
		rest.add(power().whiteOff(), "power/whiteOff");
		rest.add(power().amberOff(), "power/amberOff");
		rest.add(power().rgbOff(), "power/rgbOff");
	}

	public IPower power() {
		return new IPower() {

			public IInvoker whiteOff() {
				return new IInvoker() {

					public void invoke() {
						sendSetColorCommand(WHITE_OFF);
					}
				};
			}

			public IInvoker rgbOff() {
				return new IInvoker() {

					public void invoke() {
						sendSetColorCommand(RGB_OFF);
					}
				};
			}

			public IInvoker off() {
				return new IInvoker() {

					public void invoke() {
						sendSetColorCommand(ALL_OFF);
					}
				};
			}

			public IInvoker amberOff() {
				return new IInvoker() {

					public void invoke() {
						sendSetColorCommand(AMBER_OFF);
					}
				};
			}
		};
	}

	public void setAmber(String value) {
		sendSetColorCommand(new SetColorCommand().setAmber(value));
	}

	public void setRGB(String r, String g, String b) {
		sendSetColorCommand(new SetColorCommand().setRGB(r, g, b));
	}

	public void setWhite(String value) {
		sendSetColorCommand(new SetColorCommand().setWhite(value));
	}

	private synchronized void sendSetColorCommand(
			SetColorCommand setColorCommand) {
		String message = setColorCommand.buildMessage();
		LOG.info("publishing setColorCommand: " + message);
		publisher.publish(message);
	}

}
