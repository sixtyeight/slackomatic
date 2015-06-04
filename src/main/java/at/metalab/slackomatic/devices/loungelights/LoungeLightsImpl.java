package at.metalab.slackomatic.devices.loungelights;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;

import at.metalab.slackomatic.api.IInvoker;
import at.metalab.slackomatic.rest.RestBuilder;

public class LoungeLightsImpl implements ILoungeLights {

	private final static Logger LOG = Logger.getLogger(LoungeLightsImpl.class
			.getCanonicalName());

	private final String mqttHost;

	private final String mqttTopic;

	private final SetColorCommand ALL_OFF = new SetColorCommand().setAmber("0")
			.setWhite("0").setRGB("0", "0", "0");
	private final SetColorCommand WHITE_OFF = new SetColorCommand()
			.setWhite("0");
	private final SetColorCommand AMBER_OFF = new SetColorCommand()
			.setAmber("0");
	private final SetColorCommand RGB_OFF = new SetColorCommand().setRGB("0",
			"0", "0");

	public LoungeLightsImpl(String mqttHost, String mqttTopic) {
		this.mqttHost = mqttHost;
		this.mqttTopic = mqttTopic;
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

	private volatile BlockingConnection connection = null;

	private synchronized void sendSetColorCommand(
			SetColorCommand setColorCommand) {
		if (connection == null) {
			MQTT mqtt = new MQTT();

			try {
				mqtt.setHost(mqttHost);
			} catch (URISyntaxException uriSyntaxException) {
				LOG.severe("could not create mqtt object for: " + mqttHost);
				return;
			}

			BlockingConnection connection = mqtt.blockingConnection();
			try {
				connection.connect();
			} catch (Exception exception) {
				LOG.severe("could not connect to mqtt server at: " + mqttHost);
				return;
			}

			this.connection = connection;
		}

		String payload = setColorCommand.buildMessage();

		try {
			connection.publish(mqttTopic, payload.getBytes("ISO-8859-1"),
					QoS.AT_LEAST_ONCE, false);

			LOG.info(String.format("sent '%s' to '%s/%s'", payload, mqttHost,
					mqttTopic));
		} catch (UnsupportedEncodingException willNeverHappen) {
		} catch (Exception exception) {
			LOG.severe(String.format("sending '%s' to '%s/%s' failed: %s",
					payload, mqttHost, mqttTopic, exception.getMessage()));
		}
	}

}
