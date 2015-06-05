package at.metalab.slackomatic;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;

public class MqttUnreliablePublisher {

	private final static Logger LOG = Logger
			.getLogger(MqttUnreliablePublisher.class.getCanonicalName());

	private final MQTT mqtt;

	private final String topic;

	private volatile FutureConnection connection;

	public MqttUnreliablePublisher(MQTT mqtt, String topic) {
		this.mqtt = mqtt;
		this.topic = topic;
	}

	public synchronized void publish(String payload) {
		if (connection == null) {
			FutureConnection connection = mqtt.futureConnection();
			try {
				connection.connect().await();
			} catch (Exception exception) {
				LOG.severe("could not connect to mqtt server at: "
						+ mqtt.getHost().toString());
				return;
			}

			this.connection = connection;
		}

		try {
			connection.publish(topic, payload.getBytes("ISO-8859-1"),
					QoS.AT_LEAST_ONCE, false);

			LOG.info(String.format("sent '%s' to '%s/%s'", payload, mqtt
					.getHost().toString(), topic));
		} catch (UnsupportedEncodingException willNeverHappen) {
		} catch (Exception exception) {
			LOG.severe(String.format("publishing '%s' at '%s/%s' failed: %s",
					payload, mqtt.getHost().toString(), topic,
					exception.getMessage()));
		}
	}

}
