package at.metalab.slackomatic.test;

import at.metalab.slackomatic.devices.nec.INec;
import at.metalab.slackomatic.devices.nec.NecImpl;

public class TestPip {

	public static void main(String[] args) throws Exception {
		final String device = "/dev/null";

		INec tv = new NecImpl('A', device);

		tv.display().input().hdmi();
		tv.pip().input().vga();
		tv.mute().on();

		Thread.sleep(2000);

		tv.pip().size().large();
		Thread.sleep(1000);

		tv.pip().toggle().off();

		tv.signalInformation().off();
	}

}
