package at.metalab.slackomatic;

import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.fusesource.mqtt.client.MQTT;

import at.metalab.slackomatic.api.IToggle;
import at.metalab.slackomatic.devices.benq.IBenq;
import at.metalab.slackomatic.devices.benq.ShellBenqImpl;
import at.metalab.slackomatic.devices.killswitch.IKillswitch;
import at.metalab.slackomatic.devices.killswitch.KillswitchImpl;
import at.metalab.slackomatic.devices.loungelights.ILoungeLights;
import at.metalab.slackomatic.devices.loungelights.LoungeLightsImpl;
import at.metalab.slackomatic.devices.metacade.IMetacade;
import at.metalab.slackomatic.devices.metacade.MetacadeImpl;
import at.metalab.slackomatic.devices.nec.INec;
import at.metalab.slackomatic.devices.nec.NecImpl;
import at.metalab.slackomatic.devices.yamaha.IYamaha;
import at.metalab.slackomatic.devices.yamaha.ShellYamahaImpl;
import at.metalab.slackomatic.rest.RestAPI;
import at.metalab.slackomatic.rooms.lounge.LoungeImpl;

public class SlackomaticLounge {

	private final static Logger LOG = Logger.getLogger(SlackomaticLounge.class
			.getCanonicalName());

	public static void main(String[] args) throws Exception {
		new SlackomaticTemplate("lounge", args) {
			@Override
			protected void setup(RestAPI restAPI, File slackomaticHome) {
				// create the mqtt object used for communication with
				// our mqtt based hardware (lounge-light, regal moodlight)
				final MQTT mqtt = new MQTT();
				String mqttHost = "tcp://127.0.0.1:1883";

				try {
					mqtt.setHost(mqttHost);
				} catch (URISyntaxException uriSyntaxException) {
					LOG.severe("could not create mqtt object for: " + mqttHost);
				}

				// setup the objects which control the actual hardware
				INec nec = new NecImpl('A', new File(slackomaticHome, "nec"));

				IBenq benq = new ShellBenqImpl(
						new File(slackomaticHome, "benq"));

				IYamaha yamaha = new ShellYamahaImpl(new File(slackomaticHome,
						"yamaha"));

				IMetacade metacade = new MetacadeImpl("00:11:09:7B:73:68",
						"http://10.20.30.17:1234");

				ILoungeLights loungeLights = new LoungeLightsImpl(mqtt,
						"/lounge/lights");

				IKillswitch killswitch = new KillswitchImpl(new File(
						slackomaticHome, "killswitch"));

				IToggle lamp1 = new IToggle() {

					public void on() {
						Util.executeCommand(new File(
								"/home/pi/slackomatic-addons/homematic"),
								"./power0.sh", "1");
					}

					public void off() {
						Util.executeCommand(new File(
								"/home/pi/slackomatic-addons/homematic"),
								"./power0.sh", "0");
					}
				};

				final File blinkyDir = new File(slackomaticHome, "blinky");

				IToggle regal = new IToggle() {

					public void on() {
						Util.executeCommand(blinkyDir, "./regal_on.sh");
					}

					public void off() {
						Util.executeCommand(blinkyDir, "./regal_off.sh");
					}
				};

				IToggle spaceinvaders = new IToggle() {

					public void on() {
						Util.executeCommand(blinkyDir, "./spaceinvaders_on.sh");
						LOG.info("executed spaceinvaders on");
					}

					public void off() {
						Util.executeCommand(blinkyDir, "./spaceinvaders_off.sh");
						LOG.info("executed spaceinvaders off");
					}
				};

				// register the devices
				restAPI.addDevice("nec", nec);
				restAPI.addDevice("benq", benq);
				restAPI.addDevice("yamaha", yamaha);
				restAPI.addDevice("metacade", metacade);
				restAPI.addDevice("loungelights", loungeLights);

				// and add the lounge room
				restAPI.addRoom("lounge", new LoungeImpl(benq, yamaha, nec,
						metacade, killswitch, lamp1, loungeLights, regal,
						spaceinvaders));
			}
		};
	}

}
