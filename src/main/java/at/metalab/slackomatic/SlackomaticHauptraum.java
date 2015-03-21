package at.metalab.slackomatic;

import java.io.File;

import at.metalab.slackomatic.devices.infocus.IInfocus;
import at.metalab.slackomatic.devices.infocus.InfocusImpl;
import at.metalab.slackomatic.devices.nec.INec;
import at.metalab.slackomatic.devices.nec.NecImpl;
import at.metalab.slackomatic.rest.RestAPI;
import at.metalab.slackomatic.rooms.hauptraum.HauptraumImpl;

public class SlackomaticHauptraum {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		new SlackomaticTemplate("hauptraum", args) {

			@Override
			protected void setup(RestAPI restAPI, File slackomaticHome) {
				// setup the objects which control the actual hardware
				INec nec = new NecImpl('A', new File(slackomaticHome, "nec"));

				IInfocus infocus = new InfocusImpl("10.20.30.35");

				// register the devices
				restAPI.addDevice("nec", nec);
				restAPI.addDevice("infocus", infocus);

				// and add the hauptraum room
				restAPI.addRoom("hauptraum", new HauptraumImpl(nec, infocus));
			}
		};
	}
}